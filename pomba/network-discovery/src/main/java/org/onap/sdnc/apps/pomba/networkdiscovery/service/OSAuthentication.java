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
package org.onap.sdnc.apps.pomba.networkdiscovery.service;

import java.io.File;
import java.io.FileInputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;

import org.apache.commons.io.IOUtils;
import org.onap.logging.ref.slf4j.ONAPLogAdapter;
import org.onap.sdnc.apps.pomba.networkdiscovery.ApplicationException;

public class OSAuthentication {

    private static final String CONFIG_AUTH_DIR = "config/auth";
    private static final String X_SUBJECT_TOKEN = "X-Subject-Token";

    private static final String USER_PATTERN = "%USER%";
    private static final String PW_PATTERN = "%PASSWORD%";

    private OSAuthentication() {
        throw new IllegalStateException("Utility class");
    }

    public static String getToken(String openstackIdentityUrl, String userId, String password, Client openstackClient,
            ONAPLogAdapter adapter) throws ApplicationException {

        String mappingConfigPath = CONFIG_AUTH_DIR + File.separator + "osauth.json";
        File file = new File(mappingConfigPath);

        Response result;
        try {
            String payload = IOUtils.toString(new FileInputStream(file), "UTF-8");
            payload = payload.replaceAll(USER_PATTERN, userId);
            payload = payload.replaceAll(PW_PATTERN, password);

            result = openstackClient.target(openstackIdentityUrl).request()
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            // catch the case when the connection times out (e.g., host not available).
            throw new ApplicationException(ApplicationException.Error.GENERAL_FAILURE, Status.NOT_FOUND,
                    "Openstack API login failed - " + e.getMessage());
        }

        String jsonResult = result.readEntity(String.class);
        
        if (result.getStatusInfo().getFamily() != Family.SUCCESSFUL) {
            throw new ApplicationException(ApplicationException.Error.GENERAL_FAILURE, Status.NOT_FOUND, jsonResult);
        }

        String token = result.getHeaderString(X_SUBJECT_TOKEN);
        if (token == null) {
            throw new ApplicationException(ApplicationException.Error.GENERAL_FAILURE, Status.NOT_FOUND,
                    "Failed to get token from Openstack API response");
        }

        adapter.unwrap().debug("Got token: {}", token);

        return token;
    }

}
