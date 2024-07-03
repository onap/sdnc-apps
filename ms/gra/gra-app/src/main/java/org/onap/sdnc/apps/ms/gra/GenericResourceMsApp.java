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

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import org.onap.ccsdk.apps.ms.sliboot.controllers.RestconfApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = { "org.onap.sdnc.apps.ms.gra", "org.onap.sdnc.apps.ms.sanitytest","org.onap.ccsdk.apps.services" })
@EnableJpaRepositories(basePackages = { "org.onap.sdnc.apps.ms.gra", "org.onap.sdnc.apps.ms.sanitytest","org.onap.ccsdk.apps.ms.sliboot.data" })
@EntityScan(basePackages = { "org.onap.sdnc.apps.ms.gra","org.onap.sdnc.apps.ms.sanitytest", "org.onap.ccsdk.apps.ms.sliboot.data", "org.onap.ccsdk.apps.ms.sliboot.controllers" })
@EnableTransactionManagement
@Import(RestconfApiController.class)
// @EnableSwagger2
@EnableWebMvc

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
        String groupName = "Swagger";
        if (!"localhost".equalsIgnoreCase(swaggerUiHost)) {
            return new Docket(DocumentationType.SWAGGER_2)
                    .groupName(groupName)
                    .host(swaggerUiHost)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("org.onap")).paths(PathSelectors.any())
                    .build()
                    .apiInfo(apiInfo());
        } else {
            return new Docket(DocumentationType.SWAGGER_2)
                    .groupName(groupName)
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

    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                List<T> copy = mappings.stream()
                        .filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(copy);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    field.setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GenericResourceMsApp.class, args);
    }


}
