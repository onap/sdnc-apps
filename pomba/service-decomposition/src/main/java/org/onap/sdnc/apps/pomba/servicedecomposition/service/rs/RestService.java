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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import jakarta.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.onap.logging.ref.slf4j.ONAPLogConstants;

@Api(authorizations = @Authorization("basicAuth"))
@Path("/service")
@Produces(MediaType.APPLICATION_JSON)
public interface RestService {

    public static final String SERVICE_NAME = "service-decomposition";

    @GET
    @Path("/context")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Fetch network info for service",
            notes = "Returns a A&AI JSON object augmented with related entities"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 400, message = "Bad Request"),
                    @ApiResponse(code = 404, message = "Service not available"),
                    @ApiResponse(code = 500, message = "Unexpected Runtime error")
                    })
    public Response getContext(
            @Context HttpServletRequest request,
            @HeaderParam(HttpHeaders.AUTHORIZATION) @ApiParam(hidden=true) String authorization,
            @HeaderParam(ONAPLogConstants.Headers.PARTNER_NAME) @ApiParam(required=true) String xFromAppId,
            @HeaderParam(ONAPLogConstants.Headers.REQUEST_ID) String xTransactionId,
            @QueryParam("serviceInstanceId") @ApiParam(required=true) String serviceInstanceId);
}
