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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;

import org.onap.logging.ref.slf4j.ONAPLogAdapter;
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

    @Autowired
    private Client openstackClient;

    @Autowired
    private String openstackIdentityUrl;

    @Autowired
    private String openstackIdentityUser;

    @Autowired
    private String openstackIdentityPassword;

    @Autowired
    private String openstackApiMicroversion;

    @javax.annotation.Resource
    private Map<String, String> openstackTypeURLs;

    @Override
    public NetworkDiscoveryResponse findbyResourceIdAndType(String transactionId, String requestId, String resourceType,
            List<String> resourceIds, ONAPLogAdapter adapter) throws ApplicationException {

        NetworkDiscoveryResponse response = new NetworkDiscoveryResponse();
        response.setRequestId(requestId);

        // check if resourceType is supported
        String openstackURL = this.openstackTypeURLs.get(resourceType);
        if (openstackURL == null) {
            throw new ApplicationException(ApplicationException.Error.GENERAL_FAILURE, Status.BAD_REQUEST,
                    "Unsupported resourceType " + resourceType);
        }
        String token = OSAuthentication.getToken(openstackIdentityUrl, openstackIdentityUser, openstackIdentityPassword,
                openstackClient, adapter);
        
        NetworkDiscoveryNotification discoveryResponse = new NetworkDiscoveryNotification();
        discoveryResponse.setRequestId(requestId);
        
        List<Resource> resources = new ArrayList<>();
        MessageFormat format = new MessageFormat(openstackURL);
        
        for (String resourceId : resourceIds) {
            String url = format.format(new Object[] { resourceId });
            Resource resource = new Resource();
            resource.setType(resourceType);
            resource.setId(resourceId);
            resources.add(resource);
        
            Response result;
            try {
                result = openstackClient.target(url).request().header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                        .header(OPENSTACK_HEADER_TOKEN, token)
                        .header(OPENSTACK_HEADER_API_VERSION, openstackApiMicroversion).get();
            } catch (Exception e) {
                // in case of time-out, exit the loop and return a failure.
                throw new ApplicationException(ApplicationException.Error.GENERAL_FAILURE, Status.NOT_FOUND,
                        "Openstack API GET failed - " + e.getMessage());
            }
        
            String jsonResult = result.readEntity(String.class);
            Logger log = adapter.unwrap();
            
            log.info("Openstack GET result for resourceID {}: {}", resourceId, jsonResult);

            if (result.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
                String transformedOutput = TransformationUtil.transform(jsonResult, resourceType);
        
                log.debug("Jolt transformed output: {}", transformedOutput);
        
                resource.setDataQuality(DataQuality.ok());
                List<Attribute> attributeList = TransformationUtil.toAttributeList(transformedOutput);
                resource.setAttributeList(attributeList);
            } else {
                resource.setDataQuality(DataQuality.error(jsonResult));
            }
        }

        discoveryResponse.setResources(resources);
        discoveryResponse.setCode(Status.OK.getStatusCode());
        discoveryResponse.setMessage(Status.OK.getReasonPhrase());
        discoveryResponse.setAckFinalIndicator(true);

        return discoveryResponse;
    }

}