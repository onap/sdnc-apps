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

package org.onap.sdnc.apps.pomba.servicedecomposition.service.rs;

import static org.onap.sdnc.apps.pomba.servicedecomposition.exception.DiscoveryException.Error.*;
import java.util.UUID;
import javax.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import org.onap.logging.ref.slf4j.ONAPLogAdapter;
import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.onap.logging.ref.slf4j.ONAPLogConstants.ResponseStatus;
import org.onap.sdnc.apps.pomba.servicedecomposition.exception.DiscoveryException;
import org.onap.sdnc.apps.pomba.servicedecomposition.service.SpringService;
import org.onap.sdnc.apps.pomba.servicedecomposition.util.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestServiceImpl implements RestService {
    private static Logger log = LoggerFactory.getLogger(RestServiceImpl.class);
    private static final String EMPTY_JSON_OBJECT = "{}";
    private static UUID instanceId = UUID.randomUUID();

    @Autowired
    private SpringService service;

    @Resource(name="basicAuthHeader")
    private String basicAuthHeader;

    @Override
    public Response getContext(HttpServletRequest request,
                               String authorization,
                               String fromAppId,
                               String transactionId,
                               String serviceInstanceId) {

        ONAPLogAdapter adapter = new ONAPLogAdapter(log);
        adapter.getServiceDescriptor().setServiceName(SERVICE_NAME);
        adapter.getServiceDescriptor().setServiceUUID(instanceId.toString());
        adapter.entering(request);
        try {
            if (authorization == null || !this.basicAuthHeader.equals(authorization)) {
                throw new DiscoveryException(UNAUTHORIZED, Status.UNAUTHORIZED);
            }

            // Do some validation on Http headers and URL parameters
            if ((fromAppId == null) || fromAppId.trim().isEmpty()) {
                throw new DiscoveryException(MISSING_HEADER, Status.BAD_REQUEST, ONAPLogConstants.Headers.PARTNER_NAME);
            }
            if (transactionId == null || transactionId.isEmpty()) {
                transactionId = UUID.randomUUID().toString();
                log.debug("{} is missing; using newly generated value: {}", ONAPLogConstants.Headers.REQUEST_ID, transactionId);
            }
            RestUtil.validateURL(serviceInstanceId);
            String context = service.decomposeService(fromAppId, transactionId, serviceInstanceId, adapter);

            if (context == null) {
                context = EMPTY_JSON_OBJECT;
            }
            adapter.getResponseDescriptor().setResponseStatus(ResponseStatus.COMPLETED);
            return Response.ok().entity(context).build();

        } catch (DiscoveryException x) {
            log.error(x.getHttpStatus().getReasonPhrase(), x);
            adapter.getResponseDescriptor()
                    .setResponseCode(x.getResponseCode())
                    .setResponseStatus(ResponseStatus.ERROR);

            ResponseBuilder builder = Response.status(x.getHttpStatus()).entity(x.getMessage());
            if (authorization == null) {
                builder.header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\""+ SERVICE_NAME + "\"");
            }
            return builder.build();

        } catch (Exception e) {
            log.error(Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), e);
            adapter.getResponseDescriptor()
                    .setResponseCode(GENERAL_FAILURE.getResponseCode())
                    .setResponseStatus(ResponseStatus.ERROR);

            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } finally {
            adapter.exiting();
        }
    }
}
