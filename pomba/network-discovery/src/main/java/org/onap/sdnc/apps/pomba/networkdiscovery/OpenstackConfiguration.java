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
package org.onap.sdnc.apps.pomba.networkdiscovery;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.jetty.util.security.Password;
import org.onap.aai.restclient.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class OpenstackConfiguration {
    @Autowired
    private Environment env;

    @Value("${openstack.identity.url:http://localhost:5000/v3}")
    private String identityUrl;

    @Value("${openstack.identity.user}")
    private String identityUser;

    @Value("${openstack.identity.password}")
    private String identityPassword;

    @Value("${openstack.api.microversion}")
    private String apiMicroversion;

    @Value("${openstack.identity.keyStorePath:config/auth/client-cert-onap.p12}")
    private String keyStorePath;

    @Value("${openstack.identity.keyStorePassword:OBF:1y0q1uvc1uum1uvg1pil1pjl1uuq1uvk1uuu1y10}")
    private String keyStorePassword;

    @Value("${openstack.connectionTimeout:5000}")
    private int connectionTimeout;

    @Value("${openstack.readTimeout:60000}")
    private int readTimeout;

    @Bean(name = "openstackClient")
    public RestClient restClient() {
        return new RestClient().validateServerHostname(false)
                        .validateServerCertChain(false)
                        .connectTimeoutMs(this.connectionTimeout)
                        .readTimeoutMs(this.readTimeout);
    }

    @Bean(name = "openstackIdentityUrl")
    public String getIdentityURL() {
        return this.identityUrl;
    }

    @Bean(name = "openstackIdentityUser")
    public String getIdentityUser() {
        return this.identityUser;
    }

    @Bean(name = "openstackIdentityPassword")
    public String getIdentityPassword() {
        return Password.deobfuscate(this.identityPassword);
    }

    @Bean(name = "openstackApiMicroversion")
    public String getApiMicroversion() {
        return this.apiMicroversion;
    }

    @Bean(name = "openstackTypeURLs")
    public Map<String, String> getOpenstackTypeURLs() {

        Map<String, String> result = new HashMap<>();
        String types = this.env.getProperty("openstack.types");
        if (types == null) {
            return result;
        }

        StringTokenizer tokenizer = new StringTokenizer(types, ", ");
        while (tokenizer.hasMoreTokens()) {
            String type = tokenizer.nextToken();
            String openstackUrl = this.env.getProperty("openstack.type." + type + ".url");
            result.put(type, openstackUrl);
        }

        return result;
    }

}
