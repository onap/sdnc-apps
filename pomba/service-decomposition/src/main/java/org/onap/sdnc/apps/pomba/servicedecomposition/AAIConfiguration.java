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

import java.util.Base64;
import org.eclipse.jetty.util.security.Password;
import org.onap.aai.restclient.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
public class AAIConfiguration {
    @Value("${aai.serviceName}")
    private String host;

    @Value("${aai.servicePort}")
    private String port;

    @Value("${aai.username}")
    private String aaiUsername;

    @Value("${aai.password}")
    private String aaiPassword;

    @Value("${aai.httpProtocol}")
    private String httpProtocol;

    @Value("${aai.securityProtocol}")
    private String securityProtocol;

    @Value("${aai.authentication}")
    private String authenticationMode;

    @Value("${aai.trustStorePath}")
    private String trustStorePath;

    @Value("${aai.keyStorePath}")
    private String keyStorePath;

    @Value("${aai.keyStorePassword}")
    private String keyStorePassword;

    @Value("${aai.connectionTimeout}")
    private Integer connectionTimeout;

    @Value("${aai.readTimeout}")
    private Integer readTimeout;

    @Value("${aai.serviceInstancePath}")
    private String serviceInstancePath;

    @Value("${aai.resourceList}")
    private String resourceList;

    @Value("${aai.urlDepth}")
    private String urlDepth;


    @Value("${basicAuth.username:admin}")
    private String username;

    @Value("${basicAuth.password:OBF:1u2a1toa1w8v1tok1u30}")
    private String password;

    @Bean(name="aaiBasicAuthorization")
    public String getAAIBasicAuth() {
        return "Basic " + Base64.getEncoder().encodeToString((this.aaiUsername + ":" + Password.deobfuscate(this.aaiPassword)).getBytes());
    }

    @Bean(name="basicAuthHeader")
    public String getSdBasicAuthHeader() {
        return "Basic " + Base64.getEncoder().encodeToString((this.username + ":" + Password.deobfuscate(this.password)).getBytes());
    }

    @Conditional(AAIBasicAuthCondition.class)
    @Bean(name="aaiClient")
    public RestClient restClientWithBasicAuth() {
        return new RestClient()
                .validateServerHostname(false)
                .validateServerCertChain(false)
                .basicAuthPassword(aaiUsername)
                .basicAuthPassword(Password.deobfuscate(aaiPassword))
                .connectTimeoutMs(this.connectionTimeout)
                .readTimeoutMs(this.readTimeout);
    }

    @Conditional(AAIClientCertCondition.class)
    @Bean(name="aaiClient")
    public RestClient restClientWithClientCert() {
        RestClient restClient = new RestClient();
        if ("https".equals(httpProtocol))
            restClient.validateServerHostname(false).validateServerCertChain(false).trustStore(trustStorePath).clientCertFile(keyStorePath).clientCertPassword(keyStorePassword).connectTimeoutMs(connectionTimeout).readTimeoutMs(readTimeout);
        else
            restClient.validateServerHostname(false).validateServerCertChain(false).connectTimeoutMs(connectionTimeout).readTimeoutMs(readTimeout);
        return restClient;
    }

    @Bean(name="aaiBaseUrl")
    public String getURL() {
        return this.httpProtocol + "://" + this.host + ":" + this.port;
    }

    @Bean(name="aaiUrlDepth")
    public String getUrlDepth() {
        return this.urlDepth;
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
