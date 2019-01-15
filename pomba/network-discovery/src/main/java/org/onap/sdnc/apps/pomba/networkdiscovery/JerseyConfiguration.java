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

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.onap.sdnc.apps.pomba.networkdiscovery.service.rs.RestService;
import org.onap.sdnc.apps.pomba.networkdiscovery.service.rs.RestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

@Component
@ApplicationPath("/network-discovery")
public class JerseyConfiguration extends ResourceConfig {

    public static final String SERVICE_NAME = "network-discovery";

    @Value("${spring.jersey.application-path:/network-discovery}")
    private String apiPath;   

    private static final Logger log = Logger.getLogger(JerseyConfiguration.class.getName());

    @Autowired
    public JerseyConfiguration() {
        register(RestServiceImpl.class);
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
        register(new LoggingFeature(log));
    }

    @Bean
    public Client jerseyClient() {
        return ClientBuilder.newClient(new ClientConfig());
    }
    
    @PostConstruct
    public void init() {
        // Register components where DI is needed
        configureSwagger();
    }

    private void configureSwagger() {
        // Available at localhost:port/swagger.json
        this.register(ApiListingResource.class);
        this.register(SwaggerSerializers.class);

        BeanConfig config = new BeanConfig();
        config.setTitle("Network Discovery API");
        config.setVersion("v1");
        config.setSchemes(new String[] { "https", "http" });        
        config.setBasePath(apiPath);
        config.setResourcePackage(RestService.class.getPackage().getName());
        config.setPrettyPrint(true);
        config.setScan(true);
    }
    
}
