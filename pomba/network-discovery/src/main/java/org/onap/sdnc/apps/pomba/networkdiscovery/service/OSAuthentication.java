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

import static org.onap.sdnc.apps.pomba.networkdiscovery.ApplicationException.Error.UNAUTHORIZED;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.onap.aai.restclient.client.OperationResult;
import org.onap.aai.restclient.client.RestClient;
import org.onap.logging.ref.slf4j.ONAPLogAdapter;
import org.onap.sdnc.apps.pomba.networkdiscovery.ApplicationException;

public class OSAuthentication {

    private static final String CONFIG_AUTH_DIR = "config/auth";
    private static final String X_SUBJECT_TOKEN = "X-Subject-Token";

    private static final String USER_PATTERN = "%USER%";    
    private static final String PASSWORD_PATTERN = "%PASSWORD%";

	public static String getToken(String openstackIdentityUrl, String userId, String password, RestClient openstackClient, ONAPLogAdapter adapter)
			throws FileNotFoundException, IOException, ApplicationException {

		MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();

		String mappingConfigPath = CONFIG_AUTH_DIR + File.separator + "osauth.json";

		File file = new File(mappingConfigPath);
		String payload = IOUtils.toString(new FileInputStream(file), "UTF-8");
        payload = payload.replaceAll(USER_PATTERN, userId);
		payload = payload.replaceAll(PASSWORD_PATTERN, password);

		OperationResult result = openstackClient.post(openstackIdentityUrl, payload, headers,
				MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_JSON_TYPE);

		adapter.unwrap().info("request at url = {} resulted in http response: {}", openstackIdentityUrl,
				result.getResult());

		String token = result.getHeaders().getFirst(X_SUBJECT_TOKEN);
		
		if (token == null) {
		    throw new ApplicationException(UNAUTHORIZED, Status.UNAUTHORIZED);
		}

		adapter.unwrap().debug("Got token: " + token);

		return token;
	}

}
