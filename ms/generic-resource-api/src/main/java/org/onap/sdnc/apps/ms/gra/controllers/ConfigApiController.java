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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.ccsdk.apps.services.RestApplicationException;
import org.onap.ccsdk.apps.services.RestException;
import org.onap.ccsdk.apps.services.RestProtocolError;
import org.onap.ccsdk.apps.services.RestProtocolException;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadData;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigServices;
import org.onap.sdnc.apps.ms.gra.data.ConfigServicesRepository;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@ComponentScan(basePackages = {"org.onap.sdnc.apps.ms.gra.*"})
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

    @Override
    public ResponseEntity<GenericResourceApiPreloadModelInformation> configGENERICRESOURCEAPIpreloadInformationGet() throws RestApplicationException {
        GenericResourceApiPreloadModelInformation genericResourceApiPreloadModelInformation = new GenericResourceApiPreloadModelInformation();

        if (configPreloadDataRepository.count() == 0) {
            throw new RestApplicationException("data-missing", "Request could not be completed because the relevant data model content does not exist", HttpStatus.NOT_FOUND.value());
        }

        for (ConfigPreloadData configPreloadData : configPreloadDataRepository.findAll()) {
            GenericResourceApiPreloadmodelinformationPreloadList preloadListItem = new GenericResourceApiPreloadmodelinformationPreloadList();

            preloadListItem.setPreloadId(configPreloadData.getPreloadId());
            preloadListItem.setPreloadType(configPreloadData.getPreloadType());
            try {
                preloadListItem.setPreloadData(objectMapper.readValue(configPreloadData.getPreloadData(), GenericResourceApiPreloaddataPreloadData.class));
            } catch (JsonProcessingException e) {
                log.error("Could not convert preload data", e);
                throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
            genericResourceApiPreloadModelInformation.addPreloadListItem(preloadListItem);
        }


        return new ResponseEntity<>(genericResourceApiPreloadModelInformation, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationPost(@Valid GenericResourceApiPreloadModelInformation graPreloadModelInfo) throws RestApplicationException, RestProtocolException {

        List<GenericResourceApiPreloadmodelinformationPreloadList> preloadList = graPreloadModelInfo.getPreloadList();
        List<ConfigPreloadData> newPreloadData = new LinkedList<>();

        if (preloadList != null) {
            // Verification pass - if any items already exist, return an error
            for (GenericResourceApiPreloadmodelinformationPreloadList curItem : preloadList) {

                List<ConfigPreloadData> curPreloadData = configPreloadDataRepository.findByPreloadIdAndPreloadType(curItem.getPreloadId(), curItem.getPreloadType());
                if ((curPreloadData != null) && (!curPreloadData.isEmpty())) {
                    log.error("Preload data already exists for {}:{}", curItem.getPreloadId(), curItem.getPreloadType());
                    throw new RestProtocolException("data-exists", "Data already exists for " + curItem.getPreloadId() + ":" + curItem.getPreloadType(), HttpStatus.CONFLICT.value());
                } else {
                    try {
                        newPreloadData.add(new ConfigPreloadData(curItem.getPreloadId(), curItem.getPreloadType(), objectMapper.writeValueAsString(curItem.getPreloadData())));
                    } catch (JsonProcessingException e) {
                        log.error("Cannot convert preload data");
                        throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());

                    }
                }
            }

            // Update pass
            for (ConfigPreloadData newDataItem : newPreloadData) {
                log.info("Adding preload data for {}:{}", newDataItem.getPreloadId(), newDataItem.getPreloadType());
                configPreloadDataRepository.save(newDataItem);
            }
        } else {
            throw new RestProtocolException("data-missing", "No preload-list entries found to add", HttpStatus.CONFLICT.value());
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationPut(@Valid GenericResourceApiPreloadModelInformation graPreloadModelInfo) throws RestApplicationException {

        boolean addedNew = false;
        List<GenericResourceApiPreloadmodelinformationPreloadList> preloadList = graPreloadModelInfo.getPreloadList();

        if (preloadList != null) {
            Iterator<GenericResourceApiPreloadmodelinformationPreloadList> iter = preloadList.iterator();
            while (iter.hasNext()) {
                GenericResourceApiPreloadmodelinformationPreloadList curItem = iter.next();
                List<ConfigPreloadData> curPreloadData = configPreloadDataRepository.findByPreloadIdAndPreloadType(curItem.getPreloadId(), curItem.getPreloadType());
                if ((curPreloadData == null) || curPreloadData.isEmpty()) {
                    addedNew = true;
                }

                try {
                    configPreloadDataRepository.save(new ConfigPreloadData(curItem.getPreloadId(), curItem.getPreloadType(), objectMapper.writeValueAsString(curItem.getPreloadData())));
                } catch (JsonProcessingException e) {
                    log.error("Cannot convert preload data", e);
                    throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());

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
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPost(@Valid GenericResourceApiPreloadmodelinformationPreloadList preloadListItem) throws RestProtocolException {

        throw new RestProtocolException("data-missing", "Missing key for list \"preload-list\"", HttpStatus.NOT_FOUND.value());
    }


    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeDelete(String preloadId, String preloadType) {
        configPreloadDataRepository.deleteByPreloadIdAndPreloadType(preloadId, preloadType);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<GenericResourceApiPreloadmodelinformationPreloadList> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGet(String preloadId, String preloadType) throws RestApplicationException {
        List<ConfigPreloadData> preloadData = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);
        if (preloadData != null) {
            if (!preloadData.isEmpty()) {
                ConfigPreloadData preloadDataItem = preloadData.get(0);
                GenericResourceApiPreloadmodelinformationPreloadList preloadDataList = new GenericResourceApiPreloadmodelinformationPreloadList();
                preloadDataList.setPreloadId(preloadDataItem.getPreloadId());
                preloadDataList.setPreloadType(preloadDataItem.getPreloadType());
                try {
                    preloadDataList.setPreloadData(objectMapper.readValue(preloadDataItem.getPreloadData(), GenericResourceApiPreloaddataPreloadData.class));
                } catch (JsonProcessingException e) {
                    log.error("Cannot convert preload data", e);
                    throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
                return new ResponseEntity<>(preloadDataList, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypePost(String preloadId, String preloadType, @Valid GenericResourceApiPreloadmodelinformationPreloadList preloadListItem) throws RestApplicationException, RestProtocolException {
        List<ConfigPreloadData> preloadDataItems = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);

        if ((preloadDataItems != null) && !preloadDataItems.isEmpty()) {
            log.error("Preload data already exists for {}:{}", preloadId, preloadType);
            throw new RestProtocolException("data-exists", "Data already exists for " + preloadId + ":" + preloadType, HttpStatus.CONFLICT.value());
        }

        try {
            log.info("Adding preload data for {}:{}", preloadId, preloadType);
            configPreloadDataRepository.save(new ConfigPreloadData(preloadId, preloadType, objectMapper.writeValueAsString(preloadListItem.getPreloadData())));
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypePut(String preloadId, String preloadType, @Valid GenericResourceApiPreloadmodelinformationPreloadList preloadListItem) throws RestApplicationException, RestProtocolException {
        List<ConfigPreloadData> preloadDataItems = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);
        boolean dataExists = false;
        if ((preloadDataItems != null) && !preloadDataItems.isEmpty()) {
            dataExists = true;
        }

        if ((preloadListItem.getPreloadId() == null) ||
                (preloadListItem.getPreloadType() == null) ||
                (preloadListItem.getPreloadData() == null)) {
            log.error("Invalid list item received: {}", preloadListItem);
            throw new RestProtocolException("bad-attribute", "Invalid data received", HttpStatus.BAD_REQUEST.value());
        }

        try {
            if (dataExists) {
                log.info("Updating preload data for {}:{} -> {}", preloadId, preloadType, objectMapper.writeValueAsString(preloadListItem));

            } else {
                log.info("Adding preload data for {}:{}", preloadId, preloadType);
            }

            configPreloadDataRepository.save(new ConfigPreloadData(preloadId, preloadType, objectMapper.writeValueAsString(preloadListItem.getPreloadData())));
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());

        }

        if (dataExists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }


    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataDelete(String preloadId, String preloadType) throws RestProtocolException {
        List<ConfigPreloadData> preloadData = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);

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
    public ResponseEntity<GenericResourceApiPreloaddataPreloadData> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataGet(String preloadId, String preloadType) throws RestApplicationException, RestProtocolException {
        List<ConfigPreloadData> preloadData = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);

        if ((preloadData == null) || preloadData.isEmpty()) {
            throw new RestProtocolException("data-missing", "No preload entry found", HttpStatus.NOT_FOUND.value());
        }

        ConfigPreloadData preloadDataItem = preloadData.get(0);

        if (preloadDataItem.getPreloadData() == null) {
            throw new RestProtocolException("data-missing", "No preload-data found", HttpStatus.NOT_FOUND.value());
        }
        try {
            return new ResponseEntity<>(objectMapper.readValue(preloadDataItem.getPreloadData(), GenericResourceApiPreloaddataPreloadData.class), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataPost(String preloadId, String preloadType, @Valid GenericResourceApiPreloaddataPreloadData preloadData) throws RestApplicationException, RestProtocolException {
        List<ConfigPreloadData> preloadDataEntries = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);

        List<ConfigPreloadData> preloadDataItems = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);
        if ((preloadDataItems == null) || (preloadDataItems.isEmpty())) {
            throw new RestProtocolException("data-missing", "No preload entry found", HttpStatus.NOT_FOUND.value());
        }

        if ((preloadData == null) ||
                (preloadData.getPreloadNetworkTopologyInformation() == null)) {
            throw new RestProtocolException("bad-attribute", "Invalid preloadData received", HttpStatus.BAD_REQUEST.value());
        }

        ConfigPreloadData preloadDataItem = preloadDataItems.get(0);

        if (preloadDataItem.getPreloadData() != null) {
            log.error("Preload data already exists for {}:{} ", preloadId, preloadType);
            throw new RestProtocolException("data-exists", "Data already exists for " + preloadId + ":" + preloadType, HttpStatus.CONFLICT.value());
        }

        try {
            preloadDataItem.setPreloadData(objectMapper.writeValueAsString(preloadData));
            configPreloadDataRepository.save(preloadDataItem);
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataPut(String preloadId, String preloadType, @Valid GenericResourceApiPreloaddataPreloadData preloadData) throws RestApplicationException, RestProtocolException {
        boolean dataExists = false;
        List<ConfigPreloadData> preloadDataItems = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);
        if ((preloadDataItems == null) || (preloadDataItems.isEmpty())) {
            throw new RestProtocolException("data-missing", "No preload entry found", HttpStatus.NOT_FOUND.value());
        }

        if ((preloadData == null) ||
                (preloadData.getPreloadNetworkTopologyInformation() == null)) {
            throw new RestProtocolException("bad-attribute", "Invalid preloadData received", HttpStatus.BAD_REQUEST.value());
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
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
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
    public ResponseEntity<GenericResourceApiServiceModelInfrastructure> configGENERICRESOURCEAPIservicesGet() throws RestApplicationException {
        GenericResourceApiServiceModelInfrastructure modelInfrastructure = new GenericResourceApiServiceModelInfrastructure();

        if (configServicesRepository.count() == 0)  {
            throw new RestApplicationException("data-missing", "Request could not be completed because the relevant data model content does not exist", HttpStatus.NOT_FOUND.value());
        }

        for (ConfigServices service : configServicesRepository.findAll()) {
            GenericResourceApiServicemodelinfrastructureService serviceItem = new GenericResourceApiServicemodelinfrastructureService();
            serviceItem.setServiceInstanceId(service.getSvcInstanceId());
            if (service.getSvcData() != null) {
            try {
                serviceItem.setServiceData(objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class));
            } catch (JsonProcessingException e) {
                log.error("Could not deserialize service data for {}", service.getSvcInstanceId(), e);
                throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());

            }
        }
            serviceItem.setServiceStatus(service.getServiceStatus());
            modelInfrastructure.addServiceItem(serviceItem);
        }


        return new ResponseEntity<>(modelInfrastructure, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesPost(@Valid GenericResourceApiServiceModelInfrastructure modelInfrastructure) throws RestApplicationException, RestProtocolException {
        List<ConfigServices> newServices = new LinkedList<>();

        for (GenericResourceApiServicemodelinfrastructureService serviceItem : modelInfrastructure.getService()) {
            String svcInstanceId = serviceItem.getServiceInstanceId();
            List<ConfigServices> existingService = configServicesRepository.findBySvcInstanceId(svcInstanceId);
            if ((existingService != null) && !existingService.isEmpty()) {
                log.error("Service data already exists for {}", svcInstanceId);
                throw new RestProtocolException("data-exists", "Data already exists for service-instance-id " + svcInstanceId, HttpStatus.CONFLICT.value());
            }
            ConfigServices service = new ConfigServices();
            service.setSvcInstanceId(svcInstanceId);
            try {
                service.setSvcData(objectMapper.writeValueAsString(serviceItem.getServiceData()));
            } catch (JsonProcessingException e) {
                log.error("Could not serialize service data for {}", service.getSvcInstanceId(), e);
                throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());

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
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesPut(@Valid GenericResourceApiServiceModelInfrastructure modelInfrastructure) throws RestApplicationException {

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
                throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());

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
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIservicePost(@Valid GenericResourceApiServicemodelinfrastructureService servicesData) throws RestApplicationException {
        String svcInstanceId = servicesData.getServiceInstanceId();
        try {
            String svcData = objectMapper.writeValueAsString(servicesData.getServiceData());
            ConfigServices configService = new ConfigServices(svcInstanceId, svcData, servicesData.getServiceStatus());
            configServicesRepository.deleteBySvcInstanceId(svcInstanceId);
            configServicesRepository.save(configService);
        } catch (JsonProcessingException e) {
            log.error("Cannot convert service data", e);
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdDelete(String serviceInstanceId) {
        configServicesRepository.deleteBySvcInstanceId(serviceInstanceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<GenericResourceApiServicemodelinfrastructureService> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGet(String serviceInstanceId) throws RestApplicationException {
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
                retval.setServiceData(objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class));
            } catch (JsonProcessingException e) {
                log.error("Could not deserialize service data for service instance id {}", serviceInstanceId, e);
                throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());

            }
        }


        return new ResponseEntity<>(retval, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdPost(String svcInstanceId, @Valid GenericResourceApiServicemodelinfrastructureService newService) throws RestApplicationException, RestProtocolException {

        List<ConfigServices> existingService = configServicesRepository.findBySvcInstanceId(svcInstanceId);
        if ((existingService != null) && !existingService.isEmpty()) {
            log.error("Service data already exists for {}", svcInstanceId);
            throw new RestProtocolException("data-exists", "Data already exists for service-instance-id " + svcInstanceId, HttpStatus.CONFLICT.value());
        }
        ConfigServices service = new ConfigServices();
        service.setSvcInstanceId(svcInstanceId);
        try {
            service.setSvcData(objectMapper.writeValueAsString(newService.getServiceData()));
        } catch (JsonProcessingException e) {
            log.error("Could not serialize service data for {}", service.getSvcInstanceId(), e);
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
        service.setServiceStatus(newService.getServiceStatus());
        configServicesRepository.save(service);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdPut(String serviceInstanceId, @Valid GenericResourceApiServicemodelinfrastructureService newService) throws RestApplicationException {

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
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());

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
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataDelete(String serviceInstanceId) throws RestProtocolException {
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
    public ResponseEntity<GenericResourceApiServicedataServiceData> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataGet(String serviceInstanceId) throws RestApplicationException, RestProtocolException {
        GenericResourceApiServicedataServiceData serviceData = null;

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        try {
            serviceData = objectMapper.readValue(services.get(0).getSvcData(), GenericResourceApiServicedataServiceData.class);
            return new ResponseEntity<>(serviceData, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("Could not parse service data", e);
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataPost(String serviceInstanceId, @Valid GenericResourceApiServicedataServiceData serviceData) throws RestApplicationException, RestProtocolException {
        ConfigServices service;
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        if ((serviceData == null) ||
                (serviceData.getServiceInformation() == null)) {
            throw new RestProtocolException("bad-attribute", "Invalid service-data received", HttpStatus.BAD_REQUEST.value());

        }
        service = services.get(0);

        if ((service.getSvcData() != null) && (service.getSvcData().length() > 0)){
            log.error("service-data already exists for svcInstanceId {}", serviceInstanceId);
            throw new RestProtocolException("data-exists", "Data already exists for " + serviceInstanceId, HttpStatus.CONFLICT.value());
        }


        try {
            service.setSvcData(objectMapper.writeValueAsString(serviceData));
            configServicesRepository.save(service);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize service data for svc instance id {}", serviceInstanceId, e);
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }


        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataPut(String serviceInstanceId, @Valid GenericResourceApiServicedataServiceData serviceData) throws RestApplicationException, RestProtocolException {
        ConfigServices service;
        boolean dataExists = false;

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        if ((serviceData == null) ||
                (serviceData.getServiceInformation() == null)) {
            throw new RestProtocolException("bad-attribute", "Invalid service-data received", HttpStatus.BAD_REQUEST.value());

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
            throw new RestApplicationException("data-conversion", "Request could not be completed due to internal error", e, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (dataExists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusDelete(String serviceInstanceId) throws RestProtocolException {
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
    public ResponseEntity<GenericResourceApiServicestatusServiceStatus> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusGet(String serviceInstanceId) throws RestApplicationException, RestProtocolException {
        GenericResourceApiServicestatusServiceStatus serviceStatus = null;

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        serviceStatus = services.get(0).getServiceStatus();
        return new ResponseEntity<>(serviceStatus, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusPost(String serviceInstanceId, @Valid GenericResourceApiServicestatusServiceStatus serviceStatus) throws RestProtocolException {
        ConfigServices service;
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        if ((serviceStatus == null) ||
                (serviceStatus.getAction() == null)) {
            throw new RestProtocolException("bad-attribute", "Invalid service-status received", HttpStatus.BAD_REQUEST.value());

        }
        service = services.get(0);

        if (service.getServiceStatus() != null) {
            log.error("service-status already exists for svcInstanceId {}", serviceInstanceId);
            throw new RestProtocolException("data-exists", "Data already exists for " + serviceInstanceId, HttpStatus.CONFLICT.value());
        }


        service.setServiceStatus(serviceStatus);
        configServicesRepository.save(service);


        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusPut(String serviceInstanceId, @Valid GenericResourceApiServicestatusServiceStatus serviceStatus) throws RestProtocolException {
        ConfigServices service;
        boolean dataExists = false;

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if ((services == null) || (services.isEmpty())) {
            throw new RestProtocolException("data-missing", "No service entry found", HttpStatus.NOT_FOUND.value());
        }

        if ((serviceStatus == null) ||
                (serviceStatus.getAction() == null)) {
            throw new RestProtocolException("bad-attribute", "Invalid service-status received", HttpStatus.BAD_REQUEST.value());

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

}
