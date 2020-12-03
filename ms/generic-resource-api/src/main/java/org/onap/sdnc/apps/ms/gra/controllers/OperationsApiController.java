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

import org.onap.ccsdk.apps.services.RestException;
import org.onap.ccsdk.apps.services.SvcLogicFactory;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.core.sli.provider.base.SvcLogicServiceBase;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadData;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigServices;
import org.onap.sdnc.apps.ms.gra.data.ConfigServicesRepository;
import org.onap.sdnc.apps.ms.gra.data.OperationalPreloadData;
import org.onap.sdnc.apps.ms.gra.data.OperationalPreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.data.OperationalServices;
import org.onap.sdnc.apps.ms.gra.data.OperationalServicesRepository;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@ComponentScan(basePackages = { "org.onap.sdnc.apps.ms.gra.*", "org.onap.ccsdk.apps.services" })
@EntityScan("org.onap.sdnc.apps.ms.gra.*")
@Import(value = SvcLogicFactory.class)
public class OperationsApiController implements OperationsApi {

    private static final String CALLED_STR = "{} called.";
    private static final String MODULE_NAME = "GENERIC-RESOURCE-API";
    private static final String SERVICE_OBJECT_PATH_PARAM = "service-object-path";
    private static final String NETWORK_OBJECT_PATH_PARAM = "network-object-path";
    private static final String VNF_OBJECT_PATH_PARAM = "vnf-object-path";
    private static final String PNF_OBJECT_PATH_PARAM = "pnf-object-path";
    private static final String VF_MODULE_OBJECT_PATH_PARAM = "vf-module-object-path";
    private static final String VF_MODULE_ID_PARAM = "vf-module-id";


    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    protected SvcLogicServiceBase svc;

    @Autowired
    private ConfigPreloadDataRepository configPreloadDataRepository;

    @Autowired
    private OperationalPreloadDataRepository operationalPreloadDataRepository;

    @Autowired
    private ConfigServicesRepository configServicesRepository;

    @Autowired
    private OperationalServicesRepository operationalServicesRepository;

    private static class Iso8601Util {

        private static TimeZone timeZone = TimeZone.getTimeZone("UTC");
        private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        private Iso8601Util() {
        }

        static {
            dateFormat.setTimeZone(timeZone);
        }

