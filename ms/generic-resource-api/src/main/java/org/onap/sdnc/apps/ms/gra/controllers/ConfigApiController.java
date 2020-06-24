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
        return (new ResponseEntity<>(HttpStatus.OK));
    }

    @Override
    public ResponseEntity<GenericResourceApiPreloadModelInformation> configGENERICRESOURCEAPIpreloadInformationGet() {
        GenericResourceApiPreloadModelInformation genericResourceApiPreloadModelInformation = new GenericResourceApiPreloadModelInformation();

        configPreloadDataRepository.findAll().forEach(configPreloadData -> {
            GenericResourceApiPreloadmodelinformationPreloadList preloadListItem = new GenericResourceApiPreloadmodelinformationPreloadList();

            preloadListItem.setPreloadId(configPreloadData.getPreloadId());
            preloadListItem.setPreloadType(configPreloadData.getPreloadType());
            try {
                preloadListItem.setPreloadData(objectMapper.readValue(configPreloadData.getPreloadData(), GenericResourceApiPreloaddataPreloadData.class));
            } catch (JsonProcessingException e) {
                log.error("Could not convert preload data", e);
            }
            genericResourceApiPreloadModelInformation.addPreloadListItem(preloadListItem);
        });


        return new ResponseEntity<>(genericResourceApiPreloadModelInformation, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationPost(@Valid GenericResourceApiPreloadModelInformation graPreloadModelInfo) {

        List<GenericResourceApiPreloadmodelinformationPreloadList> preloadList = graPreloadModelInfo.getPreloadList();

        if (preloadList != null) {
            Iterator<GenericResourceApiPreloadmodelinformationPreloadList> iter = preloadList.iterator();
            while (iter.hasNext()) {
                GenericResourceApiPreloadmodelinformationPreloadList curItem = iter.next();

                try {
                    configPreloadDataRepository.save(new ConfigPreloadData(curItem.getPreloadId(), curItem.getPreloadType(), objectMapper.writeValueAsString(curItem.getPreloadData())));
                } catch (JsonProcessingException e) {
                    log.error("Cannot convert preload data", e);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationPut(@Valid GenericResourceApiPreloadModelInformation graPreloadModelInfo) {

        List<GenericResourceApiPreloadmodelinformationPreloadList> preloadList = graPreloadModelInfo.getPreloadList();

        if (preloadList != null) {
            Iterator<GenericResourceApiPreloadmodelinformationPreloadList> iter = preloadList.iterator();
            while (iter.hasNext()) {
                GenericResourceApiPreloadmodelinformationPreloadList curItem = iter.next();

                try {
                    configPreloadDataRepository.save(new ConfigPreloadData(curItem.getPreloadId(), curItem.getPreloadType(), objectMapper.writeValueAsString(curItem.getPreloadData())));
                } catch (JsonProcessingException e) {
                    log.error("Cannot convert preload data", e);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPost(@Valid GenericResourceApiPreloadmodelinformationPreloadList preloadListItem) {

        try {
            configPreloadDataRepository.save(new ConfigPreloadData(preloadListItem.getPreloadId(), preloadListItem.getPreloadType(), objectMapper.writeValueAsString(preloadListItem.getPreloadData())));
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeDelete(String preloadId, String preloadType) {
        configPreloadDataRepository.deleteByPreloadIdAndPreloadType(preloadId, preloadType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResourceApiPreloadmodelinformationPreloadList> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGet(String preloadId, String preloadType) {
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
                }
                return new ResponseEntity<>(preloadDataList, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypePost(String preloadId, String preloadType, @Valid GenericResourceApiPreloadmodelinformationPreloadList preloadListItem) {
        try {
            configPreloadDataRepository.save(new ConfigPreloadData(preloadId, preloadType, objectMapper.writeValueAsString(preloadListItem.getPreloadData())));
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataDelete(String preloadId, String preloadType) {
        List<ConfigPreloadData> preloadData = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);

        if (preloadData != null) {
            Iterator<ConfigPreloadData> iter = preloadData.iterator();

            while (iter.hasNext()) {
                configPreloadDataRepository.delete(iter.next());
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @Override
    public ResponseEntity<GenericResourceApiPreloaddataPreloadData> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataGet(String preloadId, String preloadType) {
        List<ConfigPreloadData> preloadData = configPreloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);
        if (preloadData != null) {
            if (!preloadData.isEmpty()) {
                ConfigPreloadData preloadDataItem = preloadData.get(0);
                try {
                    return new ResponseEntity<>(objectMapper.readValue(preloadDataItem.getPreloadData(), GenericResourceApiPreloaddataPreloadData.class), HttpStatus.OK);
                } catch (JsonProcessingException e) {
                    log.error("Cannot convert preload data", e);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataPost(String preloadId, String preloadType, @Valid GenericResourceApiPreloaddataPreloadData preloadData) {
        configPreloadDataRepository.deleteByPreloadIdAndPreloadType(preloadId, preloadType);
        try {
            configPreloadDataRepository.save(new ConfigPreloadData(preloadId, preloadType, objectMapper.writeValueAsString(preloadData)));
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesDelete() {
        configServicesRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResourceApiServiceModelInfrastructure> configGENERICRESOURCEAPIservicesGet() {
        GenericResourceApiServiceModelInfrastructure modelInfrastructure = new GenericResourceApiServiceModelInfrastructure();

        AtomicBoolean caughtError = new AtomicBoolean(false);
        configServicesRepository.findAll().forEach(service ->
        {
            GenericResourceApiServicemodelinfrastructureService serviceItem = new GenericResourceApiServicemodelinfrastructureService();
            serviceItem.setServiceInstanceId(service.getSvcInstanceId());
            try {
                serviceItem.setServiceData(objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class));
            } catch (JsonProcessingException e) {
                log.error("Could not deserialize service data for {}", service.getSvcInstanceId(), e);
                caughtError.set(true);
            }
            serviceItem.setServiceStatus(service.getServiceStatus());
            modelInfrastructure.addServiceItem(serviceItem);
        });

        if (caughtError.get()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(modelInfrastructure, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesPost(@Valid GenericResourceApiServiceModelInfrastructure modelInfrastructure) {

        AtomicBoolean caughtError = new AtomicBoolean(false);
        ConfigServices service = new ConfigServices();
        modelInfrastructure.getService().forEach( serviceItem -> {
            String svcInstanceId = serviceItem.getServiceInstanceId();
            service.setSvcInstanceId(svcInstanceId);
            try {
                service.setSvcData(objectMapper.writeValueAsString(svcInstanceId));
            } catch (JsonProcessingException e) {
                log.error("Could not serialize service data for {}", service.getSvcInstanceId(), e);
                caughtError.set(true);
            }
            service.setServiceStatus(serviceItem.getServiceStatus());

            // Maintain uniqueness of svcInstanceId by deleting any existing entry before append
            configServicesRepository.deleteBySvcInstanceId(svcInstanceId);
            configServicesRepository.save(service);

        });

        if (caughtError.get()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesPut(@Valid GenericResourceApiServiceModelInfrastructure modelInfrastructure) {
        AtomicBoolean caughtError = new AtomicBoolean(false);
        ConfigServices service = new ConfigServices();
        modelInfrastructure.getService().forEach( serviceItem -> {
            String svcInstanceId = serviceItem.getServiceInstanceId();
            service.setSvcInstanceId(svcInstanceId);
            try {
                service.setSvcData(objectMapper.writeValueAsString(svcInstanceId));
            } catch (JsonProcessingException e) {
                log.error("Could not serialize service data for {}", service.getSvcInstanceId(), e);
                caughtError.set(true);
            }
            service.setServiceStatus(serviceItem.getServiceStatus());

            // Maintain uniqueness of svcInstanceId by deleting any existing entry before append
            configServicesRepository.deleteBySvcInstanceId(svcInstanceId);
            configServicesRepository.save(service);

        });

        if (caughtError.get()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIservicePost(@Valid GenericResourceApiServicemodelinfrastructureService servicesData) {
        String svcInstanceId = servicesData.getServiceInstanceId();
        try {
            String svcData = objectMapper.writeValueAsString(servicesData.getServiceData());
            ConfigServices configService = new ConfigServices(svcInstanceId, svcData, servicesData.getServiceStatus());
            configServicesRepository.deleteBySvcInstanceId(svcInstanceId);
            configServicesRepository.save(configService);
        } catch (JsonProcessingException e) {
           log.error("Cannot convert service data", e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdDelete(String serviceInstanceId) {
        configServicesRepository.deleteBySvcInstanceId(serviceInstanceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResourceApiServicemodelinfrastructureService> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGet(String serviceInstanceId) {
        GenericResourceApiServicemodelinfrastructureService retval = null;

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if (services.isEmpty()) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        } else {
            ConfigServices service = services.get(0);
            retval = new GenericResourceApiServicemodelinfrastructureService();
            retval.setServiceInstanceId(serviceInstanceId);
            retval.setServiceStatus(service.getServiceStatus());
            try {
                retval.setServiceData(objectMapper.readValue(service.getSvcData(), GenericResourceApiServicedataServiceData.class));
            } catch (JsonProcessingException e) {
                log.error("Could not deserialize service data for service instance id {}", serviceInstanceId, e);
                retval = null;
            }
        }

        if (retval == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(retval, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdPost(String serviceInstanceId, @Valid GenericResourceApiServicemodelinfrastructureService newService) {
        ConfigServices service = new ConfigServices();

        service.setSvcInstanceId(newService.getServiceInstanceId());
        service.setServiceStatus(newService.getServiceStatus());
        try {
            service.setSvcData(objectMapper.writeValueAsString(newService.getServiceData()));
            configServicesRepository.save(service);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize service data for eservice instance id {}", newService.getServiceInstanceId(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdPut(String serviceInstanceId, @Valid GenericResourceApiServicemodelinfrastructureService newService) {
        ConfigServices service = new ConfigServices();

        service.setSvcInstanceId(newService.getServiceInstanceId());
        service.setServiceStatus(newService.getServiceStatus());
        try {
            service.setSvcData(objectMapper.writeValueAsString(newService.getServiceData()));
            configServicesRepository.save(service);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize service data for eservice instance id {}", newService.getServiceInstanceId(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataDelete(String serviceInstanceId) {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if (!services.isEmpty()) {
            ConfigServices service = services.get(0);
            service.setSvcData(null);
            configServicesRepository.save(service);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResourceApiServicedataServiceData> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataGet(String serviceInstanceId) {
        GenericResourceApiServicedataServiceData serviceData = null;

        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if (services.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            try {
                serviceData = objectMapper.readValue(services.get(0).getSvcData(), GenericResourceApiServicedataServiceData.class);
                return new ResponseEntity<>(serviceData, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                log.error("Could not parse service data", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataPost(String serviceInstanceId, @Valid GenericResourceApiServicedataServiceData serviceData) {
       ConfigServices service;
       AtomicBoolean caughtError = new AtomicBoolean(false);
       List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
       if (services.isEmpty()) {
           service = new ConfigServices();
           service.setSvcInstanceId(serviceInstanceId);
       } else {
           service = services.get(0);
       }

        try {
            service.setSvcData(objectMapper.writeValueAsString(serviceData));
            configServicesRepository.save(service);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize service data for svc instance id {}", serviceInstanceId, e);
            caughtError.set(true);
        }

        if (caughtError.get()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataPut(String serviceInstanceId, @Valid GenericResourceApiServicedataServiceData serviceData) {
        ConfigServices service;
        AtomicBoolean caughtError = new AtomicBoolean(false);
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);
        if (services.isEmpty()) {
            service = new ConfigServices();
            service.setSvcInstanceId(serviceInstanceId);
        } else {
            service = services.get(0);
        }

        try {
            service.setSvcData(objectMapper.writeValueAsString(serviceData));
            configServicesRepository.save(service);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize service data for svc instance id {}", serviceInstanceId, e);
            caughtError.set(true);
        }

        if (caughtError.get()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusDelete(String serviceInstanceId) {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if (!services.isEmpty()) {
            ConfigServices service = services.get(0);
            service.setServiceStatus(null);
            configServicesRepository.save(service);
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    public ResponseEntity<GenericResourceApiServicestatusServiceStatus> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusGet(String serviceInstanceId) {
        GenericResourceApiServicestatusServiceStatus serviceStatus = null;
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if (!services.isEmpty()) {
            ConfigServices service = services.get(0);
            serviceStatus = service.getServiceStatus();
        }

        if (serviceStatus == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(serviceStatus, HttpStatus.OK);
        }

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusPost(String serviceInstanceId, @Valid GenericResourceApiServicestatusServiceStatus serviceStatus) {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        ConfigServices service = null;
        if (services.isEmpty()) {
            service = services.get(0);
        } else {
            service = new ConfigServices();
            service.setSvcInstanceId(serviceInstanceId);
        }
        service.setServiceStatus(serviceStatus);
        configServicesRepository.save(service);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusPut(String serviceInstanceId, @Valid GenericResourceApiServicestatusServiceStatus serviceStatus) {
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(serviceInstanceId);

        ConfigServices service = null;
        if (services.isEmpty()) {
            service = services.get(0);
        } else {
            service = new ConfigServices();
            service.setSvcInstanceId(serviceInstanceId);
        }
        service.setServiceStatus(serviceStatus);

        configServicesRepository.deleteBySvcInstanceId(serviceInstanceId);
        configServicesRepository.save(service);

        return new ResponseEntity<>(HttpStatus.OK);

    }



}
