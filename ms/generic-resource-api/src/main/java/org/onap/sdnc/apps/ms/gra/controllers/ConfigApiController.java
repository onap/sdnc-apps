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
import org.onap.ccsdk.apps.services.RestApplicationException;
import org.onap.ccsdk.apps.services.RestException;
import org.onap.ccsdk.apps.services.RestProtocolError;
import org.onap.ccsdk.apps.services.RestProtocolException;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadData;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigServices;
import org.onap.sdnc.apps.ms.gra.data.ConfigServicesRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigPortMirrorConfigurations;
import org.onap.sdnc.apps.ms.gra.data.ConfigPortMirrorConfigurationsRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigContrailRouteAllottedResources;
import org.onap.sdnc.apps.ms.gra.data.ConfigContrailRouteAllottedResourcesRepository;
import org.onap.sdnc.apps.ms.gra.swagger.ConfigApi;
import org.onap.sdnc.apps.ms.gra.swagger.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@ComponentScan(basePackages = { "org.onap.sdnc.apps.ms.gra.*" })
@EntityScan("org.onap.sdnc.apps.ms.gra.springboot.*")
public class ConfigApiController implements ConfigApi {
    private static final Logger log = LoggerFactory.getLogger(ConfigApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private ConfigPreloadDataRepository configPreloadDataRepository;

    @Autowired
    private ConfigServicesRepository configServicesRepository;

    @Autowired
    private ConfigPortMirrorConfigurationsRepository configPortMirrorConfigurationsRepository;

    @Autowired
    private ConfigContrailRouteAllottedResourcesRepository configContrailRouteAllottedResourcesRepository;


    @Autowired
    public ConfigApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationDelete() {
        configPreloadDataRepository.deleteAll();
        return (new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * Extracts port-mirror configuration data from CONFIG_GRA_PORT_MIRROR_CONFIGURATIONS for a given, configuration-id
     * <p>
     * Maps to /config/GENERIC-RESOURCE-API:port-mirror-configurations/port-mirror-configuration/{configuration-id}/
     * @param configurationId the configuration ID for a port-mirror
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    public ResponseEntity<GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration>
                configGENERICRESOURCEAPIportMirrorConfigurationsPortMirrorConfigurationConfigurationIdGet(
                    String configurationId) throws RestApplicationException {
        GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration retval = null;

        List<ConfigPortMirrorConfigurations> pmConfigurations = configPortMirrorConfigurationsRepository.findByConfigurationId(configurationId);

        if (pmConfigurations.isEmpty()) {
            log.info("No configuration data found with id [{}]",configurationId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        } else {
            ConfigPortMirrorConfigurations pmConfiguration = pmConfigurations.get(0);
            retval = new GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration();
            retval.setConfigurationId(configurationId);
            retval.setConfigurationStatus(pmConfiguration.getPortMirrorConfigurationStatus());
            try {
                retval.setConfigurationData(objectMapper.readValue(pmConfiguration.getPmcData(), GenericResourceApiPortmirrorconfigurationsPortmirrorconfigurationConfigurationData.class));
            } catch (JsonProcessingException e) {
                log.error("Could not deserialize service data for service instance id {}", configurationId, e);
                throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
        return new ResponseEntity<>(retval, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIportMirrorConfigurationsPortMirrorConfigurationConfigurationIdPut(
            String configurationId, @Valid GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration newConfiguration)
                throws RestApplicationException {

        boolean dataExists = false;

        String newConfigurationId = newConfiguration.getConfigurationId();

        ConfigPortMirrorConfigurations portMirrorConfiguration = null;
        List<ConfigPortMirrorConfigurations> existingConfiguration = configPortMirrorConfigurationsRepository.findByConfigurationId(configurationId);
        if ((existingConfiguration != null) && !existingConfiguration.isEmpty()) {
            dataExists = true;
            portMirrorConfiguration = existingConfiguration.get(0);
        } else {
            portMirrorConfiguration = new ConfigPortMirrorConfigurations();
            portMirrorConfiguration.setConfigureationId(configurationId);
        }

        try {
            portMirrorConfiguration.setPmcData(objectMapper.writeValueAsString(newConfiguration.getConfigurationData()));
        } catch (JsonProcessingException e) {
            log.error("Could not serialize porr-mirror configuration data for {}", portMirrorConfiguration.getConfigureationId(), e);
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
        portMirrorConfiguration.setPortMirrorConfigurationStatus(newConfiguration.getConfigurationStatus());
        configPortMirrorConfigurationsRepository.save(portMirrorConfiguration);

        if (dataExists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<GenericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology>
            configGENERICRESOURCEAPIportMirrorConfigurationsPortMirrorConfigurationConfigurationIdConfigurationDataPortMirrorConfigurationTopologyGet(
                    String configurationId) throws RestApplicationException, RestProtocolException {
        @Valid GenericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology portMirrorConfigurationTopology = null;
        GenericResourceApiPortmirrorconfigurationsPortmirrorconfigurationConfigurationData portMirrorConfigurationData = null;

        List<ConfigPortMirrorConfigurations> configPortMirrorConfigurations = configPortMirrorConfigurationsRepository.findByConfigurationId(configurationId);
        if ((configPortMirrorConfigurations == null) || (configPortMirrorConfigurations.isEmpty())) {
            log.info("No configuration data found with id [{}]", configurationId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        try {
            if ( configPortMirrorConfigurations.get(0).getPmcData().isEmpty()) {
                throw new RestProtocolException("data-missing", "No configuration-data entry found", HttpStatus.NOT_FOUND.value());
            } else {
                portMirrorConfigurationData = objectMapper.readValue(configPortMirrorConfigurations.get(0).getPmcData(), GenericResourceApiPortmirrorconfigurationsPortmirrorconfigurationConfigurationData.class);
                portMirrorConfigurationTopology = portMirrorConfigurationData.getPortMirrorConfigurationTopology();
            }
            if (portMirrorConfigurationTopology == null) {
                throw new RestProtocolException("data-missing", "No service-topology entry found", HttpStatus.NOT_FOUND.value());
            }
            return new ResponseEntity<>(portMirrorConfigurationTopology, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }


    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIportMirrorConfigurationsPortMirrorConfigurationConfigurationIdDelete(String configurationId) {
        configPortMirrorConfigurationsRepository.deleteByConfigurationId(configurationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Extracts contrail-route-allotted-resource data from CONFIG_GRA_CONTRAIL_ROUTE_ALLOTTED_RESOURCES for a given allottedResourceId
     * <p>
     * Maps to /config/GENERIC-RESOURCE-API:contrail-route-allotted-resources/contrail-route-allotted-resource/{allotted-resource-id}
     * @param allottedResourceId the allotted-resource-id for a contrail-route
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    public ResponseEntity<GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource>
                configGENERICRESOURCEAPIcontrailRouteAllottedResourcesContrailRouteAllottedResourceAllottedResourceIdGet(
                        String allottedResourceId) throws RestApplicationException {
        GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource retval = null;

        List<ConfigContrailRouteAllottedResources> allottedResources = configContrailRouteAllottedResourcesRepository.findByAllottedResourceId(allottedResourceId);

        if (allottedResources.isEmpty()) {
            log.info("No contrail-route-allotted-resource found with id [{}]", allottedResourceId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }
        else {
            ConfigContrailRouteAllottedResources allottedResource = allottedResources.get(0);
            retval = new GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource();
            retval.setAllottedResourceId(allottedResourceId);
            retval.setAllottedResourceStatus(allottedResource.getAllottedResourceStatus());
            try {
                retval.setAllottedResourceData(objectMapper.readValue(allottedResource.getArData(),
                        GenericResourceApiContrailrouteallottedresourcesContrailrouteallottedresourceAllottedResourceData.class));
            } catch (JsonProcessingException e) {
                log.error("Could not deserialize service data for service instance id {}", allottedResourceId, e);
                throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
        return new ResponseEntity<>(retval, HttpStatus.OK);
    }

    /**
     * PUT contrail-route-allotted-resource data from CONFIG_GRA_CONTRAIL_ROUTE_ALLOTTED_RESOURCES for a given allottedResourceId
     * <p>
     * Maps to /config/GENERIC-RESOURCE-API:contrail-route-allotted-resources/contrail-route-allotted-resource/{allotted-resource-id}
     * @param allottedResourceId the allotted-resource-id for a contrail-route
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIcontrailRouteAllottedResourcesContrailRouteAllottedResourceAllottedResourceIdPut(
            String allottedResourceId, @Valid GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource newAllottedResource)
            throws RestApplicationException {

        boolean dataExists = false;

        String newAllottedResourceId = newAllottedResource.getAllottedResourceId();

        ConfigContrailRouteAllottedResources allottedResource = null;
        List<ConfigContrailRouteAllottedResources> existingAllottedResource =
                configContrailRouteAllottedResourcesRepository.findByAllottedResourceId(allottedResourceId);

        if ((existingAllottedResource != null) && !existingAllottedResource.isEmpty()) {
            dataExists = true;
            allottedResource = existingAllottedResource.get(0);
        } else {
            allottedResource = new ConfigContrailRouteAllottedResources();
            allottedResource.setAllottedResourceId(allottedResourceId);
        }

        try {
            allottedResource.setArData(objectMapper.writeValueAsString(newAllottedResource.getAllottedResourceData()));
        } catch (JsonProcessingException e) {
            log.error("Could not serialize porr-mirror configuration data for {}", allottedResource.getAllottedResourceId(), e);
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
        allottedResource.setAllottedResourceStatus(newAllottedResource.getAllottedResourceStatus());
        configContrailRouteAllottedResourcesRepository.save(allottedResource);

        if (dataExists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    /**
     * Extracts contrail-route-topology data from CONFIG_GRA_CONTRAIL_ROUTE_ALLOTTED_RESOURCES for a given allottedResourceId
     * <p>
     * Maps to /config/GENERIC-RESOURCE-API:contrail-route-allotted-resources/contrail-route-allotted-resource/{allotted-resource-id}/allotted-resource-data/contrail-route-topology/
     * @param allottedResourceId the allotted-resource-id for a contrail-route
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    @Override
    public ResponseEntity<GenericResourceApiContrailroutetopologyContrailRouteTopology>
            configGENERICRESOURCEAPIcontrailRouteAllottedResourcesContrailRouteAllottedResourceAllottedResourceIdAllottedResourceDataContrailRouteTopologyGet(
                String allottedResourceId) throws RestApplicationException, RestProtocolException {
        @Valid GenericResourceApiContrailroutetopologyContrailRouteTopology contrailRouteTopology = null;
        GenericResourceApiContrailrouteallottedresourcesContrailrouteallottedresourceAllottedResourceData allottedResourceData = null;

        List<ConfigContrailRouteAllottedResources> configContrailRouteAllottedResources =
                configContrailRouteAllottedResourcesRepository.findByAllottedResourceId(allottedResourceId);

        if ((configContrailRouteAllottedResources == null) || (configContrailRouteAllottedResources.isEmpty())) {
            log.info("No contrail-route-allotted-resoure data found with id [{}]", allottedResourceId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        try {
            if ( configContrailRouteAllottedResources.get(0).getArData().isEmpty()) {
                throw new RestProtocolException("data-missing", "No allotted-resource-data entry found", HttpStatus.NOT_FOUND.value());
            } else {
                allottedResourceData = objectMapper.readValue(configContrailRouteAllottedResources.get(0).getArData(),
                        GenericResourceApiContrailrouteallottedresourcesContrailrouteallottedresourceAllottedResourceData.class);

                contrailRouteTopology = allottedResourceData.getContrailRouteTopology();
            }
            if (contrailRouteTopology == null) {
                throw new RestProtocolException("data-missing", "No contrail-route-topology entry found", HttpStatus.NOT_FOUND.value());
            }
            return new ResponseEntity<>(contrailRouteTopology, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("Could not parse port-mirror-configuration data", e);
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }


    /**
     * DELETE allotted-resource data from CONFIG_GRA_CONTRAIL_ROUTE_ALLOTTED_RESOURCES for a given allottedResourceId
     * <p>
     * Maps to /config/GENERIC-RESOURCE-API:contrail-route-allotted-resources/contrail-route-allotted-resource/{allotted-resource-id}
     * @param allottedResourceId the allotted-resource-id for a contrail-route
     * @return HttpStatus.NO_CONTENT (204) if the data is found.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIcontrailRouteAllottedResourcesContrailRouteAllottedResourceAllottedResourceIdDelete(
            String allottedResourceId) {
        configContrailRouteAllottedResourcesRepository.deleteByAllottedResourceId(allottedResourceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Override
    public ResponseEntity<GenericResourceApiPreloadModelInformation> configGENERICRESOURCEAPIpreloadInformationGet()
            throws RestApplicationException {
        GenericResourceApiPreloadModelInformation genericResourceApiPreloadModelInformation = new GenericResourceApiPreloadModelInformation();

        if (configPreloadDataRepository.count() == 0) {
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        for (ConfigPreloadData configPreloadData : configPreloadDataRepository.findAll()) {
            GenericResourceApiPreloadmodelinformationPreloadList preloadListItem = new GenericResourceApiPreloadmodelinformationPreloadList();

            preloadListItem.setPreloadId(configPreloadData.getPreloadId());
            preloadListItem.setPreloadType(configPreloadData.getPreloadType());
            try {
                preloadListItem.setPreloadData(objectMapper.readValue(configPreloadData.getPreloadData(),
                        GenericResourceApiPreloaddataPreloadData.class));
            } catch (JsonProcessingException e) {
                log.error("Could not convert preload data", e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
            genericResourceApiPreloadModelInformation.addPreloadListItem(preloadListItem);
        }

        return new ResponseEntity<>(genericResourceApiPreloadModelInformation, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation> configGENERICRESOURCEAPIpreloadInformationPreloadListPreloadIdPreloadTypePreloadDataPreloadNetworkTopologyInformationGet(
            String preloadId, String preloadType) throws RestException {
        GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation netTopoInfo = null;

        if (configPreloadDataRepository.count() == 0) {
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        for (ConfigPreloadData configPreloadData : configPreloadDataRepository.findAll()) {

            try {
                GenericResourceApiPreloaddataPreloadData preloadDataItem = objectMapper
                        .readValue(configPreloadData.getPreloadData(), GenericResourceApiPreloaddataPreloadData.class);
                netTopoInfo = preloadDataItem.getPreloadNetworkTopologyInformation();
            } catch (JsonProcessingException e) {
                log.error("Could not convert preload data", e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
        
        return new ResponseEntity<>(netTopoInfo, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationPost(
            @Valid GenericResourceApiPreloadModelInformation graPreloadModelInfo)
            throws RestApplicationException, RestProtocolException {

        List<GenericResourceApiPreloadmodelinformationPreloadList> preloadList = graPreloadModelInfo.getPreloadList();
        List<ConfigPreloadData> newPreloadData = new LinkedList<>();

        if (preloadList != null) {
            // Verification pass - if any items already exist, return an error
            for (GenericResourceApiPreloadmodelinformationPreloadList curItem : preloadList) {

                List<ConfigPreloadData> curPreloadData = configPreloadDataRepository
                        .findByPreloadIdAndPreloadType(curItem.getPreloadId(), curItem.getPreloadType());
                if ((curPreloadData != null) && (!curPreloadData.isEmpty())) {
                    log.error("Preload data already exists for {}:{}", curItem.getPreloadId(),
                            curItem.getPreloadType());
                    throw new RestProtocolException("data-exists",
                            "Data already exists for " + curItem.getPreloadId() + ":" + curItem.getPreloadType(),
                            HttpStatus.CONFLICT.value());
                } else {
                    try {
                        newPreloadData.add(new ConfigPreloadData(curItem.getPreloadId(), curItem.getPreloadType(),
                                objectMapper.writeValueAsString(curItem.getPreloadData())));
                    } catch (JsonProcessingException e) {
                        log.error("Cannot convert preload data");
                        throw new RestApplicationException("data-conversion",
                                "Request could not be completed due to internal error", e,
                                HttpStatus.INTERNAL_SERVER_ERROR.value());

                    }
                }
            }

            // Update pass
            for (ConfigPreloadData newDataItem : newPreloadData) {
                log.info("Adding preload data for {}:{}", newDataItem.getPreloadId(), newDataItem.getPreloadType());
                configPreloadDataRepository.save(newDataItem);
            }
        } else {
            throw new RestProtocolException("data-missing", "No preload-list entries found to add",
                    HttpStatus.CONFLICT.value());
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationPut(
            @Valid GenericResourceApiPreloadModelInformation graPreloadModelInfo) throws RestApplicationException {

        boolean addedNew = false;
        List<GenericResourceApiPreloadmodelinformationPreloadList> preloadList = graPreloadModelInfo.getPreloadList();

        if (preloadList != null) {
            Iterator<GenericResourceApiPreloadmodelinformationPreloadList> iter = preloadList.iterator();
            while (iter.hasNext()) {
                GenericResourceApiPreloadmodelinformationPreloadList curItem = iter.next();
                List<ConfigPreloadData> curPreloadData = configPreloadDataRepository
                        .findByPreloadIdAndPreloadType(curItem.getPreloadId(), curItem.getPreloadType());
                if ((curPreloadData == null) || curPreloadData.isEmpty()) {
                    addedNew = true;
                }

                try {
                    configPreloadDataRepository.save(new ConfigPreloadData(curItem.getPreloadId(),
                            curItem.getPreloadType(), objectMapper.writeValueAsString(curItem.getPreloadData())));
                } catch (JsonProcessingException e) {
                    log.error("Cannot convert preload data", e);
                    throw new RestApplicationException("data-conversion",
                            "Request could not be completed due to internal error", e,
                            HttpStatus.INTERNAL_SERVER_ERROR.value());

                }
            }
        }

        if (addedNew) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationPreloadListPost(
            @Valid GenericResourceApiPreloadmodelinformationPreloadList preloadListItem) throws RestProtocolException {

        throw new RestProtocolException("data-missing", "Missing key for list \"preload-list\"",
                HttpStatus.NOT_FOUND.value());
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationPreloadListPreloadIdPreloadTypeDelete(
            String preloadId, String preloadType) {
        configPreloadDataRepository.deleteByPreloadIdAndPreloadType(preloadId, preloadType);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<GenericResourceApiPreloadmodelinformationPreloadList> configGENERICRESOURCEAPIpreloadInformationPreloadListPreloadIdPreloadTypeGet(
            String preloadId, String preloadType) throws RestApplicationException {
        List<ConfigPreloadData> preloadData = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId,
                preloadType);
        if (preloadData != null) {
            if (!preloadData.isEmpty()) {
                ConfigPreloadData preloadDataItem = preloadData.get(0);
                GenericResourceApiPreloadmodelinformationPreloadList preloadDataList = new GenericResourceApiPreloadmodelinformationPreloadList();
                preloadDataList.setPreloadId(preloadDataItem.getPreloadId());
                preloadDataList.setPreloadType(preloadDataItem.getPreloadType());
                try {
                    preloadDataList.setPreloadData(objectMapper.readValue(preloadDataItem.getPreloadData(),
                            GenericResourceApiPreloaddataPreloadData.class));
                } catch (JsonProcessingException e) {
                    log.error("Cannot convert preload data", e);
                    throw new RestApplicationException("data-conversion",
                            "Request could not be completed due to internal error", e,
                            HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
                return new ResponseEntity<>(preloadDataList, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationPreloadListPreloadIdPreloadTypePost(
            String preloadId, String preloadType,
            @Valid GenericResourceApiPreloadmodelinformationPreloadList preloadListItem)
            throws RestApplicationException, RestProtocolException {
        List<ConfigPreloadData> preloadDataItems = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId,
                preloadType);

        if ((preloadDataItems != null) && !preloadDataItems.isEmpty()) {
            log.error("Preload data already exists for {}:{}", preloadId, preloadType);
            throw new RestProtocolException("data-exists", "Data already exists for " + preloadId + ":" + preloadType,
                    HttpStatus.CONFLICT.value());
        }

        try {
            log.info("Adding preload data for {}:{}", preloadId, preloadType);
            configPreloadDataRepository.save(new ConfigPreloadData(preloadId, preloadType,
                    objectMapper.writeValueAsString(preloadListItem.getPreloadData())));
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationPreloadListPreloadIdPreloadTypePut(
            String preloadId, String preloadType,
            @Valid GenericResourceApiPreloadmodelinformationPreloadList preloadListItem)
            throws RestApplicationException, RestProtocolException {
        List<ConfigPreloadData> preloadDataItems = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId,
                preloadType);
        boolean dataExists = false;
        if ((preloadDataItems != null) && !preloadDataItems.isEmpty()) {
            dataExists = true;
        }

        if ((preloadListItem.getPreloadId() == null) || (preloadListItem.getPreloadType() == null)
                || (preloadListItem.getPreloadData() == null)) {
            log.error("Invalid list item received: {}", preloadListItem);
            throw new RestProtocolException("bad-attribute", "Invalid data received", HttpStatus.BAD_REQUEST.value());
        }

        try {
            if (dataExists) {
                log.info("Updating preload data for {}:{} -> {}", preloadId, preloadType,
                        objectMapper.writeValueAsString(preloadListItem));

            } else {
                log.info("Adding preload data for {}:{}", preloadId, preloadType);
            }

            configPreloadDataRepository.save(new ConfigPreloadData(preloadId, preloadType,
                    objectMapper.writeValueAsString(preloadListItem.getPreloadData())));
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());

        }

        if (dataExists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationPreloadListPreloadIdPreloadTypePreloadDataDelete(
            String preloadId, String preloadType) throws RestProtocolException {
        List<ConfigPreloadData> preloadData = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId,
                preloadType);

        if ((preloadData == null) || preloadData.isEmpty()) {
            throw new RestProtocolException("data-missing", "No preload entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigPreloadData preloadDataItem = preloadData.get(0);

        if (preloadDataItem.getPreloadData() == null) {
            throw new RestProtocolException("data-missing", "No preload-data found", HttpStatus.NOT_FOUND.value());
        }
        preloadDataItem.setPreloadData(null);
        configPreloadDataRepository.save(preloadDataItem);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<GenericResourceApiPreloaddataPreloadData> configGENERICRESOURCEAPIpreloadInformationPreloadListPreloadIdPreloadTypePreloadDataGet(
            String preloadId, String preloadType) throws RestApplicationException, RestProtocolException {
        List<ConfigPreloadData> preloadData = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId,
                preloadType);

        if ((preloadData == null) || preloadData.isEmpty()) {
            throw new RestProtocolException("data-missing", "No preload entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigPreloadData preloadDataItem = preloadData.get(0);

        if (preloadDataItem.getPreloadData() == null) {
            throw new RestProtocolException("data-missing", "No preload-data found", HttpStatus.NOT_FOUND.value());
        }
        try {
            return new ResponseEntity<>(objectMapper.readValue(preloadDataItem.getPreloadData(),
                    GenericResourceApiPreloaddataPreloadData.class), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationPreloadListPreloadIdPreloadTypePreloadDataPost(
            String preloadId, String preloadType, @Valid GenericResourceApiPreloaddataPreloadData preloadData)
            throws RestApplicationException, RestProtocolException {
        List<ConfigPreloadData> preloadDataEntries = configPreloadDataRepository
                .findByPreloadIdAndPreloadType(preloadId, preloadType);

        List<ConfigPreloadData> preloadDataItems = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId,
                preloadType);
        if ((preloadDataItems == null) || (preloadDataItems.isEmpty())) {
            throw new RestProtocolException("data-missing", "No preload entry found", HttpStatus.NOT_FOUND.value());
        }

        if ((preloadData == null) || (preloadData.getPreloadNetworkTopologyInformation() == null)) {
            throw new RestProtocolException("bad-attribute", "Invalid preloadData received",
                    HttpStatus.BAD_REQUEST.value());
        }

        ConfigPreloadData preloadDataItem = preloadDataItems.get(0);

        if (preloadDataItem.getPreloadData() != null) {
            log.error("Preload data already exists for {}:{} ", preloadId, preloadType);
            throw new RestProtocolException("data-exists", "Data already exists for " + preloadId + ":" + preloadType,
                    HttpStatus.CONFLICT.value());
        }

        try {
            preloadDataItem.setPreloadData(objectMapper.writeValueAsString(preloadData));
            configPreloadDataRepository.save(preloadDataItem);
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationPreloadListPreloadIdPreloadTypePreloadDataPut(
            String preloadId, String preloadType, @Valid GenericResourceApiPreloaddataPreloadData preloadData)
            throws RestApplicationException, RestProtocolException {
        boolean dataExists = false;
        List<ConfigPreloadData> preloadDataItems = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId,
                preloadType);
        if ((preloadDataItems == null) || (preloadDataItems.isEmpty())) {
            throw new RestProtocolException("data-missing", "No preload entry found", HttpStatus.NOT_FOUND.value());
        }

        if ((preloadData == null) || (preloadData.getPreloadNetworkTopologyInformation() == null)) {
            throw new RestProtocolException("bad-attribute", "Invalid preloadData received",
                    HttpStatus.BAD_REQUEST.value());
        }

        ConfigPreloadData preloadDataItem = preloadDataItems.get(0);

        if (preloadDataItem.getPreloadData() != null) {
            dataExists = true;
        }

        try {
            preloadDataItem.setPreloadData(objectMapper.writeValueAsString(preloadData));
            configPreloadDataRepository.save(preloadDataItem);
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (dataExists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesDelete() {
        configServicesRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<GenericResourceApiServiceModelInfrastructure> configGENERICRESOURCEAPIservicesGet()
            throws RestApplicationException {
        GenericResourceApiServiceModelInfrastructure modelInfrastructure = new GenericResourceApiServiceModelInfrastructure();

        if (configServicesRepository.count() == 0) {
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        for (ConfigServices service : configServicesRepository.findAll()) {
            GenericResourceApiServicemodelinfrastructureService serviceItem = new GenericResourceApiServicemodelinfrastructureService();
            serviceItem.setServiceInstanceId(service.getSvcInstanceId());
            if (service.getSvcData() != null) {
                try {
                    serviceItem.setServiceData(objectMapper.readValue(service.getSvcData(),
                            GenericResourceApiServicedataServiceData.class));
                } catch (JsonProcessingException e) {
                    log.error("Could not deserialize service data for {}", service.getSvcInstanceId(), e);
                    throw new RestApplicationException("data-conversion",
                            "Request could not be completed due to internal error", e,
                            HttpStatus.INTERNAL_SERVER_ERROR.value());

                }
            }
            serviceItem.setServiceStatus(service.getServiceStatus());
            modelInfrastructure.addServiceItem(serviceItem);
        }

        return new ResponseEntity<>(modelInfrastructure, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesPost(
            @Valid GenericResourceApiServiceModelInfrastructure modelInfrastructure)
            throws RestApplicationException, RestProtocolException {
        List<ConfigServices> newServices = new LinkedList<>();

        for (GenericResourceApiServicemodelinfrastructureService serviceItem : modelInfrastructure.getService()) {
            String svcInstanceId = serviceItem.getServiceInstanceId();
            List<ConfigServices> existingService = configServicesRepository.findBySvcInstanceId(svcInstanceId);
            if ((existingService != null) && !existingService.isEmpty()) {
                log.error("Service data already exists for {}", svcInstanceId);
                throw new RestProtocolException("data-exists",
                        "Data already exists for service-instance-id " + svcInstanceId, HttpStatus.CONFLICT.value());
            }
            ConfigServices service = new ConfigServices();
            service.setSvcInstanceId(svcInstanceId);
            try {
                service.setSvcData(objectMapper.writeValueAsString(serviceItem.getServiceData()));
            } catch (JsonProcessingException e) {
                log.error("Could not serialize service data for {}", service.getSvcInstanceId(), e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());

            }
            service.setServiceStatus(serviceItem.getServiceStatus());
            newServices.add(service);
        }

        for (ConfigServices service : newServices) {
            configServicesRepository.save(service);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesPut(
            @Valid GenericResourceApiServiceModelInfrastructure modelInfrastructure) throws RestApplicationException {

        List<ConfigServices> newServices = new LinkedList<>();
        boolean dataExists = false;

        for (GenericResourceApiServicemodelinfrastructureService serviceItem : modelInfrastructure.getService()) {
            String svcInstanceId = serviceItem.getServiceInstanceId();
            List<ConfigServices> existingService = configServicesRepository.findBySvcInstanceId(svcInstanceId);
            if ((existingService != null) && !existingService.isEmpty()) {
                dataExists = true;
            }
            ConfigServices service = new ConfigServices();
            service.setSvcInstanceId(svcInstanceId);
            try {
                service.setSvcData(objectMapper.writeValueAsString(serviceItem.getServiceData()));
            } catch (JsonProcessingException e) {
                log.error("Could not serialize service data for {}", service.getSvcInstanceId(), e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());

            }
            service.setServiceStatus(serviceItem.getServiceStatus());
            newServices.add(service);
        }

        for (ConfigServices service : newServices) {
            configServicesRepository.save(service);
        }

        if (dataExists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServicePost(
            @Valid GenericResourceApiServicemodelinfrastructureService servicesData) throws RestApplicationException {
        String svcInstanceId = servicesData.getServiceInstanceId();
        try {
            String svcData = objectMapper.writeValueAsString(servicesData.getServiceData());
            ConfigServices configService = new ConfigServices(svcInstanceId, svcData, servicesData.getServiceStatus());
            configServicesRepository.deleteBySvcInstanceId(svcInstanceId);
            configServicesRepository.save(configService);
        } catch (JsonProcessingException e) {
            log.error("Cannot convert service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdDelete(
            String serviceInstanceId) {
        configServicesRepository.deleteBySvcInstanceId(serviceInstanceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<GenericResourceApiServicemodelinfrastructureService> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdGet(
            String serviceInstanceId) throws RestApplicationException {
        GenericResourceApiServicemodelinfrastructureService retval = null;

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if (services.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            ConfigServices service = services.get(0);
            retval = new GenericResourceApiServicemodelinfrastructureService();
            retval.setServiceInstanceId(serviceInstanceId);
            retval.setServiceStatus(service.getServiceStatus());
            try {
                retval.setServiceData(
                        objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class));
            } catch (JsonProcessingException e) {
                log.error("Could not deserialize service data for service instance id {}", serviceInstanceId, e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());

            }
        }

        return new ResponseEntity<>(retval, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdPost(String svcInstanceId,
            @Valid GenericResourceApiServicemodelinfrastructureService newService)
            throws RestApplicationException, RestProtocolException {

        List<ConfigServices> existingService = configServicesRepository.findBySvcInstanceId(svcInstanceId);
        if ((existingService != null) && !existingService.isEmpty()) {
            log.error("Service data already exists for {}", svcInstanceId);
            throw new RestProtocolException("data-exists",
                    "Data already exists for service-instance-id " + svcInstanceId, HttpStatus.CONFLICT.value());
        }
        ConfigServices service = new ConfigServices();
        service.setSvcInstanceId(svcInstanceId);
        try {
            service.setSvcData(objectMapper.writeValueAsString(newService.getServiceData()));
        } catch (JsonProcessingException e) {
            log.error("Could not serialize service data for {}", service.getSvcInstanceId(), e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
        service.setServiceStatus(newService.getServiceStatus());
        configServicesRepository.save(service);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdPut(String serviceInstanceId,
            @Valid GenericResourceApiServicemodelinfrastructureService newService) throws RestApplicationException {

        boolean dataExists = false;

        String svcInstanceId = newService.getServiceInstanceId();

        ConfigServices service = null;
        List<ConfigServices> existingService = configServicesRepository.findBySvcInstanceId(svcInstanceId);
        if ((existingService != null) && !existingService.isEmpty()) {
            dataExists = true;
            service = existingService.get(0);
        } else {
            service = new ConfigServices();
            service.setSvcInstanceId(svcInstanceId);
        }

        try {
            service.setSvcData(objectMapper.writeValueAsString(newService.getServiceData()));
        } catch (JsonProcessingException e) {
            log.error("Could not serialize service data for {}", service.getSvcInstanceId(), e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
        service.setServiceStatus(newService.getServiceStatus());
        configServicesRepository.save(service);

        if (dataExists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataDelete(
            String serviceInstanceId) throws RestProtocolException {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigServices service = services.get(0);
        if (service.getSvcData() == null) {
            throw new RestProtocolException("data-missing", "No service-data found", HttpStatus.NOT_FOUND.value());
        }
        service.setSvcData(null);
        configServicesRepository.save(service);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<GenericResourceApiServicedataServiceData> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataGet(
            String serviceInstanceId) throws RestApplicationException, RestProtocolException {
        GenericResourceApiServicedataServiceData serviceData = null;

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        try {
            serviceData = objectMapper.readValue(services.get(0).getSvcData(),
                    GenericResourceApiServicedataServiceData.class);
            return new ResponseEntity<>(serviceData, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataPost(
            String serviceInstanceId, @Valid GenericResourceApiServicedataServiceData serviceData)
            throws RestApplicationException, RestProtocolException {
        ConfigServices service;
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        if ((serviceData == null) || (serviceData.getServiceInformation() == null)) {
            throw new RestProtocolException("bad-attribute", "Invalid service-data received",
                    HttpStatus.BAD_REQUEST.value());

        }
        service = services.get(0);

        if ((service.getSvcData() != null) && (service.getSvcData().length() > 0)) {
            log.error("service-data already exists for svcInstanceId {}", serviceInstanceId);
            throw new RestProtocolException("data-exists", "Data already exists for " + serviceInstanceId,
                    HttpStatus.CONFLICT.value());
        }

        try {
            service.setSvcData(objectMapper.writeValueAsString(serviceData));
            configServicesRepository.save(service);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataPut(
            String serviceInstanceId, @Valid GenericResourceApiServicedataServiceData serviceData)
            throws RestApplicationException, RestProtocolException {
        ConfigServices service;
        boolean dataExists = false;

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        if ((serviceData == null) || (serviceData.getServiceInformation() == null)) {
            throw new RestProtocolException("bad-attribute", "Invalid service-data received",
                    HttpStatus.BAD_REQUEST.value());

        }
        service = services.get(0);

        if ((service.getSvcData() != null) && (service.getSvcData().length() > 0)) {
            dataExists = true;
        }

        try {
            service.setSvcData(objectMapper.writeValueAsString(serviceData));
            configServicesRepository.save(service);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (dataExists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceStatusDelete(
            String serviceInstanceId) throws RestProtocolException {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigServices service = services.get(0);
        if (service.getServiceStatus() == null) {
            throw new RestProtocolException("data-missing", "No service-status found", HttpStatus.NOT_FOUND.value());
        }
        service.setServiceStatus(null);
        configServicesRepository.save(service);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @Override
    public ResponseEntity<GenericResourceApiServicestatusServiceStatus> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceStatusGet(
            String serviceInstanceId) throws RestApplicationException, RestProtocolException {
        GenericResourceApiServicestatusServiceStatus serviceStatus = null;

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        serviceStatus = services.get(0).getServiceStatus();
        return new ResponseEntity<>(serviceStatus, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceStatusPost(
            String serviceInstanceId, @Valid GenericResourceApiServicestatusServiceStatus serviceStatus)
            throws RestProtocolException {
        ConfigServices service;
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        if ((serviceStatus == null) || (serviceStatus.getAction() == null)) {
            throw new RestProtocolException("bad-attribute", "Invalid service-status received",
                    HttpStatus.BAD_REQUEST.value());

        }
        service = services.get(0);

        if (service.getServiceStatus() != null) {
            log.error("service-status already exists for svcInstanceId {}", serviceInstanceId);
            throw new RestProtocolException("data-exists", "Data already exists for " + serviceInstanceId,
                    HttpStatus.CONFLICT.value());
        }

        service.setServiceStatus(serviceStatus);
        configServicesRepository.save(service);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceStatusPut(
            String serviceInstanceId, @Valid GenericResourceApiServicestatusServiceStatus serviceStatus)
            throws RestProtocolException {
        ConfigServices service;
        boolean dataExists = false;

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        if ((serviceStatus == null) || (serviceStatus.getAction() == null)) {
            throw new RestProtocolException("bad-attribute", "Invalid service-status received",
                    HttpStatus.BAD_REQUEST.value());

        }
        service = services.get(0);

        if (service.getServiceStatus() != null) {
            dataExists = true;
        }

        service.setServiceStatus(serviceStatus);
        configServicesRepository.save(service);

        if (dataExists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    /**
     * Deletes VNF data from the Config table specified Service Instance.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/
     * 
     * @param serviceInstanceId the Service Instance ID to perform the delete on
     * @param vnfId             the VNF ID of the VNF to delete
     * @return HttpStatus.NO_CONTENT (204) on successful delete
     *         <p>
     *         HttpStatus.BAD_REQUEST (400) if unmarshalling Service Data from the
     *         database fails, there is no VNF data for {@code vnfId}, or writing
     *         Service Data back to the database fails.
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId} does not
     *         exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdDelete(
            String serviceInstanceId, String vnfId) throws RestException {
        log.info("DELETE | VNF Data for ({})", vnfId);

        /*
         * The logic may need to be moved inside of this check or this check may need to
         * be removed.
         */
        if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            log.info("Something with header.");
        } else {
            log.warn(
                    "ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
        }

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        ConfigServices data;
        if ((services == null) || (services.isEmpty())) {
            log.info("Could not find data for ({}).", serviceInstanceId);
            // Or throw the data not found error?
            throw new RestProtocolException("data-missing", "Service Instance ID not found.",
                    HttpStatus.NOT_FOUND.value());
        } else {
            data = services.get(0);
        }

        GenericResourceApiServicedataServiceData svcData;
        try {
            svcData = objectMapper.readValue(data.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            // Or throw the data not found error?
            log.error("Could not map service data for ({})", serviceInstanceId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (svcData == null) {
            // Or throw the data not found error?
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataVnfs vnfs = svcData.getVnfs();
        if (vnfs == null) {
            // Or throw the data not found error?
            log.info("VNF List not found for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "VNFs not found.", HttpStatus.NOT_FOUND.value());
        }

        Stream<GenericResourceApiServicedataServicedataVnfsVnf> vnfStream = svcData.getVnfs().getVnf().stream();
        if (vnfStream.noneMatch(targetVnf -> targetVnf.getVnfId().equals(vnfId))) {
            // Data was not found
            log.error("Did not find VNF ({}) in data.", vnfId);
            throw new RestProtocolException("data-missing", "VNF ID not found.", HttpStatus.NOT_FOUND.value());
        }
        // Recreate the stream per Sonar?
        vnfStream = svcData.getVnfs().getVnf().stream();
        svcData.getVnfs().setVnf(
                vnfStream.filter(targetVnf -> !targetVnf.getVnfId().equals(vnfId)).collect(Collectors.toList()));

        // Map and save the new data
        try {
            data.setSvcData(objectMapper.writeValueAsString(svcData));
            configServicesRepository.save(data);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (JsonProcessingException e) {
            log.error("Error mapping object to JSON", e);
            // Should probably be a 500 INTERNAL_SERVICE_ERROR
            throw new RestProtocolException("internal-service-error", "Failed to save data.",
                    HttpStatus.BAD_REQUEST.value());
        }
    }

    /**
     * Extracts VNF data from the Config table specified Service Instance.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/
     * 
     * @param serviceInstanceId the Service Instance ID to lookup data for
     * @param vnfId             the VNF ID of the VNF to return
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    @Override
    public ResponseEntity<GenericResourceApiServicedataServicedataVnfsVnf> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdGet(
            String serviceInstanceId, String vnfId) throws RestException {
        log.info("GET | VNF Data for ({})", vnfId);
        if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
            }
        } else {
            log.warn(
                    "ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
        }
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        Optional<GenericResourceApiServicedataServicedataVnfsVnf> vnf = getVnfObject(services.get(0), vnfId);
        if (vnf.isPresent()) {
            return new ResponseEntity<>(vnf.get(), HttpStatus.OK);
        } else {
            log.info("No information found for {}", vnfId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }
    }

    /**
     * Extracts a vf-module object from the database,
     * @param configServices A Config Services option created from a Service
     *                       Instance ID
     * @param vnfId the target VNF ID
     * @param vfModuleId the target vf-module ID
     * @return An empty Optional if the Service Data does not exist, an empty
     *         Optional if the VNF is not found, or an optional containing the
     *         found VNF.
     */
    private Optional<GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule> getVfModuleObject (
            ConfigServices configServices, String vnfId, String vfModuleId) {
        // Map the Marshall the JSON String into a Java Object
        log.info("Getting vf-module Data for ({})", vfModuleId);

        Optional<GenericResourceApiServicedataServicedataVnfsVnf> vnf = getVnfObject(configServices, vnfId);
        GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfData = vnf.get().getVnfData();

        return vnfData.getVfModules().getVfModule()
                .stream()
                .filter(vf -> vf.getVfModuleId().equals(vfModuleId))
                .findFirst();
    }

    /**
     * Creates or updates VNF data in the Config table for a specified Service
     * Instance. If it is a new Service Instance or a new VNF, creates all necessary
     * parent data containers, then performs the updates.
     * <p>
     * Maps to /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/
     * @param serviceInstanceId the Service Instance ID to perform the delete on
     * @param vnfId the VNF ID of the VNF to delete
     * @param genericResourceApiServicedataServicedataVnfsVnfBodyParam the playload
     * @return HttpStatus.CREATED (201) on successful create
     * <p>
     * HttpStatus.NO_CONTENT (204) on successful update
     * <p>
     * HttpStatus.BAD_REQUEST (400) if {@code vnfId} does not match what is specified in the
     * {@code genericResourceApiServicedataServicedataVnfsVnfBodyParam} , or if updating the database fails.
     * @throws RestException
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdPut(
            String serviceInstanceId, String vnfId,
            GenericResourceApiServicedataServicedataVnfsVnf genericResourceApiServicedataServicedataVnfsVnfBodyParam)
            throws RestException {
        log.info("PUT | VNF Data for ({})", vnfId);
        if (!vnfId.equals(genericResourceApiServicedataServicedataVnfsVnfBodyParam.getVnfId())) {
            throw new RestProtocolException("bad-attribute", "vnf-id mismatch", HttpStatus.BAD_REQUEST.value());
        }
        if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            log.info("Something with header");
        } else {
            log.warn(
                    "ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
        }

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        ConfigServices data;
        if ((services == null) || (services.isEmpty())) {
            log.info("Could not find data for ({}). Creating new Service Object.", serviceInstanceId);
            data = new ConfigServices();
            data.setSvcInstanceId(serviceInstanceId);
        } else {
            data = services.get(0);
        }

        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(data.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not map service data for ({})", serviceInstanceId);
        }
        if (svcData == null) {
            log.info("Could not find Service Data for ({}). Creating new Service Data Container", serviceInstanceId);
            svcData = new GenericResourceApiServicedataServiceData();
        }
        if (svcData.getVnfs() == null) {
            log.info("VNF List not found for ({}). Creating new VNF List Container.", serviceInstanceId);
            svcData.setVnfs(new GenericResourceApiServicedataServicedataVnfs());
            svcData.getVnfs().setVnf(new ArrayList<>());
        }

        GenericResourceApiServicedataServicedataVnfs vnflist = new GenericResourceApiServicedataServicedataVnfs();
        HttpStatus responseStatus = HttpStatus.NO_CONTENT;
        if (svcData.getVnfs().getVnf().isEmpty()) {
            log.info("Creating VNF data for ({})", vnfId);
            vnflist.addVnfItem(genericResourceApiServicedataServicedataVnfsVnfBodyParam);
            responseStatus = HttpStatus.CREATED;
        } else {
            log.info("Updating VNF data for ({})", vnfId);
            // Filter out all of the other vnf objects into a new VNF List
            // Replace if a delete method exists
            svcData.getVnfs().getVnf().stream().filter(targetVnf -> !targetVnf.getVnfId().equals(vnfId))
                    .forEach(vnflist::addVnfItem);
            vnflist.addVnfItem(genericResourceApiServicedataServicedataVnfsVnfBodyParam);
        }
        svcData.setVnfs(vnflist);
        // Map and save the new data
        try {
            data.setSvcData(objectMapper.writeValueAsString(svcData));
            configServicesRepository.save(data);
            return new ResponseEntity<>(responseStatus);
        } catch (JsonProcessingException e) {
            log.error("Error mapping object to JSON", e);
            // Should probably be a 500 INTERNAL_SERVICE_ERROR
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Extracts VNF Topology data from the Config table specified Service Instance
     * and VNF ID.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/vnf-data/vnf-topology/
     * 
     * @param serviceInstanceId the Service Instance ID to lookup data for
     * @param vnfId             the VNF ID of the VNF to extract topology data from.
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    @Override
    public ResponseEntity<GenericResourceApiVnftopologyVnfTopology> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVnfTopologyGet(
            String serviceInstanceId, String vnfId) throws RestException {
        log.info("GET | VNF Topology for ({})", vnfId);
        if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {

            }
        } else {
            log.warn(
                    "ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
        }
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        Optional<GenericResourceApiServicedataServicedataVnfsVnf> vnf = getVnfObject(services.get(0), vnfId);
        // Drill down to find the data
        if (vnf.isPresent() && vnf.get().getVnfData() != null && vnf.get().getVnfData().getVnfTopology() != null) {
            return new ResponseEntity<>(vnf.get().getVnfData().getVnfTopology(), HttpStatus.OK);
        } else {
            log.info("No information found for {}", vnfId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }
    }

    /**
     * Creates or updates VNF Level Operation Status data in the Config table for a
     * specified Service Instance. If it is a new Service Instance or a new VNF,
     * creates all necessary parent data containers, then performs the updates.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/vnf-data/vnf-level-oper-status/
     * 
     * @param serviceInstanceId                         the Service Instance ID to
     *                                                  perform the delete on
     * @param vnfId                                     the VNF ID of the VNF to
     *                                                  delete
     * @param genericResourceApiOperStatusDataBodyParam the payload
     * @return HttpStatus.CREATED (201) on successful create.
     *         <p>
     *         HttpStatus.NO_CONTENT (204) on successful update.
     *         <p>
     *         HttpStatus.BAD_REQUEST (400) if updating the database fails.
     * @throws RestException
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVnfLevelOperStatusPut(
            String serviceInstanceId, String vnfId,
            GenericResourceApiOperStatusData genericResourceApiOperStatusDataBodyParam) throws RestException {
        log.info("PUT | VNF Level Oper Status ({})", vnfId);
        if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
        } else {
            log.warn(
                    "ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
        }

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        ConfigServices data;
        if ((services == null) || (services.isEmpty())) {
            log.info("Could not find data for ({}). Creating new Service Object.", serviceInstanceId);
            data = new ConfigServices();
            data.setSvcInstanceId(serviceInstanceId);
        } else {
            data = services.get(0);
        }

        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(data.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not map service data for ({})", serviceInstanceId);
        }
        if (svcData == null) {
            log.info("Could not find Service Data for ({}). Creating new Service Data Container", serviceInstanceId);
            svcData = new GenericResourceApiServicedataServiceData();
        }
        if (svcData.getVnfs() == null) {
            log.info("VNF List not found for ({}). Creating new VNF List Container.", serviceInstanceId);
            svcData.setVnfs(new GenericResourceApiServicedataServicedataVnfs());
            svcData.getVnfs().setVnf(new ArrayList<>());
        }

        GenericResourceApiServicedataServicedataVnfs vnflist = new GenericResourceApiServicedataServicedataVnfs();
        HttpStatus responseStatus = HttpStatus.NO_CONTENT;
        if (svcData.getVnfs().getVnf().isEmpty()) {
            log.info("Creating VNF data for ({})", vnfId);
            GenericResourceApiServicedataServicedataVnfsVnf vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
            vnf.setVnfId(vnfId);
            vnf.setVnfData(new GenericResourceApiServicedataServicedataVnfsVnfVnfData());
            vnf.getVnfData().setVnfLevelOperStatus(genericResourceApiOperStatusDataBodyParam);
            vnflist.addVnfItem(vnf);
            responseStatus = HttpStatus.CREATED;
        } else {
            log.info("Updating VNF data for ({})", vnfId);
            // Filter out all of the other vnf objects into a new VNF List
            // Replace if a delete method exists
            svcData.getVnfs().getVnf().stream().filter(targetVnf -> !targetVnf.getVnfId().equals(vnfId))
                    .forEach(vnflist::addVnfItem);
            GenericResourceApiServicedataServicedataVnfsVnf vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
            // If the vnf exists, set it up with new data
            Optional<GenericResourceApiServicedataServicedataVnfsVnf> vnfOptional = getVnfObject(data, vnfId);
            if (vnfOptional.isPresent()) {
                vnf = vnfOptional.get();
            }
            if (vnf.getVnfData() == null) {
                vnf.setVnfData(new GenericResourceApiServicedataServicedataVnfsVnfVnfData());
                responseStatus = HttpStatus.CREATED;
            }

            vnf.getVnfData().setVnfLevelOperStatus(genericResourceApiOperStatusDataBodyParam);
            vnflist.addVnfItem(vnf);
        }

        svcData.setVnfs(vnflist);
        // Map and save the new data
        try {
            data.setSvcData(objectMapper.writeValueAsString(svcData));
            configServicesRepository.save(data);
            return new ResponseEntity<>(responseStatus);
        } catch (JsonProcessingException e) {
            log.error("Error mapping object to JSON", e);
            // Should probably be a 500 INTERNAL_SERVICE_ERROR
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Creates or updates VNF Onap Model Information data in the Config table for a
     * specified Service Instance. If it is a new Service Instance or a new VNF,
     * creates all necessary parent data containers, then performs the updates.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/vnf-data/vnf-topology/onap-model-information/
     * 
     * @param serviceInstanceId                                                   the
     *                                                                            Service
     *                                                                            Instance
     *                                                                            ID
     *                                                                            to
     *                                                                            perform
     *                                                                            the
     *                                                                            delete
     *                                                                            on
     * @param vnfId                                                               the
     *                                                                            VNF
     *                                                                            ID
     *                                                                            of
     *                                                                            the
     *                                                                            VNF
     *                                                                            to
     *                                                                            delete
     * @param genericResourceApiOnapmodelinformationOnapModelInformationBodyParam the
     *                                                                            payload
     * @return HttpStatus.CREATED (201) on successful create.
     *         <p>
     *         HttpStatus.NO_CONTENT (204) on successful update.
     *         <p>
     *         HttpStatus.BAD_REQUEST (400) if updating the database fails.
     * @throws RestException
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVnfTopologyOnapModelInformationPut(
            String serviceInstanceId, String vnfId,
            GenericResourceApiOnapmodelinformationOnapModelInformation genericResourceApiOnapmodelinformationOnapModelInformationBodyParam)
            throws RestException {
        log.info("PUT | VNF Topology Onap Model Information ({})", vnfId);
        if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
        } else {
            log.warn(
                    "ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
        }

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        ConfigServices data;
        if ((services == null) || (services.isEmpty())) {
            log.info("Could not find data for ({}). Creating new Service Object.", serviceInstanceId);
            data = new ConfigServices();
            data.setSvcInstanceId(serviceInstanceId);
        } else {
            data = services.get(0);
        }

        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(data.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not map service data for ({})", serviceInstanceId);
        }
        if (svcData == null) {
            log.info("Could not find Service Data for ({}). Creating new Service Data Container", serviceInstanceId);
            svcData = new GenericResourceApiServicedataServiceData();
        }
        if (svcData.getVnfs() == null) {
            log.info("VNF List not found for ({}). Creating new VNF List Container.", serviceInstanceId);
            svcData.setVnfs(new GenericResourceApiServicedataServicedataVnfs());
            svcData.getVnfs().setVnf(new ArrayList<>());
        }

        GenericResourceApiServicedataServicedataVnfs vnflist = new GenericResourceApiServicedataServicedataVnfs();
        HttpStatus responseStatus = HttpStatus.NO_CONTENT;
        if (svcData.getVnfs().getVnf().isEmpty()) {
            log.info("Creating VNF data for ({})", vnfId);
            GenericResourceApiServicedataServicedataVnfsVnf vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
            vnf.setVnfId(vnfId);
            vnf.setVnfData(new GenericResourceApiServicedataServicedataVnfsVnfVnfData());
            vnf.getVnfData().setVnfTopology(new GenericResourceApiVnftopologyVnfTopology());
            vnf.getVnfData().getVnfTopology()
                    .setOnapModelInformation(genericResourceApiOnapmodelinformationOnapModelInformationBodyParam);
            vnflist.addVnfItem(vnf);
            responseStatus = HttpStatus.CREATED;
        } else {
            log.info("Updating VNF data for ({})", vnfId);
            // Filter out all of the other vnf objects into a new VNF List
            // Replace if a delete method exists
            svcData.getVnfs().getVnf().stream().filter(targetVnf -> !targetVnf.getVnfId().equals(vnfId))
                    .forEach(vnflist::addVnfItem);
            GenericResourceApiServicedataServicedataVnfsVnf vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
            // If the vnf exists, set it up with new data
            Optional<GenericResourceApiServicedataServicedataVnfsVnf> vnfOptional = getVnfObject(data, vnfId);
            if (vnfOptional.isPresent()) {
                vnf = vnfOptional.get();
            }
            if (vnf.getVnfData() == null) {
                vnf.setVnfData(new GenericResourceApiServicedataServicedataVnfsVnfVnfData());
            }
            if (vnf.getVnfData().getVnfTopology() == null) {
                vnf.getVnfData().setVnfTopology(new GenericResourceApiVnftopologyVnfTopology());
                responseStatus = HttpStatus.CREATED;
            }

            vnf.getVnfData().getVnfTopology()
                    .setOnapModelInformation(genericResourceApiOnapmodelinformationOnapModelInformationBodyParam);
            vnflist.addVnfItem(vnf);
        }

        svcData.setVnfs(vnflist);
        // Map and save the new data
        try {
            data.setSvcData(objectMapper.writeValueAsString(svcData));
            configServicesRepository.save(data);
            return new ResponseEntity<>(responseStatus);
        } catch (JsonProcessingException e) {
            log.error("Error mapping object to JSON", e);
            // Should probably be a 500 INTERNAL_SERVICE_ERROR
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Creates or updates VNF Network data in the Config table for a specified
     * Service Instance. If it is a new Service Instance or a new VNF, creates all
     * necessary parent data containers, then performs the updates.
     * <p>
     * Maps to /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/vnf-data/vnf-topology/vnf-resource-assignments/vnf-networks/
     * @param serviceInstanceId the Service Instance ID to perform the delete on
     * @param vnfId the VNF ID of the VNF to delete
     * @param genericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworksBodyParam the * payload
     * @return HttpStatus.CREATED (201) on successful create.
     * <p>
     * HttpStatus.NO_CONTENT (204) on successful update.
     * <p>
     * HttpStatus.BAD_REQUEST (400) if updating the database fails.
     * @throws RestException
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVnfTopologyVnfResourceAssignmentsVnfNetworksPut(
            String serviceInstanceId, String vnfId,
            GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks genericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworksBodyParam)
            throws RestException {
        log.info("PUT | VNF Topology VNF Resource Assignments VNF Networks ({})", vnfId);
        if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
        } else {
            log.warn(
                    "ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
        }

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        ConfigServices data;
        if ((services == null) || (services.isEmpty())) {
            log.info("Could not find data for ({}). Creating new Service Object.", serviceInstanceId);
            data = new ConfigServices();
            data.setSvcInstanceId(serviceInstanceId);
        } else {
            data = services.get(0);
        }

        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(data.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not map service data for ({})", serviceInstanceId);
        }
        if (svcData == null) {
            log.info("Could not find Service Data for ({}). Creating new Service Data Container", serviceInstanceId);
            svcData = new GenericResourceApiServicedataServiceData();
        }
        if (svcData.getVnfs() == null) {
            log.info("VNF List not found for ({}). Creating new VNF List Container.", serviceInstanceId);
            svcData.setVnfs(new GenericResourceApiServicedataServicedataVnfs());
            svcData.getVnfs().setVnf(new ArrayList<>());
        }

        GenericResourceApiServicedataServicedataVnfs vnflist = new GenericResourceApiServicedataServicedataVnfs();
        HttpStatus responseStatus = HttpStatus.NO_CONTENT;
        if (svcData.getVnfs().getVnf().isEmpty()) {
            log.info("Creating VNF data for ({})", vnfId);
            GenericResourceApiServicedataServicedataVnfsVnf vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
            vnf.setVnfId(vnfId);
            vnf.setVnfData(new GenericResourceApiServicedataServicedataVnfsVnfVnfData());
            vnf.getVnfData().setVnfTopology(new GenericResourceApiVnftopologyVnfTopology());
            vnf.getVnfData().getVnfTopology()
                    .setVnfResourceAssignments(new GenericResourceApiVnfresourceassignmentsVnfResourceAssignments());
            vnf.getVnfData().getVnfTopology().getVnfResourceAssignments()
                    .setVnfNetworks(genericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworksBodyParam);
            vnflist.addVnfItem(vnf);
            responseStatus = HttpStatus.CREATED;
        } else {
            log.info("Updating VNF data for ({})", vnfId);
            // Filter out all of the other vnf objects into a new VNF List
            // Replace if a delete method exists
            svcData.getVnfs().getVnf().stream().filter(targetVnf -> !targetVnf.getVnfId().equals(vnfId))
                    .forEach(vnflist::addVnfItem);
            GenericResourceApiServicedataServicedataVnfsVnf vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
            // If the vnf exists, set it up with new data
            Optional<GenericResourceApiServicedataServicedataVnfsVnf> vnfOptional = getVnfObject(data, vnfId);
            if (vnfOptional.isPresent()) {
                vnf = vnfOptional.get();
            }
            if (vnf.getVnfData() == null) {
                vnf.setVnfData(new GenericResourceApiServicedataServicedataVnfsVnfVnfData());
            }
            if (vnf.getVnfData().getVnfTopology() == null) {
                vnf.getVnfData().setVnfTopology(new GenericResourceApiVnftopologyVnfTopology());
            }
            if (vnf.getVnfData().getVnfTopology().getVnfResourceAssignments() == null) {
                vnf.getVnfData().getVnfTopology().setVnfResourceAssignments(
                        new GenericResourceApiVnfresourceassignmentsVnfResourceAssignments());
                responseStatus = HttpStatus.CREATED;
            }

            vnf.getVnfData().getVnfTopology().getVnfResourceAssignments()
                    .setVnfNetworks(genericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworksBodyParam);
            vnflist.addVnfItem(vnf);
        }

        svcData.setVnfs(vnflist);
        // Map and save the new data
        try {
            data.setSvcData(objectMapper.writeValueAsString(svcData));
            configServicesRepository.save(data);
            return new ResponseEntity<>(responseStatus);
        } catch (JsonProcessingException e) {
            log.error("Error mapping object to JSON", e);
            // Should probably be a 500 INTERNAL_SERVICE_ERROR
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Creates or updates VNF Network Role data in the Config table for a specified
     * Service Instance. If it is a new Service Instance or a new VNF, creates all
     * necessary parent data containers, then performs the updates.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/vnf-data/vnf-topology/vnf-resource-assignments/vnf-networks/vnf-network/{network-role}/
     * 
     * @param serviceInstanceId                         the Service Instance ID to
     *                                                  perform the delete on
     * @param vnfId                                     the VNF ID of the VNF to
     *                                                  delete
     * @param genericResourceApiVnfNetworkDataBodyParam the payload
     * @return HttpStatus.CREATED (201) on successful create.
     *         <p>
     *         HttpStatus.NO_CONTENT (204) on successful update.
     *         <p>
     *         HttpStatus.BAD_REQUEST (400) if updating the database fails.
     * @throws RestException
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVnfTopologyVnfResourceAssignmentsVnfNetworksVnfNetworkNetworkRolePut(
            String serviceInstanceId, String vnfId, String networkRole,
            GenericResourceApiVnfNetworkData genericResourceApiVnfNetworkDataBodyParam) throws RestException {
        log.info("PUT | VNF Network Network Role ({})", vnfId);
        if (!networkRole.equals(genericResourceApiVnfNetworkDataBodyParam.getNetworkRole())) {
            throw new RestProtocolException("bad-attribute", "network-role mismatch", HttpStatus.BAD_REQUEST.value());
        }
        if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
        } else {
            log.warn(
                    "ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
        }

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        ConfigServices data;
        if ((services == null) || (services.isEmpty())) {
            log.info("Could not find data for ({}). Creating new Service Object.", serviceInstanceId);
            data = new ConfigServices();
            data.setSvcInstanceId(serviceInstanceId);
        } else {
            data = services.get(0);
        }

        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(data.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not map service data for ({})", serviceInstanceId);
        }
        if (svcData == null) {
            log.info("Could not find Service Data for ({}). Creating new Service Data Container", serviceInstanceId);
            svcData = new GenericResourceApiServicedataServiceData();
        }
        if (svcData.getVnfs() == null) {
            log.info("VNF List not found for ({}). Creating new VNF List Container.", serviceInstanceId);
            svcData.setVnfs(new GenericResourceApiServicedataServicedataVnfs());
            svcData.getVnfs().setVnf(new ArrayList<>());
        }

        GenericResourceApiServicedataServicedataVnfs vnflist = new GenericResourceApiServicedataServicedataVnfs();
        HttpStatus responseStatus = HttpStatus.NO_CONTENT;
        if (svcData.getVnfs().getVnf().isEmpty()) {
            log.info("Creating VNF data for ({})", vnfId);
            GenericResourceApiServicedataServicedataVnfsVnf vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
            vnf.setVnfId(vnfId);
            vnf.setVnfData(new GenericResourceApiServicedataServicedataVnfsVnfVnfData());
            vnf.getVnfData().setVnfTopology(new GenericResourceApiVnftopologyVnfTopology());
            vnf.getVnfData().getVnfTopology()
                    .setVnfResourceAssignments(new GenericResourceApiVnfresourceassignmentsVnfResourceAssignments());
            vnf.getVnfData().getVnfTopology().getVnfResourceAssignments()
                    .setVnfNetworks(new GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks());
            vnf.getVnfData().getVnfTopology().getVnfResourceAssignments().getVnfNetworks()
                    .setVnfNetwork(new ArrayList<>());
            vnf.getVnfData().getVnfTopology().getVnfResourceAssignments().getVnfNetworks()
                    .addVnfNetworkItem(genericResourceApiVnfNetworkDataBodyParam);
            vnflist.addVnfItem(vnf);
            responseStatus = HttpStatus.CREATED;
        } else {
            log.info("Updating VNF data for ({})", vnfId);
            // Filter out all of the other vnf objects into a new VNF List
            // Replace if a delete method exists
            svcData.getVnfs().getVnf().stream().filter(targetVnf -> !targetVnf.getVnfId().equals(vnfId))
                    .forEach(vnflist::addVnfItem);
            GenericResourceApiServicedataServicedataVnfsVnf vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
            // If the vnf exists, set it up with new data
            Optional<GenericResourceApiServicedataServicedataVnfsVnf> vnfOptional = getVnfObject(data, vnfId);
            if (vnfOptional.isPresent()) {
                vnf = vnfOptional.get();
            }
            if (vnf.getVnfData() == null) {
                vnf.setVnfData(new GenericResourceApiServicedataServicedataVnfsVnfVnfData());
            }
            if (vnf.getVnfData().getVnfTopology() == null) {
                vnf.getVnfData().setVnfTopology(new GenericResourceApiVnftopologyVnfTopology());
            }
            if (vnf.getVnfData().getVnfTopology().getVnfResourceAssignments() == null) {
                vnf.getVnfData().getVnfTopology().setVnfResourceAssignments(
                        new GenericResourceApiVnfresourceassignmentsVnfResourceAssignments());
            }
            if (vnf.getVnfData().getVnfTopology().getVnfResourceAssignments().getVnfNetworks() == null) {
                log.info("Creating new VnfNetworks");
                vnf.getVnfData().getVnfTopology().getVnfResourceAssignments().setVnfNetworks(
                        new GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks());
            }

            GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks networkList = new GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks();
            if (vnf.getVnfData().getVnfTopology().getVnfResourceAssignments().getVnfNetworks().getVnfNetwork()
                    .isEmpty()) {
                log.info("First entry into network info.");
                vnf.getVnfData().getVnfTopology().getVnfResourceAssignments().getVnfNetworks()
                        .addVnfNetworkItem(genericResourceApiVnfNetworkDataBodyParam);
                responseStatus = HttpStatus.CREATED;
            } else {
                log.info("Found networks. Filtering.");
                vnf.getVnfData().getVnfTopology().getVnfResourceAssignments().getVnfNetworks().getVnfNetwork().stream()
                        .filter(targetNetwork -> !targetNetwork.getNetworkRole().equals(networkRole))
                        .forEach(networkList::addVnfNetworkItem);
                networkList.addVnfNetworkItem(genericResourceApiVnfNetworkDataBodyParam);

                if (networkList.getVnfNetwork().size() != vnf.getVnfData().getVnfTopology().getVnfResourceAssignments()
                        .getVnfNetworks().getVnfNetwork().size()) {
                    log.info("Added a new Item");
                    responseStatus = HttpStatus.CREATED;
                }
                vnf.getVnfData().getVnfTopology().getVnfResourceAssignments().setVnfNetworks(networkList);
            }

            vnflist.addVnfItem(vnf);
        }

        svcData.setVnfs(vnflist);
        // Map and save the new data
        try {
            data.setSvcData(objectMapper.writeValueAsString(svcData));
            configServicesRepository.save(data);
            return new ResponseEntity<>(responseStatus);
        } catch (JsonProcessingException e) {
            log.error("Error mapping object to JSON", e);
            // Should probably be a 500 INTERNAL_SERVICE_ERROR
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Extracts a VNF object from the database,
     * 
     * @param configServices A Config Services option created from a Service
     *                       Instance ID
     * @param vnfId          the target VNF ID
     * @return An empty Optional if the Service Data does not exist, an empty
     *         Optional if the VNF is not found, or an optional containing the found
     *         VNF.
     */
    private Optional<GenericResourceApiServicedataServicedataVnfsVnf> getVnfObject(ConfigServices configServices,
            String vnfId) {
        // Map the Marshall the JSON String into a Java Object
        log.info("Getting VNF Data for ({})", vnfId);
        GenericResourceApiServicedataServiceData svcData;
        try {
            svcData = objectMapper.readValue(configServices.getSvcData(),
                    GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Error", e);
            return Optional.empty();
        }

        /*
         * Get a stream of the VNF Objects and return the target if it's found, assuming
         * that each VNF ID is unique within a Service Instance Object
         */
        return svcData.getVnfs().getVnf().stream().filter(targetVnf -> targetVnf.getVnfId().equals(vnfId)).findFirst();
    }

    @Override
    public ResponseEntity<GenericResourceApiServicetopologyServiceTopology> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataServiceTopologyGet(
            String serviceInstanceId) throws RestApplicationException, RestProtocolException {
        GenericResourceApiServicetopologyServiceTopology serviceTopology = null;
        GenericResourceApiServicedataServiceData serviceData = null;

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        try {
            if (services.get(0).getSvcData().isEmpty()) {
                throw new RestProtocolException("data-missing", "No service-data entry found",
                        HttpStatus.NOT_FOUND.value());
            } else {
                serviceData = objectMapper.readValue(services.get(0).getSvcData(),
                        GenericResourceApiServicedataServiceData.class);
                serviceTopology = serviceData.getServiceTopology();
            }
            if (serviceTopology == null) {
                throw new RestProtocolException("data-missing", "No service-topology entry found",
                        HttpStatus.NOT_FOUND.value());
            }
            return new ResponseEntity<>(serviceTopology, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    /**
     * Extracts VF MODULE data from CONFIG_GRA_SERVICES for a given, service-instance-id, vnf-id, and vf-module-id
     * <p>
     * Maps to /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/vnf-data/vf-modules/vf-module/{vf-module-id}/
     * @param serviceInstanceId the Service Instance ID to lookup data for
     * @param vnfId the VNF ID of the VNF to return
     * @param vfModuleId the vf-moudle ID of a specific VNF to return
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    @Override
    public ResponseEntity<GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule>
    configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVfModulesVfModuleVfModuleIdGet(
            String serviceInstanceId, String vnfId, String vfModuleId) throws RestException {

        log.info("GET | Vf Module Data for ({})", vfModuleId);

        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if(getAcceptHeader().get().contains("application/json")) {
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
        }
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        Optional<GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule> vfModule =
                getVfModuleObject(services.get(0), vnfId, vfModuleId);
        if(vfModule.isPresent()) {
            return new ResponseEntity<>(vfModule.get(), HttpStatus.OK);
        } else {
            log.info("No vf-module found for [{}]", vfModuleId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }
    }

    /**
     * PUT VF MODULE data into CONFIG_GRA_SERVICES of a given, service-instance-id, vnf-id
     * <p>
     * Maps to /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/vnf-data/vf-modules/vf-module/{vf-module-id}/
     * @param serviceInstanceId the Service Instance ID
     * @param vnfId the VNF ID as the parent of the specified vf-module-id and child of the specified service-instance
     * @param vfModuleId the vf-moudle ID as a child of the specified VNF
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVfModulesVfModuleVfModuleIdPut(
            String serviceInstanceId, String vnfId, String vfModuleId,
            GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule genericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModuleBodyParam)
            throws RestException {
        log.info("PUT | vf-module Data of ({}) for vnf ({}) in service({})", vfModuleId, vnfId, serviceInstanceId);

        if(! vfModuleId.equals(genericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModuleBodyParam.getVfModuleId())) {
            throw new RestProtocolException("bad-attribute", "vf-module-id mismatch", HttpStatus.BAD_REQUEST.value());
        }
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            log.info("Something with header");
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
        }

        HttpStatus responseStatus = HttpStatus.NO_CONTENT;
        ConfigServices service;
        GenericResourceApiServicedataServiceData svcData;
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if((services == null) || (services.isEmpty())) {
            log.error("service-instance-id ({}) not found in SDN.", serviceInstanceId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            service = services.get(0);
        }

        try {
            svcData = objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch(JsonProcessingException e) {
            log.error("Error", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        GenericResourceApiServicedataServicedataVnfs vnfs = svcData.getVnfs();
        Optional<GenericResourceApiServicedataServicedataVnfsVnf> vnf =
                vnfs.getVnf()
                        .stream()
                        .filter(targetVnf -> targetVnf.getVnfId().equals(vnfId))
                        .findFirst();

        if(! vnf.isPresent()) {
            log.error("vnf-id ({}) not found in SDN.", vnfId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfData = vnf.get().getVnfData();
        GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules existingVfModules = vnfData.getVfModules();
        GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules newVfModules = new GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules();

        if (existingVfModules == null || existingVfModules.getVfModule().isEmpty()) {
            log.info("No existing vf-module found. Creating the first vf-module for vnf [{}]", vnfId);
            newVfModules.addVfModuleItem(genericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModuleBodyParam);
            responseStatus = HttpStatus.CREATED;
        }
        else {
            ArrayList<String> vfModuleIds = new ArrayList<>();
            for (GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule vf : existingVfModules.getVfModule()) {
                vfModuleIds.add(vf.getVfModuleId());
            }
            log.info("[{}] vf-module(s) {} found in vnf [{}]", existingVfModules.getVfModule().size(), vfModuleIds, vnfId);
            if (!vfModuleIds.isEmpty() && vfModuleIds.contains(vfModuleId)) {
                log.info("Overwriting vf-module [{}] in vnf [{}]",  vfModuleId, vnfId);
            } else {
                log.info("Adding vf-module [{}] to vnf [{}]", vfModuleId, vnfId);
            }
            existingVfModules.getVfModule()
                    .stream()
                    .filter(vf -> ! vf.getVfModuleId().equals(vfModuleId))
                    .forEach(newVfModules::addVfModuleItem);
            newVfModules.addVfModuleItem(genericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModuleBodyParam);
            responseStatus = HttpStatus.OK;
        }
        vnfData.setVfModules(newVfModules);
        // Map and save the new data
        try {
            service.setSvcData(objectMapper.writeValueAsString(svcData));
            configServicesRepository.save(service);
            return new ResponseEntity<>(responseStatus);
        } catch(JsonProcessingException e) {
            log.error("Error mapping object to JSON", e);
            // Should probably be a 500 INTERNAL_SERVICE_ERROR
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Extracts VF MODULE Topology data from the Config table specified Service
     * Instance and VNF ID.
     * <p>
     * Maps to /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/vnf-data/vf-modules/vf-module/{vf-module-id}/vf-module-data/vf-module-topology/
     * @param serviceInstanceId the Service Instance ID to lookup data for
     * @param vnfId the VNF ID of the VNF to extract topology data from.
     * @param vfModuleId the vf-module-idof the vf-module to extract topology data from.
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    @Override
    public ResponseEntity<GenericResourceApiVfmoduletopologyVfModuleTopology>
    configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVfModulesVfModuleVfModuleIdVfModuleDataVfModuleTopologyGet(
            String serviceInstanceId, String vnfId, String vfModuleId) throws RestException {
        log.info("GET | vf-module-topology for ({})", vfModuleId);
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                log.info("Something with header");
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
        }
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if((services == null) || (services.isEmpty())) {
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        Optional<GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule> vfModule =
                getVfModuleObject(services.get(0), vnfId, vfModuleId);
        if(vfModule.isPresent()
                && vfModule.get().getVfModuleData() != null
                && vfModule.get().getVfModuleData().getVfModuleTopology() != null) {
            return new ResponseEntity<>(vfModule.get().getVfModuleData().getVfModuleTopology(), HttpStatus.OK);
        } else {
            log.info("No information found for {}", vfModuleId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVfModulesVfModuleVfModuleIdDelete(
            String serviceInstanceId, String vnfId, String vfModuleId) throws RestProtocolException {

        log.info("DELETE | vf-module Data for ({})", vfModuleId);

        if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            log.info("Something with header.");
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default ConfigApi interface so no example is generated");
        }

        ConfigServices service;
        GenericResourceApiServicedataServiceData svcData;
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if((services == null) || (services.isEmpty())) {
            log.error("service-instance-id ({}) not found in SDN.", serviceInstanceId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            service = services.get(0);
        }

        try {
            svcData = objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch(JsonProcessingException e) {
            log.error("Error", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        GenericResourceApiServicedataServicedataVnfs vnfs = svcData.getVnfs();
        Optional<GenericResourceApiServicedataServicedataVnfsVnf> vnf =
                vnfs.getVnf()
                        .stream()
                        .filter(targetVnf -> targetVnf.getVnfId().equals(vnfId))
                        .findFirst();

        if(! vnf.isPresent()) {
            log.error("vnf-id ({}) not found in SDN.", vnfId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfData = vnf.get().getVnfData();
        GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules existingVfModules = vnfData.getVfModules();
        GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules newVfModules = new GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules();

        if (existingVfModules == null || existingVfModules.getVfModule().isEmpty()) {
            log.info("No existing vf-module found. Creating the first vf-module for vnf [{}]", vnfId);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        ArrayList<String> vfModuleIds = new ArrayList<>();
        for (GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule vf : existingVfModules.getVfModule()) {
            vfModuleIds.add(vf.getVfModuleId());
        }
        log.info("[{}] vf-module(s) {} found in vnf [{}]", existingVfModules.getVfModule().size(), vfModuleIds, vnfId);
        if (!vfModuleIds.isEmpty() && vfModuleIds.contains(vfModuleId)) {
            log.info("Deleting vf-module [{}] from vnf [{}]",  vfModuleId, vnfId);
        } else {
            log.info("vf-module [{}] not found in vnf [{}]", vfModuleId, vnfId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        existingVfModules.getVfModule()
                .stream()
                .filter(vf -> ! vf.getVfModuleId().equals(vfModuleId))
                .forEach(newVfModules::addVfModuleItem);
        vnfData.setVfModules(newVfModules);
        log.info("vf-module [{}] deleted from vnf [{}]", vfModuleId, vnfId);

        // Map and save the new data
        try {
            service.setSvcData(objectMapper.writeValueAsString(svcData));
            configServicesRepository.save(service);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(JsonProcessingException e) {
            log.error("Error mapping object to JSON", e);
            // Should probably be a 500 INTERNAL_SERVICE_ERROR
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
