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
    @Value("${aai.host}")
    private String host;

    @Value("${aai.port}")
    private String port;

    @Value("${aai.httpProtocol}")
    private String httpProtocol;

    @Value("${aai.trustStorePath}")
    private String trustStorePath;

    @Value("${aai.keyStorePath}")
    private String keyStorePath;

    @Value("${aai.keyStorePassword}")
    private String keyStorePassword;

    @Value("${aai.keyManagerFactoryAlgorithm}")
    private String keyManagerFactoryAlgorithm;

    @Value("${aai.keyStoreType}")
    private String keyStoreType;

    @Value("${aai.securityProtocol}")
    private String securityProtocol;

    @Value("${aai.connectionTimeout}")
    private Integer connectionTimeout;

    @Value("${aai.readTimeout}")
    private Integer readTimeout;

    @Value("${aai.serviceInstancePath}")
    private String serviceInstancePath;

    @Value("${aai.resourceList}")
    private String resourceList;

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
    public String getServiceInstancePath() {
        return this.serviceInstancePath;
    }

    @Bean(name="aaiResourceList")
    public String getResourceList() {
        return this.resourceList;
    }
}