        private static String now() {
            return dateFormat.format(new Date());
        }
    }

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
    public ResponseEntity<GenericResourceApiPreloadNetworkTopologyOperation> operationsGENERICRESOURCEAPIpreloadNetworkTopologyOperationPost(
            @Valid GenericResourceApiPreloadnetworktopologyoperationInputBodyparam graInput) {
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

        String preloadId = graInput.getInput().getPreloadNetworkTopologyInformation()
                .getNetworkTopologyIdentifierStructure().getNetworkId();
        String preloadType = "network";

        resp.setSvcRequestId(graInput.getInput().getSdncRequestHeader().getSvcRequestId());

        SvcLogicContext ctxIn = new SvcLogicContext();

        GenericResourceApiPreloaddataPreloadData preloadData = null;

        // Add input to SvcLogicContext
        try {
            ctxIn.mergeJson(svcOperation + "-input", objectMapper.writeValueAsString(graInput.getInput()));
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
                GenericResourceApiPreloaddataPreloadData preloadToLoad = objectMapper.readValue(ctxJson,
                        GenericResourceApiPreloaddataPreloadData.class);
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
    public ResponseEntity<GenericResourceApiPreloadVfModuleTopologyOperation> operationsGENERICRESOURCEAPIpreloadVfModuleTopologyOperationPost(
            @Valid GenericResourceApiPreloadvfmoduletopologyoperationInputBodyparam graInput) {
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

        String preloadId = graInput.getInput().getPreloadVfModuleTopologyInformation().getVfModuleTopology()
                .getVfModuleTopologyIdentifier().getVfModuleName();
        String preloadType = "vf-module";

        resp.setSvcRequestId(graInput.getInput().getSdncRequestHeader().getSvcRequestId());

        SvcLogicContext ctxIn = new SvcLogicContext();

        GenericResourceApiPreloaddataPreloadData preloadData = null;

        // Add input to SvcLogicContext
        try {
            ctxIn.mergeJson(svcOperation + "-input", objectMapper.writeValueAsString(graInput.getInput()));
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
                GenericResourceApiPreloaddataPreloadData preloadToLoad = objectMapper.readValue(ctxJson,
                        GenericResourceApiPreloaddataPreloadData.class);
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

    private boolean hasInvalidPreloadNetwork(
            GenericResourceApiPreloadnetworktopologyoperationInputBodyparam preloadData) {
        return ((preloadData == null) || (preloadData.getInput() == null)
                || (preloadData.getInput().getPreloadNetworkTopologyInformation() == null));
    }

    private boolean hasInvalidPreloadNetwork(
            GenericResourceApiPreloadvfmoduletopologyoperationInputBodyparam preloadData) {
        return ((preloadData == null) || (preloadData.getInput() == null)
                || (preloadData.getInput().getPreloadVfModuleTopologyInformation() == null));
    }

    private boolean hasInvalidServiceId(GenericResourceApiServiceOperationInformation input) {

        return input == null || input.getServiceInformation() == null
                || input.getServiceInformation().getServiceInstanceId() == null
                || input.getServiceInformation().getServiceInstanceId().length() == 0;
    }

    private boolean hasInvalidServiceId(GenericResourceApiNetworkOperationInformation input) {

        return input == null || input.getServiceInformation() == null
                || input.getServiceInformation().getServiceInstanceId() == null
                || input.getServiceInformation().getServiceInstanceId().length() == 0;
    }

    private boolean hasInvalidServiceId(GenericResourceApiVnfOperationInformation input) {

        return input == null || input.getServiceInformation() == null
                || input.getServiceInformation().getServiceInstanceId() == null
                || input.getServiceInformation().getServiceInstanceId().length() == 0;
    }

    private GenericResourceApiPreloaddataPreloadData getConfigPreloadData(String preloadId, String preloadType)
            throws JsonProcessingException {

        List<ConfigPreloadData> configPreloadData = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId,
                preloadType);

        if (configPreloadData.isEmpty()) {
            return (null);
        } else {
            return (objectMapper.readValue(configPreloadData.get(0).getPreloadData(),
                    GenericResourceApiPreloaddataPreloadData.class));
        }
    }

    private GenericResourceApiPreloaddataPreloadData getOperationalPreloadData(String preloadId, String preloadType)
            throws JsonProcessingException {

        List<OperationalPreloadData> configPreloadData = operationalPreloadDataRepository
                .findByPreloadIdAndPreloadType(preloadId, preloadType);

        if (configPreloadData.isEmpty()) {
            return (null);
        } else {
            return (objectMapper.readValue(configPreloadData.get(0).getPreloadData(),
                    GenericResourceApiPreloaddataPreloadData.class));
        }
    }

    private void saveConfigPreloadData(String preloadId, String preloadType,
            GenericResourceApiPreloaddataPreloadData preloadData) throws JsonProcessingException {

        configPreloadDataRepository.deleteByPreloadIdAndPreloadType(preloadId, preloadType);
        configPreloadDataRepository
                .save(new ConfigPreloadData(preloadId, preloadType, objectMapper.writeValueAsString(preloadData)));

    }

    private void saveOperationalPreloadData(String preloadId, String preloadType,
            GenericResourceApiPreloaddataPreloadData preloadData) throws JsonProcessingException {

        operationalPreloadDataRepository.deleteByPreloadIdAndPreloadType(preloadId, preloadType);
        operationalPreloadDataRepository
                .save(new OperationalPreloadData(preloadId, preloadType, objectMapper.writeValueAsString(preloadData)));

    }

    private GenericResourceApiServicedataServiceData getConfigServiceData(String svcInstanceId)
            throws JsonProcessingException {

        List<ConfigServices> configServices = configServicesRepository.findBySvcInstanceId(svcInstanceId);

        if (configServices.isEmpty()) {
            return (null);
        } else {
            return (objectMapper.readValue(configServices.get(0).getSvcData(),
                    GenericResourceApiServicedataServiceData.class));
        }
    }

    @Override
    public ResponseEntity<GenericResourceApiNetworkTopologyOperation> operationsGENERICRESOURCEAPInetworkTopologyOperationPost(
            @Valid GenericResourceApiNetworkOperationInformationBodyparam input) throws RestException {
        final String svcOperation = "network-topology-operation";
        GenericResourceApiNetworkTopologyOperation retval = new GenericResourceApiNetworkTopologyOperation();
        GenericResourceApiNetworktopologyoperationOutput resp = new GenericResourceApiNetworktopologyoperationOutput();

        log.info(CALLED_STR, svcOperation);
        // Verify input contains service instance id
        if (hasInvalidServiceId(input.getInput())) {
            log.debug("exiting {} because of null or empty service-instance-id", svcOperation);

            resp.setResponseCode("404");
            resp.setResponseMessage("null or empty service-instance-id");
            resp.setAckFinalIndicator("Y");

            retval.setOutput(resp);

            return new ResponseEntity<>(retval, HttpStatus.OK);
        }

        String svcInstanceId = input.getInput().getServiceInformation().getServiceInstanceId();

        SvcLogicContext ctxIn = new SvcLogicContext();

        // Add input to SvcLogicContext
        try {
            ctxIn.mergeJson(svcOperation + "-input", objectMapper.writeValueAsString(input.getInput()));
        } catch (JsonProcessingException e) {
            log.error("exiting {} due to parse error on input data", svcOperation);
            resp.setResponseCode("500");
            resp.setResponseMessage("internal error");
            resp.setAckFinalIndicator("Y");
            retval.setOutput(resp);
            return new ResponseEntity<>(retval, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Add config tree data to SvcLogicContext
        List<ConfigServices> configServices = configServicesRepository.findBySvcInstanceId(svcInstanceId);
        ConfigServices configService = null;
        String svcData = null;
        if (configServices != null && !configServices.isEmpty()) {
            configService = configServices.get(0);
            svcData = configService.getSvcData();
        }
        if (svcData != null) {
            ctxIn.mergeJson("service-data", configService.getSvcData());
        } else {
            log.debug("exiting {} because the service-instance does not have any service data in SDN", svcOperation);

            resp.setResponseCode("404");
            resp.setResponseMessage("invalid input: the service-instance does not have any service data in SDNC");
            resp.setAckFinalIndicator("Y");

            retval.setOutput(resp);

            return new ResponseEntity<>(retval, HttpStatus.OK);
        }

        // Add operational tree data to SvcLogicContext
        List<OperationalServices> operServices = operationalServicesRepository.findBySvcInstanceId(svcInstanceId);
        OperationalServices operService = null;
        boolean saveOperationalData = false;

        if (operServices != null && !operServices.isEmpty()) {
            operService = operServices.get(0);
            ctxIn.mergeJson("operational-data", operService.getSvcData());
        } else {
            operService = new OperationalServices(svcInstanceId, null, null);
        }

        // Update service status info in config entry from input
        configService.setServiceStatusAction(input.getInput().getRequestInformation().getRequestAction().toString());
        configService.setServiceStatusRpcAction(input.getInput().getSdncRequestHeader().getSvcAction().toString());
        configService.setServiceStatusRpcName(svcOperation);

        // Call DG
        try {
            // Any of these can throw a nullpointer exception
            // execute should only throw a SvcLogicException
            SvcLogicContext ctxOut = svc.execute(MODULE_NAME, svcOperation, null, "sync", ctxIn);
            Properties respProps = ctxOut.toProperties();

            resp.setAckFinalIndicator(respProps.getProperty("ack-final-indicator", "Y"));
            resp.setResponseCode(respProps.getProperty("error-code", "200"));
            resp.setResponseMessage(respProps.getProperty("error-message", "SUCCESS"));



            configService
                    .setServiceStatusRequestStatus(GenericResourceApiRequestStatusEnumeration.SYNCCOMPLETE.toString());

            if ("200".equals(resp.getResponseCode())) {

                GenericResourceApiInstanceReference serviceReference = new GenericResourceApiInstanceReference();
                serviceReference.setInstanceId(svcInstanceId);
                serviceReference.setObjectPath(respProps.getProperty(SERVICE_OBJECT_PATH_PARAM));
                resp.setServiceResponseInformation(serviceReference);
    
                GenericResourceApiInstanceReference networkReference = new GenericResourceApiInstanceReference();
                networkReference.setInstanceId(respProps.getProperty("networkId"));
                networkReference.setObjectPath(respProps.getProperty(NETWORK_OBJECT_PATH_PARAM));
                resp.setNetworkResponseInformation(networkReference);

                // If DG returns success, update svcData in config and operational trees
                // and remember to save operational data.
                String ctxJson = ctxOut.toJsonString("service-data");
                configService.setSvcData(ctxJson);
                operService.setSvcData(ctxJson);
                saveOperationalData = true;
            }

        } catch (NullPointerException npe) {
            resp.setAckFinalIndicator("true");
            resp.setResponseCode("500");
            resp.setResponseMessage("Check that you populated module, rpc and or mode correctly.");
        } catch (SvcLogicException e) {
            resp.setAckFinalIndicator("true");
            resp.setResponseCode("500");
            resp.setResponseMessage(e.getMessage());
        }

        // Update status in config services entry

        configService.setServiceStatusFinalIndicator(resp.getAckFinalIndicator());
        configService.setServiceStatusResponseCode(resp.getResponseCode());
        configService.setServiceStatusResponseMessage(resp.getResponseMessage());
        configService.setServiceStatusResponseTimestamp(Iso8601Util.now());

        // Update config tree
        configServicesRepository.save(configService);

        // If necessary, sync status to operation service entry and save
        if (saveOperationalData) {
            operService.setServiceStatus(configService.getServiceStatus());
            operationalServicesRepository.save(operService);
        }
        retval.setOutput(resp);
        
        return (new ResponseEntity<>(retval, HttpStatus.OK));
    }

    @Override
    public ResponseEntity<GenericResourceApiServiceTopologyOperation> operationsGENERICRESOURCEAPIserviceTopologyOperationPost(
            @Valid GenericResourceApiServiceOperationInformationBodyparam input) throws RestException {
        final String svcOperation = "service-topology-operation";
        GenericResourceApiServiceTopologyOperation retval = new GenericResourceApiServiceTopologyOperation();
        GenericResourceApiServicetopologyoperationOutput resp = new GenericResourceApiServicetopologyoperationOutput();

        log.info(CALLED_STR, svcOperation);

        // Verify input contains service instance id
        if (hasInvalidServiceId(input.getInput())) {
            log.debug("exiting {} because of null or empty service-instance-id", svcOperation);

            resp.setResponseCode("404");
            resp.setResponseMessage("null or empty service-instance-id");
            resp.setAckFinalIndicator("Y");

            retval.setOutput(resp);

            return new ResponseEntity<>(retval, HttpStatus.OK);
        }

        String svcInstanceId = input.getInput().getServiceInformation().getServiceInstanceId();

        SvcLogicContext ctxIn = new SvcLogicContext();

        // Add input to SvcLogicContext
        try {
            ctxIn.mergeJson(svcOperation + "-input", objectMapper.writeValueAsString(input.getInput()));
        } catch (JsonProcessingException e) {
            log.error("exiting {} due to parse error on input data", svcOperation);
            resp.setResponseCode("500");
            resp.setResponseMessage("internal error");
            resp.setAckFinalIndicator("Y");
            retval.setOutput(resp);
            return new ResponseEntity<>(retval, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Add config tree data to SvcLogicContext
        List<ConfigServices> configServices = configServicesRepository.findBySvcInstanceId(svcInstanceId);
        ConfigServices configService = null;
        if (configServices != null && !configServices.isEmpty()) {
            configService = configServices.get(0);
            if (configService.getSvcData() != null) {
                ctxIn.mergeJson("service-data", configService.getSvcData());
            }
        } else {
            configService = new ConfigServices(svcInstanceId, null);
        }

        // Add operational tree data to SvcLogicContext
        List<OperationalServices> operServices = operationalServicesRepository.findBySvcInstanceId(svcInstanceId);
        OperationalServices operService = null;
        boolean saveOperationalData = false;

        if (operServices != null && !operServices.isEmpty()) {
            operService = operServices.get(0);
            ctxIn.mergeJson("operational-data", operService.getSvcData());
        } else {
            operService = new OperationalServices(svcInstanceId, null, null);
        }

        // Update service status info in config entry from input
        configService.setServiceStatusAction(input.getInput().getRequestInformation().getRequestAction().toString());
        configService.setServiceStatusRpcAction(input.getInput().getSdncRequestHeader().getSvcAction().toString());
        configService.setServiceStatusRpcName(svcOperation);

        // Call DG
        try {
            // Any of these can throw a nullpointer exception
            // execute should only throw a SvcLogicException
            SvcLogicContext ctxOut = svc.execute(MODULE_NAME, svcOperation, null, "sync", ctxIn);
            Properties respProps = ctxOut.toProperties();

            resp.setAckFinalIndicator(respProps.getProperty("ack-final-indicator", "Y"));
            resp.setResponseCode(respProps.getProperty("error-code", "200"));
            resp.setResponseMessage(respProps.getProperty("error-message", "SUCCESS"));

            configService
                    .setServiceStatusRequestStatus(GenericResourceApiRequestStatusEnumeration.SYNCCOMPLETE.toString());

            if ("200".equals(resp.getResponseCode())) {                 
                GenericResourceApiInstanceReference serviceReference = new GenericResourceApiInstanceReference();
                serviceReference.setInstanceId(svcInstanceId);
                serviceReference.setObjectPath(respProps.getProperty(SERVICE_OBJECT_PATH_PARAM));
                resp.setServiceResponseInformation(serviceReference);

                // If DG returns success, update svcData in config and operational trees
                // and remember to save operational data.
                String ctxJson = ctxOut.toJsonString("service-data");
                configService.setSvcData(ctxJson);
                operService.setSvcData(ctxJson);
                saveOperationalData = true;
            }

        } catch (NullPointerException npe) {
            resp.setAckFinalIndicator("true");
            resp.setResponseCode("500");
            resp.setResponseMessage("Check that you populated module, rpc and or mode correctly.");
        } catch (SvcLogicException e) {
            resp.setAckFinalIndicator("true");
            resp.setResponseCode("500");
            resp.setResponseMessage(e.getMessage());
        }

        // Update status in config services entry

        configService.setServiceStatusFinalIndicator(resp.getAckFinalIndicator());
        configService.setServiceStatusResponseCode(resp.getResponseCode());
        configService.setServiceStatusResponseMessage(resp.getResponseMessage());
        configService.setServiceStatusResponseTimestamp(Iso8601Util.now());

        // Update config tree
        configServicesRepository.save(configService);

        // If necessary, sync status to operation service entry and save
        if (saveOperationalData) {
            operService.setServiceStatus(configService.getServiceStatus());
            operationalServicesRepository.save(operService);
        }
        retval.setOutput(resp);
        return (new ResponseEntity<>(retval, HttpStatus.OK));

    }

    @Override
    public ResponseEntity<GenericResourceApiVnfTopologyOperation> operationsGENERICRESOURCEAPIvnfTopologyOperationPost(
            @Valid GenericResourceApiVnfOperationInformationBodyparam input)
            throws RestException {
                final String svcOperation = "vnf-topology-operation";
                GenericResourceApiVnfTopologyOperation retval = new GenericResourceApiVnfTopologyOperation();
                GenericResourceApiVnftopologyoperationOutput resp = new GenericResourceApiVnftopologyoperationOutput();
        
                log.info(CALLED_STR, svcOperation);
                // Verify input contains service instance id
                if (hasInvalidServiceId(input.getInput())) {
                    log.debug("exiting {} because of null or empty service-instance-id", svcOperation);
        
                    resp.setResponseCode("404");
                    resp.setResponseMessage("null or empty service-instance-id");
                    resp.setAckFinalIndicator("Y");
        
                    retval.setOutput(resp);
        
                    return new ResponseEntity<>(retval, HttpStatus.OK);
                }
        
                String svcInstanceId = input.getInput().getServiceInformation().getServiceInstanceId();
                String vnfId = null;
                
                if ((input.getInput() != null) && (input.getInput().getVnfInformation() != null)) {
                    vnfId = input.getInput().getVnfInformation().getVnfId();
                }
        
                SvcLogicContext ctxIn = new SvcLogicContext();
        
                // Add input to SvcLogicContext
                try {
                    ctxIn.mergeJson(svcOperation + "-input", objectMapper.writeValueAsString(input.getInput()));
                } catch (JsonProcessingException e) {
                    log.error("exiting {} due to parse error on input data", svcOperation);
                    resp.setResponseCode("500");
                    resp.setResponseMessage("internal error");
                    resp.setAckFinalIndicator("Y");
                    retval.setOutput(resp);
                    return new ResponseEntity<>(retval, HttpStatus.INTERNAL_SERVER_ERROR);
                }
        
                // Add config tree data to SvcLogicContext
                List<ConfigServices> configServices = configServicesRepository.findBySvcInstanceId(svcInstanceId);
                ConfigServices configService = null;
                if (configServices != null && !configServices.isEmpty()) {
                    configService = configServices.get(0);
                    ctxIn.mergeJson("service-data", configService.getSvcData());
                } else {
                    log.debug("exiting {} because the service-instance does not have any service data in SDN", svcOperation);
        
                    resp.setResponseCode("404");
                    resp.setResponseMessage("invalid input: the service-instance does not have any service data in SDNC");
                    resp.setAckFinalIndicator("Y");
        
                    retval.setOutput(resp);
        
                    return new ResponseEntity<>(retval, HttpStatus.OK);
                }
        
                // Add operational tree data to SvcLogicContext
                List<OperationalServices> operServices = operationalServicesRepository.findBySvcInstanceId(svcInstanceId);
                OperationalServices operService = null;
                boolean saveOperationalData = false;
        
                if (operServices != null && !operServices.isEmpty()) {
                    operService = operServices.get(0);
                    ctxIn.mergeJson("operational-data", operService.getSvcData());
                } else {
                    operService = new OperationalServices(svcInstanceId, null, null);
                }
        
                // Update service status info in config entry from input
                configService.setServiceStatusAction(input.getInput().getRequestInformation().getRequestAction().toString());
                configService.setServiceStatusRpcAction(input.getInput().getSdncRequestHeader().getSvcAction().toString());
                configService.setServiceStatusRpcName(svcOperation);


        
                // Call DG
                try {
                    // Any of these can throw a nullpointer exception
                    // execute should only throw a SvcLogicException
                    SvcLogicContext ctxOut = svc.execute(MODULE_NAME, svcOperation, null, "sync", ctxIn);
                    Properties respProps = ctxOut.toProperties();
        
                    resp.setAckFinalIndicator(respProps.getProperty("ack-final-indicator", "Y"));
                    resp.setResponseCode(respProps.getProperty("error-code", "200"));
                    resp.setResponseMessage(respProps.getProperty("error-message", "SUCCESS"));

                    configService
                            .setServiceStatusRequestStatus(GenericResourceApiRequestStatusEnumeration.SYNCCOMPLETE.toString());
        
                    if ("200".equals(resp.getResponseCode())) {

                        GenericResourceApiInstanceReference serviceReference = new GenericResourceApiInstanceReference();
                        serviceReference.setInstanceId(svcInstanceId);
                        serviceReference.setObjectPath(respProps.getProperty(SERVICE_OBJECT_PATH_PARAM));
                        resp.setServiceResponseInformation(serviceReference);
                        
                        if (vnfId == null) {
                            vnfId = respProps.getProperty("vnfId");
                        }
                        GenericResourceApiInstanceReference vnfReference = new GenericResourceApiInstanceReference();
                        vnfReference.setInstanceId(vnfId);
                        vnfReference.setObjectPath(respProps.getProperty(VNF_OBJECT_PATH_PARAM));
                        resp.setVnfResponseInformation(vnfReference);
                        
                        // If DG returns success, update svcData in config and operational trees
                        // and remember to save operational data.
                        String ctxJson = ctxOut.toJsonString("service-data");
                        configService.setSvcData(ctxJson);
                        operService.setSvcData(ctxJson);
                        saveOperationalData = true;
                    }
        
                } catch (NullPointerException npe) {
                    resp.setAckFinalIndicator("true");
                    resp.setResponseCode("500");
                    resp.setResponseMessage("Check that you populated module, rpc and or mode correctly.");
                } catch (SvcLogicException e) {
                    resp.setAckFinalIndicator("true");
                    resp.setResponseCode("500");
                    resp.setResponseMessage(e.getMessage());
                }
        
                // Update status in config services entry
        
                configService.setServiceStatusFinalIndicator(resp.getAckFinalIndicator());
                configService.setServiceStatusResponseCode(resp.getResponseCode());
                configService.setServiceStatusResponseMessage(resp.getResponseMessage());
                configService.setServiceStatusResponseTimestamp(Iso8601Util.now());
        
                // Update config tree
                configServicesRepository.save(configService);
        
                // If necessary, sync status to operation service entry and save
                if (saveOperationalData) {
                    operService.setServiceStatus(configService.getServiceStatus());
                    operationalServicesRepository.save(operService);
                }
                retval.setOutput(resp);
                return (new ResponseEntity<>(retval, HttpStatus.OK));
    }

    
}
