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
import static org.onap.sdnc.apps.pomba.servicedecomposition.exception.DiscoveryException.Error.RELATIONSHIP_LINK_PARSE_ERROR;
import static org.onap.sdnc.apps.pomba.servicedecomposition.exception.DiscoveryException.Error.SERVICE_INSTANCE_NOT_FOUND;
import static org.onap.sdnc.apps.pomba.servicedecomposition.exception.DiscoveryException.Error.SERVICE_RELATIONSHIP_PARSE_ERROR;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.aai.restclient.client.OperationResult;
import org.onap.aai.restclient.client.RestClient;
import org.onap.logging.ref.slf4j.ONAPLogAdapter;
import org.onap.sdnc.apps.pomba.servicedecomposition.exception.DiscoveryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class RestUtil {
    // Parameters for Query AAI Model Data API
    // HTTP headers
    private static final String TRANSACTION_ID = "X-TransactionId";
    private static final String FROM_APP_ID = "X-FromAppId";
    private static final String APP_NAME = "aaiCtxBuilder";
    private static final String AUTHORIZATION = "Authorization";

    private static final Resource GENERIC_VNF = new Resource("generic-vnf");

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
    }

    private static final String JSON_ATT_RELATED_TO = "related-to";
    private static final String JSON_ATT_RELATED_LINK = "related-link";

    private static final String EMPTY_JSON_STRING = "{}";

    private static final String DEPTH = "?depth=2";
    private static Logger logger = LoggerFactory.getLogger(RestUtil.class);

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


    /*
     * Trigger external API call to AAI to retrieve Service Instance data (i.e. genericVNF and VNFC)
     */

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
            String transactionId, String serviceInstanceId, ONAPLogAdapter adapter) throws DiscoveryException {

        // Follow two variables for transform purpose
        String url = baseURL + generateServiceInstanceURL(aaiServiceInstancePath, serviceInstanceId);
        // Response from service instance API call
        JSONObject serviceInstancePayload = new JSONObject(
                getResource(aaiClient, url, aaiBasicAuthorization, transactionId));
        // Handle the case if the service instance is not found in AAI
        if (serviceInstancePayload == null || serviceInstancePayload.length() == 0) {
            logger.info("Service Instance " + serviceInstanceId + " is not found from AAI");
            // Only return the empty Json on the root level. i.e service instance
            throw new DiscoveryException(SERVICE_INSTANCE_NOT_FOUND, Status.NOT_FOUND);
        }

        HashMap<String, List<String>> relationMap = extractServiceRelationShips(serviceInstancePayload);
        logger.info("The number of the relationships for service instance id {} is: {}", serviceInstanceId,
                relationMap.size());

        JSONObject response = processVNFRelationMap(aaiClient, aaiResourceList, baseURL, aaiBasicAuthorization, transactionId, relationMap, serviceInstancePayload);
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
            HashMap<String, List<String>> relationMap, JSONObject serviceInstancePayload) throws DiscoveryException {
        List<JSONObject> vnfLst = new ArrayList<JSONObject>(); // List of the VNF JSON along with related resources

        JSONObject response = serviceInstancePayload;

        List<Resource> resourceTypes = getResourceTypes(aaiResourceList);

        if (relationMap.get(GENERIC_VNF.getResourceName()) != null) {
            List<JSONObject> vnfList = processResourceList(aaiClient, baseURL, aaiBasicAuthorization, transactionId, GENERIC_VNF.getResourceName(),
                    relationMap.get(GENERIC_VNF.getResourceName()));
            // Logic to Create the Generic VNF JSON and extract further relationships
            for (JSONObject vnfPayload : vnfList) {
                for (Resource resourceType : resourceTypes) {
                    List<String> vnfcLinkLst = extractRelatedLink(vnfPayload, resourceType.getResourceName());
                    if (vnfcLinkLst != null && !vnfcLinkLst.isEmpty()) {
                        logger.info("The number of the API call for vnfc is:" + vnfcLinkLst.size());
                        List<JSONObject> vnfcList = processResourceList(aaiClient, baseURL, aaiBasicAuthorization, transactionId,
                                resourceType.getResourceName(), vnfcLinkLst);
                        if (vnfcList != null) {
                            vnfPayload.put(resourceType.getCollectionName(), vnfcList);
                        }
                    } else {
                        logger.info("No " + resourceType.getResourceName() + " found for vnf-id:" + vnfPayload.getString("vnf-id"));
                    }
                }
            // Add final vnf payload to list
            vnfLst.add(vnfPayload);
            }
        } else {
            logger.info("No " + GENERIC_VNF.getResourceName() +  " found for :" + serviceInstancePayload.getString("service-instance-id"));
        }

        // Add generic vnf with related resource payload to response
        if (vnfLst != null && !vnfLst.isEmpty()) {
            response.put(GENERIC_VNF.getCollectionName(), vnfLst);
        }
        return response;

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
        List<JSONObject> resourcePayloadList = new ArrayList<JSONObject>();
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
            if (resourcePayload == null || resourcePayload.length() == 0) {
                logger.info("Resource with url " + resourceLink + " is not found from AAI");
            } else {
                resourcePayloadList.add(resourcePayload);
            }
        }
        return resourcePayloadList;
    }


    /**
     * @param serviceInstancePayload
     * @param relationMap
     * @return
     * @throws DiscoveryException
     */
    private static HashMap<String, List<String>> extractServiceRelationShips(JSONObject payload)
            throws DiscoveryException {

        JSONArray relationships = null;
        HashMap<String, List<String>> relationMap = new HashMap<String, List<String>>();
        logger.info("Fetching Service Instance Relationships");
        try {
            JSONObject relationshipList = payload.getJSONObject("relationship-list");
            if (relationshipList != null) {
                relationships = relationshipList.getJSONArray("relationship");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DiscoveryException(SERVICE_RELATIONSHIP_PARSE_ERROR, Status.INTERNAL_SERVER_ERROR,
                    e.getMessage());
        }

        if (relationships != null && relationships.length() > 0) {
            for (int i = 0; i < relationships.length(); i++) {
                JSONObject obj = relationships.optJSONObject(i);
                String relatedToObj = obj.getString(JSON_ATT_RELATED_TO);
                String relatedLinkObj = obj.getString(JSON_ATT_RELATED_LINK);

                if (relatedToObj == null) {
                    logger.info("Related-To Object found null");
                    continue;
                }
                List<String> relatedLinkList = relationMap.get(relatedToObj);
                if (relatedLinkList == null) {
                    relatedLinkList = new ArrayList<>();
                    relationMap.put(relatedToObj, relatedLinkList);
                }
                relatedLinkList.add(relatedLinkObj);
            }
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

        if (result.getResultCode() == 200) {
            String jsonString = result.getResult();
            return jsonString;
        } else if (result.getResultCode() == 404) {
            // Resource not found, generate empty JSON format
            logger.info("Resource for " + url + " is not found " + "return empty Json format");
            return EMPTY_JSON_STRING;
        } else {
            throw new DiscoveryException(FETCH_RESOURCE_FAILED, Status.INTERNAL_SERVER_ERROR, result.getFailureCause());
        }
    }

    /**
     * Extract the related-Link from Json payload. For example
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
     */
    private static List<String> extractRelatedLink(JSONObject payload, String catalog) throws DiscoveryException {
        List<String> relatedLinkList = new ArrayList<String>();
        JSONArray relationships = null;
        logger.info("Fetching relationships for resource type: " + catalog);
        try {
            JSONObject relationshipLst = payload.getJSONObject("relationship-list");
            if (relationshipLst != null) {
                relationships = relationshipLst.getJSONArray("relationship");
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DiscoveryException(RELATIONSHIP_LINK_PARSE_ERROR, Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        if (relationships != null && relationships.length() > 0) {
            for (int i = 0; i < relationships.length(); i++) {
                Object relatedToObj = null;
                Object relatedLinkObj = null;

                JSONObject obj = relationships.optJSONObject(i);
                relatedToObj = obj.get(JSON_ATT_RELATED_TO);

                if (relatedToObj.toString().equals(catalog)) {
                    relatedLinkObj = obj.get(JSON_ATT_RELATED_LINK);
                    if (relatedLinkObj != null) {
                        relatedLinkList.add(relatedLinkObj.toString());
                    }
                }

            }
        }
        if (relatedLinkList != null) {
            logger.info(
                    "Number of relationships found for resource type: " + catalog + " are: " + relatedLinkList.size());
        }
        return relatedLinkList;
    }



    private static Map<String, List<String>> buildHeaders(String aaiBasicAuthorization, String transactionId) {
        MultivaluedMap<String, String> headers = new MultivaluedMapImpl();
        headers.put(TRANSACTION_ID, Collections.singletonList(transactionId));
        headers.put(FROM_APP_ID, Collections.singletonList(APP_NAME));
        headers.put(AUTHORIZATION, Collections.singletonList(aaiBasicAuthorization));
        return headers;
    }

    private static List<Resource> getResourceTypes(String aaiResourceList) {
        List<Resource> resources = new ArrayList<Resource>();
        String noSpaceAaiResourceList = aaiResourceList.replaceAll("\\s", "");
        String[] resourceList = noSpaceAaiResourceList.split(",");
        for (int i = 0; i < resourceList.length; i++) {
                resources.add(new Resource(resourceList[i]));
        }
        return resources;
    }

}