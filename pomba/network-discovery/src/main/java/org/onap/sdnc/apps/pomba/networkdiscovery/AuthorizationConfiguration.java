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

import java.util.Base64;
import org.eclipse.jetty.util.security.Password;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationConfiguration {

    @Value("${basicAuth.username:admin}")
    private String username;

    @Value("${basicAuth.password:OBF:1u2a1toa1w8v1tok1u30}")
    private String password;

    @Bean(name="basicAuthHeader")
    public String getNdBasicAuthHeader() {
        return "Basic " + Base64.getEncoder().encodeToString((this.username + ":" + Password.deobfuscate(this.password)).getBytes());
    }
}
