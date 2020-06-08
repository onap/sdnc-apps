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

package org.onap.sdnc.apps.ms.gra.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.sdnc.apps.ms.gra.swagger.OperationsApi;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadNetworkTopologyOperation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadTopologyResponseBody;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadnetworktopologyoperationInputBodyparam;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@ComponentScan(basePackages = {"org.onap.sdnc.apps.ms.gra.*"})
@EntityScan("org.onap.sdnc.apps.ms.gra.*")
public class OperationsApiController implements OperationsApi {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public OperationsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<GenericResourceApiPreloadNetworkTopologyOperation> operationsGENERICRESOURCEAPIpreloadNetworkTopologyOperationPost(@Valid GenericResourceApiPreloadnetworktopologyoperationInputBodyparam graInput) {
        final String svcOperation = "preload-network-topology-operation";

        if (hasInvalidPreloadNetwork(graInput)) {
            log.debug("exiting {} because of null or empty preload-network-topology-information", svcOperation);

            GenericResourceApiPreloadTopologyResponseBody resp = new GenericResourceApiPreloadTopologyResponseBody();

            resp.setResponseCode("403");
            resp.setResponseMessage("invalid input, null or empty preload-network-topology-information");
            resp.setAckFinalIndicator("Y");


            GenericResourceApiPreloadNetworkTopologyOperation retval = new GenericResourceApiPreloadNetworkTopologyOperation();
            retval.setOutput(resp);

            return new ResponseEntity<>(retval, HttpStatus.FORBIDDEN);
        }

        String preloadId = graInput.getInput().getPreloadNetworkTopologyInformation().getNetworkTopologyIdentifierStructure().getNetworkId();


        return null;
    }

    private boolean hasInvalidPreloadNetwork(GenericResourceApiPreloadnetworktopologyoperationInputBodyparam preloadData)
    {
        return ((preloadData == null) ||
                (preloadData.getInput() == null) ||
                (preloadData.getInput().getPreloadNetworkTopologyInformation() == null));
    }
}
