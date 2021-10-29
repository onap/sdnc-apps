/*-
 * ============LICENSE_START=======================================================
 * ONAP - SDNC
 * ================================================================================
 * Copyright (C) 2020 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.sdnc.apps.ms.gra;

import org.onap.ccsdk.apps.ms.sliboot.controllers.RestconfApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = { "org.onap.sdnc.apps.ms.gra", "org.onap.sdnc.apps.ms.sanitytest","org.onap.ccsdk.apps.services" })
@EnableJpaRepositories(basePackages = { "org.onap.sdnc.apps.ms.gra", "org.onap.sdnc.apps.ms.sanitytest","org.onap.ccsdk.apps.ms.sliboot.data" })
@EntityScan(basePackages = { "org.onap.sdnc.apps.ms.gra","org.onap.sdnc.apps.ms.sanitytest", "org.onap.ccsdk.apps.ms.sliboot.data", "org.onap.ccsdk.apps.ms.sliboot.controllers" })
@EnableTransactionManagement
@Import(RestconfApiController.class)
@EnableSwagger2

public class GenericResourceMsApp {

    private static final Logger log = LoggerFactory.getLogger(GenericResourceMsApp.class);

	@Value( "${swagger-ui.host:localhost}" )
	private String swaggerUiHost;

    @Value( "${swagger-ui.title:'SDNC : API Documentation'}" )
    private String swaggerUiTitle;

    @Value( "${swagger-ui.description:'SDNC : API Documentation'}" )
    private String swaggerUiDescription;

    @Bean
    public Docket api() {
        if (!"localhost".equalsIgnoreCase(swaggerUiHost)) {
            return new Docket(DocumentationType.SWAGGER_2)
                    .host(swaggerUiHost)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("org.onap")).paths(PathSelectors.any())
                    .build()
                    .apiInfo(apiInfo());
        } else {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("org.onap")).paths(PathSelectors.any())
                    .build()
                    .apiInfo(apiInfo());
        }
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
               .title(swaggerUiTitle)
               .description(swaggerUiDescription)
               .build();
    }



    public static void main(String[] args) throws Exception {
        SpringApplication.run(GenericResourceMsApp.class, args);
    }


}
