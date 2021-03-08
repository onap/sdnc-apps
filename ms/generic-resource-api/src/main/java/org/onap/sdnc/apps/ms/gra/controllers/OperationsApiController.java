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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.onap.ccsdk.apps.ms.sliboot.swagger.OperationsApi;
import org.onap.ccsdk.apps.services.RestException;
import org.onap.ccsdk.apps.services.SvcLogicFactory;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.core.sli.provider.base.SvcLogicServiceBase;
import org.onap.sdnc.apps.ms.gra.data.ConfigContrailRouteAllottedResourcesRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigNetworks;
import org.onap.sdnc.apps.ms.gra.data.ConfigNetworksRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigPortMirrorConfigurations;
import org.onap.sdnc.apps.ms.gra.data.ConfigPortMirrorConfigurationsRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadData;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigServices;
import org.onap.sdnc.apps.ms.gra.data.ConfigServicesRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigVfModules;
import org.onap.sdnc.apps.ms.gra.data.ConfigVfModulesRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigVnfs;
import org.onap.sdnc.apps.ms.gra.data.ConfigVnfsRepository;
import org.onap.sdnc.apps.ms.gra.data.OperationalContrailRouteAllottedResourcesRepository;
import org.onap.sdnc.apps.ms.gra.data.OperationalPortMirrorConfigurationsRepository;
import org.onap.sdnc.apps.ms.gra.data.OperationalPreloadData;
import org.onap.sdnc.apps.ms.gra.data.OperationalPreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.data.OperationalServices;
import org.onap.sdnc.apps.ms.gra.data.OperationalServicesRepository;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiInstanceReference;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkOperationInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkOperationInformationBodyparam;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkTopologyOperation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworktopologyoperationOutput;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPolicyUpdateNotifyOperation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPolicyupdatenotifyoperationInput;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPolicyupdatenotifyoperationInputBodyparam;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPolicyupdatenotifyoperationOutput;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPortMirrorTopologyOperation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPortMirrorTopologyOperationInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPortMirrorTopologyOperationInformationBodyparam;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPortmirrortopologyoperationOutput;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadNetworkTopologyOperation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadTopologyResponseBody;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadVfModuleTopologyOperation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloaddataPreloadData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadnetworktopologyoperationInputBodyparam;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadvfmoduletopologyoperationInputBodyparam;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRequestStatusEnumeration;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceOperationInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceOperationInformationBodyparam;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceTopologyOperation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServiceData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataNetworks;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataNetworksNetwork;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataNetworksNetworkNetworkData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfs;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnf;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnfVnfData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfmoduleVfModuleData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicetopologyoperationOutput;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVfModuleOperationInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVfModuleOperationInformationBodyparam;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVfModuleTopologyOperation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVfmoduletopologyoperationOutput;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfGetResourceRequest;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfOperationInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfOperationInformationBodyparam;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfTopologyOperation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfgetresourcerequestInput;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfgetresourcerequestInputBodyparam;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfgetresourcerequestOutput;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnftopologyoperationOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

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
    private static final String PORT_MIRROR_OBJECT_PATH_PARAM = "port-mirror-object-path";
    private static final String BACKGROUND_THREAD_STARTED_MESSAGE = "Start background thread";
    private static final String BACKGROUND_THREAD_INFO = "Background thread: input conf_id is {}";
    private static final String SKIP_MDSAL_UPDATE_PROP = "skip-mdsal-update";
    private static final String ADDING_INPUT_DATA_LOG = "Adding INPUT data for {} [{}] input: {}";
    private static final String ADDING_OPERATIONAL_DATA_LOG = "Adding OPERATIONAL data for {} [{}] operational-data: {}";

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

    @Autowired
    private ConfigNetworksRepository configNetworksRepository;

    @Autowired 
    private ConfigVnfsRepository configVnfsRepository;

    @Autowired
    private ConfigVfModulesRepository configVfModulesRepository;

    @Autowired
    private ConfigContrailRouteAllottedResourcesRepository configContrailRouteAllottedResourcesRepository;

    @Autowired
    private OperationalContrailRouteAllottedResourcesRepository operationalContrailRouteAllottedResourcesRepository;

    @Autowired
    private ConfigPortMirrorConfigurationsRepository configPortMirrorConfigurationsRepository;

    @Autowired
    private OperationalPortMirrorConfigurationsRepository operationalPortMirrorConfigurationsRepository;

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

    private boolean hasInvalidServiceId(GenericResourceApiVfModuleOperationInformation input) {

        return input == null || input.getServiceInformation() == null
                || input.getServiceInformation().getServiceInstanceId() == null
                || input.getServiceInformation().getServiceInstanceId().length() == 0;
    }

    private boolean hasInvalidServiceId(GenericResourceApiPortMirrorTopologyOperationInformation input) {

        return input == null || input.getServiceInformation() == null
                || input.getServiceInformation().getServiceInstanceId() == null
                || input.getServiceInformation().getServiceInstanceId().length() == 0;
    }

    private boolean hasInvalidServiceId(GenericResourceApiVnfgetresourcerequestInput input) {

        return input == null || input.getServiceInformation() == null
                || input.getServiceInformation().getServiceInstanceId() == null
                || input.getServiceInformation().getServiceInstanceId().length() == 0;
    }

    private boolean hasInvalidVnfId(GenericResourceApiVfModuleOperationInformation input) {

        return input == null || input.getVnfInformation() == null
                || input.getVnfInformation().getVnfId() == null
                || input.getVnfInformation().getVnfId().length() == 0;
    }

    private boolean hasInvalidConfigurationId(GenericResourceApiPortMirrorTopologyOperationInformation input) {
        return input.getConfigurationInformation() == null
                || input.getConfigurationInformation().getConfigurationId() == null
                || input.getConfigurationInformation().getConfigurationId().length() == 0;
    }

    private boolean hasInvalidPolicyUpdateInput(GenericResourceApiPolicyupdatenotifyoperationInput input) {
        return (input.getPolicyName() == null) || (input.getUpdateType() == null) || (input.getVersionId() == null);
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

    private String getConfigServiceDataAsString(String svcInstanceId) throws JsonProcessingException {
        return(objectMapper.writeValueAsString(getConfigServiceData(svcInstanceId)));
    }

    private GenericResourceApiServicedataServiceData getConfigServiceData(String svcInstanceId)
            throws JsonProcessingException {

        List<ConfigServices> configServices = configServicesRepository.findBySvcInstanceId(svcInstanceId);

        if (configServices.isEmpty()) {
            return (null);
        }

        GenericResourceApiServicedataServiceData svcData = (objectMapper.readValue(configServices.get(0).getSvcData(), GenericResourceApiServicedataServiceData.class));
        
        // Get networks
        List<ConfigNetworks> configNetworks = configNetworksRepository.findBySvcInstanceId(svcInstanceId);
        GenericResourceApiServicedataServicedataNetworks networks = new GenericResourceApiServicedataServicedataNetworks();
        for (ConfigNetworks configNetwork : configNetworks) {
            GenericResourceApiServicedataServicedataNetworksNetwork network = new GenericResourceApiServicedataServicedataNetworksNetwork();
            network.setNetworkId(configNetwork.getNetworkId());
            network.setNetworkData(objectMapper.readValue(configNetwork.getNetworkData(), GenericResourceApiServicedataServicedataNetworksNetworkNetworkData.class));
            networks.addNetworkItem(network);
        }
        svcData.setNetworks(networks);

        // Get VNFs
        List<ConfigVnfs> configVnfs = configVnfsRepository.findBySvcInstanceId(svcInstanceId);
        GenericResourceApiServicedataServicedataVnfs vnfs = new GenericResourceApiServicedataServicedataVnfs();
        for (ConfigVnfs configVnf : configVnfs) {
            GenericResourceApiServicedataServicedataVnfsVnf vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
            vnf.setVnfId(configVnf.getVnfId());
            GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfData = objectMapper.readValue(configVnf.getVnfData(), GenericResourceApiServicedataServicedataVnfsVnfVnfData.class);
            
            // Get vf modules for this vnf
            List<ConfigVfModules> configVfModules = configVfModulesRepository.findBySvcInstanceIdAndVnfId(svcInstanceId, configVnf.getVnfId());
            GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules vfModules = new GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules();
            for (ConfigVfModules configVfModule : configVfModules) {
                GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule vfModule = new GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule();
                vfModule.setVfModuleId(configVfModule.getVfModuleId());
                vfModule.setVfModuleData(objectMapper.readValue(configVfModule.getVfModuleData(), GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfmoduleVfModuleData.class));
                vfModules.addVfModuleItem(vfModule);
            }
            vnfData.setVfModules(vfModules);
            vnf.setVnfData(vnfData);
            vnfs.addVnfItem(vnf);
        }
        svcData.setVnfs(vnfs);
        return(svcData);
    }

    private void saveSvcData(ConfigServices configService, GenericResourceApiServicedataServiceData svcData) {
        if (svcData == null) {
            return;
        }

        String svcInstanceId = configService.getSvcInstanceId();
        
        // Write networks
        GenericResourceApiServicedataServicedataNetworks networks = svcData.getNetworks();
        List<GenericResourceApiServicedataServicedataNetworksNetwork> networkItems = networks.getNetwork();
        for (GenericResourceApiServicedataServicedataNetworksNetwork networkItem : networkItems) {
            List<ConfigNetworks> configNetworks = configNetworksRepository.findBySvcInstanceIdAndNetworkId(svcInstanceId, networkItem.getNetworkId());
            ConfigNetworks configNetwork;
            if ((configNetworks == null) || (configNetworks.isEmpty())) {
                configNetwork = new ConfigNetworks(svcInstanceId, networkItem.getNetworkId());
            } else {
                configNetwork = configNetworks.get(0);
            }
            configNetwork.setNetworkData(objectMapper.writeValueAsString(networkItem.getNetworkData()));
            configNetworksRepository.save(configNetwork);
        }
        svcData.setNetworks(null);

        // Write vnfs
        GenericResourceApiServicedataServicedataVnfs vnfs = svcData.getVnfs();
        List<GenericResourceApiServicedataServicedataVnfsVnf> vnfItems = vnfs.getVnf();
        for (GenericResourceApiServicedataServicedataVnfsVnf vnfItem : vnfItems) {
            String vnfId = vnfItem.getVnfId();
            List<ConfigVnfs> configVnfs = configVnfsRepository.findBySvcInstanceIdAndVnfId(svcInstanceId, vnfId);
            ConfigVnfs configVnf;
            if ((configVnfs == null) || (configVnfs.isEmpty())) {
                configVnf = new ConfigVnfs(svcInstanceId, vnfId);
            } else {
                configVnf = configVnfs.get(0);
            }

            GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfData = vnfItem.getVnfData();

            // Write vf modules
            GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules vfModules = vnfData.getVfModules();
            List<GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule> vfModuleItems = vfModules.getVfModule();
            for (GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule vfModuleItem : vfModuleItems) {
                List<ConfigVfModules> configVfModules = configVfModulesRepository.findBySvcInstanceIdAndVnfIdAndVfModuleId(svcInstanceId, vnfId, vfModuleItem.getVfModuleId());
                ConfigVfModules configVfModule;
                if ((configVfModules == null) || (configVfModules.isEmpty())) {
                    configVfModule = new ConfigVfModules(svcInstanceId, vnfId, vfModuleItem.getVfModuleId());
                } else {
                    configVfModule = configVfModules.get(0);
                }
                configVfModule.setVfModuleData(objectMapper.writeValueAsString(vfModuleItem.getVfModuleData()));
                configVfModulesRepository.save(configVfModule);
            }
            vnfData.setVfModules(null);

            configVnf.setVnfData(objectMapper.writeValueAsString(vnfData));
            configVnfsRepository.save(configVnf);
        }
        svcData.setVnfs(null);

        configService.setSvcData(objectMapper.writeValueAsString(svcData));
        configServicesRepository.save(configService);
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
        if(hasInvalidServiceId(input.getInput())) {
            log.debug("exiting {} because of null or empty service-instance-id", svcOperation);

            resp.setResponseCode("404");
            resp.setResponseMessage("null or empty service-instance-id");
            resp.setAckFinalIndicator("Y");

            retval.setOutput(resp);

            return new ResponseEntity<>(retval, HttpStatus.OK);
        }

        String svcInstanceId = input.getInput().getServiceInformation().getServiceInstanceId();
        String vnfId = null;

        if((input.getInput() != null) && (input.getInput().getVnfInformation() != null)) {
            vnfId = input.getInput().getVnfInformation().getVnfId();
        }

        SvcLogicContext ctxIn = new SvcLogicContext();

        // Add input to SvcLogicContext
        try {
            ctxIn.mergeJson(svcOperation + "-input", objectMapper.writeValueAsString(input.getInput()));
        }
        catch(JsonProcessingException e) {
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
        if(configServices != null && !configServices.isEmpty()) {
            configService = configServices.get(0);
            ctxIn.mergeJson("service-data", configService.getSvcData());
        }
        else {
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

        if(operServices != null && !operServices.isEmpty()) {
            operService = operServices.get(0);
            ctxIn.mergeJson("operational-data", operService.getSvcData());
        }
        else {
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

            if("200".equals(resp.getResponseCode())) {

                GenericResourceApiInstanceReference serviceReference = new GenericResourceApiInstanceReference();
                serviceReference.setInstanceId(svcInstanceId);
                serviceReference.setObjectPath(respProps.getProperty(SERVICE_OBJECT_PATH_PARAM));
                resp.setServiceResponseInformation(serviceReference);

                if(vnfId == null) {
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

        }
        catch(NullPointerException npe) {
            resp.setAckFinalIndicator("Y");
            resp.setResponseCode("500");
            resp.setResponseMessage("Check that you populated module, rpc and or mode correctly.");
        }
        catch(SvcLogicException e) {
            resp.setAckFinalIndicator("Y");
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
        if(saveOperationalData) {
            operService.setServiceStatus(configService.getServiceStatus());
            operationalServicesRepository.save(operService);
        }

        if(resp.getAckFinalIndicator().equals("N")) {
            // Spawn background thread to invoke the Async DG
            Runnable backgroundThread = () -> {
                log.info(BACKGROUND_THREAD_STARTED_MESSAGE);
                processAsyncVnfTopologyOperation(svcOperation, input);
            };
            new Thread(backgroundThread).start();
        }

        log.info("Returned {} for {} [{}] {}.", resp.getResponseCode(), svcOperation, vnfId, resp.getResponseMessage());
        retval.setOutput(resp);
        return (new ResponseEntity<>(retval, HttpStatus.OK));
    }

    public void processAsyncVnfTopologyOperation(String parentOperation, @Valid GenericResourceApiVnfOperationInformationBodyparam input) {
        final String svcOperation = "vnf-topology-operation-async";
        GenericResourceApiVnftopologyoperationOutput resp = new GenericResourceApiVnftopologyoperationOutput();

        log.info(CALLED_STR, svcOperation);
        // Verify input contains service instance id
        String svcInstanceId = input.getInput().getServiceInformation().getServiceInstanceId();
        String vnfId = input.getInput().getVnfInformation().getVnfId();

        SvcLogicContext ctxIn = new SvcLogicContext();

        // Add input to SvcLogicContext
        try {
            ctxIn.mergeJson(parentOperation + "-input", objectMapper.writeValueAsString(input.getInput()));
        } catch (JsonProcessingException e) {
            log.error("exiting {} due to parse error on input data", svcOperation);
            return;
        }

        // Add config tree data to SvcLogicContext
        List<ConfigServices> configServices = configServicesRepository.findBySvcInstanceId(svcInstanceId);
        ConfigServices configService = null;
        if(configServices != null && !configServices.isEmpty()) {
            configService = configServices.get(0);
            ctxIn.mergeJson("service-data", configService.getSvcData());
        }
        else {
            log.error("exiting {} because there is no service data with id [{}] in SDN", svcOperation, svcInstanceId);
            return;
        }

        // Add operational tree data to SvcLogicContext
        List<OperationalServices> operServices = operationalServicesRepository.findBySvcInstanceId(svcInstanceId);
        OperationalServices operService = null;

        if(operServices != null && !operServices.isEmpty()) {
            operService = operServices.get(0);
            ctxIn.mergeJson("operational-data", operService.getSvcData());
        }
        else {
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

            if("200".equals(resp.getResponseCode())) {
                GenericResourceApiInstanceReference serviceReference = new GenericResourceApiInstanceReference();
                serviceReference.setInstanceId(svcInstanceId);
                serviceReference.setObjectPath(respProps.getProperty(SERVICE_OBJECT_PATH_PARAM));
                resp.setServiceResponseInformation(serviceReference);

                if(vnfId == null) {
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

                // Update status in config services entry

                configService.setServiceStatusFinalIndicator(resp.getAckFinalIndicator());
                configService.setServiceStatusResponseCode(resp.getResponseCode());
                configService.setServiceStatusResponseMessage(resp.getResponseMessage());
                configService.setServiceStatusResponseTimestamp(Iso8601Util.now());

                // Update config and operational tree
                configServicesRepository.save(configService);
                operService.setServiceStatus(configService.getServiceStatus());
                operationalServicesRepository.save(operService);
                log.info("Returned SUCCESS for {} [{}]", svcOperation, vnfId);
            }
        }
        catch(Exception e) {
            log.error("Caught Exception updating configuration status in SDN for {} [{}] \n", svcOperation, vnfId);
        }
    }

    @Override
    public ResponseEntity<GenericResourceApiVfModuleTopologyOperation> operationsGENERICRESOURCEAPIvfModuleTopologyOperationPost(
            @Valid GenericResourceApiVfModuleOperationInformationBodyparam input)
            throws RestException {
        final String svcOperation = "vf-module-topology-operation";
        GenericResourceApiVfModuleTopologyOperation retval = new GenericResourceApiVfModuleTopologyOperation();
        GenericResourceApiVfmoduletopologyoperationOutput resp = new GenericResourceApiVfmoduletopologyoperationOutput();

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

        // Verify input contains vnf-id
        if (hasInvalidVnfId(input.getInput())) {
            log.debug("exiting {} because of null or empty vnf-id", svcOperation);

            resp.setResponseCode("404");
            resp.setResponseMessage("null or empty vnf-id");
            resp.setAckFinalIndicator("Y");
            retval.setOutput(resp);
            return new ResponseEntity<>(retval, HttpStatus.OK);
        }

        String svcInstanceId = input.getInput().getServiceInformation().getServiceInstanceId();
        String vnfId = input.getInput().getVnfInformation().getVnfId();
        String vfModuleId = input.getInput().getVfModuleInformation().getVfModuleId();

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
            log.info("Read ({}) data for [{}] service-data: {}",
                    "CONFIG_GRA_PORT_MIRROR_CONFIGURATIONS", svcInstanceId, configService.getSvcData().toString());
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

        if (operServices != null && !operServices.isEmpty()) {
            operService = operServices.get(0);
            log.info("Read ({}) data for [{}] operational-data: {}",
                    "OPERATIONAL_GRA_PORT_MIRROR_CONFIGURATIONS", svcInstanceId, operService.getSvcData().toString());
            //ctxIn.mergeJson("operational-data", operService.getSvcData());
        } else {
            log.info("No operational-data found in OPERATIONAL_GRA_PORT_MIRROR_CONFIGURATIONS for [{}]", svcInstanceId);
            operService = new OperationalServices(svcInstanceId, null, null);
        }

        // Update service status info in config entry from input
        configService.setServiceStatusAction(input.getInput().getRequestInformation().getRequestAction().toString());
        configService.setServiceStatusRpcAction(input.getInput().getSdncRequestHeader().getSvcAction().toString());
        configService.setServiceStatusRpcName(svcOperation);

        String ackFinal = "Y";
        String skipMdsalUpdate;

        // Call DG
        try {
            // Any of these can throw a nullpointer exception
            // execute should only throw a SvcLogicException
            SvcLogicContext ctxOut = svc.execute(MODULE_NAME, svcOperation, null, "sync", ctxIn);
            Properties respProps = ctxOut.toProperties();

            ackFinal = respProps.getProperty("ack-final", "Y");
            skipMdsalUpdate = respProps.getProperty("skip-mdsal-update", "N");
            log.info("ackFinal [{}], skipMdsalUpdate [{}]", ackFinal, skipMdsalUpdate);

            resp.setAckFinalIndicator(ackFinal);
            resp.setResponseCode(respProps.getProperty("error-code", "200"));
            resp.setResponseMessage(respProps.getProperty("error-message", "SUCCESS"));

            // Update status in config services entry
            configService.setServiceStatusFinalIndicator(resp.getAckFinalIndicator());
            configService.setServiceStatusResponseCode(resp.getResponseCode());
            configService.setServiceStatusResponseMessage(resp.getResponseMessage());
            configService.setServiceStatusResponseTimestamp(Iso8601Util.now());
            configService
                    .setServiceStatusRequestStatus(GenericResourceApiRequestStatusEnumeration.SYNCCOMPLETE.toString());

            if ("200".equals(resp.getResponseCode())) {

                GenericResourceApiInstanceReference serviceReference = new GenericResourceApiInstanceReference();
                serviceReference.setInstanceId(svcInstanceId);
                serviceReference.setObjectPath(respProps.getProperty(SERVICE_OBJECT_PATH_PARAM));
                resp.setServiceResponseInformation(serviceReference);

                GenericResourceApiInstanceReference vnfReference = new GenericResourceApiInstanceReference();
                vnfReference.setInstanceId(vnfId);
                vnfReference.setObjectPath(respProps.getProperty(VNF_OBJECT_PATH_PARAM));
                resp.setVnfResponseInformation(vnfReference);

                GenericResourceApiInstanceReference vfModuleReference = new GenericResourceApiInstanceReference();
                vnfReference.setInstanceId(vfModuleId);
                vnfReference.setObjectPath(respProps.getProperty(VF_MODULE_OBJECT_PATH_PARAM));
                resp.setVnfResponseInformation(vfModuleReference);

                if (skipMdsalUpdate.equals("N")) {
                    // If DG returns success,
                    // ONLY update svcData in config and operational trees
                    // and remember to save operational data when skip-mdsal-update is Y in ctx.
                    String ctxJson = ctxOut.toJsonString("service-data");
                    log.info("Saving service-data in SDN because skiMdsalUpdate is {}", skipMdsalUpdate);
                    configService.setSvcData(ctxJson);
                    configServicesRepository.save(configService);

                    log.info("Copying service-data to operational-data");
                    operService.setSvcData(ctxJson);
                    operService.setServiceStatus(configService.getServiceStatus());
                    operationalServicesRepository.save(operService);
                }
            }
        } catch (NullPointerException npe) {
            resp.setAckFinalIndicator("Y");
            resp.setResponseCode("500");
            resp.setResponseMessage("Check that you populated module, rpc and or mode correctly.");
        } catch (SvcLogicException e) {
            resp.setAckFinalIndicator("Y");
        }

        if (ackFinal.equals("N")) {
            // Spawn background thread to invoke the Async DG
            Runnable backgroundThread = new Runnable() {
                public void run() {
                    log.info(BACKGROUND_THREAD_STARTED_MESSAGE);
                    processAsyncVfModuleTopologyOperation(svcOperation, input);
                }
            };
            new Thread(backgroundThread).start();
        }
        retval.setOutput(resp);
        return (new ResponseEntity<>(retval, HttpStatus.OK));
    }

    public void processAsyncVfModuleTopologyOperation( String parentOperation,
            @Valid GenericResourceApiVfModuleOperationInformationBodyparam input) {
        log.info(BACKGROUND_THREAD_INFO, input.getInput().getVfModuleInformation().getVfModuleId());
        final String svcOperation = "vf-module-topology-operation-async";

        log.info(CALLED_STR, svcOperation);
        String svcInstanceId = input.getInput().getServiceInformation().getServiceInstanceId();
        SvcLogicContext ctxIn = new SvcLogicContext();

        // Add input to SvcLogicContext
        try {
            ctxIn.mergeJson(parentOperation + "-input", objectMapper.writeValueAsString(input.getInput()));
        } catch (JsonProcessingException e) {
            log.error("exiting {} due to parse error on input data", svcOperation);
            return;
        }

        // Add config tree data to SvcLogicContext
        List<ConfigServices> configServices = configServicesRepository.findBySvcInstanceId(svcInstanceId);
        ConfigServices configService = null;
        if (configServices != null && !configServices.isEmpty()) {
            configService = configServices.get(0);
            log.info("Read ({}) data for [{}] service-data: {}",
                    "CONFIG_GRA_PORT_MIRROR_CONFIGURATIONS", svcInstanceId, configService.getSvcData().toString());
            ctxIn.mergeJson("service-data", configService.getSvcData());
        } else {
            log.debug("exiting {} because the service-instance does not have any service data in SDN", svcOperation);
            return;
        }

        // Add operational tree data to SvcLogicContext
        List<OperationalServices> operServices = operationalServicesRepository.findBySvcInstanceId(svcInstanceId);
        OperationalServices operService = null;

        if (operServices != null && !operServices.isEmpty()) {
            operService = operServices.get(0);
            log.info("Read ({}) data for [{}] operational-data: {}",
                    "OPERATIONAL_GRA_PORT_MIRROR_CONFIGURATIONS", svcInstanceId, operService.getSvcData().toString());
            //ctxIn.mergeJson("operational-data", operService.getSvcData());
        } else {
            log.info("No operational-data found in OPERATIONAL_GRA_PORT_MIRROR_CONFIGURATIONS for [{}]", svcInstanceId);
            operService = new OperationalServices(svcInstanceId, null, null);
        }

        // Update service status info in config entry from input
        configService.setServiceStatusAction(input.getInput().getRequestInformation().getRequestAction().toString());
        configService.setServiceStatusRpcAction(input.getInput().getSdncRequestHeader().getSvcAction().toString());
        configService.setServiceStatusRpcName(svcOperation);

        String respStatus = "SUCCESS";
        String errorMessage = null;

        log.info ("Adding INPUT data for {} [{}] input: {}", svcOperation, svcInstanceId, input.getInput().toString());

        // Call DG
        try {
            // Any of these can throw a nullpointer exception
            // execute should only throw a SvcLogicException
            SvcLogicContext ctxOut = svc.execute(MODULE_NAME, svcOperation, null, "sync", ctxIn);
            Properties respProps = ctxOut.toProperties();

            String ackFinal = respProps.getProperty("ack-final-indicator", "Y");
            String errorCode = respProps.getProperty("error-code", "200");
            errorMessage = respProps.getProperty("error-message", "SUCCESS");

            if (! "200".equals(errorCode)) {
                respStatus = "FAILED";
            }

            // Update status in config services entry
            configService.setServiceStatusFinalIndicator(ackFinal);
            configService.setServiceStatusResponseTimestamp(Iso8601Util.now());
            configService.setServiceStatusResponseCode(errorCode);
            configService.setServiceStatusResponseMessage(errorMessage);
            configService
                    .setServiceStatusRequestStatus(GenericResourceApiRequestStatusEnumeration.SYNCCOMPLETE.toString());

            String ctxJson = ctxOut.toJsonString("service-data");
            configServicesRepository.save(configService);

            operService.setSvcData(ctxJson);
            operService.setServiceStatus(configService.getServiceStatus());
            operationalServicesRepository.save(operService);

        } catch (Exception ex) {
            log.error("Caught Exception updating service status in SDN for {} [{}] \n", svcOperation, svcInstanceId);
        }
        log.info("Returned {} for {} [{}] {}.", respStatus, svcOperation, svcInstanceId, errorMessage);
    }

    @Override
    public ResponseEntity<GenericResourceApiPortMirrorTopologyOperation> operationsGENERICRESOURCEAPIportMirrorTopologyOperationPost(
            @Valid GenericResourceApiPortMirrorTopologyOperationInformationBodyparam input)
            throws RestException {
        final String svcOperation = "port-mirror-topology-operation";
        GenericResourceApiPortMirrorTopologyOperation retval = new GenericResourceApiPortMirrorTopologyOperation();
        GenericResourceApiPortmirrortopologyoperationOutput resp = new GenericResourceApiPortmirrortopologyoperationOutput();

        log.info(CALLED_STR, svcOperation);

        // Verify input contains configuration-id
        if (hasInvalidConfigurationId(input.getInput())) {
            log.debug("exiting {} because of null or empty configuration-id", svcOperation);

            resp.setResponseCode("404");
            resp.setResponseMessage("null or empty configuration-id");
            resp.setAckFinalIndicator("Y");
            retval.setOutput(resp);
            return new ResponseEntity<>(retval, HttpStatus.OK);
        }

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
        String configurationId = input.getInput().getConfigurationInformation().getConfigurationId();

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

        // Add service instance config data to SvcLogicContext
        List<ConfigServices> configServices = configServicesRepository.findBySvcInstanceId(svcInstanceId);
        ConfigServices configService = null;
        if (configServices != null && !configServices.isEmpty()) {
            configService = configServices.get(0);
            log.info("Read ({}) data for [{}] service-data: {}", "CONFIG_GRA_SERVICES", svcInstanceId, configService.getSvcData());
            ctxIn.mergeJson("service-data", configService.getSvcData());

        } else {
            log.debug("exiting {} because there is no service data with id [{}] in SDN", svcOperation, svcInstanceId);
            resp.setResponseCode("404");
            resp.setResponseMessage("invalid input: there is no service-instance with id [{}] in SDNC");
            resp.setAckFinalIndicator("Y");

            retval.setOutput(resp);
            return new ResponseEntity<>(retval, HttpStatus.OK);
        }

        // Add configuration config data to SvcLogicContext
        List<ConfigPortMirrorConfigurations> configPortMirrorConfigurations =
                configPortMirrorConfigurationsRepository.findByConfigurationId(configurationId);
        ConfigPortMirrorConfigurations configPortMirrorConfiguration;
        if (configPortMirrorConfigurations != null && !configPortMirrorConfigurations.isEmpty()) {
            configPortMirrorConfiguration = configPortMirrorConfigurations.get(0);
            log.info("Read ({}) data for [{}] configuration-data: {}",
                    "CONFIG_GRA_PORT_MIRROR_CONFIGURATIONS", configurationId, configPortMirrorConfiguration.getPmcData());
            ctxIn.mergeJson("configuration-data", configPortMirrorConfiguration.getPmcData());

        } else {
            log.info("No configuration-data found ({}) for [{}]", "CONFIG_GRA_PORT_MIRROR_CONFIGURATIONS", configurationId);
            configPortMirrorConfiguration = new ConfigPortMirrorConfigurations(configurationId, null);
        }

        /*
        // Add configuration operational data to SvcLogicContext
        List<OperationalPortMirrorConfigurations> operPortMirrorConfigurations =
                operationalPortMirrorConfigurationsRepository.findByConfigurationId(configurationId);
        OperationalPortMirrorConfigurations operPortMirrorConfiguration;

        if (operPortMirrorConfigurations != null && !operPortMirrorConfigurations.isEmpty()) {
            operPortMirrorConfiguration = operPortMirrorConfigurations.get(0);
            log.info("Read ({}) data for [{}] operational-data: {}",
                    "OPERATIONAL_GRA_PORT_MIRROR_CONFIGURATIONS", configurationId, operPortMirrorConfiguration.getPmcData());
            ctxIn.mergeJson("operational-data", operPortMirrorConfiguration.getPmcData());

        } else {
            log.info("No operational-data found ({}) for [{}]", "OPERATIONAL_GRA_PORT_MIRROR_CONFIGURATIONS", configurationId);
            operPortMirrorConfiguration = new OperationalPortMirrorConfigurations(configurationId, null, null);
        }

        */

        String ackFinal = "Y";
        String errorCode = "200";
        String errorMessage = "SUCCESS";
        String respStatus = "SUCCESS";

        log.info(ADDING_INPUT_DATA_LOG, svcOperation, svcInstanceId, input.getInput().toString());
        //log.info(ADDING_INPUT_DATA_LOG, svcOperation, svcInstanceId, input.toString());

        // Update service status info in config entry from input
        configPortMirrorConfiguration.setPortMirrorConfigurationStatusAction(input.getInput().getRequestInformation().getRequestAction().toString());
        configPortMirrorConfiguration.setPortMirrorConfigurationStatusRpcAction(input.getInput().getSdncRequestHeader().getSvcAction().toString());
        configPortMirrorConfiguration.setPortMirrorConfigurationStatusRpcName(svcOperation);

        // Call DG
        try {
            // Any of these can throw a nullpointer exception
            // execute should only throw a SvcLogicException
            SvcLogicContext ctxOut = svc.execute(MODULE_NAME, svcOperation, null, "sync", ctxIn);
            Properties respProps = ctxOut.toProperties();

            ackFinal = respProps.getProperty("ack-final", "Y");
            errorCode = respProps.getProperty("error-code", "200");
            errorMessage = respProps.getProperty("error-message", "SUCCESS");
            log.info("ackFinal [{}], error-code [{}], error-message [{}]", ackFinal, errorCode, errorMessage);

            resp.setAckFinalIndicator(ackFinal);
            resp.setResponseCode(errorCode);
            resp.setResponseMessage(errorMessage);

            // Update status in config services entry
            configPortMirrorConfiguration
                    .setPortMirrorConfigurationStatusRequestStatus(GenericResourceApiRequestStatusEnumeration.SYNCCOMPLETE.toString());
            configPortMirrorConfiguration.setPortMirrorConfigurationStatusFinalIndicator(resp.getAckFinalIndicator());
            configPortMirrorConfiguration.setPortMirrorConfigurationStatusResponseCode(resp.getResponseCode());
            configPortMirrorConfiguration.setPortMirrorConfigurationStatusResponseMessage(resp.getResponseMessage());
            configPortMirrorConfiguration.setPortMirrorConfigurationStatusResponseTimestamp(Iso8601Util.now());

            GenericResourceApiInstanceReference serviceReference = new GenericResourceApiInstanceReference();
            serviceReference.setInstanceId(svcInstanceId);
            serviceReference.setObjectPath(respProps.getProperty(SERVICE_OBJECT_PATH_PARAM));
            resp.setServiceResponseInformation(serviceReference);

            GenericResourceApiInstanceReference pmcReference = new GenericResourceApiInstanceReference();
            serviceReference.setInstanceId(configurationId);
            serviceReference.setObjectPath(respProps.getProperty(PORT_MIRROR_OBJECT_PATH_PARAM));
            resp.setServiceResponseInformation(pmcReference);

            retval.setOutput(resp);

            // ONLY update pmcData in config and operational trees
            //String ctxJson = ctxOut.toJsonString("configuration-data");
            //configPortMirrorConfiguration.setPmcData(ctxJson);
            //operPortMirrorConfiguration.setPmcData(ctxJson);
            //configPortMirrorConfigurationsRepository.save(configPortMirrorConfiguration);

            // If necessary, sync status to operation service entry and save
            //operPortMirrorConfiguration.setPortMirrorConfigurationStatus(configPortMirrorConfiguration.getPortMirrorConfigurationStatus());
            //operationalPortMirrorConfigurationsRepository.save(operPortMirrorConfiguration);

            if (! "200".equals(errorCode)) {
                respStatus = "FAILED";
            }

        } catch (NullPointerException npe) {
            resp.setAckFinalIndicator("Y");
            resp.setResponseCode("500");
            resp.setResponseMessage("Check that you populated module, rpc and or mode correctly.");
        } catch (SvcLogicException e) {
            resp.setAckFinalIndicator("Y");
        }

        if (ackFinal.equals("N")) {
            // Spawn background thread to invoke the Async DG
            Runnable backgroundThread = new Runnable() {
                public void run() {
                    log.info(BACKGROUND_THREAD_STARTED_MESSAGE);
                    processAsyncPortMirrorTopologyOperation(svcOperation, input);
                 }
            };
            new Thread(backgroundThread).start();
        }
        log.info("Returned {} for {} [{}] {}.", respStatus, svcOperation, configurationId, errorMessage);
        return (new ResponseEntity<>(retval, HttpStatus.OK));
    }

    public void processAsyncPortMirrorTopologyOperation( String parentOperation,
             @Valid GenericResourceApiPortMirrorTopologyOperationInformationBodyparam input) {
        log.info(BACKGROUND_THREAD_INFO, input.getInput().getConfigurationInformation().getConfigurationId());
        final String svcOperation = "port-mirror-topology-operation-async";

        log.info(CALLED_STR, svcOperation);
        String svcInstanceId = input.getInput().getServiceInformation().getServiceInstanceId();
        String configurationId = input.getInput().getConfigurationInformation().getConfigurationId();
        SvcLogicContext ctxIn = new SvcLogicContext();

        String errorMessage;

        // Add input to SvcLogicContext
        try {
            ctxIn.mergeJson(parentOperation + "-input", objectMapper.writeValueAsString(input.getInput()));
        } catch (JsonProcessingException e) {
            log.error("exiting {} due to parse error on input data", svcOperation);
            return;
        }

        // Add service instance config data to SvcLogicContext
        List<ConfigServices> configServices = configServicesRepository.findBySvcInstanceId(svcInstanceId);
        ConfigServices configService = null;
        if (configServices != null && !configServices.isEmpty()) {
            configService = configServices.get(0);
            ctxIn.mergeJson("service-data", configService.getSvcData());
        } else {
            log.error("exiting {} because there is no service data with id [{}] in SDN", svcOperation, svcInstanceId);
            return;
        }

        // Add config tree data to SvcLogicContext
        List<ConfigPortMirrorConfigurations> configPortMirrorConfigurations = configPortMirrorConfigurationsRepository.findByConfigurationId(configurationId);
        ConfigPortMirrorConfigurations configPortMirrorConfiguration;
        if (configPortMirrorConfigurations != null && !configPortMirrorConfigurations.isEmpty()) {
            configPortMirrorConfiguration = configPortMirrorConfigurations.get(0);
            ctxIn.mergeJson("configuration-data", configPortMirrorConfiguration.getPmcData());
        } else {
            configPortMirrorConfiguration = new ConfigPortMirrorConfigurations(configurationId, null);
        }

        /*
        // Add operational tree data to SvcLogicContext
        List<OperationalPortMirrorConfigurations> operPortMirrorConfigurations = operationalPortMirrorConfigurationsRepository.findByConfigurationId(configurationId);
        OperationalPortMirrorConfigurations operPortMirrorConfiguration = null;

        if (operPortMirrorConfigurations != null && !operPortMirrorConfigurations.isEmpty()) {
            operPortMirrorConfiguration = operPortMirrorConfigurations.get(0);
            ctxIn.mergeJson("operational-data", operPortMirrorConfiguration.getPmcData());
        } else {
            operPortMirrorConfiguration = new OperationalPortMirrorConfigurations(configurationId, null, null);
        }

        */

        // Update service status info in config entry from input
        configPortMirrorConfiguration.setPortMirrorConfigurationStatusAction(input.getInput().getRequestInformation().getRequestAction().toString());
        configPortMirrorConfiguration.setPortMirrorConfigurationStatusRpcAction(input.getInput().getSdncRequestHeader().getSvcAction().toString());
        configPortMirrorConfiguration.setPortMirrorConfigurationStatusRpcName(parentOperation);

        log.info("Adding INPUT data for {} [{}] input: {}", svcOperation, svcInstanceId, input.toString());

        // Call DG
        try {
            // Any of these can throw a nullpointer exception
            // execute should only throw a SvcLogicException
            SvcLogicContext ctxOut = svc.execute(MODULE_NAME, svcOperation, null, "sync", ctxIn);
            Properties respProps = ctxOut.toProperties();

            String ackFinalIndicator = respProps.getProperty("ack-final-indicator", "Y");
            String errorCode = respProps.getProperty("error-code", "200");
            errorMessage = respProps.getProperty("error-message", "SUCCESS");

            // Update status in config services entry
            configPortMirrorConfiguration.setPortMirrorConfigurationStatusFinalIndicator(ackFinalIndicator);
            configPortMirrorConfiguration.setPortMirrorConfigurationStatusResponseTimestamp(Iso8601Util.now());
            configPortMirrorConfiguration.setPortMirrorConfigurationStatusResponseCode(errorCode);
            configPortMirrorConfiguration.setPortMirrorConfigurationStatusResponseMessage(errorMessage);
            configPortMirrorConfiguration
                    .setPortMirrorConfigurationStatusRequestStatus(GenericResourceApiRequestStatusEnumeration.SYNCCOMPLETE.toString());

            // ONLY update status
            //String ctxJson = ctxOut.toJsonString("configuration-data");
            //configPortMirrorConfiguration.setPmcData(ctxJson);
            //configPortMirrorConfiguration.setPmcData(ctxJson);

            // Update config tree
            configPortMirrorConfigurationsRepository.save(configPortMirrorConfiguration);

            //update operational tree
            //operPortMirrorConfiguration.setPortMirrorConfigurationStatus(configPortMirrorConfiguration.getPortMirrorConfigurationStatus());
            //operationalPortMirrorConfigurationsRepository.save(operPortMirrorConfiguration);

        } catch (Exception e) {
            log.error("Caught Exception updating configuration status in SDN for {} [{}] \n", svcOperation, configurationId);
        }
        log.info("Returned SUCCESS for {} [{}]", svcOperation, configurationId);
    }

    @Override
    public ResponseEntity<GenericResourceApiVnfGetResourceRequest> operationsGENERICRESOURCEAPIvnfGetResourceRequestPost(
            @Valid GenericResourceApiVnfgetresourcerequestInputBodyparam input)
            throws RestException {
        final String svcOperation = "vnf-get-resource-request";
        GenericResourceApiVnfGetResourceRequest retval = new GenericResourceApiVnfGetResourceRequest();

        log.info(CALLED_STR, svcOperation);
        // Verify input contains service instance id
        if (hasInvalidServiceId(input.getInput())) {
            log.debug("exiting {} because of null or empty service-instance-id", svcOperation);
            //return new ResponseEntity<>(retval, HttpStatus.OK);
        }

        String svcInstanceId = input.getInput().getServiceInformation().getServiceInstanceId();
        SvcLogicContext ctxIn = new SvcLogicContext();

        // Add input to SvcLogicContext
        try {
            ctxIn.mergeJson(svcOperation + "-input", objectMapper.writeValueAsString(input.getInput()));
        } catch (JsonProcessingException e) {
            log.error("exiting {} due to parse error on input data", svcOperation);
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
            return new ResponseEntity<>(retval, HttpStatus.OK);
        }

        // Call DG
        try {
            // Any of these can throw a nullpointer exception
            // execute should only throw a SvcLogicException
            SvcLogicContext ctxOut = svc.execute(MODULE_NAME, svcOperation, null, "sync", ctxIn);
            Properties respProps = ctxOut.toProperties();

            /* For debugging Only
            respProps.forEach((k,v) -> {
                log.debug("prop: {} -> {}",k,v);
            });
            */

            String ctxJson = ctxOut.toJsonString("vnf-get-resource-request-output");
            GenericResourceApiVnfgetresourcerequestOutput vnfgetresourcerequestOutput =
                    objectMapper.readValue(ctxJson, GenericResourceApiVnfgetresourcerequestOutput.class);

            retval.setOutput(vnfgetresourcerequestOutput);

        } catch (Exception e) {
            return (new ResponseEntity<>(retval, HttpStatus.INTERNAL_SERVER_ERROR));
        }
        return (new ResponseEntity<>(retval, HttpStatus.OK));
    }

    @Override
    public ResponseEntity<GenericResourceApiPolicyUpdateNotifyOperation> operationsGENERICRESOURCEAPIpolicyUpdateNotifyOperationPost(
            @Valid GenericResourceApiPolicyupdatenotifyoperationInputBodyparam input)
            throws RestException {
        final String svcOperation = "policy-update-notify-operation";
        GenericResourceApiPolicyUpdateNotifyOperation retval = new GenericResourceApiPolicyUpdateNotifyOperation();
        GenericResourceApiPolicyupdatenotifyoperationOutput resp = new GenericResourceApiPolicyupdatenotifyoperationOutput();

        log.info(CALLED_STR, svcOperation);
        // Verify required input elements
        if (hasInvalidPolicyUpdateInput(input.getInput())) {
            log.debug("exiting {} because policy name, update type, or version id was not provided", svcOperation);
            resp.setErrorCode("404");
            resp.setErrorMsg("policy-name, update-type, and/or version-id is null or empty");
            retval.setOutput(resp);
            return new ResponseEntity<>(retval, HttpStatus.OK);
        }

        SvcLogicContext ctxIn = new SvcLogicContext();

        // Add input to SvcLogicContext
        try {
            ctxIn.mergeJson(svcOperation + "-input", objectMapper.writeValueAsString(input.getInput()));
        } catch (JsonProcessingException e) {
            log.error("exiting {} due to parse error on input data", svcOperation);
            resp.setErrorCode("500");
            resp.setErrorMsg("internal error");
            retval.setOutput(resp);
            return new ResponseEntity<>(retval, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Call DG
        try {
            // Any of these can throw a nullpointer exception
            // execute should only throw a SvcLogicException
            SvcLogicContext ctxOut = svc.execute(MODULE_NAME, svcOperation, null, "sync", ctxIn);
            Properties respProps = ctxOut.toProperties();
            resp.setErrorCode(respProps.getProperty("error-code", "200"));
            resp.setErrorMsg(respProps.getProperty("error-message", "SUCCESS"));

            /* For debugging Only
            respProps.forEach((k,v) -> {
                log.debug("prop: {} -> {}",k,v);
            });
             */

        } catch (NullPointerException npe) {
            resp.setErrorCode("500");
            resp.setErrorMsg("Check that you populated module, rpc and or mode correctly.");
        } catch (SvcLogicException e) {
            resp.setErrorCode("500");
            resp.setErrorMsg(e.getMessage());
        }

        retval.setOutput(resp);
        return (new ResponseEntity<>(retval, HttpStatus.OK));
    }

}
