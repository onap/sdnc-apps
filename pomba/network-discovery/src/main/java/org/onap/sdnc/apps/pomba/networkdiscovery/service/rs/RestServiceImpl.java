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

package org.onap.sdnc.apps.pomba.networkdiscovery.service.rs;

import static org.onap.sdnc.apps.pomba.networkdiscovery.ApplicationException.Error.GENERAL_FAILURE;
import static org.onap.sdnc.apps.pomba.networkdiscovery.ApplicationException.Error.MISSING_PARAM;
import static org.onap.sdnc.apps.pomba.networkdiscovery.ApplicationException.Error.UNAUTHORIZED;

import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import org.onap.logging.ref.slf4j.ONAPLogAdapter;
import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.onap.logging.ref.slf4j.ONAPLogConstants.ResponseStatus;
import org.onap.sdnc.apps.pomba.networkdiscovery.ApplicationException;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.NetworkDiscoveryResponse;
import org.onap.sdnc.apps.pomba.networkdiscovery.service.SpringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestServiceImpl implements RestService {

    private static Logger log = LoggerFactory.getLogger(RestService.class);

    @Autowired
    private SpringService service;

    @Resource(name="basicAuthHeader")
    private String basicAuthHeader;

    @Override
    public Response findbyResourceIdAndType(HttpServletRequest request,
                                            String version,
                                            String authorization,
                                            String fromAppId,
                                            String transactionId,
                                            String requestId,
                                            String resourceType,
                                            List<String> resourceIds,
                                            String notificationURL) throws ApplicationException {

        ONAPLogAdapter adapter = new ONAPLogAdapter(log);

        try {
            adapter.getServiceDescriptor().setServiceName(SERVICE_NAME);
            adapter.entering(request);

            if (version == null) {
                throw new ApplicationException(MISSING_PARAM, Status.BAD_REQUEST, "version");
            }

            if (authorization == null || !this.basicAuthHeader.equals(authorization)) {
                throw new ApplicationException(UNAUTHORIZED, Status.UNAUTHORIZED);
            }
            if ((fromAppId == null) || fromAppId.trim().isEmpty()) {
                throw new ApplicationException(MISSING_PARAM, Status.BAD_REQUEST, ONAPLogConstants.Headers.PARTNER_NAME);
            }
            if (requestId == null || requestId.isEmpty()) {
                throw new ApplicationException(MISSING_PARAM, Status.BAD_REQUEST, "requestId");
            }
            if (notificationURL == null) {
                throw new ApplicationException(MISSING_PARAM, Status.BAD_REQUEST, "notificationURL");
            }
            if (resourceType == null || resourceType.isEmpty()) {
                throw new ApplicationException(MISSING_PARAM, Status.BAD_REQUEST, "resourceType");
            }
            if (resourceIds == null || resourceIds.isEmpty()) {
                throw new ApplicationException(MISSING_PARAM, Status.BAD_REQUEST, "resourceIds");
            }

            if (transactionId == null || transactionId.isEmpty()) {
                transactionId = UUID.randomUUID().toString();
                log.debug("transactionId is missing; using newly generated value: {}", transactionId);
            }

            // just reuse received Authorization header in callback
            NetworkDiscoveryResponse response = this.service.findbyResourceIdAndType(transactionId,
                    requestId, resourceType, resourceIds, notificationURL, authorization, adapter);
            adapter.getResponseDescriptor().setResponseStatus(ResponseStatus.COMPLETED);
            return Response.ok(response).build();

        } catch (ApplicationException x) {
            adapter.getResponseDescriptor()
                    .setResponseCode(x.getResponseCode())
                    .setResponseStatus(ResponseStatus.ERROR);
            log.error(x.getMessage(), x);
            ResponseBuilder builder = Response.status(x.getHttpStatus()).entity(x.getMessage());
            if (authorization == null) {
                builder.header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"network-discovery\"");
            }
            return builder.build();

        } catch (Exception x) {
            adapter.getResponseDescriptor()
                    .setResponseCode(GENERAL_FAILURE.getResponseCode())
                    .setResponseStatus(ResponseStatus.ERROR);
            log.error(GENERAL_FAILURE.getMessage(x), x);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(x.getMessage()).build();
        } finally {
            adapter.exiting();
        }
    }
}
