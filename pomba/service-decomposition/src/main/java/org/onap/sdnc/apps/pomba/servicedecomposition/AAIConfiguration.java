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
package org.onap.sdnc.apps.pomba.servicedecomposition;

import org.onap.aai.restclient.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AAIConfiguration {
    @Autowired
    @Value("${aai.host}")
    private String host;

    @Autowired
    @Value("${aai.port}")
    private String port;

    @Autowired
    @Value("${aai.httpProtocol}")
    private String httpProtocol;

    @Autowired
    @Value("${aai.trustStorePath}")
    private String trustStorePath;

    @Autowired
    @Value("${aai.keyStorePath}")
    private String keyStorePath;

    @Autowired
    @Value("${aai.keyStorePassword}")
    private String keyStorePassword;

    @Autowired
    @Value("${aai.keyManagerFactoryAlgorithm}")
    private String keyManagerFactoryAlgorithm;

    @Autowired
    @Value("${aai.keyStoreType}")
    private String keyStoreType;

    @Autowired
    @Value("${aai.securityProtocol}")
    private String securityProtocol;

    @Autowired
    @Value("${aai.connectionTimeout}")
    private Integer connectionTimeout;

    @Autowired
    @Value("${aai.readTimeout}")
    private Integer readTimeout;

    @Autowired
    @Value("${aai.serviceInstancePath}")
    private String serviceInstancePath;

    @Bean(name="aaiClient")
    public RestClient restClient() {
        return new RestClient()
                .validateServerHostname(false)
                .validateServerCertChain(false)
                .connectTimeoutMs(this.connectionTimeout)
                .readTimeoutMs(this.readTimeout);
    }

    @Bean(name="aaiBaseUrl")
    public String getURL() {
        return this.httpProtocol + "://" + this.host + ":" + this.port;
    }

    @Bean(name="aaiServiceInstancePath")
    public String getserviceInstancePathL() {
        return this.serviceInstancePath;
    }

}
