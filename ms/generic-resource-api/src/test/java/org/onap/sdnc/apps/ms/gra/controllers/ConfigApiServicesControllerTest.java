package org.onap.sdnc.apps.ms.gra.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.sdnc.apps.ms.gra.GenericResourceMsApp;
import org.onap.sdnc.apps.ms.gra.controllers.ServiceDataHelper.ServiceDataTransaction;
import org.onap.sdnc.apps.ms.gra.data.*;
import org.onap.sdnc.apps.ms.gra.swagger.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={GenericResourceMsApp.class})
@AutoConfigureMockMvc
@Transactional
public class ConfigApiServicesControllerTest {

    private final static String TEST_SVC_INSTANCE_ID = "5c4f2d89-57a3-47e9-b49b-d3c63eb0b3ca";
    private final static String TEST_VNF_ID = "fae319cc-68d6-496f-be1e-a09e133c71d4";
    private final static String TEST_VF_MODULE_ID = "45841173-3729-4a1d-a811-a3bde399e22d";
    private final static String CONFIG_SERVICES_URL = "/config/GENERIC-RESOURCE-API:services/";
    private final static String CONFIG_SERVICES_SERVICE_URL = "/config/GENERIC-RESOURCE-API:services/service/";
    private final static String CONFIG_CR_ARS_CR_AR_URL = "/config/GENERIC-RESOURCE-API:contrail-route-allotted-resources/contrail-route-allotted-resource/";
    private final static String CONFIG_PM_CONFIGS_PM_CONFIG_URL = "/config/GENERIC-RESOURCE-API:port-mirror-configurations/port-mirror-configuration/";

    @Autowired
    private MockMvc mvc;

    @Autowired
    ConfigServicesRepository configServicesRepository;

    @Autowired
    ConfigNetworksRepository configNetworksRepository;

    @Autowired
    ConfigVnfsRepository configVnfsRepository;

    @Autowired
    ConfigVfModulesRepository configVfModulesRepository;

    @Autowired
    ConfigContrailRouteAllottedResourcesRepository configContrailRouteAllottedResourcesRepository;

    @Autowired
    ConfigPortMirrorConfigurationsRepository configPortMirrorConfigurationsRepository;

    @Autowired
    ServiceDataHelper serviceDataHelper;

    @BeforeClass
    public static void setUp() throws Exception {
        System.out.println("ConfigApiServicesControllerTest: Setting serviceLogicProperties, serviceLogicDirectory and sdnc.config.dir");
        System.setProperty("serviceLogicProperties", "src/test/resources/svclogic.properties");
        System.setProperty("serviceLogicDirectory", "src/test/resources/svclogic");
        System.setProperty("sdnc.config.dir", "src/test/resources");
   
    }

    @Test
    public void serviceDataLoadTest() throws Exception {
        // Clear service data
        clearServicesData();
        assertEquals(0, configServicesRepository.count());
        assertEquals(0, configNetworksRepository.count());
        assertEquals(0, configVnfsRepository.count());
        assertEquals(0, configVfModulesRepository.count());

        // Add service data - just service
        loadServicesData("src/test/resources/service1-service.json");
        assertEquals(1, configServicesRepository.count());
        assertEquals(0, configNetworksRepository.count());
        assertEquals(0, configVnfsRepository.count());
        assertEquals(0, configVfModulesRepository.count());

        // Clear service data
        clearServicesData();
        assertEquals(0, configServicesRepository.count());
        assertEquals(0, configNetworksRepository.count());
        assertEquals(0, configVnfsRepository.count());
        assertEquals(0, configVfModulesRepository.count());

       // Add service data - service, vnf, vf-module
       loadServicesData("src/test/resources/service1.json");
       assertEquals(1, configServicesRepository.count());
       assertEquals(0, configNetworksRepository.count());
       assertEquals(1, configVnfsRepository.count());
       assertEquals(1, configVfModulesRepository.count());

   

    }

