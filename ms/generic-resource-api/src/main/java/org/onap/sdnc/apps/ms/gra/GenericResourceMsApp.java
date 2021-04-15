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

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.PropertiesRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.onap.aaf.cadi.shiro.AAFRealm;
import org.onap.ccsdk.apps.ms.sliboot.controllers.RestconfApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = { "org.onap.sdnc.apps.ms.gra", "org.onap.ccsdk.apps.services" })
@EnableJpaRepositories(basePackages = { "org.onap.sdnc.apps.ms.gra", "org.onap.ccsdk.apps.ms.sliboot" })
@EntityScan(basePackages = { "org.onap.sdnc.apps.ms.gra", "org.onap.ccsdk.apps.ms.sliboot" })
@EnableTransactionManagement
@Import(RestconfApiController.class)
@EnableSwagger2

public class GenericResourceMsApp {

    private static final Logger log = LoggerFactory.getLogger(GenericResourceMsApp.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GenericResourceMsApp.class, args);
    }
}
