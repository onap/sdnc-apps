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

    // Service Catalog
    public enum Catalog {

        VNF("generic-vnf"), VNFC("vnfc"), VSERVER("vserver"), L3NETWORK("l3-network");

        private String resourceName;

        Catalog(String objName) {
            this.resourceName = objName;
        }

        public String objName() {
            return resourceName;
        }
    };


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
     * @param aaiServiceInstancePath
     * @param transactionId
     * @param serviceInstanceId
     * @param modelVersionId
     * @param modelInvariantId
     * @return
     * @throws DiscoveryException
     */
    public static JSONObject retrieveAAIModelData(RestClient aaiClient, String baseURL, String aaiServiceInstancePath,
            String transactionId, String serviceInstanceId, ONAPLogAdapter adapter) throws DiscoveryException {
        JSONObject serviceInstancePayload = null;
        JSONObject response = null;

        // Follow two variables for transform purpose
        String url = baseURL + generateServiceInstanceURL(aaiServiceInstancePath, serviceInstanceId);
        // Response from service instance API call
        serviceInstancePayload = new JSONObject(
                getResource(aaiClient, url, transactionId, MediaType.valueOf(MediaType.APPLICATION_XML)));

        // Handle the case if the service instance is not found in AAI
        if (serviceInstancePayload == null || serviceInstancePayload.length() == 0) {
            logger.info("Service Instance " + serviceInstanceId + " is not found from AAI");
            // Only return the empty Json on the root level. i.e service instance
            throw new DiscoveryException(SERVICE_INSTANCE_NOT_FOUND, Status.NOT_FOUND);
        }

        HashMap<String, List<String>> relationMap = extractServiceRelationShips(serviceInstancePayload);
        logger.info("The number of the relationships for service instance id %d is: %d", serviceInstanceId,
                relationMap.size());

        response = processVNFRelationMap(aaiClient, baseURL, transactionId, relationMap, serviceInstancePayload);

        return response;
    }


    /**
     * @param aaiClient
     * @param baseURL
     * @param transactionId
     * @param relationMap
     * @throws DiscoveryException
     */
    private static JSONObject processVNFRelationMap(RestClient aaiClient, String baseURL, String transactionId,
            HashMap<String, List<String>> relationMap, JSONObject serviceInstancePayload) throws DiscoveryException {
        List<JSONObject> vnfLst = new ArrayList<JSONObject>(); // List of the VNF JSON along with related resources

        JSONObject response = serviceInstancePayload;


        if (relationMap.get(Catalog.VNF.resourceName) != null) {
            List<JSONObject> vnfList = processResourceList(aaiClient, baseURL, transactionId, Catalog.VNF.resourceName,
                    relationMap.get(Catalog.VNF.resourceName));
            // Logic to Create the Generic VNF JSON and extract further relationships
            for (JSONObject vnfPayload : vnfList) {
                List<String> vnfcLinkLst = extractRelatedLink(vnfPayload, Catalog.VNFC.resourceName);
                if (vnfcLinkLst != null && vnfcLinkLst.size() != 0) {
                    logger.info("The number of the API call for vnfc is:" + vnfcLinkLst.size());
                    List<JSONObject> vnfcList = processResourceList(aaiClient, baseURL, transactionId,
                            Catalog.VNFC.resourceName, vnfcLinkLst);
                    if (vnfcList != null) {
                        vnfPayload.put(Catalog.VNFC.resourceName, vnfcList);
                    }
                } else {
                    logger.info("No vnfc found for vnf-id:" + vnfPayload.getString("vnf-id"));
                }

                List<String> networkLinkLst = extractRelatedLink(vnfPayload, Catalog.L3NETWORK.resourceName);
                if (networkLinkLst != null && networkLinkLst.size() != 0) {
                    logger.info("The number of the API call for l3-network is:" + networkLinkLst.size());
                    List<JSONObject> networkList = processResourceList(aaiClient, baseURL, transactionId,
                            Catalog.L3NETWORK.resourceName, networkLinkLst);
                    if (networkList != null) {
                        vnfPayload.put(Catalog.L3NETWORK.resourceName, networkList);
                    }
                } else {
                    logger.info("No l3-network found for vnf-id:" + vnfPayload.getString("vnf-id"));
                }
                List<String> vserverLinkLst = extractRelatedLink(vnfPayload, Catalog.VSERVER.resourceName);
                if (vserverLinkLst != null && vserverLinkLst.size() != 0) {
                    logger.info("The number of the API call for vserver is:" + vserverLinkLst.size());
                    List<JSONObject> vserverList = processResourceList(aaiClient, baseURL, transactionId,
                            Catalog.VSERVER.resourceName, vserverLinkLst);
                    if (vserverList != null) {
                        vnfPayload.put(Catalog.VSERVER.resourceName, vserverList);
                    }
                } else {
                    logger.info("No vserver found for vnf-id:" + vnfPayload.getString("vnf-id"));
                }

                // Add final vnf payload to list
                vnfLst.add(vnfPayload);
            }
        } else {
            logger.info("No generic vnf found for :" + serviceInstancePayload.getString("service-instance-id"));
        }

        // Add generic vnf with related resource payload to response
        if (vnfLst != null && vnfLst.size() != 0) {
            response.put("generic-vnfs", vnfLst);
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
    private static List<JSONObject> processResourceList(RestClient aaiClient, String aaiBaseURL, String transactionId,
            String resourceType, List<String> resourceList) throws DiscoveryException {
        List<JSONObject> resourcePayloadList = new ArrayList<JSONObject>();
        String resourceURL = null;
        for (String resourceLink : resourceList) {
            // With latest AAI development, in order to retrieve the both generic VNF + vf_module, we can use
            // one API call but with depth=2
            if (resourceType.equals(Catalog.VNF.resourceName)) {
                resourceURL = aaiBaseURL + resourceLink + DEPTH;
            } else {
                resourceURL = aaiBaseURL + resourceLink;
            }
            // Response from generic VNF API call
            JSONObject resourcePayload = new JSONObject(
                    getResource(aaiClient, resourceURL, transactionId, MediaType.valueOf(MediaType.APPLICATION_XML)));
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

        JSONObject relationshipList = null;
        JSONArray relationships = null;
        List<String> relatedLinkList = null;
        HashMap<String, List<String>> relationMap = new HashMap<String, List<String>>();
        logger.info("Fetching Service Instance Relationships");
        try {
            relationshipList = payload.getJSONObject("relationship-list");
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
                Object relatedToObj = null;
                Object relatedLinkObj = null;

                JSONObject obj = relationships.optJSONObject(i);
                relatedToObj = obj.get(JSON_ATT_RELATED_TO);
                relatedLinkObj = obj.get(JSON_ATT_RELATED_LINK);

                if (relatedToObj == null) {
                    logger.info("Related-To Object found null");
                } else if (relationMap.get(relatedToObj.toString()) == null) {
                    relatedLinkList = new ArrayList<String>();
                    relatedLinkList.add(relatedLinkObj.toString());
                    relationMap.put(relatedToObj.toString(), relatedLinkList);
                } else if (relationMap.get(relatedToObj.toString()) != null) {
                    relatedLinkList = relationMap.get(relatedToObj.toString());
                    relatedLinkList.add(relatedLinkObj.toString());
                    relationMap.put(relatedToObj.toString(), relatedLinkList);
                }

            }
        }
        return relationMap;
    }

    /**
     * @param client
     * @param url
     * @param transId
     * @param mediaType
     * @return
     * @throws DiscoveryException
     */
    private static String getResource(RestClient client, String url, String transId, MediaType mediaType)
            throws DiscoveryException {
        OperationResult result = client.get(url, buildHeaders(transId), MediaType.valueOf(MediaType.APPLICATION_JSON));

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



    /*
     * Extract the related-Link from Json payload. For example
     *   {
         "related-to": "vnfc",
         "related-link": "/aai/v11/network/vnfcs/vnfc/zrdm5aepdg01vmg003",
         "relationship-data": [         {
            "relationship-key": "vnfc.vnfc-name",
            "relationship-value": "zrdm5aepdg01vmg003"
         }]
      },
     */
    /**
     * @param payload
     * @param catalog
     * @return
     * @throws DiscoveryException
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



    private static Map<String, List<String>> buildHeaders(String transactionId) {
        MultivaluedMap<String, String> headers = new MultivaluedMapImpl();
        headers.put(TRANSACTION_ID, Collections.singletonList(transactionId));
        headers.put(FROM_APP_ID, Collections.singletonList(APP_NAME));
        return headers;
    }

    public static boolean isEmptyJson(String serviceInstancePayload) {

        return (serviceInstancePayload.equals(EMPTY_JSON_STRING)) ? true : false;
    }

}
