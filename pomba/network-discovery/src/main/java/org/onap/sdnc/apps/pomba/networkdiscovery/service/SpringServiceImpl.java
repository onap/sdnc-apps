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

import java.io.IOException;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.onap.aai.restclient.client.OperationResult;
import org.onap.aai.restclient.client.RestClient;
import org.onap.logging.ref.slf4j.ONAPLogAdapter;
import org.onap.logging.ref.slf4j.ONAPLogAdapter.RequestBuilder;
import org.onap.logging.ref.slf4j.ONAPLogConstants.InvocationMode;
import org.onap.sdnc.apps.pomba.networkdiscovery.ApplicationException;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.Attribute;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.DataQuality;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.NetworkDiscoveryNotification;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.NetworkDiscoveryResponse;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.Resource;
import org.onap.sdnc.apps.pomba.networkdiscovery.service.rs.RestService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Service
public class SpringServiceImpl implements SpringService {
    private static final String ENRICHER_HEADER_APPLICATION = "X-FromAppId";
    private static final String ENRICHER_HEADER_TRANSACTION = "X-TransactionId";
    
    private static final int DEFAULT_WORKER_THREADS = 3;
    
    private ExecutorService executor = Executors.newFixedThreadPool(
            Integer.getInteger("discovery.threads", DEFAULT_WORKER_THREADS),
            new ThreadFactoryBuilder().setNameFormat("discovery-worker-%d").build());
    
    @Autowired
    private RestClient enricherClient;
    
    @Autowired
    private String enricherBaseUrl;
    
    @javax.annotation.Resource
    private Client callbackClient;
    
    @javax.annotation.Resource
    private Map<String, String> enricherTypeURLs;
    
    private DocumentBuilder parser;
    
    @javax.annotation.Resource
    private Map<String, String> enricherAttributeNameMapping;
    
    public SpringServiceImpl() throws ParserConfigurationException {
        this.parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }
    
