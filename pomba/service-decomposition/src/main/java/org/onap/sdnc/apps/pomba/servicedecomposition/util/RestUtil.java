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
package org.onap.sdnc.apps.pomba.servicedecomposition.util;

import static org.onap.sdnc.apps.pomba.servicedecomposition.exception.DiscoveryException.Error.FETCH_RESOURCE_FAILED;
import static org.onap.sdnc.apps.pomba.servicedecomposition.exception.DiscoveryException.Error.INVALID_URL;
import static org.onap.sdnc.apps.pomba.servicedecomposition.exception.DiscoveryException.Error.SERVICE_INSTANCE_NOT_FOUND;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.aai.restclient.client.OperationResult;
import org.onap.aai.restclient.client.RestClient;
import org.onap.sdnc.apps.pomba.servicedecomposition.exception.DiscoveryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class RestUtil {
    
    
    private static final String RELATIONSHIP_KEY = "relationship";
    private static final String RELATIONSHIP_LIST_KEY = "relationship-list";
    // Parameters for Query AAI Model Data API
    // HTTP headers
    private static final String TRANSACTION_ID = "X-TransactionId";
    private static final String FROM_APP_ID = "X-FromAppId";
    private static final String APP_NAME = "aaiCtxBuilder";
    private static final String AUTHORIZATION = "Authorization";

    private static final Resource GENERIC_VNF = new Resource("generic-vnf");
    private static final Resource VF_MODULE = new Resource("vf-module");
    private static final Resource L3_NETWORK = new Resource("l3-network");

    public static class Resource {
        private String resourceName;
        private String collectionName;

        private Resource(String resource) {
            this.resourceName = resource;
            this.collectionName = resource + "s";
        }

        private String getResourceName() {
            return this.resourceName;
        }

        private String getCollectionName() {
            return this.collectionName;
        }

        @Override
        public String toString() {
            return "Resource [resourceName=" + resourceName + ", collectionName=" + collectionName + "]";
        }
    }

    private static final String JSON_ATT_RELATED_TO = "related-to";
    private static final String JSON_ATT_RELATED_LINK = "related-link";

    private static final String EMPTY_JSON_STRING = "{}";

    private static final String DEPTH = "?depth=2";
    private static Logger logger = LoggerFactory.getLogger(RestUtil.class);

    private RestUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Validates the URL parameter.
     *
     * @throws DiscoveryException if there is missing parameter
     */
    public static void validateURL(String serviceInstanceId) throws DiscoveryException {

        if (serviceInstanceId == null || serviceInstanceId.isEmpty()) {
            throw new DiscoveryException(INVALID_URL, Status.BAD_REQUEST);
        }
    }

    private static String generateServiceInstanceURL(String siPath, String serviceInstanceId) {
        return MessageFormat.format(siPath, serviceInstanceId);
    }

    /**
     * @param aaiClient
     * @param baseURL
     * @param aaiBasicAuthorization
     * @param aaiServiceInstancePath
     * @param aaiResourceList
     * @param transactionId
     * @param serviceInstanceId
     * @param adapter
     * @return
     * @throws DiscoveryException
     */
    public static JSONObject retrieveAAIModelData(RestClient aaiClient, String baseURL, String aaiBasicAuthorization, String aaiServiceInstancePath, String aaiResourceList,
            String transactionId, String serviceInstanceId) throws DiscoveryException {

        // Follow two variables for transform purpose
        String url = baseURL + generateServiceInstanceURL(aaiServiceInstancePath, serviceInstanceId);
        // Response from service instance API call
        JSONObject serviceInstancePayload = new JSONObject(
                getResource(aaiClient, url, aaiBasicAuthorization, transactionId));
        // Handle the case if the service instance is not found in AAI
        if (serviceInstancePayload.length() == 0) {
            logger.info("Service Instance {} is not found from AAI", serviceInstanceId);
            // Only return the empty Json on the root level. i.e service instance
            throw new DiscoveryException(SERVICE_INSTANCE_NOT_FOUND, Status.NOT_FOUND);
        }

        Map<String, List<String>> relationMap = extractRelationships(serviceInstancePayload);
        logger.info("The number of the relationship types for service instance id {} is: {}", serviceInstanceId,
                relationMap.size());

        JSONObject response = processVNFRelationMap(aaiClient, aaiResourceList, baseURL, aaiBasicAuthorization, transactionId, relationMap, serviceInstancePayload);
               
        if (relationMap.containsKey(L3_NETWORK.getResourceName())) {
            List<String> l3NetworkRelatedLinks = relationMap.get(L3_NETWORK.getResourceName());           
            List<JSONObject> l3networkPayload = processResourceList(aaiClient, baseURL, aaiBasicAuthorization, transactionId,
                    L3_NETWORK.getResourceName(), l3NetworkRelatedLinks);
            
            response.put(L3_NETWORK.getCollectionName(), l3networkPayload);
        }

        return response;

    }

    /**
     * @param aaiClient
     * @param baseURL
     * @param transactionId
     * @param relationMap
     * @throws DiscoveryException
     */
    private static JSONObject processVNFRelationMap(RestClient aaiClient, String aaiResourceList, String baseURL, String aaiBasicAuthorization, String transactionId,
            Map<String, List<String>> relationMap, JSONObject serviceInstancePayload) throws DiscoveryException {
        List<JSONObject> vnfLst = new ArrayList<>(); // List of the VNF JSON along with related resources

        JSONObject response = serviceInstancePayload;

        List<Resource> resourceTypes = getResourceTypes(aaiResourceList);

        if (relationMap.get(GENERIC_VNF.getResourceName()) != null) {
            List<JSONObject> vnfList = processResourceList(aaiClient, baseURL, aaiBasicAuthorization, transactionId,
                    GENERIC_VNF.getResourceName(), relationMap.get(GENERIC_VNF.getResourceName()));
            // Logic to Create the Generic VNF JSON and extract further relationships
            for (JSONObject vnfPayload : vnfList) {
                Map<String, List<String>> vnfRelationMap = extractRelationships(vnfPayload);
                String vnfId = vnfPayload.optString("vnf-id");

                for (Resource resourceType : resourceTypes) {
                    List<String> vnfcLinkLst = vnfRelationMap.get(resourceType.getResourceName());
                    if (vnfcLinkLst == null || vnfcLinkLst.isEmpty()) {
                        logger.info("No relationships found for generic-vnf '{}', resource type '{}'", vnfId,
                                resourceType.getResourceName());
                        continue;
                    }

                    logger.info("Number of relationships found for generic-vnf '{}', resource type '{}' are: {}", vnfId,
                            resourceType.getResourceName(), vnfcLinkLst.size());
                    List<JSONObject> vnfcList = processResourceList(aaiClient, baseURL, aaiBasicAuthorization,
                            transactionId, resourceType.getResourceName(), vnfcLinkLst);
                    vnfPayload.put(resourceType.getCollectionName(), vnfcList);
                }

                // Process vf-module looking for l3-network:
                processVfModuleList(aaiClient, baseURL, aaiBasicAuthorization, transactionId, vnfPayload);
                // Add final vnf payload to list
                vnfLst.add(vnfPayload);
            }
        } else {
            logger.info("No {} found for service-instance-id: {}", GENERIC_VNF.getResourceName(),
                    serviceInstancePayload.optString("service-instance-id"));
        }

        // Add generic vnf with related resource payload to response
        if (!vnfLst.isEmpty()) {
            response.put(GENERIC_VNF.getCollectionName(), vnfLst);
        }
        return response;

    }

    private static void processVfModuleList(RestClient aaiClient, String baseURL, String aaiBasicAuthorization, String transactionId,
            JSONObject vnfPayload) throws DiscoveryException {

       if (!vnfPayload.has(VF_MODULE.getCollectionName())) {
           return;
       }

       JSONObject vfmoduleCollection = vnfPayload.getJSONObject(VF_MODULE.getCollectionName());

       if (!vfmoduleCollection.has(VF_MODULE.getResourceName())) {
           return;
       }
       
       JSONArray vfModuleList = vfmoduleCollection.getJSONArray(VF_MODULE.getResourceName());

        for (int i = 0; i < vfModuleList.length(); i++) {
            JSONObject vfModulePayload = vfModuleList.optJSONObject(i);
            if (vfModulePayload == null) {
                logger.error("VF Module not found for vnf-id {}", vnfPayload.opt("vnf-id"));
                continue;
            }
            processVfModule(aaiClient, baseURL, aaiBasicAuthorization, transactionId, vfModulePayload);
        }     
   }

    private static void processVfModule(RestClient aaiClient, String baseURL, String aaiBasicAuthorization,
            String transactionId, JSONObject vfModulePayload) throws DiscoveryException {

        Map<String, List<String>> relationMap = extractRelationships(vfModulePayload);
        Object vfModuleId = vfModulePayload.opt("vf-module-id");

        List<String> l3NetworkRelatedLinks = relationMap.get(L3_NETWORK.getResourceName());
        if (l3NetworkRelatedLinks == null) {
            logger.info("No relationships found for vf-module '{}', resource type '{}'", vfModuleId, L3_NETWORK.getResourceName());
            // No L3-network relationships exist.
            return;
        }

        logger.info("Number of relationships found for vf-module '{}', resource type '{}' are: {}", vfModuleId, L3_NETWORK.getResourceName(), l3NetworkRelatedLinks.size());

        List<JSONObject> l3NetworkObjects = processResourceList(aaiClient, baseURL, aaiBasicAuthorization,
                transactionId, L3_NETWORK.getResourceName(), l3NetworkRelatedLinks);

        // Add l3-network with related resource payload to the vfModulePayload:
        vfModulePayload.put(L3_NETWORK.getCollectionName(), l3NetworkObjects);
    }

    /**
     * @param aaiClient
     * @param aaiBaseURL
     * @param transactionId
     * @param resourceType
     * @param resourceList
     * @return
     * @throws DiscoveryException
     */
    private static List<JSONObject> processResourceList(RestClient aaiClient, String aaiBaseURL, String aaiBasicAuthorization, String transactionId,
            String resourceType, List<String> resourceList) throws DiscoveryException {
        List<JSONObject> resourcePayloadList = new ArrayList<>();
        for (String resourceLink : resourceList) {
            String resourceURL = aaiBaseURL + resourceLink;
            // With latest AAI development, in order to retrieve the both generic VNF + vf_module, we can use
            // one API call but with depth=2
            if (resourceType.equals(GENERIC_VNF.getResourceName())) {
                resourceURL += DEPTH;
            }

            // Response from generic VNF API call
            JSONObject resourcePayload = new JSONObject(
                    getResource(aaiClient, resourceURL, aaiBasicAuthorization, transactionId));
            if (resourcePayload.length() == 0) {
                logger.info("Resource with url {} is not found from AAI", resourceLink);
            } else {
                resourcePayloadList.add(resourcePayload);
            }
        }
        return resourcePayloadList;
    }

    /**
     * Extract the related-Link from Json payload. For example
     * <pre>
     * {
     *    "related-to": "vnfc",
     *    "related-link": "/aai/v11/network/vnfcs/vnfc/zrdm5aepdg01vmg003",
     *    "relationship-data": [
     *       {
     *          "relationship-key": "vnfc.vnfc-name",
     *          "relationship-value": "zrdm5aepdg01vmg003"
     *       }
     *    ]
     * }
     * </pre>
     * @param payload input pay load json.
     * @return Map of "related-to" to list of "related-link" strings.
     */
    private static Map<String, List<String>> extractRelationships(JSONObject payload) {

        Map<String, List<String>> relationMap = new HashMap<>();

        if (!payload.has(RELATIONSHIP_LIST_KEY)) {
            return relationMap;
        }

        JSONObject relationshipList = payload.getJSONObject(RELATIONSHIP_LIST_KEY);

        if (!relationshipList.has(RELATIONSHIP_KEY)) {
            return relationMap;
        }

        JSONArray relationships = relationshipList.getJSONArray(RELATIONSHIP_KEY);

        for (int i = 0; i < relationships.length(); i++) {
            JSONObject obj = relationships.optJSONObject(i);
            String relatedToObj = obj.optString(JSON_ATT_RELATED_TO);

            if (relatedToObj == null) {
                logger.error("Related-To Object is null");
                continue;
            }
            List<String> relatedLinkList = relationMap.get(relatedToObj);
            if (relatedLinkList == null) {
                relatedLinkList = new ArrayList<>();
                relationMap.put(relatedToObj, relatedLinkList);
            }
            String relatedLinkObj = obj.getString(JSON_ATT_RELATED_LINK);
            relatedLinkList.add(relatedLinkObj);
        }

        return relationMap;
    }

    /**
     * @param client
     * @param url
     * @param aaiBasicAuthorization
     * @param transId
     * @return
     * @throws DiscoveryException
     */
    private static String getResource(RestClient client, String url, String aaiBasicAuthorization, String transId)
            throws DiscoveryException {
        OperationResult result = client.get(url, buildHeaders(aaiBasicAuthorization, transId), MediaType.valueOf(MediaType.APPLICATION_JSON));

        if (result.wasSuccessful()) {
            return result.getResult();
        } else if (result.getResultCode() == 404) {
            // Resource not found, generate empty JSON format
            logger.info("Resource for {} is not found, return empty Json format", url);
            return EMPTY_JSON_STRING;
        } else {
            throw new DiscoveryException(FETCH_RESOURCE_FAILED, Status.INTERNAL_SERVER_ERROR, result.getFailureCause());
        }
    }

    private static Map<String, List<String>> buildHeaders(String aaiBasicAuthorization, String transactionId) {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.put(TRANSACTION_ID, Collections.singletonList(transactionId));
        headers.put(FROM_APP_ID, Collections.singletonList(APP_NAME));
        headers.put(AUTHORIZATION, Collections.singletonList(aaiBasicAuthorization));
        return headers;
    }

    private static List<Resource> getResourceTypes(String aaiResourceList) {
        List<Resource> resources = new ArrayList<>();
        String noSpaceAaiResourceList = aaiResourceList.replaceAll("\\s", "");
        String[] resourceList = noSpaceAaiResourceList.split(",");
        for (int i = 0; i < resourceList.length; i++) {
                resources.add(new Resource(resourceList[i]));
        }
        return resources;
    }

}
