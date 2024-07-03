package org.onap.sdnc.apps.ms.gra.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.sdnc.apps.ms.gra.data.*;
import org.onap.sdnc.apps.ms.gra.swagger.OperationalApi;
import org.onap.sdnc.apps.ms.gra.swagger.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@ComponentScan(basePackages = {"org.onap.sdnc.apps.ms.gra.*"})
@EntityScan("org.onap.sdnc.apps.ms.gra.springboot.*")
public class OperationalApiController implements OperationalApi {
    private static final Logger log = LoggerFactory.getLogger(ConfigApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private OperationalServicesRepository operationalServicesRepository;

    @Autowired
    public OperationalApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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
    public ResponseEntity<GenericResourceApiServiceModelInfrastructure> operationalGENERICRESOURCEAPIservicesGet() {
        GenericResourceApiServiceModelInfrastructure modelInfrastructure = new GenericResourceApiServiceModelInfrastructure();

        AtomicBoolean caughtError = new AtomicBoolean(false);
        operationalServicesRepository.findAll().forEach(service ->
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
    public ResponseEntity<GenericResourceApiServicemodelinfrastructureService> operationalGENERICRESOURCEAPIservicesServiceServiceInstanceIdGet(String serviceInstanceId) {
        GenericResourceApiServicemodelinfrastructureService retval = null;

        List<OperationalServices> services = operationalServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if (services.isEmpty()) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        } else {
            OperationalServices service = services.get(0);
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
    public ResponseEntity<GenericResourceApiServicedataServiceData> operationalGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataGet(String serviceInstanceId) {
        GenericResourceApiServicedataServiceData serviceData = null;

        List<OperationalServices> services = operationalServicesRepository.findBySvcInstanceId(serviceInstanceId);
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
    public ResponseEntity<GenericResourceApiServicestatusServiceStatus> operationalGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceStatusGet(String serviceInstanceId) {
        GenericResourceApiServicestatusServiceStatus serviceStatus = null;
        List<OperationalServices> services = operationalServicesRepository.findBySvcInstanceId(serviceInstanceId);

        if (!services.isEmpty()) {
            OperationalServices service = services.get(0);
            serviceStatus = service.getServiceStatus();
        }

        if (serviceStatus == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(serviceStatus, HttpStatus.OK);
        }

    }

}