    @Override
    public NetworkDiscoveryResponse findbyResourceIdAndType(String transactionId, String requestId, String resourceType,
            List<String> resourceIds, String notificationURL, String notificationAuthorization, ONAPLogAdapter adapter)
            throws ApplicationException {
        
        NetworkDiscoveryResponse response = new NetworkDiscoveryResponse();
        response.setRequestId(requestId);
        
        String enricherURL = this.enricherTypeURLs.get(resourceType);
        // check if resourceType is supported
        if (enricherURL == null) {
            // don't know what to do with this - return empty response
            response.setCode(Status.NO_CONTENT.getStatusCode());
            response.setMessage("Unsupported resourceType " + resourceType);
            response.setAckFinalIndicator(true);
            return response;
        }
        
        // schedule discovery of specified resources
        Runnable task = new ResourceTask(transactionId, requestId, resourceType, resourceIds, notificationURL,
                notificationAuthorization, enricherURL, adapter);
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
    
    private List<Attribute> toAttributeList(String xml) throws SAXException, IOException {
        // TODO don't return raw A&AI attributes but coerce to swagger-defined
        // enums
        Document doc = this.parser.parse(new InputSource(new StringReader(xml)));
        NodeList children = doc.getDocumentElement().getChildNodes();
        List<Attribute> result = new ArrayList<>();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Attribute attr = new Attribute();
                attr.setName(((Element) child).getTagName());
                attr.setValue(((Element) child).getTextContent());
                attr.setDataQuality(DataQuality.ok());
                result.add(attr);
            }
        }
        return result;
    }
    
    private void sendNotification(String url, String authorization, Object notification, ONAPLogAdapter adapter) {
        
        Invocation.Builder request = this.callbackClient.target(url).request().accept(MediaType.TEXT_PLAIN_TYPE);
        
        if (authorization != null) {
            request.header(HttpHeaders.AUTHORIZATION, authorization);
        }
        Logger log = adapter.unwrap();
        adapter.invoke(new RequestBuilderWrapper(request), InvocationMode.SYNCHRONOUS);
        try {
            // native client marshaller doesn't skip null fields
            // so manually marshal notification to json
            
            if (log.isDebugEnabled()) {
                StringBuilder debugRequest = new StringBuilder("REQUEST:\n");
                debugRequest.append("URL: ").append(url).append("\n");
                debugRequest.append("Payload: ").append(notification).append("\n");
                // if (headers != null) {
                // debugRequest.append("Headers: ");
                // for (Entry<String, List<String>> header : headers.entrySet())
                // {
                // debugRequest.append("\n\t").append(header.getKey()).append(":");
                // for (String headerEntry : header.getValue()) {
                // debugRequest.append("\"").append(headerEntry).append("\" ");
                // }
                // }
                // }
                log.debug(debugRequest.toString());
            }
            
            Response result = request.post(Entity.json(notification));
            
            adapter.unwrap().info("request at url = {} resulted in http response: {}", url,
                    result.getStatusInfo().getStatusCode() + " " + result.getStatusInfo().getReasonPhrase());
            
            if (log.isDebugEnabled()) {
                StringBuilder debugResponse = new StringBuilder("RESPONSE:\n");
                debugResponse.append("Result: ").append(result.getStatus()).append("\n");
                String content = result.hasEntity() ? result.getEntity().toString() : null;
                if (result.getStatus() >= 300) {
                    debugResponse.append("Failure Cause: ").append(content).append("\n");
                } else {
                    debugResponse.append("Payload: ").append(content).append("\n");
                }
                if (result.getHeaders() != null) {
                    debugResponse.append("Headers: ");
                    for (Entry<String, List<Object>> header : result.getHeaders().entrySet()) {
                        debugResponse.append("\n\t").append(header.getKey()).append(":");
                        for (Object headerEntry : header.getValue()) {
                            debugResponse.append("\"").append(headerEntry).append("\" ");
                        }
                    }
                }
                log.debug(debugResponse.toString());
            }
            
        } catch (Exception x) {
            log.error("Error during {} operation to endpoint at url = {} with error = {}", "POST", url,
                    x.getLocalizedMessage());
        }
    }
    
    private void convertEnricherAttributeNamesToPombaNames(NetworkDiscoveryNotification notification) {
        List<Resource> currentNotifiList = notification.getResources();
        for (Resource currResource : currentNotifiList) {
            List<Attribute> currentAttributeList = currResource.getAttributeList();
            for (Attribute currAttribute : currentAttributeList) {
                String currentAttributeName = currAttribute.getName().replaceAll("\\s", ""); // remove
                                                                                             // white
                                                                                             // space
                                                                                             // in
                                                                                             // the
                                                                                             // name
                if (enricherAttributeNameMapping.containsKey(currentAttributeName)) {
                    currAttribute.setName(enricherAttributeNameMapping.get(currentAttributeName));
                }
            }
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
            
            NetworkDiscoveryNotification notification = new NetworkDiscoveryNotification();
            notification.setRequestId(this.requestId);
            
            List<Resource> resources = null;
            MessageFormat format = new MessageFormat(SpringServiceImpl.this.enricherBaseUrl + this.resourceURL);
            MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
            headers.add(ENRICHER_HEADER_APPLICATION, RestService.SERVICE_NAME);
            headers.add(ENRICHER_HEADER_TRANSACTION, this.transactionId);
            
            Logger log = adapter.unwrap();
            
            for (String resourceId : this.resourceIds) {
                String url = format.format(new Object[] { resourceId });
                OperationResult result = SpringServiceImpl.this.enricherClient.get(url, headers,
                        MediaType.APPLICATION_XML_TYPE);
                
                Resource resource = new Resource();
                resource.setType(this.resourceType);
                resource.setId(resourceId);
                if (resources == null) {
                    resources = new ArrayList<>();
                    notification.setResources(resources);
                }
                resources.add(resource);
                
                if (result.wasSuccessful()) {
                    resource.setDataQuality(DataQuality.ok());
                    try {
                        resource.setAttributeList(toAttributeList(result.getResult()));
                    } catch (Exception x) {
                        resource.setDataQuality(DataQuality.error(x.getMessage()));
                    }
                } else {
                    resource.setDataQuality(DataQuality.error(result.getFailureCause()));
                }
            }
            
            notification.setCode(Status.OK.getStatusCode());
            notification.setMessage(Status.OK.getReasonPhrase());
            notification.setAckFinalIndicator(true);
            
            SpringServiceImpl.this.convertEnricherAttributeNamesToPombaNames(notification);
            log.info(notification.toString());
            
            // call client back with resource details
            sendNotification(this.notificationURL, this.notificationAuthorization, notification, adapter);
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