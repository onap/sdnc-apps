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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;
import org.onap.ccsdk.apps.services.SvcLogicFactory;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.core.sli.provider.base.SvcLogicServiceBase;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadData;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.data.OperationalPreloadData;
import org.onap.sdnc.apps.ms.gra.data.OperationalPreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.swagger.OperationsApi;
import org.onap.sdnc.apps.ms.gra.swagger.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


@Controller
@ComponentScan(basePackages = {"org.onap.sdnc.apps.ms.gra.*"})
@EntityScan("org.onap.sdnc.apps.ms.gra.*")
@Import(value = SvcLogicFactory.class)
public class OperationsApiController implements OperationsApi {

    private static final String CALLED_STR = "{} called.";
    private static final String MODULE_NAME = "GENERIC-RESOURCE-API";

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    protected SvcLogicServiceBase svc;

    @Autowired
    private ConfigPreloadDataRepository configPreloadDataRepository;

    @Autowired
    private OperationalPreloadDataRepository operationalPreloadDataRepository;


    @org.springframework.beans.factory.annotation.Autowired
    public OperationsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
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
        GenericResourceApiPreloadNetworkTopologyOperation retval = new GenericResourceApiPreloadNetworkTopologyOperation();
        GenericResourceApiPreloadTopologyResponseBody resp = new GenericResourceApiPreloadTopologyResponseBody();

        log.info(CALLED_STR, svcOperation);
        if (hasInvalidPreloadNetwork(graInput)) {
            log.debug("exiting {} because of null or empty preload-network-topology-information", svcOperation);


            resp.setResponseCode("403");
            resp.setResponseMessage("invalid input, null or empty preload-network-topology-information");
            resp.setAckFinalIndicator("Y");


            retval.setOutput(resp);

            return new ResponseEntity<>(retval, HttpStatus.FORBIDDEN);
        }

        String preloadId = graInput.getInput().getPreloadNetworkTopologyInformation().getNetworkTopologyIdentifierStructure().getNetworkId();
        String preloadType = "network";

        resp.setSvcRequestId(graInput.getInput().getSdncRequestHeader().getSvcRequestId());

        SvcLogicContext ctxIn = new SvcLogicContext();

        GenericResourceApiPreloaddataPreloadData preloadData = null;


