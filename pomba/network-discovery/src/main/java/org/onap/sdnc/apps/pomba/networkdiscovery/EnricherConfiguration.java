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
import org.onap.aai.restclient.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class EnricherConfiguration {
    @Autowired
    private Environment env;

    @Value("${enricher.url}")
    private String url;

    @Value("${enricher.keyStorePath}")
    private String keyStorePath;

    @Value("${enricher.keyStorePassword}")
    private String keyStorePassword;

    @Value("${enricher.connectionTimeout:5000}")
    private int connectionTimeout;

    @Value("${enricher.readTimeout:60000}")
    private int readTimeout;

    @Bean(name="enricherClient")
    public RestClient restClient() {
        return new RestClient()
                .validateServerHostname(false)
                .validateServerCertChain(false)
                .connectTimeoutMs(this.connectionTimeout)
                .readTimeoutMs(this.readTimeout)
                .clientCertFile(this.keyStorePath)
                .clientCertPassword(this.keyStorePassword);
    }

    @Bean(name="enricherBaseUrl")
    public String getURL() {
        return this.url;
    }

    @Bean(name="enricherTypeURLs")
    public Map<String, String> enricherTypeURLs() {

        Map<String, String> result = new HashMap<>();
        String types = this.env.getProperty("enricher.types");
        if (types == null) {
            return result;
        }

        StringTokenizer tokenizer = new StringTokenizer(types, ", ");
        while (tokenizer.hasMoreTokens()) {
            String type = tokenizer.nextToken();
            String enricherUrl = this.env.getProperty("enricher.type." + type + ".url");
            result.put(type, enricherUrl);
        }

        return result;
    }


}
