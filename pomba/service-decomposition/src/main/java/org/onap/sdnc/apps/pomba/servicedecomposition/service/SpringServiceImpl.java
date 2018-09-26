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
package org.onap.sdnc.apps.pomba.servicedecomposition.service;

import org.json.JSONObject;
import org.onap.aai.restclient.client.RestClient;
import org.onap.logging.ref.slf4j.ONAPLogAdapter;
import org.onap.sdnc.apps.pomba.servicedecomposition.exception.DiscoveryException;
import org.onap.sdnc.apps.pomba.servicedecomposition.util.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.onap.sdnc.apps.pomba.servicedecomposition.exception.DiscoveryException.Error.*;


@org.springframework.stereotype.Service
public class SpringServiceImpl implements SpringService {
    private static Logger log = LoggerFactory.getLogger(SpringService.class);
    public static final String APP_NAME = "ServiceDiscovery";

    @Autowired
    private RestClient aaiClient;

    @Autowired
    private String aaiBaseUrl;

    @Autowired
    private String aaiBasicAuthorization;

    @Autowired
    private String aaiServiceInstancePath;

    @Autowired
    private String aaiResourceList;

    @Override
    public String decomposeService(String fromAppId,
                                   String transactionId,
                                   String serviceInstanceId,
                                   ONAPLogAdapter adapter) throws DiscoveryException {


        log.info("Querying A&AI for service instance " + serviceInstanceId);
        JSONObject serviceInstance = null;

        try {
            serviceInstance = RestUtil.retrieveAAIModelData(aaiClient, aaiBaseUrl, aaiBasicAuthorization, aaiServiceInstancePath, aaiResourceList,
                    transactionId, serviceInstanceId, adapter);
        } catch (DiscoveryException de) {
            throw de;
        } catch (Exception e) {
            throw  new DiscoveryException(GENERAL_FAILURE, e , e.getLocalizedMessage());
        }
        return serviceInstance.toString();
    }


}