        // Add input to SvcLogicContext
        try {
            ctxIn.mergeJson(svcOperation+"-input", objectMapper.writeValueAsString(graInput.getInput()));
        } catch (JsonProcessingException e) {
            log.error("exiting {} due to parse error on input preload data", svcOperation);
            resp.setResponseCode("500");
            resp.setResponseMessage("internal error");
            resp.setAckFinalIndicator("Y");
            retval.setOutput(resp);
            return new ResponseEntity<>(retval, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        // Add config tree data to SvcLogicContext
        try {
            preloadData = getConfigPreloadData(preloadId, preloadType);
            ctxIn.mergeJson("preload-data", objectMapper.writeValueAsString(preloadData));
        } catch (JsonProcessingException e) {
            log.error("exiting {} due to parse error on saved config preload data", svcOperation);
            resp.setResponseCode("500");
            resp.setResponseMessage("internal error");
            resp.setAckFinalIndicator("Y");
            retval.setOutput(resp);
            return new ResponseEntity<>(retval, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        // Add operational tree data to SvcLogicContext
        try {
            preloadData = getOperationalPreloadData(preloadId, preloadType);
            ctxIn.mergeJson("operational-data", objectMapper.writeValueAsString(preloadData));
        } catch (JsonProcessingException e) {
            log.error("exiting {} due to parse error on saved operational preload data", svcOperation);
            resp.setResponseCode("500");
            resp.setResponseMessage("internal error");
            resp.setAckFinalIndicator("Y");
            retval.setOutput(resp);
            return new ResponseEntity<>(retval, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        // Call DG
        try {
            // Any of these can throw a nullpointer exception
            // execute should only throw a SvcLogicException
            SvcLogicContext ctxOut = svc.execute(MODULE_NAME, svcOperation, null, "sync", ctxIn);
            Properties respProps = ctxOut.toProperties();

            resp.setAckFinalIndicator(respProps.getProperty("ack-final-indicator", "Y"));
            resp.setResponseCode(respProps.getProperty("error-code", "200"));
            resp.setResponseMessage(respProps.getProperty("error-message", "SUCCESS"));

            if ("200".equals(resp.getResponseCode())) {
                // If DG returns success, update database
                String ctxJson = ctxOut.toJsonString("preload-data");
                log.info("DG preload-data is {}", ctxJson);
                GenericResourceApiPreloaddataPreloadData preloadToLoad = objectMapper.readValue(ctxJson, GenericResourceApiPreloaddataPreloadData.class);
                saveConfigPreloadData(preloadId, preloadType, preloadToLoad);
                saveOperationalPreloadData(preloadId, preloadType, preloadToLoad);
            }

        } catch (NullPointerException npe) {
            log.error("Caught NPE", npe);
            resp.setAckFinalIndicator("true");
            resp.setResponseCode("500");
            resp.setResponseMessage("Check that you populated module, rpc and or mode correctly.");
        } catch (SvcLogicException e) {
            log.error("Caught SvcLogicException", e);
            resp.setAckFinalIndicator("true");
            resp.setResponseCode("500");
            resp.setResponseMessage(e.getMessage());
        } catch (JsonMappingException e) {
            log.error("Caught JsonMappingException", e);
            resp.setAckFinalIndicator("true");
            resp.setResponseCode("500");
            resp.setResponseMessage(e.getMessage());
        } catch (JsonProcessingException e) {
            log.error("Caught JsonProcessingException", e);
            resp.setAckFinalIndicator("true");
            resp.setResponseCode("500");
            resp.setResponseMessage(e.getMessage());
        }


        retval.setOutput(resp);
        return (new ResponseEntity<>(retval, HttpStatus.valueOf(Integer.parseInt(resp.getResponseCode()))));
    }

    @Override
    public ResponseEntity<GenericResourceApiPreloadVfModuleTopologyOperation> operationsGENERICRESOURCEAPIpreloadVfModuleTopologyOperationPost(@Valid GenericResourceApiPreloadvfmoduletopologyoperationInputBodyparam graInput) {
        final String svcOperation = "preload-vf-module-topology-operation";
        GenericResourceApiPreloadVfModuleTopologyOperation retval = new GenericResourceApiPreloadVfModuleTopologyOperation();
        GenericResourceApiPreloadTopologyResponseBody resp = new GenericResourceApiPreloadTopologyResponseBody();

        log.info(CALLED_STR, svcOperation);
        if (hasInvalidPreloadNetwork(graInput)) {
            log.debug("exiting {} because of null or empty preload-network-topology-information", svcOperation);


            resp.setResponseCode("403");
            resp.setResponseMessage("invalid input, null or empty preload-network-topology-information");
            resp.setAckFinalIndicator("Y");


            retval.setOutput(resp);

            return new ResponseEntity<>(retval, HttpStatus.FORBIDDEN);
        }

        String preloadId = graInput.getInput().getPreloadVfModuleTopologyInformation().getVfModuleTopology().getVfModuleTopologyIdentifier().getVfModuleName();
        String preloadType = "vf-module";

        resp.setSvcRequestId(graInput.getInput().getSdncRequestHeader().getSvcRequestId());

        SvcLogicContext ctxIn = new SvcLogicContext();

        GenericResourceApiPreloaddataPreloadData preloadData = null;


        // Add input to SvcLogicContext
        try {
            ctxIn.mergeJson(svcOperation+"-input", objectMapper.writeValueAsString(graInput.getInput()));
        } catch (JsonProcessingException e) {
            log.error("exiting {} due to parse error on input preload data", svcOperation);
            resp.setResponseCode("500");
            resp.setResponseMessage("internal error");
            resp.setAckFinalIndicator("Y");
            retval.setOutput(resp);
            return new ResponseEntity<>(retval, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        // Add config tree data to SvcLogicContext
        try {
            preloadData = getConfigPreloadData(preloadId, preloadType);
            ctxIn.mergeJson("preload-data", objectMapper.writeValueAsString(preloadData));
        } catch (JsonProcessingException e) {
            log.error("exiting {} due to parse error on saved config preload data", svcOperation);
            resp.setResponseCode("500");
            resp.setResponseMessage("internal error");
            resp.setAckFinalIndicator("Y");
            retval.setOutput(resp);
            return new ResponseEntity<>(retval, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        // Add operational tree data to SvcLogicContext
        try {
            preloadData = getOperationalPreloadData(preloadId, preloadType);
            ctxIn.mergeJson("operational-data", objectMapper.writeValueAsString(preloadData));
        } catch (JsonProcessingException e) {
            log.error("exiting {} due to parse error on saved operational preload data", svcOperation);
            resp.setResponseCode("500");
            resp.setResponseMessage("internal error");
            resp.setAckFinalIndicator("Y");
            retval.setOutput(resp);
            return new ResponseEntity<>(retval, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        // Call DG
        try {
            // Any of these can throw a nullpointer exception
            // execute should only throw a SvcLogicException
            SvcLogicContext ctxOut = svc.execute(MODULE_NAME, svcOperation, null, "sync", ctxIn);
            Properties respProps = ctxOut.toProperties();

            resp.setAckFinalIndicator(respProps.getProperty("ack-final-indicator", "Y"));
            resp.setResponseCode(respProps.getProperty("error-code", "200"));
            resp.setResponseMessage(respProps.getProperty("error-message", "SUCCESS"));

            if ("200".equals(resp.getResponseCode())) {
                // If DG returns success, update database
                String ctxJson = ctxOut.toJsonString("preload-data");
                GenericResourceApiPreloaddataPreloadData preloadToLoad = objectMapper.readValue(ctxJson, GenericResourceApiPreloaddataPreloadData.class);
                saveConfigPreloadData(preloadId, preloadType, preloadToLoad);
                saveOperationalPreloadData(preloadId, preloadType, preloadToLoad);
            }

        } catch (NullPointerException npe) {
            resp.setAckFinalIndicator("true");
            resp.setResponseCode("500");
            resp.setResponseMessage("Check that you populated module, rpc and or mode correctly.");
        } catch (SvcLogicException e) {
            resp.setAckFinalIndicator("true");
            resp.setResponseCode("500");
            resp.setResponseMessage(e.getMessage());
        } catch (JsonMappingException e) {
            resp.setAckFinalIndicator("true");
            resp.setResponseCode("500");
            resp.setResponseMessage(e.getMessage());
        } catch (JsonProcessingException e) {
            resp.setAckFinalIndicator("true");
            resp.setResponseCode("500");
            resp.setResponseMessage(e.getMessage());
        }


        retval.setOutput(resp);
        return (new ResponseEntity<>(retval, HttpStatus.valueOf(Integer.parseInt(resp.getResponseCode()))));
    }

    private boolean hasInvalidPreloadNetwork(GenericResourceApiPreloadnetworktopologyoperationInputBodyparam preloadData) {
        return ((preloadData == null) ||
                (preloadData.getInput() == null) ||
                (preloadData.getInput().getPreloadNetworkTopologyInformation() == null));
    }
    private boolean hasInvalidPreloadNetwork(GenericResourceApiPreloadvfmoduletopologyoperationInputBodyparam preloadData) {
        return ((preloadData == null) ||
                (preloadData.getInput() == null) ||
                (preloadData.getInput().getPreloadVfModuleTopologyInformation() == null));
    }

    private GenericResourceApiPreloaddataPreloadData getConfigPreloadData(String preloadId, String preloadType) throws JsonProcessingException {

        List<ConfigPreloadData> configPreloadData = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);

        if (configPreloadData.isEmpty()) {
            return(null);
        } else {
            return(objectMapper.readValue(configPreloadData.get(0).getPreloadData(), GenericResourceApiPreloaddataPreloadData.class));
        }
    }

    private GenericResourceApiPreloaddataPreloadData getOperationalPreloadData(String preloadId, String preloadType) throws JsonProcessingException {

        List<OperationalPreloadData> configPreloadData = operationalPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);

        if (configPreloadData.isEmpty()) {
            return(null);
        } else {
            return(objectMapper.readValue(configPreloadData.get(0).getPreloadData(), GenericResourceApiPreloaddataPreloadData.class));
        }
    }

    private void saveConfigPreloadData(String preloadId, String preloadType, GenericResourceApiPreloaddataPreloadData preloadData) throws JsonProcessingException {

        configPreloadDataRepository.deleteByPreloadIdAndPreloadType(preloadId, preloadType);
        configPreloadDataRepository.save(new ConfigPreloadData(preloadId, preloadType, objectMapper.writeValueAsString(preloadData)));

    }

    private void saveOperationalPreloadData(String preloadId, String preloadType, GenericResourceApiPreloaddataPreloadData preloadData) throws JsonProcessingException {

        operationalPreloadDataRepository.deleteByPreloadIdAndPreloadType(preloadId, preloadType);
        operationalPreloadDataRepository.save(new OperationalPreloadData(preloadId, preloadType, objectMapper.writeValueAsString(preloadData)));

    }

}
