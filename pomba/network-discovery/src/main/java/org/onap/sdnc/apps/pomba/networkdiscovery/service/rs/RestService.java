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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.onap.sdnc.apps.pomba.networkdiscovery.ApplicationException;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.NetworkDiscoveryNotification;


@Api(protocols="http", tags= {"resource"})
@Path("{version: v1}/network")
@Produces(MediaType.APPLICATION_JSON)
public interface RestService {

    @GET
    @Path("/resource")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Get Network Information",
            notes = "Retrieve information from primary data sources",
            authorizations = @Authorization("basicAuth")
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Request has completed.", response = NetworkDiscoveryNotification.class),
                    @ApiResponse(code = 400, message = "Missing mandatory field in the request or HTTP header."),
                    @ApiResponse(code = 401, message = "Missing basicAuth header."),
                    @ApiResponse(code = 404, message = "Requested resource was not found."),
                    @ApiResponse(code = 500, message = "Request failed due to internal error")
            })
    public Response findbyResourceIdAndType(@Context
                                            HttpServletRequest request,

                                            @PathParam("version")
                                            @ApiParam(required=true, defaultValue="v1", allowableValues="v1")
                                            String version,

                                            @HeaderParam(HttpHeaders.AUTHORIZATION)
                                            @ApiParam(hidden=true)
                                            String authorization,

                                            @HeaderParam(ONAPLogConstants.Headers.PARTNER_NAME)
                                            @ApiParam(required=true)
                                            String xFromAppId,

                                            @HeaderParam(ONAPLogConstants.Headers.REQUEST_ID)
                                            String xTransactionId,

                                            @QueryParam("requestId")
                                            @ApiParam(required=true)
                                            String requestId,

                                            @QueryParam("resourceType")
                                            @ApiParam(required=true)
                                            String resourceType,

                                            @QueryParam("resourceId")
                                            @ApiParam(required=true)
                                            List<String> resourceIds) throws ApplicationException;

}