    @Test
    public void VnfDataLoadTest() throws Exception {
        // Clear service data
        clearServicesData();
        assertEquals(0, configServicesRepository.count());
        assertEquals(0, configNetworksRepository.count());
        assertEquals(0, configVnfsRepository.count());
        assertEquals(0, configVfModulesRepository.count());

        // Add vnf data
        loadVnfData("src/test/resources/vnf-data.json");
        assertEquals(1, configServicesRepository.count());
        assertEquals(0, configNetworksRepository.count());
        assertEquals(1, configVnfsRepository.count());
        assertEquals(1, configVfModulesRepository.count());

        // Clear service data
        clearServicesData();
        assertEquals(0, configServicesRepository.count());
        assertEquals(0, configNetworksRepository.count());
        assertEquals(0, configVnfsRepository.count());
        assertEquals(0, configVfModulesRepository.count());

    }

    @Test
    public void configGENERICRESOURCEAPIservicesDelete() throws Exception {

        // Clean up data
        clearServicesData();

        // Load test data
        loadServicesData( "src/test/resources/service1.json");

        assertEquals(1, configServicesRepository.count());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_URL).contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Test with no data
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_URL).contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());

    }

    @Test
    public void configGENERICRESOURCEAPIservicesGet() throws Exception {
        // Clean up data
        clearServicesData();

        // Test with data
        loadServicesData("src/test/resources/service1.json");
        assert(configServicesRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_URL).contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with no data
        clearServicesData();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_URL).contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesPost() throws Exception {
        // Clean up data
        clearServicesData();

        String content = readFileContent("src/test/resources/service1.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        // Test with existing data - should return 409
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(409, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        // Clean up data
        clearServicesData();

    }

    @Test
    public void configGENERICRESOURCEAPIservicesPut() throws Exception {
        // Clean up data
        clearServicesData();

        String content = readFileContent("src/test/resources/service1.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        // Test with existing data - should return 409
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        // Clean up data
        clearServicesData();

    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdDelete() throws Exception {
        // Clean up data
        clearServicesData();

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Load data
        loadServicesData("src/test/resources/service1.json");
        assertEquals(1, configServicesRepository.count());

        // Test with data
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGet() throws Exception {
        // Clean up data
        clearServicesData();

        // Test with data
        loadServicesData("src/test/resources/service1.json");
        assert(configServicesRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with no data
        clearServicesData();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdPost() throws Exception {
        // Clean up data
        clearServicesData();

        String content = readFileContent("src/test/resources/service1-serviceitem.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        // Test with existing data - should return 409
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(409, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        // Clean up data
        clearServicesData();

    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdPut() throws Exception {
        // Clean up data
        clearServicesData();

        String content = readFileContent("src/test/resources/service1-serviceitem.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        // Test with existing data - should return 409
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        // Clean up data
        clearServicesData();
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataDelete() throws Exception {
        // Clean up data
        clearServicesData();

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Load data
        loadServicesData("src/test/resources/service1.json");
        assertEquals(1, configServicesRepository.count());

        // Test with data
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(TEST_SVC_INSTANCE_ID);
        assertEquals(1, services.size());
        assertEquals(null, services.get(0).getSvcData());


    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataGet() throws Exception {
        // Clean up data
        clearServicesData();

        // Test with data
        loadServicesData("src/test/resources/service1.json");
        assert(configServicesRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with no data
        clearServicesData();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataPost() throws Exception {
        // Clean up data
        clearServicesData();

        String content = readFileContent("src/test/resources/service1-servicedata.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Test with empty service data
        ConfigServices service = new ConfigServices();
        service.setSvcInstanceId(TEST_SVC_INSTANCE_ID);
        configServicesRepository.save(service);
        assertEquals(1, configServicesRepository.count());
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
        List<ConfigServices> updatedService = configServicesRepository.findBySvcInstanceId(TEST_SVC_INSTANCE_ID);
        assertEquals(1, updatedService.size());
        assertNotEquals(null, updatedService.get(0).getSvcData());

        // Test with existing data - should return 409
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(409, mvcResult.getResponse().getStatus());

        // Clean up data
        clearServicesData();
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataPut() throws Exception {
        // Clean up data
        clearServicesData();

        String content = readFileContent("src/test/resources/service1-servicedata.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Test with empty service data
        ConfigServices service = new ConfigServices();
        service.setSvcInstanceId(TEST_SVC_INSTANCE_ID);
        configServicesRepository.save(service);
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
        List<ConfigServices> updatedService = configServicesRepository.findBySvcInstanceId(TEST_SVC_INSTANCE_ID);
        assertEquals(1, updatedService.size());
        assertNotEquals(null, updatedService.get(0).getSvcData());

        // Test with existing data - should return 204
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());

        // Clean up data
        clearServicesData();
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusDelete() throws Exception {
        // Clean up data
        clearServicesData();

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-status/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Load data
        loadServicesData("src/test/resources/service1.json");
        assertEquals(1, configServicesRepository.count());

        // Test with data
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-status/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId(TEST_SVC_INSTANCE_ID);
        assertEquals(1, services.size());
        assertEquals(null, services.get(0).getServiceStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusGet() throws Exception {
        // Clean up data
        clearServicesData();

        // Test with data
        loadServicesData("src/test/resources/service1.json");
        assert(configServicesRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-status/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with no data
        clearServicesData();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-status/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusPost() throws Exception {
        // Clean up data
        clearServicesData();

        String content = readFileContent("src/test/resources/service1-servicestatus.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-status/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Test with empty service data
        ConfigServices service = new ConfigServices();
        service.setSvcInstanceId(TEST_SVC_INSTANCE_ID);
        configServicesRepository.save(service);
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-status/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
        List<ConfigServices> updatedService = configServicesRepository.findBySvcInstanceId(TEST_SVC_INSTANCE_ID);
        assertEquals(1, updatedService.size());
        assertNotEquals(null, updatedService.get(0).getServiceStatus());

        // Test with existing data - should return 409
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-status/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(409, mvcResult.getResponse().getStatus());

        // Clean up data
        clearServicesData();
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusPut() throws Exception {
        // Clean up data
        clearServicesData();

        String content = readFileContent("src/test/resources/service1-servicestatus.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-status/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Test with empty service status
        ConfigServices service = new ConfigServices();
        service.setSvcInstanceId(TEST_SVC_INSTANCE_ID);
        configServicesRepository.save(service);
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-status/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
        List<ConfigServices> updatedService = configServicesRepository.findBySvcInstanceId(TEST_SVC_INSTANCE_ID);
        assertEquals(1, updatedService.size());
        assertNotEquals(null, updatedService.get(0).getServiceStatus());

        // Test with existing data - should return 204
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-status/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());

        // Clean up data
        clearServicesData();
    }

    @Test
    public void configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataServiceTopologyGet() throws Exception {
        // Clean up data
        clearServicesData();

        // Test with data
        loadServicesData("src/test/resources/service1.json");
        assert(configServicesRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/service-topology/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with no data
        clearServicesData();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/service-topology/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdDelete() throws Exception {
        // Clean up data
        clearServicesData();

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/config/GENERIC-RESOURCE-API:services/service/"+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/").contentType(MediaType.APPLICATION_JSON).content(""))
                                      .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Load data
        loadVnfData("src/test/resources/vnf-data.json");
        assertEquals(1, configServicesRepository.count());

        // Test with data
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/config/GENERIC-RESOURCE-API:services/service/"+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/").contentType(MediaType.APPLICATION_JSON).content(""))
                            .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        // Test with bad data

        clearServicesData();
        createBadVnfData(true, true);
        assertEquals(1, configServicesRepository.count());
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/config/GENERIC-RESOURCE-API:services/service/"+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/").contentType(MediaType.APPLICATION_JSON).content(""))
                            .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());

        clearServicesData();
        createBadVnfData(false, false);
        assertEquals(1, configServicesRepository.count());
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/config/GENERIC-RESOURCE-API:services/service/"+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/").contentType(MediaType.APPLICATION_JSON).content(""))
                            .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());

        clearServicesData();
        createBadVnfData(false, true);
        assertEquals(1, configServicesRepository.count());
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/config/GENERIC-RESOURCE-API:services/service/"+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/").contentType(MediaType.APPLICATION_JSON).content(""))
                            .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdGet() throws Exception {
        // Clean up data
        clearServicesData();

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/config/GENERIC-RESOURCE-API:services/service/"+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/").contentType(MediaType.APPLICATION_JSON).content(""))
                                      .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Load data
        loadVnfData("src/test/resources/vnf-data.json");
        assertEquals(1, configServicesRepository.count());

        // Test with data
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/config/GENERIC-RESOURCE-API:services/service/"+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/").contentType(MediaType.APPLICATION_JSON).content(""))
                            .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        clearServicesData();
        createBadVnfData(false, false);
        assertEquals(1, configServicesRepository.count());
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/config/GENERIC-RESOURCE-API:services/service/"+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/").contentType(MediaType.APPLICATION_JSON).content(""))
                            .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdPut() throws Exception {
        // Clean up data
        clearServicesData();
        assertEquals(0, configServicesRepository.count());
        assertEquals(0, configVnfsRepository.count());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/config/GENERIC-RESOURCE-API:services/service/"+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/").contentType(MediaType.APPLICATION_JSON).content(readFileContent("src/test/resources/vnf-data.json")))
                                      .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configVnfsRepository.count());

        mvcResult = mvc.perform(MockMvcRequestBuilders.put("/config/GENERIC-RESOURCE-API:services/service/"+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/").contentType(MediaType.APPLICATION_JSON).content(readFileContent("src/test/resources/vnf-data.json")))
                            .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configVnfsRepository.count());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVnfTopologyGet() throws Exception {
        // Clean up data
        clearServicesData();

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/config/GENERIC-RESOURCE-API:services/service/"+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/vnf-data/vnf-topology/").contentType(MediaType.APPLICATION_JSON).content(""))
                                      .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Load data
        loadVnfData("src/test/resources/vnf-data.json");
        assertEquals(1, configServicesRepository.count());

        // Test with data
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/config/GENERIC-RESOURCE-API:services/service/"+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/vnf-data/vnf-topology/").contentType(MediaType.APPLICATION_JSON).content(""))
                            .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        clearServicesData();
        createBadVnfData(false, false);
        assertEquals(1, configServicesRepository.count());
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/config/GENERIC-RESOURCE-API:services/service/"+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/vnf-data/vnf-topology/").contentType(MediaType.APPLICATION_JSON).content(""))
                            .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVnfLevelOperStatusPut() throws Exception {
        // Clean up data
        clearServicesData();
        assertEquals(0, configServicesRepository.count());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/config/GENERIC-RESOURCE-API:services/service/test-siid/service-data/vnfs/vnf/2a3bfc93-cd4c-4845-8919-434b2d999ada/vnf-data/vnf-level-oper-status/").contentType(MediaType.APPLICATION_JSON).content(readFileContent("src/test/resources/vnf-level-oper-status.json")))
                                      .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        mvcResult = mvc.perform(MockMvcRequestBuilders.put("/config/GENERIC-RESOURCE-API:services/service/test-siid/service-data/vnfs/vnf/2a3bfc93-cd4c-4845-8919-434b2d999ada/vnf-data/vnf-level-oper-status/").contentType(MediaType.APPLICATION_JSON).content(readFileContent("src/test/resources/vnf-level-oper-status.json")))
                            .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVnfTopologyOnapModelInformationPut() throws Exception {
        // Clean up data
        clearServicesData();
        assertEquals(0, configServicesRepository.count());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/config/GENERIC-RESOURCE-API:services/service/test-siid/service-data/vnfs/vnf/2a3bfc93-cd4c-4845-8919-434b2d999ada/vnf-data/vnf-topology/onap-model-information/").contentType(MediaType.APPLICATION_JSON).content(readFileContent("src/test/resources/vnf-onap-model-info.json")))
                                      .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        mvcResult = mvc.perform(MockMvcRequestBuilders.put("/config/GENERIC-RESOURCE-API:services/service/test-siid/service-data/vnfs/vnf/2a3bfc93-cd4c-4845-8919-434b2d999ada/vnf-data/vnf-topology/onap-model-information/").contentType(MediaType.APPLICATION_JSON).content(readFileContent("src/test/resources/vnf-onap-model-info.json")))
                            .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVnfTopologyVnfResourceAssignmentsVnfNetworksPut() throws Exception {
        // Clean up data
        clearServicesData();
        assertEquals(0, configServicesRepository.count());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/config/GENERIC-RESOURCE-API:services/service/test-siid/service-data/vnfs/vnf/2a3bfc93-cd4c-4845-8919-434b2d999ada/vnf-data/vnf-topology/vnf-resource-assignments/vnf-networks/").contentType(MediaType.APPLICATION_JSON).content(readFileContent("src/test/resources/vnf-vnf-networks.json")))
                                      .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        mvcResult = mvc.perform(MockMvcRequestBuilders.put("/config/GENERIC-RESOURCE-API:services/service/test-siid/service-data/vnfs/vnf/2a3bfc93-cd4c-4845-8919-434b2d999ada/vnf-data/vnf-topology/vnf-resource-assignments/vnf-networks/").contentType(MediaType.APPLICATION_JSON).content(readFileContent("src/test/resources/vnf-vnf-networks.json")))
                            .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVnfTopologyVnfResourceAssignmentsVnfNetworksVnfNetworkNetworkRolePut() throws Exception {
        // Clean up data
        clearServicesData();
        assertEquals(0, configServicesRepository.count());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/config/GENERIC-RESOURCE-API:services/service/test-siid/service-data/vnfs/vnf/2a3bfc93-cd4c-4845-8919-434b2d999ada/vnf-data/vnf-topology/vnf-resource-assignments/vnf-networks/vnf-network/test-network-role/").contentType(MediaType.APPLICATION_JSON).content(readFileContent("src/test/resources/vnf-vnf-networks-network-role.json")))
                                      .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        mvcResult = mvc.perform(MockMvcRequestBuilders.put("/config/GENERIC-RESOURCE-API:services/service/test-siid/service-data/vnfs/vnf/2a3bfc93-cd4c-4845-8919-434b2d999ada/vnf-data/vnf-topology/vnf-resource-assignments/vnf-networks/vnf-network/test-network-role/").contentType(MediaType.APPLICATION_JSON).content(readFileContent("src/test/resources/vnf-vnf-networks-network-role.json")))
                            .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
    }

    @Test
    public void configGENERICRESOURCEAPIportMirrorConfigurationsPortMirrorConfigurationConfigurationIdPut() throws Exception {
        // Clean up data
        configPortMirrorConfigurationsRepository.deleteAll();

        String content = readFileContent("src/test/resources/port-mirror-configuration-item.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_PM_CONFIGS_PM_CONFIG_URL+"pm-config-2/")
                .contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());

        // Test with existing port-mirror-configuration
        // Load data
        loadPortMirrorConfigurationData("src/test/resources/port-mirror-configuration-1.json");
        assertEquals(2, configPortMirrorConfigurationsRepository.count());

        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_PM_CONFIGS_PM_CONFIG_URL+"pm-config-2/")
                .contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(2, configPortMirrorConfigurationsRepository.count());

        // Clean up data
        configPortMirrorConfigurationsRepository.deleteAll();
    }

    @Test
    public void configGENERICRESOURCEAPIportMirrorConfigurationsPortMirrorConfigurationConfigurationIdDelete() throws Exception {
        // Clean up data
        configPortMirrorConfigurationsRepository.deleteAll();

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_PM_CONFIGS_PM_CONFIG_URL+"pm-config-1/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(0, configPortMirrorConfigurationsRepository.count());

        // Load data
        loadPortMirrorConfigurationData("src/test/resources/port-mirror-configuration-1.json");
        assertEquals(1, configPortMirrorConfigurationsRepository.count());

        // Test with data
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_PM_CONFIGS_PM_CONFIG_URL+"pm-config-1/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(0, configPortMirrorConfigurationsRepository.count());
    }

    @Test
    public void configGENERICRESOURCEAPIportMirrorConfigurationsPortMirrorConfigurationConfigurationIdGet() throws Exception {
        // Clean up data
        configPortMirrorConfigurationsRepository.deleteAll();

        // Test with data
        loadPortMirrorConfigurationData("src/test/resources/port-mirror-configuration-1.json");
        assert(configPortMirrorConfigurationsRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_PM_CONFIGS_PM_CONFIG_URL+"pm-config-1/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with bad allotted-resource-id in input
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_PM_CONFIGS_PM_CONFIG_URL+"dummy/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());

        // Test with no data
        configPortMirrorConfigurationsRepository.deleteAll();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_PM_CONFIGS_PM_CONFIG_URL+"pm-config-1/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIportMirrorConfigurationsPortMirrorConfigurationConfigurationIdConfigurationDataPortMirrorConfigurationTopologyGet() throws Exception {
        // Clean up data
        configPortMirrorConfigurationsRepository.deleteAll();

        // Test with data
        loadPortMirrorConfigurationData("src/test/resources/port-mirror-configuration-1.json");
        assert(configPortMirrorConfigurationsRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_PM_CONFIGS_PM_CONFIG_URL+"pm-config-1/configuration-data/port-mirror-configuration-topology/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with dummy allotted-resource-id
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_PM_CONFIGS_PM_CONFIG_URL+"dummy/configuration-data/port-mirror-configuration-topology/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());

        // Test with no data
        configPortMirrorConfigurationsRepository.deleteAll();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_PM_CONFIGS_PM_CONFIG_URL+"pm-config-1/configuration-data/port-mirror-configuration-topology/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIcontrailRouteAllottedResourcesContrailRouteAllottedResourceAllottedResourceIdPut() throws Exception {
        // Clean up data
        configContrailRouteAllottedResourcesRepository.deleteAll();

        String content = readFileContent("src/test/resources/allotted-resource-item.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_CR_ARS_CR_AR_URL+"ar2/")
                .contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());

        // Test with existing allotted-resource
        // Load data
        configContrailRouteAllottedResourcesRepository.deleteAll();
        loadContrailRouteAllottedResourceData("src/test/resources/contrail-route-allotted-resource-1.json");
        assertEquals(1, configContrailRouteAllottedResourcesRepository.count());

        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_CR_ARS_CR_AR_URL+"ar2/")
                .contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(2, configContrailRouteAllottedResourcesRepository.count());

        // Clean up data
        configContrailRouteAllottedResourcesRepository.deleteAll();
    }

    @Test
    public void configGENERICRESOURCEAPIcontrailRouteAllottedResourcesContrailRouteAllottedResourceAllottedResourceIdDelete() throws Exception {
        // Clean up data
        configContrailRouteAllottedResourcesRepository.deleteAll();

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_CR_ARS_CR_AR_URL+"ar1/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(0, configContrailRouteAllottedResourcesRepository.count());

        // Load data
        loadContrailRouteAllottedResourceData("src/test/resources/contrail-route-allotted-resource-1.json");
        assertEquals(1, configContrailRouteAllottedResourcesRepository.count());

        // Test with data
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_CR_ARS_CR_AR_URL+"ar1/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(0, configContrailRouteAllottedResourcesRepository.count());
    }

    @Test
    public void configGENERICRESOURCEAPIcontrailRouteAllottedResourcesContrailRouteAllottedResourceAllottedResourceIdGet() throws Exception {
        // Clean up data
        configContrailRouteAllottedResourcesRepository.deleteAll();

        // Test with data
        loadContrailRouteAllottedResourceData("src/test/resources/contrail-route-allotted-resource-1.json");
        assert(configContrailRouteAllottedResourcesRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_CR_ARS_CR_AR_URL+"ar1/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with bad allotted-resource-id in input
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_CR_ARS_CR_AR_URL+"dummy/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());

        // Test with no data
        configContrailRouteAllottedResourcesRepository.deleteAll();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_CR_ARS_CR_AR_URL+"ar1/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIcontrailRouteAllottedResourcesContrailRouteAllottedResourceAllottedResourceIdAllottedResourceDataContrailRouteTopologyGet() throws Exception {
        // Clean up data
        configContrailRouteAllottedResourcesRepository.deleteAll();

        // Test with data
        loadContrailRouteAllottedResourceData("src/test/resources/contrail-route-allotted-resource-1.json");
        assert(configContrailRouteAllottedResourcesRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_CR_ARS_CR_AR_URL+"ar1/allotted-resource-data/contrail-route-topology/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with dummy allotted-resource-id
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_CR_ARS_CR_AR_URL+"dummy/allotted-resource-data/contrail-route-topology/").contentType(MediaType.APPLICATION_JSON).content("")).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());

        // Test with no data
        configContrailRouteAllottedResourcesRepository.deleteAll();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_CR_ARS_CR_AR_URL+"ar1/allotted-resource-data/contrail-route-topology/").contentType(MediaType.APPLICATION_JSON).content("")).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVfModulesVfModuleVfModuleIdPut() throws Exception {
        // Clean up data
        clearServicesData();

        String content = readFileContent("src/test/resources/service1-vfmodule-item.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/vnf-data/vf-modules/vf-module/"+TEST_VF_MODULE_ID+"/")
                .contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();
        assertEquals(400, mvcResult.getResponse().getStatus());

        // Test with existing service and vnf
        // Load data
        loadServicesData("src/test/resources/service1.json");
        assertEquals(1, configServicesRepository.count());

        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/vnf-data/vf-modules/vf-module/"+TEST_VF_MODULE_ID+"/")
                .contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        // Clean up data
        clearServicesData();
    }

    @Test
    public void configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVfModulesVfModuleVfModuleIdDelete() throws Exception {
        // Clean up data
        clearServicesData();

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/vnf-data/vf-modules/vf-module/269bda16-f40c-41a9-baef-e8905ab2b70e/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Load data
        loadServicesData("src/test/resources/service1.json");
        assertEquals(1, configServicesRepository.count());

        // Test with data
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/vnf-data/vf-modules/vf-module/269bda16-f40c-41a9-baef-e8905ab2b70e/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVfModulesVfModuleVfModuleIdGet() throws Exception {
        // Clean up data
        clearServicesData();

        // Test with data
        loadServicesData("src/test/resources/service1.json");
        assert(configServicesRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/vnf-data/vf-modules/vf-module/269bda16-f40c-41a9-baef-e8905ab2b70e/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with bad vf-module-id in input
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/vnf-data/vf-modules/vf-module/dummyid/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());

        // Test with no data
        clearServicesData();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/vnf-data/vf-modules/vf-module/269bda16-f40c-41a9-baef-e8905ab2b70e/")
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesServiceServiceInstanceIdServiceDataVnfsVnfVnfIdVnfDataVfModulesVfModuleVfModuleIdVfModuleDataVfModuleTopologyGet() throws Exception {
        // Clean up data
        clearServicesData();

        // Test with data
        loadServicesData("src/test/resources/service1.json");
        assert(configServicesRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/vnf-data/vf-modules/vf-module/269bda16-f40c-41a9-baef-e8905ab2b70e/vf-module-data/vf-module-topology/").contentType(MediaType.APPLICATION_JSON)
                .content("")).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with existing service and vnf but with dummy vf-module-id in input
        clearServicesData();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/vnf-data/vf-modules/vf-module/dummy/vf-module-data/vf-module-topology/").contentType(MediaType.APPLICATION_JSON)
                .content("")).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());

        // Test with no data
        clearServicesData();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+TEST_SVC_INSTANCE_ID+"/service-data/vnfs/vnf/"+TEST_VNF_ID+"/vnf-data/vf-modules/vf-module/269bda16-f40c-41a9-baef-e8905ab2b70e/vf-module-data/vf-module-topology/").contentType(MediaType.APPLICATION_JSON)
                .content("")).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    private String readFileContent(String path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        return content;
    }

    private void deleteData(String url) throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(url).contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
    }

    private void clearServicesData() {
        configServicesRepository.deleteAll();
        configNetworksRepository.deleteAll();
        configVnfsRepository.deleteAll();
        configVfModulesRepository.deleteAll();
    }

    private void loadServicesData(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = readFileContent(path);
        GenericResourceApiServiceModelInfrastructure services = objectMapper.readValue(content, GenericResourceApiServiceModelInfrastructure.class);

        for (GenericResourceApiServicemodelinfrastructureService service : services.getService()) {
            ConfigServices newService = new ConfigServices();
            newService.setSvcInstanceId(service.getServiceInstanceId());
            newService.setServiceStatus(service.getServiceStatus());
            serviceDataHelper.saveService(newService, service.getServiceData());
        }
    }

    private void loadServicesData(String path, ServiceDataTransaction transaction) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = readFileContent(path);
        GenericResourceApiServiceModelInfrastructure services = objectMapper.readValue(content, GenericResourceApiServiceModelInfrastructure.class);

        for (GenericResourceApiServicemodelinfrastructureService service : services.getService()) {
            ConfigServices newService = new ConfigServices();
            newService.setSvcInstanceId(service.getServiceInstanceId());
            newService.setServiceStatus(service.getServiceStatus());
            serviceDataHelper.saveService(newService, service.getServiceData(), transaction);
        }
        transaction.commit();
    }

    private void loadVnfData(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = readFileContent(path);
        GenericResourceApiServicedataServicedataVnfsVnf vnfData = objectMapper.readValue(content, GenericResourceApiServicedataServicedataVnfsVnf.class);
        String svcInstanceId = TEST_SVC_INSTANCE_ID;
        ConfigServices newService = new ConfigServices(svcInstanceId);
        configServicesRepository.save(newService);
        serviceDataHelper.saveVnf(svcInstanceId, vnfData, null);
    }

    private void loadVnfData(String path, ServiceDataTransaction transaction) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = readFileContent(path);
        GenericResourceApiServicedataServicedataVnfsVnf vnfData = objectMapper.readValue(content, GenericResourceApiServicedataServicedataVnfsVnf.class);
        String svcInstanceId = TEST_SVC_INSTANCE_ID;
        ConfigServices newService = new ConfigServices(svcInstanceId);
        configServicesRepository.save(newService);
        serviceDataHelper.saveVnf(svcInstanceId, vnfData, transaction);
        if (transaction != null) {
            transaction.commit();
        }
    }

    private void createBadVnfData(boolean useNullSvc, boolean useNullVnfs) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ConfigServices newService = new ConfigServices();
        GenericResourceApiServicedataServiceData svcData = useNullSvc ? null : new GenericResourceApiServicedataServiceData();
        GenericResourceApiServicedataServicedataVnfs vnfs = useNullVnfs ? null : new GenericResourceApiServicedataServicedataVnfs();

        // Overrides useNullSvc
        if(!useNullVnfs) {
            svcData = new GenericResourceApiServicedataServiceData();
            vnfs.setVnf(new ArrayList<>());
            svcData.setVnfs(vnfs);
        }

        newService.setSvcInstanceId("test-siid");
        newService.setSvcData(objectMapper.writeValueAsString(svcData));
        configServicesRepository.save(newService);
    }

    private void loadContrailRouteAllottedResourceData(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = readFileContent(path);
        GenericResourceApiContrailRouteAllottedResources allottedResources = objectMapper.readValue(content, GenericResourceApiContrailRouteAllottedResources.class);

        for (GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource allottedResource : allottedResources.getContrailRouteAllottedResource()) {
            ConfigContrailRouteAllottedResources newContrailRouteAllottedResource = new ConfigContrailRouteAllottedResources();
            newContrailRouteAllottedResource.setAllottedResourceId(allottedResource.getAllottedResourceId());
            newContrailRouteAllottedResource.setArData(objectMapper.writeValueAsString(allottedResource.getAllottedResourceData()));
            newContrailRouteAllottedResource.setAllottedResourceStatus(allottedResource.getAllottedResourceStatus());
            configContrailRouteAllottedResourcesRepository.save(newContrailRouteAllottedResource);
        }
    }

    private void loadPortMirrorConfigurationData(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = readFileContent(path);
        GenericResourceApiPortMirrorConfigurations pmConfigurations = objectMapper.readValue(content, GenericResourceApiPortMirrorConfigurations.class);

        for (GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration pmConfig : pmConfigurations.getPortMirrorConfiguration()) {
            ConfigPortMirrorConfigurations newPmConfig = new ConfigPortMirrorConfigurations();
            newPmConfig.setConfigureationId(pmConfig.getConfigurationId());
            newPmConfig.setPmcData(objectMapper.writeValueAsString(pmConfig.getConfigurationData()));
            newPmConfig.setPortMirrorConfigurationStatus(pmConfig.getConfigurationStatus());
            configPortMirrorConfigurationsRepository.save(newPmConfig);
        }
    }

}