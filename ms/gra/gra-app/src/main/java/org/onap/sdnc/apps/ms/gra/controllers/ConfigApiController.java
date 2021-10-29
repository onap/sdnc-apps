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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.onap.ccsdk.apps.services.RestApplicationException;
import org.onap.ccsdk.apps.services.RestException;
import org.onap.ccsdk.apps.services.RestProtocolException;
import org.onap.sdnc.apps.ms.gra.controllers.ServiceDataHelper.ServiceDataTransaction;
import org.onap.sdnc.apps.ms.gra.data.ConfigContrailRouteAllottedResources;
import org.onap.sdnc.apps.ms.gra.data.ConfigContrailRouteAllottedResourcesRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigNetworksRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigPortMirrorConfigurations;
import org.onap.sdnc.apps.ms.gra.data.ConfigPortMirrorConfigurationsRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadData;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigServices;
import org.onap.sdnc.apps.ms.gra.data.ConfigServicesRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigVfModules;
import org.onap.sdnc.apps.ms.gra.data.ConfigVfModulesRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigVnfsRepository;
import org.onap.sdnc.apps.ms.gra.swagger.ConfigApi;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiAggregateroutesAggregateRoutes;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiContrailRouteAllottedResources;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiContrailrouteallottedresourcesContrailrouteallottedresourceAllottedResourceData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiContrailroutetopologyContrailRouteTopology;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkInstanceGroup;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkinstancegroupNetworkInstanceGroup;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkinstancegroupNetworkinstancegroupAggregateRoutePolicy;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetworkCustomerBondingRequests;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetworkCustomerbondingrequestsCustomerBondingRequest;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkinstancegroupNetworkinstancegroupSubnetAssignmentPolicy;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkinstancegroupNetworkinstancegroupVpnBindingPolicy;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworktopologyNetworkTopology;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiOnapmodelinformationOnapModelInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiOperStatusData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPortMirrorConfigurations;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPortmirrorconfigurationsPortmirrorconfigurationConfigurationData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadModelInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloaddataPreloadData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadmodelinformationPreloadList;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRelatedNetwork;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRelatednetworkRelatedNetwork;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceModelInfrastructure;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServiceData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataNetworks;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataNetworksNetwork;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataNetworksNetworkNetworkData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataNetworksNetworkNetworkdataNetworkProvidedAllottedResources;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnf;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnfVnfData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicemodelinfrastructureService;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicestatusServiceStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicetopologyServiceTopology;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiSubnetsSubnets;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVfmoduletopologyVfModuleTopology;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVlantagsVlanTags;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfNetworkData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfresourceassignmentsVnfResourceAssignments;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnftopologyVnfTopology;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVpnbindingsVpnBindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

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
    private ConfigNetworksRepository configNetworksRepository;

    @Autowired
    private ConfigVnfsRepository configVnfsRepository;

    @Autowired
    private ConfigVfModulesRepository configVfModulesRepository;

    @Autowired
    private ConfigPortMirrorConfigurationsRepository configPortMirrorConfigurationsRepository;

    @Autowired
    private ConfigContrailRouteAllottedResourcesRepository configContrailRouteAllottedResourcesRepository;

    @Autowired
    private ServiceDataHelper serviceDataHelper;

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
     * Extracts port-mirror configuration data from
     * CONFIG_GRA_PORT_MIRROR_CONFIGURATIONS for a given, configuration-id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:port-mirror-configurations/port-mirror-configuration/{configuration-id}/
     * 
     * @param configurationId the configuration ID for a port-mirror
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    public ResponseEntity<GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration> configGENERICRESOURCEAPIportMirrorConfigurationsPortMirrorConfigurationConfigurationIdGet(
            String configurationId) throws RestApplicationException {
        GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration retval = null;

        List<ConfigPortMirrorConfigurations> pmConfigurations = configPortMirrorConfigurationsRepository
                .findByConfigurationId(configurationId);

        if (pmConfigurations.isEmpty()) {
            log.info("No configuration data found with id [{}]", configurationId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        } else {
            ConfigPortMirrorConfigurations pmConfiguration = pmConfigurations.get(0);
            retval = new GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration();
            retval.setConfigurationId(configurationId);
            retval.setConfigurationStatus(pmConfiguration.getPortMirrorConfigurationStatus());
            try {
                retval.setConfigurationData(objectMapper.readValue(pmConfiguration.getPmcData(),
                        GenericResourceApiPortmirrorconfigurationsPortmirrorconfigurationConfigurationData.class));
            } catch (JsonProcessingException e) {
                log.error("Could not deserialize service data for service instance id {}", configurationId, e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
        return new ResponseEntity<>(retval, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIportMirrorConfigurationsPortMirrorConfigurationConfigurationIdPut(
            String configurationId,
            @Valid GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration newConfiguration)
            throws RestApplicationException {

        boolean dataExists = false;

        String newConfigurationId = newConfiguration.getConfigurationId();

        ConfigPortMirrorConfigurations portMirrorConfiguration = null;
        List<ConfigPortMirrorConfigurations> existingConfiguration = configPortMirrorConfigurationsRepository
                .findByConfigurationId(configurationId);
        if ((existingConfiguration != null) && !existingConfiguration.isEmpty()) {
            dataExists = true;
            portMirrorConfiguration = existingConfiguration.get(0);
        } else {
            portMirrorConfiguration = new ConfigPortMirrorConfigurations();
            portMirrorConfiguration.setConfigurationId(configurationId);
        }

        try {
            portMirrorConfiguration
                    .setPmcData(objectMapper.writeValueAsString(newConfiguration.getConfigurationData()));
        } catch (JsonProcessingException e) {
            log.error("Could not serialize porr-mirror configuration data for {}",
                    portMirrorConfiguration.getConfigurationId(), e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());

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
    public ResponseEntity<GenericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology> configGENERICRESOURCEAPIportMirrorConfigurationsPortMirrorConfigurationConfigurationIdConfigurationDataPortMirrorConfigurationTopologyGet(
            String configurationId) throws RestApplicationException, RestProtocolException {
        @Valid
        GenericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology portMirrorConfigurationTopology = null;
        GenericResourceApiPortmirrorconfigurationsPortmirrorconfigurationConfigurationData portMirrorConfigurationData = null;

        List<ConfigPortMirrorConfigurations> configPortMirrorConfigurations = configPortMirrorConfigurationsRepository
                .findByConfigurationId(configurationId);
        if ((configPortMirrorConfigurations == null) || (configPortMirrorConfigurations.isEmpty())) {
            log.info("No configuration data found with id [{}]", configurationId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        try {
            if (configPortMirrorConfigurations.get(0).getPmcData().isEmpty()) {
                throw new RestProtocolException("data-missing", "No configuration-data entry found",
                        HttpStatus.NOT_FOUND.value());
            } else {
                portMirrorConfigurationData = objectMapper.readValue(configPortMirrorConfigurations.get(0).getPmcData(),
                        GenericResourceApiPortmirrorconfigurationsPortmirrorconfigurationConfigurationData.class);
                portMirrorConfigurationTopology = portMirrorConfigurationData.getPortMirrorConfigurationTopology();
            }
            if (portMirrorConfigurationTopology == null) {
                throw new RestProtocolException("data-missing", "No service-topology entry found",
                        HttpStatus.NOT_FOUND.value());
            }
            return new ResponseEntity<>(portMirrorConfigurationTopology, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIportMirrorConfigurationsPortMirrorConfigurationConfigurationIdDelete(
            String configurationId) {
        configPortMirrorConfigurationsRepository.deleteByConfigurationId(configurationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIportMirrorConfigurationsPost(
            @Valid GenericResourceApiPortMirrorConfigurations genericResourceApiPortMirrorConfigurationsBodyParam)
            throws RestException {
        boolean dataExists = false;

        for (GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration portMirrorConfigItem : genericResourceApiPortMirrorConfigurationsBodyParam
                .getPortMirrorConfiguration()) {
            String configurationId = portMirrorConfigItem.getConfigurationId();
            List<ConfigPortMirrorConfigurations> existingConfiguration = configPortMirrorConfigurationsRepository
                    .findByConfigurationId(configurationId);
            if ((existingConfiguration != null) && !existingConfiguration.isEmpty()) {

                log.error("Port mirror configuration data already exists for {}", configurationId);
                throw new RestProtocolException("data-exists",
                        "Data already exists for configuration-id " + configurationId, HttpStatus.CONFLICT.value());
            }
            ConfigPortMirrorConfigurations portMirrorConfiguration = null;

            if ((existingConfiguration != null) && !existingConfiguration.isEmpty()) {
                dataExists = true;
                portMirrorConfiguration = existingConfiguration.get(0);
            } else {
                portMirrorConfiguration = new ConfigPortMirrorConfigurations();
                portMirrorConfiguration.setConfigurationId(configurationId);
            }

            try {
                portMirrorConfiguration
                        .setPmcData(objectMapper.writeValueAsString(portMirrorConfigItem.getConfigurationData()));
            } catch (JsonProcessingException e) {
                log.error("Could not serialize port-mirror configuration data for {}",
                        portMirrorConfiguration.getConfigurationId(), e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());

            }
            portMirrorConfiguration.setPortMirrorConfigurationStatus(portMirrorConfigItem.getConfigurationStatus());
            configPortMirrorConfigurationsRepository.save(portMirrorConfiguration);

        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    
    @Override
    public ResponseEntity<GenericResourceApiPortMirrorConfigurations> configGENERICRESOURCEAPIportMirrorConfigurationsGet()
            throws RestException {

        if (configPortMirrorConfigurationsRepository.count() == 0) {
            throw new RestApplicationException("data-missing",
              "Request could not be completed because the relevant data model content does not exist",
               HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiPortMirrorConfigurations retval = new GenericResourceApiPortMirrorConfigurations();

        for (ConfigPortMirrorConfigurations pmConfiguration : configPortMirrorConfigurationsRepository.findAll()) {
            GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration pmItem = new GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration();
            pmItem.setConfigurationId(pmConfiguration.getConfigurationId());
            pmItem.setConfigurationStatus(pmConfiguration.getPortMirrorConfigurationStatus());
            try {
                pmItem.setConfigurationData(objectMapper.readValue(pmConfiguration.getPmcData(),
                        GenericResourceApiPortmirrorconfigurationsPortmirrorconfigurationConfigurationData.class));
                retval.addPortMirrorConfigurationItem(pmItem);
            } catch (JsonProcessingException e) {
                log.error("Could not deserialize service data for service instance id {}", pmConfiguration.getConfigurationId(), e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }

        return new ResponseEntity<>(retval, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIportMirrorConfigurationsPut(
            @Valid GenericResourceApiPortMirrorConfigurations genericResourceApiPortMirrorConfigurationsBodyParam)
            throws RestException {
        boolean dataExists = false;

        for (GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration portMirrorConfigItem : genericResourceApiPortMirrorConfigurationsBodyParam
                .getPortMirrorConfiguration()) {
            String configurationId = portMirrorConfigItem.getConfigurationId();
            List<ConfigPortMirrorConfigurations> existingConfiguration = configPortMirrorConfigurationsRepository
                    .findByConfigurationId(configurationId);

            ConfigPortMirrorConfigurations portMirrorConfiguration = null;

            if ((existingConfiguration != null) && !existingConfiguration.isEmpty()) {
                dataExists = true;
                portMirrorConfiguration = existingConfiguration.get(0);
            } else {
                portMirrorConfiguration = new ConfigPortMirrorConfigurations();
                portMirrorConfiguration.setConfigurationId(configurationId);
            }

            try {
                portMirrorConfiguration
                        .setPmcData(objectMapper.writeValueAsString(portMirrorConfigItem.getConfigurationData()));
            } catch (JsonProcessingException e) {
                log.error("Could not serialize port-mirror configuration data for {}",
                        portMirrorConfiguration.getConfigurationId(), e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());

            }
            portMirrorConfiguration.setPortMirrorConfigurationStatus(portMirrorConfigItem.getConfigurationStatus());
            configPortMirrorConfigurationsRepository.save(portMirrorConfiguration);

        }

        if (dataExists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    /**
     * Extracts contrail-route-allotted-resource data from
     * CONFIG_GRA_CONTRAIL_ROUTE_ALLOTTED_RESOURCES for a given allottedResourceId
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:contrail-route-allotted-resources/contrail-route-allotted-resource/{allotted-resource-id}
     * 
     * @param allottedResourceId the allotted-resource-id for a contrail-route
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    public ResponseEntity<GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource> configGENERICRESOURCEAPIcontrailRouteAllottedResourcesContrailRouteAllottedResourceAllottedResourceIdGet(
            String allottedResourceId) throws RestApplicationException {
        GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource retval = null;

        List<ConfigContrailRouteAllottedResources> allottedResources = configContrailRouteAllottedResourcesRepository
                .findByAllottedResourceId(allottedResourceId);

        if (allottedResources.isEmpty()) {
            log.info("No contrail-route-allotted-resource found with id [{}]", allottedResourceId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        } else {
            ConfigContrailRouteAllottedResources allottedResource = allottedResources.get(0);
            retval = new GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource();
            retval.setAllottedResourceId(allottedResourceId);
            retval.setAllottedResourceStatus(allottedResource.getAllottedResourceStatus());
            try {
                retval.setAllottedResourceData(objectMapper.readValue(allottedResource.getArData(),
                        GenericResourceApiContrailrouteallottedresourcesContrailrouteallottedresourceAllottedResourceData.class));
            } catch (JsonProcessingException e) {
                log.error("Could not deserialize service data for service instance id {}", allottedResourceId, e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
        return new ResponseEntity<>(retval, HttpStatus.OK);
    }

    /**
     * PUT contrail-route-allotted-resource data from
     * CONFIG_GRA_CONTRAIL_ROUTE_ALLOTTED_RESOURCES for a given allottedResourceId
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:contrail-route-allotted-resources/contrail-route-allotted-resource/{allotted-resource-id}
     * 
     * @param allottedResourceId the allotted-resource-id for a contrail-route
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIcontrailRouteAllottedResourcesContrailRouteAllottedResourceAllottedResourceIdPut(
            String allottedResourceId,
            @Valid GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource newAllottedResource)
            throws RestApplicationException {

        boolean dataExists = false;

        String newAllottedResourceId = newAllottedResource.getAllottedResourceId();

        ConfigContrailRouteAllottedResources allottedResource = null;
        List<ConfigContrailRouteAllottedResources> existingAllottedResource = configContrailRouteAllottedResourcesRepository
                .findByAllottedResourceId(allottedResourceId);

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
            log.error("Could not serialize port-mirror configuration data for {}",
                    allottedResource.getAllottedResourceId(), e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());

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
     * Extracts contrail-route-topology data from
     * CONFIG_GRA_CONTRAIL_ROUTE_ALLOTTED_RESOURCES for a given allottedResourceId
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:contrail-route-allotted-resources/contrail-route-allotted-resource/{allotted-resource-id}/allotted-resource-data/contrail-route-topology/
     * 
     * @param allottedResourceId the allotted-resource-id for a contrail-route
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    @Override
    public ResponseEntity<GenericResourceApiContrailroutetopologyContrailRouteTopology> configGENERICRESOURCEAPIcontrailRouteAllottedResourcesContrailRouteAllottedResourceAllottedResourceIdAllottedResourceDataContrailRouteTopologyGet(
            String allottedResourceId) throws RestApplicationException, RestProtocolException {
        @Valid
        GenericResourceApiContrailroutetopologyContrailRouteTopology contrailRouteTopology = null;
        GenericResourceApiContrailrouteallottedresourcesContrailrouteallottedresourceAllottedResourceData allottedResourceData = null;

        List<ConfigContrailRouteAllottedResources> configContrailRouteAllottedResources = configContrailRouteAllottedResourcesRepository
                .findByAllottedResourceId(allottedResourceId);

        if ((configContrailRouteAllottedResources == null) || (configContrailRouteAllottedResources.isEmpty())) {
            log.info("No contrail-route-allotted-resource data found with id [{}]", allottedResourceId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        try {
            if (configContrailRouteAllottedResources.get(0).getArData().isEmpty()) {
                throw new RestProtocolException("data-missing", "No allotted-resource-data entry found",
                        HttpStatus.NOT_FOUND.value());
            } else {
                allottedResourceData = objectMapper.readValue(configContrailRouteAllottedResources.get(0).getArData(),
                        GenericResourceApiContrailrouteallottedresourcesContrailrouteallottedresourceAllottedResourceData.class);

                contrailRouteTopology = allottedResourceData.getContrailRouteTopology();
            }
            if (contrailRouteTopology == null) {
                throw new RestProtocolException("data-missing", "No contrail-route-topology entry found",
                        HttpStatus.NOT_FOUND.value());
            }
            return new ResponseEntity<>(contrailRouteTopology, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("Could not parse port-mirror-configuration data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * DELETE allotted-resource data from
     * CONFIG_GRA_CONTRAIL_ROUTE_ALLOTTED_RESOURCES for a given allottedResourceId
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:contrail-route-allotted-resources/contrail-route-allotted-resource/{allotted-resource-id}
     * 
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
    public ResponseEntity<GenericResourceApiContrailRouteAllottedResources> configGENERICRESOURCEAPIcontrailRouteAllottedResourcesGet()
            throws RestException {
                
        if (configContrailRouteAllottedResourcesRepository.count() == 0) {
             throw new RestApplicationException("data-missing",
                "Request could not be completed because the relevant data model content does not exist",
                HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiContrailRouteAllottedResources retval = new GenericResourceApiContrailRouteAllottedResources();
        
        for (ConfigContrailRouteAllottedResources allottedResource : configContrailRouteAllottedResourcesRepository.findAll()) {

            GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource curItem = new GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource();
            curItem.setAllottedResourceId(allottedResource.getAllottedResourceId());
            curItem.setAllottedResourceStatus(allottedResource.getAllottedResourceStatus());
            try {
                curItem.setAllottedResourceData(objectMapper.readValue(allottedResource.getArData(),
                        GenericResourceApiContrailrouteallottedresourcesContrailrouteallottedresourceAllottedResourceData.class));
                retval.addContrailRouteAllottedResourceItem(curItem);
            } catch (JsonProcessingException e) {
                log.error("Could not deserialize service data for service instance id {}", allottedResource.getAllottedResourceId(), e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }

        return new ResponseEntity<>(retval, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIcontrailRouteAllottedResourcesPost(
            @Valid GenericResourceApiContrailRouteAllottedResources genericResourceApiContrailRouteAllottedResourcesBodyParam)
            throws RestException {
        for (GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource contrailItem : genericResourceApiContrailRouteAllottedResourcesBodyParam
                .getContrailRouteAllottedResource()) {
            String allottedResourceId = contrailItem.getAllottedResourceId();
            List<ConfigContrailRouteAllottedResources> existingResources = configContrailRouteAllottedResourcesRepository
                    .findByAllottedResourceId(allottedResourceId);

            ConfigContrailRouteAllottedResources allottedResource = null;

            if ((existingResources != null) && !existingResources.isEmpty()) {
                log.error("Contrail route allotted resource data already exists for {}", allottedResourceId);
                throw new RestProtocolException("data-exists",
                        "Data already exists for allotted-resource-id " + allottedResourceId,
                        HttpStatus.CONFLICT.value());
            } else {
                allottedResource = new ConfigContrailRouteAllottedResources();
                allottedResource.setAllottedResourceId(allottedResourceId);
            }

            try {
                allottedResource.setArData(objectMapper.writeValueAsString(contrailItem.getAllottedResourceData()));
            } catch (JsonProcessingException e) {
                log.error("Could not serialize porr-mirror configuration data for {}",
                        allottedResource.getAllottedResourceId(), e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());

            }
            allottedResource.setAllottedResourceStatus(contrailItem.getAllottedResourceStatus());
            configContrailRouteAllottedResourcesRepository.save(allottedResource);

        }

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIcontrailRouteAllottedResourcesPut(
            @Valid GenericResourceApiContrailRouteAllottedResources genericResourceApiContrailRouteAllottedResourcesBodyParam)
            throws RestException {
        boolean dataExists = false;

        for (GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource contrailItem : genericResourceApiContrailRouteAllottedResourcesBodyParam
                .getContrailRouteAllottedResource()) {
            String allottedResourceId = contrailItem.getAllottedResourceId();
            List<ConfigContrailRouteAllottedResources> existingResources = configContrailRouteAllottedResourcesRepository
                    .findByAllottedResourceId(allottedResourceId);

            ConfigContrailRouteAllottedResources allottedResource = null;

            if ((existingResources != null) && !existingResources.isEmpty()) {
                dataExists = true;
                allottedResource = existingResources.get(0);
            } else {
                allottedResource = new ConfigContrailRouteAllottedResources();
                allottedResource.setAllottedResourceId(allottedResourceId);
            }

            try {
                allottedResource.setArData(objectMapper.writeValueAsString(contrailItem.getAllottedResourceData()));
            } catch (JsonProcessingException e) {
                log.error("Could not serialize porr-mirror configuration data for {}",
                        allottedResource.getAllottedResourceId(), e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());

            }
            allottedResource.setAllottedResourceStatus(contrailItem.getAllottedResourceStatus());
            configContrailRouteAllottedResourcesRepository.save(allottedResource);

        }

        if (dataExists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
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
        configVfModulesRepository.deleteAll();
        configVnfsRepository.deleteAll();
        configNetworksRepository.deleteAll();
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
            String svcInstanceId = service.getSvcInstanceId();

            serviceItem.setServiceInstanceId(svcInstanceId);
            serviceItem.setServiceStatus(service.getServiceStatus());

            // Construct service data from networks/vnfs/vfModules
            GenericResourceApiServicedataServiceData serviceItemSvcData;
            try {
                serviceItemSvcData = serviceDataHelper.getServiceData(service);
            } catch (JsonProcessingException e) {
                log.error("Could not deserialize service data for {}", service.getSvcInstanceId(), e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());
            }

            if (serviceItemSvcData != null) {
                serviceItem.setServiceData(serviceItemSvcData);
            }
            modelInfrastructure.addServiceItem(serviceItem);
        }

        return new ResponseEntity<>(modelInfrastructure, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesPost(
            @Valid GenericResourceApiServiceModelInfrastructure modelInfrastructure)
            throws RestApplicationException, RestProtocolException {

        ServiceDataTransaction transaction = serviceDataHelper.createTransaction();

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
            service.setServiceStatus(serviceItem.getServiceStatus());

            try {
                serviceDataHelper.saveService(service, serviceItem.getServiceData(), transaction);
            } catch (JsonProcessingException e) {
                log.error("Could not serialize service data for {}", service.getSvcInstanceId(), e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());

            }
        }

        transaction.commit();

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesPut(
            @Valid GenericResourceApiServiceModelInfrastructure modelInfrastructure) throws RestApplicationException {

        ServiceDataTransaction transaction = serviceDataHelper.createTransaction();
        boolean dataExists = false;

        for (GenericResourceApiServicemodelinfrastructureService serviceItem : modelInfrastructure.getService()) {
            String svcInstanceId = serviceItem.getServiceInstanceId();
            List<ConfigServices> existingService = configServicesRepository.findBySvcInstanceId(svcInstanceId);
            if ((existingService != null) && !existingService.isEmpty()) {
                dataExists = true;
            }
            ConfigServices service = new ConfigServices();
            service.setSvcInstanceId(svcInstanceId);
            service.setServiceStatus(serviceItem.getServiceStatus());
            try {
                serviceDataHelper.saveService(service, serviceItem.getServiceData(), transaction);
            } catch (JsonProcessingException e) {
                log.error("Could not serialize service data for {}", service.getSvcInstanceId(), e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());

            }
        }

        transaction.commit();

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
            ConfigServices configService = new ConfigServices(svcInstanceId, servicesData.getServiceStatus());
            serviceDataHelper.saveService(configService, servicesData.getServiceData());
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
        serviceDataHelper.deleteService(serviceInstanceId);
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
                retval.setServiceData(serviceDataHelper.getServiceData(service));
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
        service.setServiceStatus(newService.getServiceStatus());
        try {
            serviceDataHelper.saveService(service, newService.getServiceData());
        } catch (JsonProcessingException e) {
            log.error("Could not serialize service data for {}", service.getSvcInstanceId(), e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());

        }

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

        service.setServiceStatus(newService.getServiceStatus());
        try {
            serviceDataHelper.saveService(service, newService.getServiceData());
        } catch (JsonProcessingException e) {
            log.error("Could not serialize service data for {}", service.getSvcInstanceId(), e);
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
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataDelete(
            String serviceInstanceId) throws RestProtocolException {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigServices service = services.get(0);
        if (!serviceDataHelper.hasServiceData(service)) {
            throw new RestProtocolException("data-missing", "No service-data found", HttpStatus.NOT_FOUND.value());
        }
        serviceDataHelper.clearServiceData(service);

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
            serviceData = serviceDataHelper.getServiceData(services.get(0));
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

        if (serviceDataHelper.hasServiceData(service)) {
            log.error("service-data already exists for svcInstanceId {}", serviceInstanceId);
            throw new RestProtocolException("data-exists", "Data already exists for " + serviceInstanceId,
                    HttpStatus.CONFLICT.value());
        }

        try {
            serviceDataHelper.saveService(service, serviceData);
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

        dataExists = serviceDataHelper.hasServiceData(service);

        try {
            serviceDataHelper.saveService(service, serviceData);
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
     * Delete network instance group data from the Config table specified Service
     * Instance.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/network-instance-group/network-instance-group/{network-instance-group-id}/
     * 
     * @param serviceInstanceId      the Service Instance ID to delete from
     * @param networkInstanceGroupId the network instance group ID to delete
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId} does not
     *         exist.
     */

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworkInstanceGroupsNetworkInstanceGroupNetworkInstanceGroupIdDelete(
            String serviceInstanceId, String networkInstanceGroupId) throws RestException {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigServices service = services.get(0);

        if (service.getSvcData() == null) {
            throw new RestProtocolException("data-missing", "No service-data found", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not deserialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        GenericResourceApiNetworkInstanceGroup curNetInstanceGroup = svcData.getNetworkInstanceGroups();
        GenericResourceApiNetworkInstanceGroup newNetInstanceGroup = new GenericResourceApiNetworkInstanceGroup();

        List<GenericResourceApiNetworkinstancegroupNetworkInstanceGroup> curList = curNetInstanceGroup
                .getNetworkInstanceGroup();
        if (curList != null) {
            for (GenericResourceApiNetworkinstancegroupNetworkInstanceGroup curItem : curList) {
                if (!networkInstanceGroupId.equals(curItem.getNetworkInstanceGroupId())) {
                    newNetInstanceGroup.addNetworkInstanceGroupItem(curItem);
                }
            }
        }
        svcData.setNetworkInstanceGroups(newNetInstanceGroup);
        try {
            service.setSvcData(objectMapper.writeValueAsString(svcData));
        } catch (JsonProcessingException e) {

            log.error("Could not serialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        configServicesRepository.save(service);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Get network instance group data from the Config table specified Service
     * Instance.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/network-instance-group/network-instance-group/{network-instance-group-id}/
     * 
     * @param serviceInstanceId      the Service Instance ID to get
     * @param networkInstanceGroupId the network instance group ID to get
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId} does not
     *         exist.
     */
    @Override
    public ResponseEntity<GenericResourceApiNetworkinstancegroupNetworkInstanceGroup> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworkInstanceGroupsNetworkInstanceGroupNetworkInstanceGroupIdGet(
            String serviceInstanceId, String networkInstanceGroupId) throws RestException {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigServices service = services.get(0);

        if (service.getSvcData() == null) {
            throw new RestProtocolException("data-missing", "No service-data found", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not deserialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        GenericResourceApiNetworkInstanceGroup curNetInstanceGroup = svcData.getNetworkInstanceGroups();

        if (curNetInstanceGroup == null) {
            throw new RestProtocolException("data-missing", "No network-instance-group found",
                    HttpStatus.NOT_FOUND.value());
        }
        List<GenericResourceApiNetworkinstancegroupNetworkInstanceGroup> curList = curNetInstanceGroup
                .getNetworkInstanceGroup();

        if (curList == null) {
            throw new RestProtocolException("data-missing", "Empty network-instance-group ",
                    HttpStatus.NOT_FOUND.value());
        }

        for (GenericResourceApiNetworkinstancegroupNetworkInstanceGroup curItem : curList) {
            if (networkInstanceGroupId.equals(curItem.getNetworkInstanceGroupId())) {
                return new ResponseEntity<>(curItem, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.OK);

    }

    /**
     * Delete network instance group network data from the Config table specified
     * Service Instance.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/network-instance-group/network-instance-group/{network-instance-group-id}/networks/network/{network-id}/
     * 
     * @param serviceInstanceId      the Service Instance ID to delete from
     * @param networkInstanceGroupId the network instance group ID to delete
     * @param networkId              the network ID to delete
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId} does not
     *         exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworkInstanceGroupsNetworkInstanceGroupNetworkInstanceGroupIdNetworksNetworkNetworkIdDelete(
            String serviceInstanceId, String networkInstanceGroupId, String networkId) throws RestException {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigServices service = services.get(0);

        if (service.getSvcData() == null) {
            throw new RestProtocolException("data-missing", "No service-data found", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not deserialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        GenericResourceApiNetworkInstanceGroup curNetInstanceGroup = svcData.getNetworkInstanceGroups();

        List<GenericResourceApiNetworkinstancegroupNetworkInstanceGroup> curList = curNetInstanceGroup
                .getNetworkInstanceGroup();
        if (curList != null) {
            for (GenericResourceApiNetworkinstancegroupNetworkInstanceGroup curItem : curList) {
                if (networkInstanceGroupId.equals(curItem.getNetworkInstanceGroupId())) {
                    GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks curNetworks = curItem
                            .getNetworks();
                    GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks newNetworks = new GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks();
                    List<GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork> curNetList = curNetworks
                            .getNetwork();
                    for (GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork curNetListItem : curNetList) {
                        if (!networkId.equals(curNetListItem.getNetworkId())) {
                            newNetworks.addNetworkItem(curNetListItem);
                        }
                    }
                    curItem.setNetworks(newNetworks);
                }
            }

        }
        try {
            service.setSvcData(objectMapper.writeValueAsString(svcData));
        } catch (JsonProcessingException e) {

            log.error("Could not serialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        configServicesRepository.save(service);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Get network instance group network data from the Config table specified
     * Service Instance.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/network-instance-group/network-instance-group/{network-instance-group-id}/networks/network/{network-id}/
     * 
     * @param serviceInstanceId      the Service Instance ID to get
     * @param networkInstanceGroupId the network instance group ID to get
     * @param networkId              the network ID to get
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId} does not
     *         exist.
     */
    @Override
    public ResponseEntity<GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworkInstanceGroupsNetworkInstanceGroupNetworkInstanceGroupIdNetworksNetworkNetworkIdGet(
            String serviceInstanceId, String networkInstanceGroupId, String networkId) throws RestException {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigServices service = services.get(0);

        if (service.getSvcData() == null) {
            throw new RestProtocolException("data-missing", "No service-data found", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not deserialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        GenericResourceApiNetworkInstanceGroup curNetInstanceGroup = svcData.getNetworkInstanceGroups();

        List<GenericResourceApiNetworkinstancegroupNetworkInstanceGroup> curList = curNetInstanceGroup
                .getNetworkInstanceGroup();
        if (curList != null) {
            for (GenericResourceApiNetworkinstancegroupNetworkInstanceGroup curItem : curList) {
                if (networkInstanceGroupId.equals(curItem.getNetworkInstanceGroupId())) {
                    GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks curNetworks = curItem
                            .getNetworks();
                    List<GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork> curNetList = curNetworks
                            .getNetwork();
                    for (GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork curNetListItem : curNetList) {
                        if (networkId.equals(curNetListItem.getNetworkId())) {
                            return new ResponseEntity<>(curNetListItem, HttpStatus.OK);
                        }
                    }
                }
            }

        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * Post network to network instance group network data from the Config table
     * specified Service Instance.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/network-instance-group/network-instance-group/{network-instance-group-id}/networks/network/{network-id}/
     * 
     * @param serviceInstanceId      the Service Instance ID to update
     * @param networkInstanceGroupId the network instance group ID to update
     * @param networkId              the network ID to update
     * @param network                the network to add
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId} does not
     *         exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworkInstanceGroupsNetworkInstanceGroupNetworkInstanceGroupIdNetworksNetworkNetworkIdPost(
            String serviceInstanceId, String networkInstanceGroupId, String networkId,
            @Valid GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork network)
            throws RestException {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigServices service = services.get(0);

        if (service.getSvcData() == null) {
            throw new RestProtocolException("data-missing", "No service-data found", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not deserialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        GenericResourceApiNetworkInstanceGroup curNetInstanceGroup = svcData.getNetworkInstanceGroups();

        List<GenericResourceApiNetworkinstancegroupNetworkInstanceGroup> curList = curNetInstanceGroup
                .getNetworkInstanceGroup();
        if (curList != null) {
            for (GenericResourceApiNetworkinstancegroupNetworkInstanceGroup curItem : curList) {
                if (networkInstanceGroupId.equals(curItem.getNetworkInstanceGroupId())) {
                    GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks curNetworks = curItem
                            .getNetworks();
                    GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks newNetworks = new GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks();
                    List<GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork> curNetList = curNetworks
                            .getNetwork();
                    for (GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork curNetListItem : curNetList) {
                        if (!networkId.equals(curNetListItem.getNetworkId())) {
                            newNetworks.addNetworkItem(curNetListItem);
                        } else {
                            log.error(
                                    "network already exists for svcInstanceId, networkInstanceGroupId, networkId {}, {}, {}",
                                    serviceInstanceId, networkInstanceGroupId, networkId);
                            throw new RestProtocolException("data-exists", "Data already exists for "
                                    + serviceInstanceId + "/" + networkInstanceGroupId + "/" + networkId,
                                    HttpStatus.CONFLICT.value());
                        }
                    }
                    newNetworks.addNetworkItem(network);
                    curItem.setNetworks(newNetworks);
                }
            }
        }

        try {
            service.setSvcData(objectMapper.writeValueAsString(svcData));
        } catch (JsonProcessingException e) {

            log.error("Could not serialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        configServicesRepository.save(service);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Put network to network instance group network data from the Config table
     * specified Service Instance.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/network-instance-group/network-instance-group/{network-instance-group-id}/networks/network/{network-id}/
     * 
     * @param serviceInstanceId      the Service Instance ID to update
     * @param networkInstanceGroupId the network instance group ID to update
     * @param networkId              the network ID to update
     * @param network                the network to add
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId} does not
     *         exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworkInstanceGroupsNetworkInstanceGroupNetworkInstanceGroupIdNetworksNetworkNetworkIdPut(
            String serviceInstanceId, String networkInstanceGroupId, String networkId,
            @Valid GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork network)
            throws RestException {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        boolean networkExists = false;

        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigServices service = services.get(0);

        if (service.getSvcData() == null) {
            throw new RestProtocolException("data-missing", "No service-data found", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not deserialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        GenericResourceApiNetworkInstanceGroup curNetInstanceGroup = svcData.getNetworkInstanceGroups();

        List<GenericResourceApiNetworkinstancegroupNetworkInstanceGroup> curList = curNetInstanceGroup
                .getNetworkInstanceGroup();
        if (curList != null) {
            for (GenericResourceApiNetworkinstancegroupNetworkInstanceGroup curItem : curList) {
                if (networkInstanceGroupId.equals(curItem.getNetworkInstanceGroupId())) {
                    GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks curNetworks = curItem
                            .getNetworks();
                    GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks newNetworks = new GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks();
                    List<GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork> curNetList = curNetworks
                            .getNetwork();
                    for (GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork curNetListItem : curNetList) {
                        if (!networkId.equals(curNetListItem.getNetworkId())) {
                            newNetworks.addNetworkItem(curNetListItem);
                        } else {
                            networkExists = true;
                            newNetworks.addNetworkItem(network);
                        }
                    }
                    if (!networkExists) {
                        newNetworks.addNetworkItem(network);
                    }
                    curItem.setNetworks(newNetworks);
                }
            }
        }

        try {
            service.setSvcData(objectMapper.writeValueAsString(svcData));
        } catch (JsonProcessingException e) {

            log.error("Could not serialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        configServicesRepository.save(service);

        if (networkExists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    /**
     * Delete vpn binding from network instance group network data from the Config
     * table specified Service Instance.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/network-instance-group/network-instance-group/{network-instance-group-id}/networks/network/{network-id}/vpn-bindings/vpn-binding/{vpn-binding-id}/
     * 
     * @param serviceInstanceId      the Service Instance ID to delete from
     * @param networkInstanceGroupId the network instance group ID to delete
     * @param networkId              the network ID to delete
     * @param vpnBindingId           the vpn binding ID to delete
     * @return HttpStatus.OK (200) on successful DELETE
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId} does not
     *         exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworkInstanceGroupsNetworkInstanceGroupNetworkInstanceGroupIdNetworksNetworkNetworkIdVpnBindingsVpnBindingIdDelete(
            String serviceInstanceId, String networkInstanceGroupId, String networkId, String vpnBindingId)
            throws RestException {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigServices service = services.get(0);

        if (service.getSvcData() == null) {
            throw new RestProtocolException("data-missing", "No service-data found", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not deserialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        GenericResourceApiNetworkInstanceGroup curNetInstanceGroup = svcData.getNetworkInstanceGroups();

        List<GenericResourceApiNetworkinstancegroupNetworkInstanceGroup> curList = curNetInstanceGroup
                .getNetworkInstanceGroup();
        if (curList != null) {
            for (GenericResourceApiNetworkinstancegroupNetworkInstanceGroup curItem : curList) {
                if (networkInstanceGroupId.equals(curItem.getNetworkInstanceGroupId())) {
                    GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks curNetworks = curItem
                            .getNetworks();
                    List<GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork> curNetList = curNetworks
                            .getNetwork();
                    for (GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork curNetListItem : curNetList) {
                        if (networkId.equals(curNetListItem.getNetworkId())) {
                            List<GenericResourceApiVpnbindingsVpnBindings> curVpnBindings = curNetListItem
                                    .getVpnBindings();
                            ArrayList<GenericResourceApiVpnbindingsVpnBindings> newVpnBindings = new ArrayList<>(
                                    curVpnBindings.size() - 1);
                            for (GenericResourceApiVpnbindingsVpnBindings curBinding : curVpnBindings) {
                                if (!vpnBindingId.equals(curBinding.getVpnBindingId())) {
                                    newVpnBindings.add(curBinding);
                                }
                            }
                            curNetListItem.setVpnBindings(newVpnBindings);
                        }
                    }
                }
            }

        }
        try {
            service.setSvcData(objectMapper.writeValueAsString(svcData));
        } catch (JsonProcessingException e) {

            log.error("Could not serialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        configServicesRepository.save(service);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Get vpn binding from network instance group network data from the Config
     * table specified Service Instance.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/network-instance-group/network-instance-group/{network-instance-group-id}/networks/network/{network-id}/vpn-bindings/vpn-binding/{vpn-binding-id}/
     * 
     * @param serviceInstanceId      the Service Instance ID to get
     * @param networkInstanceGroupId the network instance group ID to get
     * @param networkId              the network ID to get
     * @param vpnBindingId           the vpn binding ID to get
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId} does not
     *         exist.
     */
    @Override
    public ResponseEntity<GenericResourceApiVpnbindingsVpnBindings> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworkInstanceGroupsNetworkInstanceGroupNetworkInstanceGroupIdNetworksNetworkNetworkIdVpnBindingsVpnBindingIdGet(
            String serviceInstanceId, String networkInstanceGroupId, String networkId, String vpnBindingId)
            throws RestException {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigServices service = services.get(0);

        if (service.getSvcData() == null) {
            throw new RestProtocolException("data-missing", "No service-data found", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not deserialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        GenericResourceApiNetworkInstanceGroup curNetInstanceGroup = svcData.getNetworkInstanceGroups();

        List<GenericResourceApiNetworkinstancegroupNetworkInstanceGroup> curList = curNetInstanceGroup
                .getNetworkInstanceGroup();
        if (curList != null) {
            for (GenericResourceApiNetworkinstancegroupNetworkInstanceGroup curItem : curList) {
                if (networkInstanceGroupId.equals(curItem.getNetworkInstanceGroupId())) {
                    GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks curNetworks = curItem
                            .getNetworks();
                    List<GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork> curNetList = curNetworks
                            .getNetwork();
                    for (GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork curNetListItem : curNetList) {
                        if (networkId.equals(curNetListItem.getNetworkId())) {
                            List<GenericResourceApiVpnbindingsVpnBindings> curVpnBindings = curNetListItem
                                    .getVpnBindings();
                            for (GenericResourceApiVpnbindingsVpnBindings curBinding : curVpnBindings) {
                                if (vpnBindingId.equals(curBinding.getVpnBindingId())) {
                                    return new ResponseEntity<>(curBinding, HttpStatus.OK);
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * Post vpn binding from network instance group network data from the Config
     * table specified Service Instance.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/network-instance-group/network-instance-group/{network-instance-group-id}/networks/network/{network-id}/vpn-bindings/vpn-binding/{vpn-binding-id}/
     * 
     * @param serviceInstanceId      the Service Instance ID to update
     * @param networkInstanceGroupId the network instance group ID to update
     * @param networkId              the network ID to update
     * @param vpnBindingId           the vpn binding ID to post
     * @param vpnBinding             the vpn binding body to post
     * @return HttpStatus.CREATED (201) on successful POST
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId} does not
     *         exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworkInstanceGroupsNetworkInstanceGroupNetworkInstanceGroupIdNetworksNetworkNetworkIdVpnBindingsVpnBindingIdPost(
            String serviceInstanceId, String networkInstanceGroupId, String networkId, String vpnBindingId,
            @Valid GenericResourceApiVpnbindingsVpnBindings vpnBinding) throws RestException {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigServices service = services.get(0);

        if (service.getSvcData() == null) {
            throw new RestProtocolException("data-missing", "No service-data found", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not deserialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        GenericResourceApiNetworkInstanceGroup curNetInstanceGroup = svcData.getNetworkInstanceGroups();

        List<GenericResourceApiNetworkinstancegroupNetworkInstanceGroup> curList = curNetInstanceGroup
                .getNetworkInstanceGroup();
        if (curList != null) {
            for (GenericResourceApiNetworkinstancegroupNetworkInstanceGroup curItem : curList) {
                if (networkInstanceGroupId.equals(curItem.getNetworkInstanceGroupId())) {
                    GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks curNetworks = curItem
                            .getNetworks();
                    List<GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork> curNetList = curNetworks
                            .getNetwork();
                    for (GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork curNetListItem : curNetList) {
                        if (networkId.equals(curNetListItem.getNetworkId())) {
                            List<GenericResourceApiVpnbindingsVpnBindings> curVpnBindings = curNetListItem
                                    .getVpnBindings();
                            ArrayList<GenericResourceApiVpnbindingsVpnBindings> newVpnBindings = new ArrayList<>(
                                    curVpnBindings.size() - 1);
                            for (GenericResourceApiVpnbindingsVpnBindings curBinding : curVpnBindings) {
                                if (!vpnBindingId.equals(curBinding.getVpnBindingId())) {
                                    newVpnBindings.add(curBinding);
                                } else {
                                    log.error(
                                            "vpn-binding already exists for svcInstanceId, networkInstanceGroupId, networkId, vpnBindingId {}, {}, {}, {}",
                                            serviceInstanceId, networkInstanceGroupId, networkId, vpnBindingId);
                                    throw new RestProtocolException("data-exists",
                                            "Data already exists for " + serviceInstanceId + "/"
                                                    + networkInstanceGroupId + "/" + networkId + "/" + vpnBindingId,
                                            HttpStatus.CONFLICT.value());
                                }
                            }
                            newVpnBindings.add(vpnBinding);
                            curNetListItem.setVpnBindings(newVpnBindings);
                        }
                    }
                }
            }

        }
        try {
            service.setSvcData(objectMapper.writeValueAsString(svcData));
        } catch (JsonProcessingException e) {

            log.error("Could not serialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        configServicesRepository.save(service);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Put vpn binding from network instance group network data from the Config
     * table specified Service Instance.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/network-instance-group/network-instance-group/{network-instance-group-id}/networks/network/{network-id}/vpn-bindings/vpn-binding/{vpn-binding-id}/
     * 
     * @param serviceInstanceId      the Service Instance ID to update
     * @param networkInstanceGroupId the network instance group ID to update
     * @param networkId              the network ID to update
     * @param vpnBindingId           the vpn binding ID to post
     * @param vpnBinding             the vpn binding body to post
     * @return HttpStatus.CREATED (201) on successful POST
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId} does not
     *         exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworkInstanceGroupsNetworkInstanceGroupNetworkInstanceGroupIdNetworksNetworkNetworkIdVpnBindingsVpnBindingIdPut(
            String serviceInstanceId, String networkInstanceGroupId, String networkId, String vpnBindingId,
            @Valid GenericResourceApiVpnbindingsVpnBindings vpnBinding) throws RestException {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        boolean vpnBindingExists = false;
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigServices service = services.get(0);

        if (service.getSvcData() == null) {
            throw new RestProtocolException("data-missing", "No service-data found", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServiceData svcData = null;
        try {
            svcData = objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class);
        } catch (JsonProcessingException e) {
            log.error("Could not deserialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        GenericResourceApiNetworkInstanceGroup curNetInstanceGroup = svcData.getNetworkInstanceGroups();

        List<GenericResourceApiNetworkinstancegroupNetworkInstanceGroup> curList = curNetInstanceGroup
                .getNetworkInstanceGroup();
        if (curList != null) {
            for (GenericResourceApiNetworkinstancegroupNetworkInstanceGroup curItem : curList) {
                if (networkInstanceGroupId.equals(curItem.getNetworkInstanceGroupId())) {
                    GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks curNetworks = curItem
                            .getNetworks();
                    List<GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork> curNetList = curNetworks
                            .getNetwork();
                    for (GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork curNetListItem : curNetList) {
                        if (networkId.equals(curNetListItem.getNetworkId())) {
                            List<GenericResourceApiVpnbindingsVpnBindings> curVpnBindings = curNetListItem
                                    .getVpnBindings();
                            ArrayList<GenericResourceApiVpnbindingsVpnBindings> newVpnBindings = new ArrayList<>(
                                    curVpnBindings.size() - 1);
                            for (GenericResourceApiVpnbindingsVpnBindings curBinding : curVpnBindings) {
                                if (!vpnBindingId.equals(curBinding.getVpnBindingId())) {
                                    newVpnBindings.add(curBinding);
                                } else {
                                    vpnBindingExists = true;
                                    newVpnBindings.add(vpnBinding);
                                }
                            }
                            if (!vpnBindingExists) {
                                newVpnBindings.add(vpnBinding);
                            }
                            curNetListItem.setVpnBindings(newVpnBindings);
                        }
                    }
                }
            }

        }
        try {
            service.setSvcData(objectMapper.writeValueAsString(svcData));
        } catch (JsonProcessingException e) {

            log.error("Could not serialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        configServicesRepository.save(service);

        if (vpnBindingExists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    /**
     * Get network data from the Config table specified Service Instance.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/networks/
     * 
     * @param serviceInstanceId the Service Instance ID to perform the get on
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId} does not
     *         exist.
     */

    @Override
    public ResponseEntity<GenericResourceApiServicedataServicedataNetworks> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksGet(
            String serviceInstanceId) throws RestException {
        log.info("GET | Network Data for svcInstanceId ({})", serviceInstanceId);

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataNetworks networks = null;
        try {
            networks = serviceDataHelper.getNetworks(serviceInstanceId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return new ResponseEntity<>(networks, HttpStatus.OK);
    }

    /**
     * Delete network data from the Config table specified Service Instance and network
     * id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/networks/network/{network-id}/
     * 
     * @param serviceInstanceId the Service Instance ID to perform the get on
     * @param networkId         the network ID to perform the delete on
     * @return HttpStatus.NO_CONTENT (204) on successful DELETE
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId}
     *         does not exist.
     */

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdDelete(
        String serviceInstanceId, String networkId) throws RestException {
        log.info("DELETE | Network Data for ({}, {})", serviceInstanceId, networkId);

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

        if (!serviceDataHelper.hasServiceData(data)) {
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServicedataNetworksNetwork network = null;
        try {
            network = serviceDataHelper.getNetwork(serviceInstanceId, networkId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (network != null) {
            serviceDataHelper.deleteNetwork(serviceInstanceId, networkId);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    /**
     * Get network data from the Config table specified Service Instance and network
     * id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/networks/network/{network-id}/
     * 
     * @param serviceInstanceId the Service Instance ID to perform the get on
     * @param networkId         the network ID to perform the get on
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId, networkId}
     *         does not exist.
     */

    @Override
    public ResponseEntity<GenericResourceApiServicedataServicedataNetworksNetwork> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdGet(
            String serviceInstanceId, String networkId) throws RestException {
        log.info("GET | Network Data for serviceInstanceId, networkId ({})", serviceInstanceId, networkId);

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataNetworksNetwork network = null;
        try {
            network = serviceDataHelper.getNetwork(serviceInstanceId, networkId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (network == null) {
            log.info("No information found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        return new ResponseEntity<>(network, HttpStatus.OK);
    }
     /**
     * Post network data to the Config table specified Service Instance and network
     * id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/networks/network/{network-id}/
     * 
     * @param serviceInstanceId the Service Instance ID to perform the get on
     * @param networkId         the network ID to perform the get on
     * @return HttpStatus.CREATED (201) on successful POST if new network
     *         <p>
     *         HttpStatus.CONFLICT (409) if network already existed
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId, networkId}
     *         does not exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdPost(
            String serviceInstanceId, String networkId,
            @Valid GenericResourceApiServicedataServicedataNetworksNetwork network)
            throws RestException {
        log.info("POST | Network Data for ({}, {})", serviceInstanceId, networkId);

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        ConfigServices data;
        ServiceDataTransaction transaction = serviceDataHelper.createTransaction();

        if ((services == null) || (services.isEmpty())) {
            log.info("Could not find data for ({}).", serviceInstanceId);
            // Or throw the data not found error?
            throw new RestProtocolException("data-missing", "Service Instance ID not found.",
                    HttpStatus.NOT_FOUND.value());
        } else {
            data = services.get(0);
        }

        if (!serviceDataHelper.hasServiceData(data)) {
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }

        if (serviceDataHelper.networkExists(serviceInstanceId, networkId)) {

            log.error("network already exists for svcInstanceId, networkId {}, {}", serviceInstanceId, networkId);
            throw new RestProtocolException("data-exists", "Data already exists for " + serviceInstanceId+","+networkId,
                    HttpStatus.CONFLICT.value());
        }

        try {
            serviceDataHelper.saveNetwork(serviceInstanceId, network, null);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }


        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Put network data to the Config table specified Service Instance and network
     * id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/networks/network/{network-id}/
     * 
     * @param serviceInstanceId the Service Instance ID to perform the get on
     * @param networkId         the network ID to perform the get on
     * @return HttpStatus.CREATED (201) on successful PUT if new network
     *         <p>
     *         HttpStatus.NO_CONTENT(204) on successful PUT if network already existed
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId, networkId}
     *         does not exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdPut(
            String serviceInstanceId, String networkId,
            @Valid GenericResourceApiServicedataServicedataNetworksNetwork network) throws RestException {
        log.info("PUT | Network Data for ({}, {})", serviceInstanceId, networkId);

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        ConfigServices data;
        ServiceDataTransaction transaction = serviceDataHelper.createTransaction();

        if ((services == null) || (services.isEmpty())) {
            log.info("Could not find data for ({}).", serviceInstanceId);
            // Or throw the data not found error?
            throw new RestProtocolException("data-missing", "Service Instance ID not found.",
                    HttpStatus.NOT_FOUND.value());
        } else {
            data = services.get(0);
        }

        if (!serviceDataHelper.hasServiceData(data)) {
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }

        boolean networkAlreadyExisted = serviceDataHelper.networkExists(serviceInstanceId, networkId);

        try {
            serviceDataHelper.saveNetwork(serviceInstanceId, network, null);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (networkAlreadyExisted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

    }

    /**
     * Delete network oper status data from the Config table specified Service
     * Instance and network id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/networks/network/{network-id}/network-data/network-level-oper-status/
     * 
     * @param serviceInstanceId the Service Instance ID to update
     * @param networkId         the network ID to update
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId, networkId}
     *         does not exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdNetworkDataNetworkLevelOperStatusDelete(
            String serviceInstanceId, String networkId) throws RestException {
        log.info("DELETE | Network level oper status Data for ({}, {})", serviceInstanceId, networkId);

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

        if (!serviceDataHelper.hasServiceData(data)) {
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServicedataNetworksNetwork network = null;
        try {
            network = serviceDataHelper.getNetwork(serviceInstanceId, networkId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (network == null) {
            log.info("No information found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        if (network.getNetworkData() != null) {
            network.getNetworkData().setNetworkLevelOperStatus(null);
            try {
                serviceDataHelper.saveNetwork(serviceInstanceId, network, null);
            } catch (JsonProcessingException e) {
                log.error("Could not parse service data", e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Get network oper status data from the Config table specified Service Instance
     * and network id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/networks/network/{network-id}/network-data/network-level-oper-status/
     * 
     * @param serviceInstanceId the Service Instance ID to perform the get on
     * @param networkId         the network ID to perform the get on
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId, networkId}
     *         does not exist.
     */
    @Override
    public ResponseEntity<GenericResourceApiOperStatusData> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdNetworkDataNetworkLevelOperStatusGet(
            String serviceInstanceId, String networkId) throws RestException {
        log.info("GET | Network level oper status Data for serviceInstanceId, networkId ({})", serviceInstanceId,
                networkId);

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataNetworksNetwork network = null;
        try {
            network = serviceDataHelper.getNetwork(serviceInstanceId, networkId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (network == null) {
            log.info("No information found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataNetworksNetworkNetworkData networkData = network.getNetworkData();
        if (networkData == null) {
            log.info("No network-data  found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        return new ResponseEntity<>(networkData.getNetworkLevelOperStatus(), HttpStatus.OK);
    }

    /**
     * Post network oper status data to the Config table specified Service Instance
     * and network id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/networks/network/{network-id}/network-data/network-level-oper-status/
     * 
     * @param serviceInstanceId the Service Instance ID to post to
     * @param networkId         the network ID to post to
     * @param operStatus        the oper status to set
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId, networkId}
     *         does not exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdNetworkDataNetworkLevelOperStatusPost(
            String serviceInstanceId, String networkId, @Valid GenericResourceApiOperStatusData operStatusData)
            throws RestException {
        log.info("POST | Network level oper status Data for ({}, {})", serviceInstanceId, networkId);

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

        if (!serviceDataHelper.hasServiceData(data)) {
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServicedataNetworksNetwork network = null;
        try {
            network = serviceDataHelper.getNetwork(serviceInstanceId, networkId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (network == null) {
            log.info("No information found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataNetworksNetworkNetworkData networkData = network.getNetworkData();
        if (networkData == null) {
            networkData = new GenericResourceApiServicedataServicedataNetworksNetworkNetworkData();
        }

        networkData.setNetworkLevelOperStatus(operStatusData);
        network.setNetworkData(networkData);
        try {
            serviceDataHelper.saveNetwork(serviceInstanceId, network, null);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Put network oper status data from the Config table specified Service Instance
     * and network id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/networks/network/{network-id}/network-data/network-level-oper-status/
     * 
     * @param serviceInstanceId the Service Instance ID to put to
     * @param networkId         the network ID to put to
     * @param operStatus        the oper status to set
     * @return HttpStatus.OK (200) on successful PUT
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId, networkId}
     *         does not exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdNetworkDataNetworkLevelOperStatusPut(
            String serviceInstanceId, String networkId, @Valid GenericResourceApiOperStatusData operStatusData)
            throws RestException {
        log.info("PUT | Network level oper status Data for ({}, {})", serviceInstanceId, networkId);

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

        if (!serviceDataHelper.hasServiceData(data)) {
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServicedataNetworksNetwork network = null;
        try {
            network = serviceDataHelper.getNetwork(serviceInstanceId, networkId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (network == null) {
            log.info("No information found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataNetworksNetworkNetworkData networkData = network.getNetworkData();
        if (networkData == null) {
            networkData = new GenericResourceApiServicedataServicedataNetworksNetworkNetworkData();
        }

        networkData.setNetworkLevelOperStatus(operStatusData);
        network.setNetworkData(networkData);
        try {
            serviceDataHelper.saveNetwork(serviceInstanceId, network, null);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Delete network oper status data from the Config table specified Service
     * Instance and network id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/networks/network/{network-id}/network-data/network-level-oper-status/
     * 
     * @param serviceInstanceId the Service Instance ID to delete
     * @param networkId         the Service Instance ID to delete
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId, networkId}
     *         does not exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdNetworkDataNetworkProvidedAllottedResourcesDelete(
            String serviceInstanceId, String networkId) throws RestException {
        log.info("DELETE | Network provided allotted resource Data for ({}, {})", serviceInstanceId, networkId);

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

        if (!serviceDataHelper.hasServiceData(data)) {
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServicedataNetworksNetwork network = null;
        try {
            network = serviceDataHelper.getNetwork(serviceInstanceId, networkId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (network == null) {
            log.info("No information found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        if (network.getNetworkData() != null) {
            network.getNetworkData().setNetworkProvidedAllottedResources(null);
            try {
                serviceDataHelper.saveNetwork(serviceInstanceId, network, null);
            } catch (JsonProcessingException e) {
                log.error("Could not parse service data", e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<GenericResourceApiServicedataServicedataNetworksNetworkNetworkdataNetworkProvidedAllottedResources> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdNetworkDataNetworkProvidedAllottedResourcesGet(
            String serviceInstanceId, String networkId) throws RestException {
        log.info("GET | Network provided allotted resources for serviceInstanceId, networkId ({})", serviceInstanceId,
                networkId);

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataNetworksNetwork network = null;
        try {
            network = serviceDataHelper.getNetwork(serviceInstanceId, networkId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (network == null) {
            log.info("No information found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataNetworksNetworkNetworkData networkData = network.getNetworkData();
        if (networkData == null) {
            log.info("No network-data  found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        return new ResponseEntity<>(networkData.getNetworkProvidedAllottedResources(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdNetworkDataNetworkProvidedAllottedResourcesPost(
            String serviceInstanceId, String networkId,
            @Valid GenericResourceApiServicedataServicedataNetworksNetworkNetworkdataNetworkProvidedAllottedResources netAllottedResources)
            throws RestException {
        log.info("POST | Network provided allotted resource Data for ({}, {})", serviceInstanceId, networkId);

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

        if (!serviceDataHelper.hasServiceData(data)) {
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServicedataNetworksNetwork network = null;
        try {
            network = serviceDataHelper.getNetwork(serviceInstanceId, networkId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (network == null) {
            log.info("No information found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataNetworksNetworkNetworkData networkData = network.getNetworkData();
        if (networkData == null) {
            networkData = new GenericResourceApiServicedataServicedataNetworksNetworkNetworkData();
        }

        networkData.setNetworkProvidedAllottedResources(netAllottedResources);
        network.setNetworkData(networkData);
        try {
            serviceDataHelper.saveNetwork(serviceInstanceId, network, null);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdNetworkDataNetworkProvidedAllottedResourcesPut(
            String serviceInstanceId, String networkId,
            @Valid GenericResourceApiServicedataServicedataNetworksNetworkNetworkdataNetworkProvidedAllottedResources netAllottedResources)
            throws RestException {
        log.info("POST | Network provided allotted resource Data for ({}, {})", serviceInstanceId, networkId);

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

        if (!serviceDataHelper.hasServiceData(data)) {
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServicedataNetworksNetwork network = null;
        try {
            network = serviceDataHelper.getNetwork(serviceInstanceId, networkId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (network == null) {
            log.info("No information found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataNetworksNetworkNetworkData networkData = network.getNetworkData();
        if (networkData == null) {
            networkData = new GenericResourceApiServicedataServicedataNetworksNetworkNetworkData();
        }

        networkData.setNetworkProvidedAllottedResources(netAllottedResources);
        network.setNetworkData(networkData);
        try {
            serviceDataHelper.saveNetwork(serviceInstanceId, network, null);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Delete network topology identifier structure from service data based on
     * service Instance and network id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/networks/network/{network-id}/network-data/network-topology/network-topology-identifier-structure/
     * 
     * @param serviceInstanceId the Service Instance ID to delete
     * @param networkId         the network ID to delete
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId, networkId}
     *         does not exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdNetworkDataNetworkTopologyNetworkTopologyIdentifierStructureDelete(
            String serviceInstanceId, String networkId) throws RestException {
        log.info("DELETE | Network topology identifier structure for ({}, {})", serviceInstanceId, networkId);

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

        if (!serviceDataHelper.hasServiceData(data)) {
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServicedataNetworksNetwork network = null;
        try {
            network = serviceDataHelper.getNetwork(serviceInstanceId, networkId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (network == null) {
            log.info("No information found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        if (network.getNetworkData() != null) {
            GenericResourceApiNetworktopologyNetworkTopology netTopology = network.getNetworkData()
                    .getNetworkTopology();
            if (netTopology == null) {

                log.info("No network-topology found for {}", networkId);
                throw new RestApplicationException("data-missing",
                        "Request could not be completed because the relevant data model content does not exist",
                        HttpStatus.NOT_FOUND.value());
            }
            netTopology.setNetworkTopologyIdentifierStructure(null);
            try {
                serviceDataHelper.saveNetwork(serviceInstanceId, network, null);
            } catch (JsonProcessingException e) {
                log.error("Could not parse service data", e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Get network topology identifier structure from service data based on service
     * Instance and network id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/networks/network/{network-id}/network-data/network-topology/network-topology-identifier-structure/
     * 
     * @param serviceInstanceId the Service Instance ID to update
     * @param networkId         the network ID to update
     * @return HttpStatus.OK (200) on successful GET
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId, networkId}
     *         does not exist.
     */
    @Override
    public ResponseEntity<GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdNetworkDataNetworkTopologyNetworkTopologyIdentifierStructureGet(
            String serviceInstanceId, String networkId) throws RestException {
        log.info("GET | Network topology identifier structure for ({}, {})", serviceInstanceId, networkId);

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

        if (!serviceDataHelper.hasServiceData(data)) {
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServicedataNetworksNetwork network = null;
        try {
            network = serviceDataHelper.getNetwork(serviceInstanceId, networkId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (network == null) {
            log.info("No information found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        if (network.getNetworkData() != null) {
            GenericResourceApiNetworktopologyNetworkTopology netTopology = network.getNetworkData()
                    .getNetworkTopology();
            if (netTopology == null) {

                log.info("No network-topology found for {}", networkId);
                throw new RestApplicationException("data-missing",
                        "Request could not be completed because the relevant data model content does not exist",
                        HttpStatus.NOT_FOUND.value());
            }
            return new ResponseEntity<>(netTopology.getNetworkTopologyIdentifierStructure(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * Post network topology identifier structure from service data based on service
     * Instance and network id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/networks/network/{network-id}/network-data/network-topology/network-topology-identifier-structure/
     * 
     * @param serviceInstanceId              the Service Instance ID to update
     * @param networkId                      the network ID to update
     * @param netTopologyIdentifierStructure the network topology identifier
     *                                       structure to set
     * @return HttpStatus.CREATED (201) on successful POST
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId, networkId}
     *         does not exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdNetworkDataNetworkTopologyNetworkTopologyIdentifierStructurePost(
            String serviceInstanceId, String networkId,
            @Valid GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure netTopologyIdentifierStructure)
            throws RestException {
        log.info("POST | Network topology identifier structure for ({}, {})", serviceInstanceId, networkId);

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

        if (!serviceDataHelper.hasServiceData(data)) {
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServicedataNetworksNetwork network = null;
        try {
            network = serviceDataHelper.getNetwork(serviceInstanceId, networkId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (network == null) {
            log.info("No information found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        if (network.getNetworkData() != null) {
            GenericResourceApiNetworktopologyNetworkTopology netTopology = network.getNetworkData()
                    .getNetworkTopology();
            if (netTopology == null) {

                log.info("No network-topology found for {}", networkId);
                throw new RestApplicationException("data-missing",
                        "Request could not be completed because the relevant data model content does not exist",
                        HttpStatus.NOT_FOUND.value());
            }
            netTopology.setNetworkTopologyIdentifierStructure(netTopologyIdentifierStructure);
            try {
                serviceDataHelper.saveNetwork(serviceInstanceId, network, null);
            } catch (JsonProcessingException e) {
                log.error("Could not parse service data", e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Put network topology identifier structure from service data based on service
     * Instance and network id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/networks/network/{network-id}/network-data/network-topology/network-topology-identifier-structure/
     * 
     * @param serviceInstanceId              the Service Instance ID to update
     * @param networkId                      the network ID to update
     * @param netTopologyIdentifierStructure the network topology identifier
     *                                       structure to set
     * @return HttpStatus.CREATED (201) on successful POST
     *         <p>
     *         HttpStatus.INTERNAL_SERVER_ERROR (500) if unmarshalling Service Data
     *         from the database fails
     *         <p>
     *         HttpStatus.NOT_FOUND (404) if {@code serviceInstanceId, networkId}
     *         does not exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataNetworksNetworkNetworkIdNetworkDataNetworkTopologyNetworkTopologyIdentifierStructurePut(
            String serviceInstanceId, String networkId,
            @Valid GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure netTopologyIdentifierStructure)
            throws RestException {
        log.info("PUT | Network topology identifier structure for ({}, {})", serviceInstanceId, networkId);

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

        if (!serviceDataHelper.hasServiceData(data)) {
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }
        GenericResourceApiServicedataServicedataNetworksNetwork network = null;
        try {
            network = serviceDataHelper.getNetwork(serviceInstanceId, networkId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (network == null) {
            log.info("No information found for {}", networkId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        if (network.getNetworkData() != null) {
            GenericResourceApiNetworktopologyNetworkTopology netTopology = network.getNetworkData()
                    .getNetworkTopology();
            if (netTopology == null) {

                log.info("No network-topology found for {}", networkId);
                throw new RestApplicationException("data-missing",
                        "Request could not be completed because the relevant data model content does not exist",
                        HttpStatus.NOT_FOUND.value());
            }
            netTopology.setNetworkTopologyIdentifierStructure(netTopologyIdentifierStructure);
            try {
                serviceDataHelper.saveNetwork(serviceInstanceId, network, null);
            } catch (JsonProcessingException e) {
                log.error("Could not parse service data", e);
                throw new RestApplicationException("data-conversion",
                        "Request could not be completed due to internal error", e,
                        HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
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

        if (!serviceDataHelper.hasServiceData(data)) {
            log.info("Could not find Service Data for ({}).", serviceInstanceId);
            throw new RestProtocolException("data-missing", "Service data not found.", HttpStatus.NOT_FOUND.value());
        }

        if (serviceDataHelper.deleteVnf(serviceInstanceId, vnfId) > 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // Data was not found
            log.error("Did not find VNF ({}) in data.", vnfId);
            throw new RestProtocolException("data-missing", "VNF ID not found.", HttpStatus.NOT_FOUND.value());
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

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataVnfsVnf vnf = null;
        try {
            vnf = serviceDataHelper.getVnf(serviceInstanceId, vnfId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (vnf != null) {
            return new ResponseEntity<>(vnf, HttpStatus.OK);
        } else {
            log.info("No information found for {}", vnfId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }
    }

    /**
     * Creates or updates VNF data in the Config table for a specified Service
     * Instance. If it is a new Service Instance or a new VNF, creates all necessary
     * parent data containers, then performs the updates.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/
     * 
     * @param serviceInstanceId                                        the Service
     *                                                                 Instance ID
     *                                                                 to perform
     *                                                                 the delete on
     * @param vnfId                                                    the VNF ID of
     *                                                                 the VNF to
     *                                                                 delete
     * @param genericResourceApiServicedataServicedataVnfsVnfBodyParam the playload
     * @return HttpStatus.CREATED (201) on successful create
     *         <p>
     *         HttpStatus.NO_CONTENT (204) on successful update
     *         <p>
     *         HttpStatus.BAD_REQUEST (400) if {@code vnfId} does not match what is
     *         specified in the
     *         {@code genericResourceApiServicedataServicedataVnfsVnfBodyParam} , or
     *         if updating the database fails.
     * @throws RestException
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdPut(
            String serviceInstanceId, String vnfId, GenericResourceApiServicedataServicedataVnfsVnf vnf)
            throws RestException {
        log.info("PUT | VNF Data for ({})", vnfId);
        if (!vnfId.equals(vnf.getVnfId())) {
            throw new RestProtocolException("bad-attribute", "vnf-id mismatch", HttpStatus.BAD_REQUEST.value());
        }

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        ConfigServices data;
        ServiceDataTransaction transaction = serviceDataHelper.createTransaction();
        if ((services == null) || (services.isEmpty())) {
            log.info("Could not find data for ({}). Creating new Service Object.", serviceInstanceId);
            data = new ConfigServices();
            data.setSvcInstanceId(serviceInstanceId);
            transaction.save(data);
        } else {
            data = services.get(0);
        }

        try {
            HttpStatus responseStatus = HttpStatus.NO_CONTENT;
            if (serviceDataHelper.getVnf(serviceInstanceId, vnfId) == null) {
                responseStatus = HttpStatus.CREATED;
            }
            serviceDataHelper.saveVnf(serviceInstanceId, vnf, transaction);
            transaction.commit();
            return new ResponseEntity<>(responseStatus);
        } catch (JsonProcessingException e) {
            log.error("Error saving vnf", e);
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

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataVnfsVnf vnf = null;

        try {
            vnf = serviceDataHelper.getVnf(serviceInstanceId, vnfId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        // Drill down to find the data
        if (vnf != null && vnf.getVnfData() != null && vnf.getVnfData().getVnfTopology() != null) {
            return new ResponseEntity<>(vnf.getVnfData().getVnfTopology(), HttpStatus.OK);
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
     * @param serviceInstanceId the Service Instance ID to perform the delete on
     * @param vnfId             the VNF ID of the VNF to delete
     * @param operStatus        the payload
     * @return HttpStatus.CREATED (201) on successful create.
     *         <p>
     *         HttpStatus.NO_CONTENT (204) on successful update.
     *         <p>
     *         HttpStatus.BAD_REQUEST (400) if updating the database fails.
     * @throws RestException
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVnfLevelOperStatusPut(
            String serviceInstanceId, String vnfId, GenericResourceApiOperStatusData operStatus) throws RestException {
        log.info("PUT | VNF Level Oper Status ({})", vnfId);
        ServiceDataTransaction transaction = serviceDataHelper.createTransaction();

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        ConfigServices data;
        if ((services == null) || (services.isEmpty())) {
            log.info("Could not find data for ({}). Creating new Service Object.", serviceInstanceId);
            data = new ConfigServices();
            data.setSvcInstanceId(serviceInstanceId);
            transaction.save(data);
        } else {
            data = services.get(0);
        }

        HttpStatus responseStatus = HttpStatus.NO_CONTENT;

        try {
            GenericResourceApiServicedataServicedataVnfsVnf vnf = serviceDataHelper.getVnf(serviceInstanceId, vnfId);
            if (vnf == null) {
                responseStatus = HttpStatus.CREATED;
                vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
                vnf.setVnfId(vnfId);
            }
            GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfData = vnf.getVnfData();
            if (vnfData == null) {
                vnfData = new GenericResourceApiServicedataServicedataVnfsVnfVnfData();
            }
            vnfData.setVnfLevelOperStatus(operStatus);
            vnf.setVnfData(vnfData);
            serviceDataHelper.saveVnf(serviceInstanceId, vnf, transaction);
            transaction.commit();
            return new ResponseEntity<>(responseStatus);
        } catch (JsonProcessingException e) {
            log.error("Error saving vnf", e);
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
     * @param serviceInstanceId the Service Instance ID to perform the delete on
     * @param vnfId             the VNF ID of the VNF to delete
     * @param onapModelInfo     the payload
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
            GenericResourceApiOnapmodelinformationOnapModelInformation onapModelInfo) throws RestException {
        log.info("PUT | VNF Topology Onap Model Information ({})", vnfId);
        ServiceDataTransaction transaction = serviceDataHelper.createTransaction();

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        ConfigServices data;
        if ((services == null) || (services.isEmpty())) {
            log.info("Could not find data for ({}). Creating new Service Object.", serviceInstanceId);
            data = new ConfigServices();
            data.setSvcInstanceId(serviceInstanceId);
            transaction.save(data);
        } else {
            data = services.get(0);
        }

        GenericResourceApiServicedataServicedataVnfsVnf vnf = null;
        HttpStatus responseStatus = HttpStatus.NO_CONTENT;

        try {
            vnf = serviceDataHelper.getVnf(serviceInstanceId, vnfId);
        } catch (JsonProcessingException e) {

            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (vnf == null) {
            responseStatus = HttpStatus.CREATED;
            vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
            vnf.setVnfId(vnfId);
            vnf.setVnfData(new GenericResourceApiServicedataServicedataVnfsVnfVnfData());
            vnf.getVnfData().setVnfTopology(new GenericResourceApiVnftopologyVnfTopology());
        }

        vnf.getVnfData().getVnfTopology().setOnapModelInformation(onapModelInfo);

        // Save the updated VNF (and service, if new)
        try {
            serviceDataHelper.saveVnf(serviceInstanceId, vnf, transaction);
            transaction.commit();
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
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/vnf-data/vnf-topology/vnf-resource-assignments/vnf-networks/
     * 
     * @param serviceInstanceId the Service Instance ID to perform the delete on
     * @param vnfId             the VNF ID of the VNF to delete
     * @param vnfNetworks       the * payload
     * @return HttpStatus.CREATED (201) on successful create.
     *         <p>
     *         HttpStatus.NO_CONTENT (204) on successful update.
     *         <p>
     *         HttpStatus.BAD_REQUEST (400) if updating the database fails.
     * @throws RestException
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVnfTopologyVnfResourceAssignmentsVnfNetworksPut(
            String serviceInstanceId, String vnfId,
            GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks vnfNetworks)
            throws RestException {
        log.info("PUT | VNF Topology VNF Resource Assignments VNF Networks ({})", vnfId);

        ServiceDataTransaction transaction = serviceDataHelper.createTransaction();
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        ConfigServices data;
        if ((services == null) || (services.isEmpty())) {
            log.info("Could not find data for ({}). Creating new Service Object.", serviceInstanceId);
            data = new ConfigServices();
            data.setSvcInstanceId(serviceInstanceId);
            transaction.save(data);
        } else {
            data = services.get(0);
        }

        HttpStatus responseStatus = HttpStatus.NO_CONTENT;
        GenericResourceApiServicedataServicedataVnfsVnf vnf = null;

        try {
            vnf = serviceDataHelper.getVnf(serviceInstanceId, vnfId);
        } catch (JsonProcessingException e) {

            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (vnf == null) {
            log.info("Creating VNF data for ({})", vnfId);
            vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
            vnf.setVnfId(vnfId);
            responseStatus = HttpStatus.CREATED;
        }
        GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfData = vnf.getVnfData();
        if (vnfData == null) {
            vnfData = new GenericResourceApiServicedataServicedataVnfsVnfVnfData();
        }

        GenericResourceApiVnftopologyVnfTopology vnfTopology = vnfData.getVnfTopology();
        if (vnfTopology == null) {
            vnfTopology = new GenericResourceApiVnftopologyVnfTopology();
        }

        GenericResourceApiVnfresourceassignmentsVnfResourceAssignments vnfResourceAssignments = vnfTopology
                .getVnfResourceAssignments();
        if (vnfResourceAssignments == null) {
            vnfResourceAssignments = new GenericResourceApiVnfresourceassignmentsVnfResourceAssignments();
        }
        vnfResourceAssignments.setVnfNetworks(vnfNetworks);
        vnfTopology.setVnfResourceAssignments(vnfResourceAssignments);
        vnfData.setVnfTopology(vnfTopology);
        vnf.setVnfData(vnfData);

        // Map and save the new data
        try {
            serviceDataHelper.saveVnf(serviceInstanceId, vnf, transaction);
            transaction.commit();
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
     * @param serviceInstanceId the Service Instance ID to perform the delete on
     * @param vnfId             the VNF ID of the VNF to delete
     * @param vnfNetwork        the payload
     * @return HttpStatus.CREATED (201) on successful create.
     *         <p>
     *         HttpStatus.NO_CONTENT (204) on successful update.
     *         <p>
     *         HttpStatus.BAD_REQUEST (400) if updating the database fails.
     * @throws RestException
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVnfTopologyVnfResourceAssignmentsVnfNetworksVnfNetworkNetworkRolePut(
            String serviceInstanceId, String vnfId, String networkRole, GenericResourceApiVnfNetworkData vnfNetwork)
            throws RestException {
        log.info("PUT | VNF Network Network Role ({})", vnfId);
        if (!networkRole.equals(vnfNetwork.getNetworkRole())) {
            throw new RestProtocolException("bad-attribute", "network-role mismatch", HttpStatus.BAD_REQUEST.value());
        }

        ServiceDataTransaction transaction = serviceDataHelper.createTransaction();

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        ConfigServices data;
        if ((services == null) || (services.isEmpty())) {
            log.info("Could not find data for ({}). Creating new Service Object.", serviceInstanceId);
            data = new ConfigServices();
            data.setSvcInstanceId(serviceInstanceId);
            transaction.save(data);
        } else {
            data = services.get(0);
        }

        HttpStatus responseStatus = HttpStatus.NO_CONTENT;
        GenericResourceApiServicedataServicedataVnfsVnf vnf = null;

        try {
            vnf = serviceDataHelper.getVnf(serviceInstanceId, vnfId);
        } catch (JsonProcessingException e) {

            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (vnf == null) {
            log.info("Creating VNF data for ({})", vnfId);
            vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
            vnf.setVnfId(vnfId);
            responseStatus = HttpStatus.CREATED;
        }
        GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfData = vnf.getVnfData();
        if (vnfData == null) {
            vnfData = new GenericResourceApiServicedataServicedataVnfsVnfVnfData();
        }

        GenericResourceApiVnftopologyVnfTopology vnfTopology = vnfData.getVnfTopology();
        if (vnfTopology == null) {
            vnfTopology = new GenericResourceApiVnftopologyVnfTopology();
        }

        GenericResourceApiVnfresourceassignmentsVnfResourceAssignments vnfResourceAssignments = vnfTopology
                .getVnfResourceAssignments();
        if (vnfResourceAssignments == null) {
            vnfResourceAssignments = new GenericResourceApiVnfresourceassignmentsVnfResourceAssignments();
        }

        GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks vnfNetworks = vnfResourceAssignments
                .getVnfNetworks();
        if (vnfNetworks == null) {
            vnfNetworks = new GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks();
            vnfNetworks.setVnfNetwork(new ArrayList<>());
        }

        if (vnfNetworks.getVnfNetwork().isEmpty()) {
            vnfNetworks.addVnfNetworkItem(vnfNetwork);
        } else {
            GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks vnfNetworksNew = new GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks();
            vnfNetworksNew.setVnfNetwork(new ArrayList<>());
            vnfNetworks.getVnfNetwork().stream()
                    .filter(targetNetwork -> !targetNetwork.getNetworkRole().equals(networkRole))
                    .forEach(vnfNetworksNew::addVnfNetworkItem);

            vnfNetworksNew.addVnfNetworkItem(vnfNetwork);
            vnfNetworks = vnfNetworksNew;
        }

        vnfResourceAssignments.setVnfNetworks(vnfNetworks);
        vnfTopology.setVnfResourceAssignments(vnfResourceAssignments);
        vnfData.setVnfTopology(vnfTopology);
        vnf.setVnfData(vnfData);
        try {
            serviceDataHelper.saveVnf(serviceInstanceId, vnf, transaction);
            transaction.commit();
            return new ResponseEntity<>(responseStatus);
        } catch (JsonProcessingException e) {
            log.error("Error mapping object to JSON", e);
            // Should probably be a 500 INTERNAL_SERVICE_ERROR
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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

        ConfigServices service = services.get(0);

        try {
            if ((service.getSvcData() == null) || service.getSvcData().isEmpty()) {
                throw new RestProtocolException("data-missing", "No service-data entry found",
                        HttpStatus.NOT_FOUND.value());
            } else {
                serviceData = serviceDataHelper.getServiceData(service);
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
     * Extracts VF MODULE data from CONFIG_GRA_SERVICES for a given,
     * service-instance-id, vnf-id, and vf-module-id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/vnf-data/vf-modules/vf-module/{vf-module-id}/
     * 
     * @param serviceInstanceId the Service Instance ID to lookup data for
     * @param vnfId             the VNF ID of the VNF to return
     * @param vfModuleId        the vf-moudle ID of a specific VNF to return
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    @Override
    public ResponseEntity<GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVfModulesVfModuleVfModuleIdGet(
            String serviceInstanceId, String vnfId, String vfModuleId) throws RestException {

        log.info("GET | Vf Module Data for ({})", vfModuleId);

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule vfModule;
        try {
            vfModule = serviceDataHelper.getVfModule(serviceInstanceId, vnfId, vfModuleId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (vfModule != null) {
            return new ResponseEntity<>(vfModule, HttpStatus.OK);
        } else {
            log.info("No vf-module found for [{}]", vfModuleId);
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }
    }

    /**
     * PUT VF MODULE data into CONFIG_GRA_SERVICES of a given, service-instance-id,
     * vnf-id
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/vnf-data/vf-modules/vf-module/{vf-module-id}/
     * 
     * @param serviceInstanceId the Service Instance ID
     * @param vnfId             the VNF ID as the parent of the specified
     *                          vf-module-id and child of the specified
     *                          service-instance
     * @param vfModuleId        the vf-module ID as a child of the specified VNF
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVfModulesVfModuleVfModuleIdPut(
            String serviceInstanceId, String vnfId, String vfModuleId,
            GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule genericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModuleBodyParam)
            throws RestException {
        log.info("PUT | vf-module Data of ({}) for vnf ({}) in service({})", vfModuleId, vnfId, serviceInstanceId);

        if (!vfModuleId.equals(
                genericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModuleBodyParam.getVfModuleId())) {
            throw new RestProtocolException("bad-attribute", "vf-module-id mismatch", HttpStatus.BAD_REQUEST.value());
        }

        HttpStatus responseStatus = HttpStatus.NO_CONTENT;
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if ((services == null) || (services.isEmpty())) {
            log.error("service-instance-id ({}) not found in SDN.", serviceInstanceId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        GenericResourceApiServicedataServicedataVnfsVnf vnf;
        try {
            vnf = serviceDataHelper.getVnf(serviceInstanceId, vnfId);
        } catch (JsonProcessingException e1) {
            log.error("Could not parse service data", e1);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e1,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (vnf == null) {
            log.error("vnf-id ({}) not found in SDN.", vnfId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // See if this is the first vf module being added
        List<ConfigVfModules> configVfModules = configVfModulesRepository.findBySvcInstanceIdAndVnfId(serviceInstanceId,
                vnfId);

        if ((configVfModules == null) || configVfModules.isEmpty()) {
            responseStatus = HttpStatus.CREATED;
        }

        // Add vf module
        try {
            serviceDataHelper.saveVfModule(serviceInstanceId, vnfId,
                    genericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModuleBodyParam, null);
            return new ResponseEntity<>(responseStatus);
        } catch (JsonProcessingException e) {
            log.error("Error mapping object to JSON", e);
            // Should probably be a 500 INTERNAL_SERVICE_ERROR
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Extracts VF MODULE Topology data from the Config table specified Service
     * Instance and VNF ID.
     * <p>
     * Maps to
     * /config/GENERIC-RESOURCE-API:services/service/{service-instance-id}/service-data/vnfs/vnf/{vnf-id}/vnf-data/vf-modules/vf-module/{vf-module-id}/vf-module-data/vf-module-topology/
     * 
     * @param serviceInstanceId the Service Instance ID to lookup data for
     * @param vnfId             the VNF ID of the VNF to extract topology data from.
     * @param vfModuleId        the vf-module-idof the vf-module to extract topology
     *                          data from.
     * @return HttpStatus.OK (200) if the data is found.
     * @throws RestException if the data does not exist.
     */
    @Override
    public ResponseEntity<GenericResourceApiVfmoduletopologyVfModuleTopology> configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVfModulesVfModuleVfModuleIdVfModuleDataVfModuleTopologyGet(
            String serviceInstanceId, String vnfId, String vfModuleId) throws RestException {
        log.info("GET | vf-module-topology for ({})", vfModuleId);

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestApplicationException("data-missing",
                    "Request could not be completed because the relevant data model content does not exist",
                    HttpStatus.NOT_FOUND.value());
        }

        GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule vfModule;
        try {
            vfModule = serviceDataHelper.getVfModule(serviceInstanceId, vnfId, vfModuleId);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion",
                    "Request could not be completed due to internal error", e,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if (vfModule != null && vfModule.getVfModuleData() != null
                && vfModule.getVfModuleData().getVfModuleTopology() != null) {
            return new ResponseEntity<>(vfModule.getVfModuleData().getVfModuleTopology(), HttpStatus.OK);
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

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if ((services == null) || (services.isEmpty())) {
            log.error("service-instance-id ({}) not found in SDN.", serviceInstanceId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        serviceDataHelper.deleteVfModule(serviceInstanceId, vnfId, vfModuleId);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
