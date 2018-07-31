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

import com.att.eelf.configuration.EELFLogger;
import com.att.eelf.configuration.EELFManager;
import org.json.JSONObject;
import org.onap.aai.restclient.client.RestClient;
import org.onap.logging.ref.slf4j.ONAPLogAdapter;
import org.onap.sdnc.apps.pomba.servicedecomposition.exception.DiscoveryException;
import org.onap.sdnc.apps.pomba.servicedecomposition.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;


@org.springframework.stereotype.Service
public class SpringServiceImpl implements SpringService {
    private static EELFLogger log = EELFManager.getInstance().getLogger(SpringServiceImpl.class);
    public static final String APP_NAME = "ServiceDiscovery";

    @Autowired
    private RestClient aaiClient;

    @Autowired
    private String aaiBaseUrl;

    @Autowired
    private String aaiServiceInstancePath;

    @Override
    public String decomposeService(String fromAppId,
                                   String transactionId,
                                   String serviceInstanceId,
                                   ONAPLogAdapter adapter) throws DiscoveryException {


        log.info("Querying A&AI for service instance " + serviceInstanceId);
        JSONObject serviceInstance = RestUtil.retrieveAAIModelData(aaiClient, aaiBaseUrl, aaiServiceInstancePath,
                transactionId, serviceInstanceId, adapter);
        return serviceInstance.toString();
    }


}