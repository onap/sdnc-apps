/*
 *  ============LICENSE_START===================================================
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CallbackConfiguration {
    /** Use same object mapper as embedded tomcat server */
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${callback.keyStorePath:}")
    private String keyStorePath;

    @Value("${callback.keyStorePassword:}")
    private String keyStorePassword;

    @Value("${callback.connectionTimeout:5000}")
    private int connectionTimeout;

    @Value("${callback.readTimeout:60000}")
    private int readTimeout;

    @Value("${callback.username:}")
    private String basicAuthUsername;

    @Value("${callback.password:}")
    private String basicAuthPassword;

    @Bean(name="callbackClient")
    public Client getClient() throws Exception {
        ClientConfig configuration = new ClientConfig()
                .property(ClientProperties.CONNECT_TIMEOUT, this.connectionTimeout)
                .property(ClientProperties.READ_TIMEOUT, this.readTimeout)
                .register(new JacksonJaxbJsonProvider(objectMapper, null));

        // TODO set up SSL if configured
//      if (useSsl) {
//      setupSecureSocketLayerClientConfig(clientConfig);
//  }
        // Finally, create and initialize our client...
        Client client = ClientBuilder.newClient(configuration);

        // ...and return it to the caller.
        return client;
    }


}
