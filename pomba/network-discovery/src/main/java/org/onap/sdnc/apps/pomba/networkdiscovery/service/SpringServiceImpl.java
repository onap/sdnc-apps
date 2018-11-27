/*
 * ============LICENSE_START===================================================
 * Copyright (c) 2018 Amdocs
 * ============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=====================================================
 */
package org.onap.sdnc.apps.pomba.networkdiscovery.service;

import com.bazaarvoice.jolt.JsonUtils;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.parsers.ParserConfigurationException;

import org.onap.aai.restclient.client.OperationResult;
import org.onap.aai.restclient.client.RestClient;
import org.onap.logging.ref.slf4j.ONAPLogAdapter;
import org.onap.logging.ref.slf4j.ONAPLogAdapter.RequestBuilder;
import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.onap.logging.ref.slf4j.ONAPLogConstants.InvocationMode;
import org.onap.pomba.common.datatypes.DataQuality;
import org.onap.sdnc.apps.pomba.networkdiscovery.ApplicationException;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.Attribute;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.NetworkDiscoveryNotification;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.NetworkDiscoveryResponse;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.Resource;
import org.onap.sdnc.apps.pomba.networkdiscovery.service.util.TransformationUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpringServiceImpl implements SpringService {
    private static final String OPENSTACK_HEADER_TOKEN = "X-Auth-Token";
    private static final String OPENSTACK_HEADER_API_VERSION = "X-OpenStack-Nova-API-Version";
    private static final int DEFAULT_WORKER_THREADS = 3;

    private ExecutorService executor = Executors.newFixedThreadPool(
            Integer.getInteger("discovery.threads", DEFAULT_WORKER_THREADS),
            new ThreadFactoryBuilder().setNameFormat("discovery-worker-%d").build());

    @Autowired
    private RestClient openstackClient;

    @Autowired
    private String openstackIdentityUrl;

    @Autowired
    private String openstackIdentityUser;

    @Autowired
    private String openstackIdentityPassword;

    @Autowired
    private String openstackApiMicroversion;

    @javax.annotation.Resource
    private Client callbackClient;

    @javax.annotation.Resource
    private Map<String, String> openstackTypeURLs;

    public SpringServiceImpl() throws ParserConfigurationException {
    }

    @Override
    public NetworkDiscoveryResponse findbyResourceIdAndType(String transactionId, String requestId, String resourceType,
            List<String> resourceIds, String notificationURL, String notificationAuthorization, ONAPLogAdapter adapter)
            throws ApplicationException {

        NetworkDiscoveryResponse response = new NetworkDiscoveryResponse();
        response.setRequestId(requestId);

        String openstackURL = this.openstackTypeURLs.get(resourceType);
        // check if resourceType is supported
        if (openstackURL == null) {
            // don't know what to do with this - return empty response
            response.setCode(Status.NO_CONTENT.getStatusCode());
            response.setMessage("Unsupported resourceType " + resourceType);
            response.setAckFinalIndicator(true);
            return response;
        }

        // schedule discovery of specified resources
        Runnable task = new ResourceTask(transactionId, requestId, resourceType, resourceIds, notificationURL,
                notificationAuthorization, openstackURL, adapter);
        this.executor.submit(task);

        response.setCode(Status.ACCEPTED.getStatusCode());
        response.setMessage(Status.ACCEPTED.getReasonPhrase());
        response.setAckFinalIndicator(false);
        return response;
    }

    @PreDestroy
    public void shutdown() {
        this.executor.shutdown();
    }

    private void sendNotification(String url, String transactionId, String authorization, Object notification,
            ONAPLogAdapter adapter) {

        Invocation.Builder request = this.callbackClient.target(url).request().accept(MediaType.TEXT_PLAIN_TYPE);

        if (authorization != null) {
            request.header(HttpHeaders.AUTHORIZATION, authorization);
            request.header(ONAPLogConstants.Headers.REQUEST_ID, transactionId);
        }
        Logger log = adapter.unwrap();
        adapter.invoke(new RequestBuilderWrapper(request), InvocationMode.SYNCHRONOUS);
        try {
            adapter.unwrap().info("Posting notfication to url = {} , payload: {}", url,
                    JsonUtils.toJsonString(Entity.json(notification).getEntity()));

            Response result = request.post(Entity.json(notification));

            adapter.unwrap().info("request at url = {} resulted in http status {} and response: {}", url,
                    result.getStatus(), result.getEntity());

        } catch (Exception x) {
            log.error("Error during {} operation to endpoint at url = {} with error = {}", "POST", url,
                    x.getLocalizedMessage());
        }
    }

    private class ResourceTask implements Runnable {
        private final String transactionId;
        private final String requestId;
        private final String resourceType;
        private final List<String> resourceIds;
        private final String notificationURL;
        private final String notificationAuthorization;
        private final String resourceURL;
        private final ONAPLogAdapter adapter;

        public ResourceTask(String transactionId, String requestId, String resourceType, List<String> resourceIds,
                String notificationURL, String notificationAuthorization, String resourceURL, ONAPLogAdapter adapter) {
            this.transactionId = transactionId;
            this.requestId = requestId;
            this.resourceType = resourceType;
            this.resourceIds = resourceIds;
            this.notificationURL = notificationURL;
            this.notificationAuthorization = notificationAuthorization;
            this.resourceURL = resourceURL;
            this.adapter = adapter;
        }

        @Override
        public void run() {
            try {
                runResourceDiscoveryTask();
            } catch (Exception e) {
                Logger log = adapter.unwrap();
                log.error("Failure in resource task", e);

                // Try to send out a notification of the failure:
                NetworkDiscoveryNotification notification = new NetworkDiscoveryNotification();
                notification.setRequestId(this.requestId);
                notification.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
                notification.setMessage(e.getMessage());
                notification.setAckFinalIndicator(true);

                // call client back with resource details
                sendNotification(this.notificationURL, this.transactionId, this.notificationAuthorization, notification,
                        adapter);
            }
        }

        private void runResourceDiscoveryTask() throws IOException, ApplicationException {

            String token = OSAuthentication.getToken(openstackIdentityUrl, openstackIdentityUser,
                    openstackIdentityPassword, openstackClient, adapter);

            NetworkDiscoveryNotification notification = new NetworkDiscoveryNotification();
            notification.setRequestId(this.requestId);

            List<Resource> resources = null;
            MessageFormat format = new MessageFormat(this.resourceURL);
            MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
            headers.add(OPENSTACK_HEADER_TOKEN, token);
            headers.add(OPENSTACK_HEADER_API_VERSION, openstackApiMicroversion);

            for (String resourceId : this.resourceIds) {
                String url = format.format(new Object[] { resourceId });
                OperationResult result = SpringServiceImpl.this.openstackClient.get(url, headers,
                        MediaType.APPLICATION_JSON_TYPE);

                adapter.unwrap().info("Openstack GET result: {}", result.getResult());

                Resource resource = new Resource();
                resource.setType(this.resourceType);
                resource.setId(resourceId);
                if (resources == null) {
                    resources = new ArrayList<>();
                    notification.setResources(resources);
                }
                resources.add(resource);

                if (result.wasSuccessful()) {
                    String transformedOutput = TransformationUtil.transform(result.getResult(), this.resourceType);

                    adapter.unwrap().info("Jolt transformed output: {}", transformedOutput);

                    resource.setDataQuality(DataQuality.ok());
                    List<Attribute> attributeList = TransformationUtil.toAttributeList(transformedOutput);
                    resource.setAttributeList(attributeList);
                } else {
                    resource.setDataQuality(DataQuality.error(result.getFailureCause()));
                }
            }
            notification.setCode(Status.OK.getStatusCode());
            notification.setMessage(Status.OK.getReasonPhrase());
            notification.setAckFinalIndicator(true);

            // call client back with resource details
            sendNotification(this.notificationURL, this.transactionId, this.notificationAuthorization, notification,
                    adapter);
        }

    }

    private static class RequestBuilderWrapper implements RequestBuilder<RequestBuilderWrapper> {
        private Invocation.Builder builder;

        private RequestBuilderWrapper(Invocation.Builder builder) {
            this.builder = builder;
        }

        @Override
        public RequestBuilderWrapper setHeader(String name, String value) {
            this.builder.header(name, value);
            return this;
        }

    }
}