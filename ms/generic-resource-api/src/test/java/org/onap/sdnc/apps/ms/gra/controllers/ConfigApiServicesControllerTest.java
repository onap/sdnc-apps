package org.onap.sdnc.apps.ms.gra.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.sdnc.apps.ms.gra.core.GenericResourceMsApp;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadData;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigServices;
import org.onap.sdnc.apps.ms.gra.data.ConfigServicesRepository;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceModelInfrastructure;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicemodelinfrastructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={GenericResourceMsApp.class})
@AutoConfigureMockMvc
@Transactional
public class ConfigApiServicesControllerTest {

    private final static String CONFIG_SERVICES_URL = "/config/GENERIC-RESOURCE-API:services/";
    private final static String CONFIG_SERVICES_SERVICE_URL = "/config/GENERIC-RESOURCE-API:services/GENERIC-RESOURCE-API:service/";

    @Autowired
    private MockMvc mvc;

    @Autowired
    ConfigServicesRepository configServicesRepository;

    @BeforeClass
    public static void setUp() throws Exception {
        System.setProperty("serviceLogicProperties", "src/test/resources/svclogic.properties");
    }


    @Test
    public void configGENERICRESOURCEAPIservicesDelete() throws Exception {

        // Clean up data
        configServicesRepository.deleteAll();

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
        configServicesRepository.deleteAll();

        // Test with data
        loadServicesData("src/test/resources/service1.json");
        assert(configServicesRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_URL).contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with no data
        configServicesRepository.deleteAll();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_URL).contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesPost() throws Exception {
        // Clean up data
        configServicesRepository.deleteAll();

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
        configServicesRepository.deleteAll();

    }

    @Test
    public void configGENERICRESOURCEAPIservicesPut() throws Exception {
        // Clean up data
        configServicesRepository.deleteAll();

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
        configServicesRepository.deleteAll();

    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdDelete() throws Exception {
        // Clean up data
        configServicesRepository.deleteAll();

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_SERVICE_URL+"service1/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Load data
        loadServicesData("src/test/resources/service1.json");
        assertEquals(1, configServicesRepository.count());

        // Test with data
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_SERVICE_URL+"service1/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGet() throws Exception {
        // Clean up data
        configServicesRepository.deleteAll();

        // Test with data
        loadServicesData("src/test/resources/service1.json");
        assert(configServicesRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+"service1/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with no data
        configServicesRepository.deleteAll();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+"service1/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdPost() throws Exception {
        // Clean up data
        configServicesRepository.deleteAll();

        String content = readFileContent("src/test/resources/service1-serviceitem.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+"service1/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        // Test with existing data - should return 409
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+"service1/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(409, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        // Clean up data
        configServicesRepository.deleteAll();

    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdPut() throws Exception {
        // Clean up data
        configServicesRepository.deleteAll();

        String content = readFileContent("src/test/resources/service1-serviceitem.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+"service1/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        // Test with existing data - should return 409
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+"service1/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());

        // Clean up data
        configServicesRepository.deleteAll();
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataDelete() throws Exception {
        // Clean up data
        configServicesRepository.deleteAll();

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-data/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Load data
        loadServicesData("src/test/resources/service1.json");
        assertEquals(1, configServicesRepository.count());

        // Test with data
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-data/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId("service1");
        assertEquals(1, services.size());
        assertEquals(null, services.get(0).getSvcData());


    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataGet() throws Exception {
        // Clean up data
        configServicesRepository.deleteAll();

        // Test with data
        loadServicesData("src/test/resources/service1.json");
        assert(configServicesRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-data/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with no data
        configServicesRepository.deleteAll();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-data/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataPost() throws Exception {
        // Clean up data
        configServicesRepository.deleteAll();

        String content = readFileContent("src/test/resources/service1-servicedata.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-data/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Test with empty service data
        ConfigServices service = new ConfigServices();
        service.setSvcInstanceId("service1");
        configServicesRepository.save(service);
        assertEquals(1, configServicesRepository.count());
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-data/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
        List<ConfigServices> updatedService = configServicesRepository.findBySvcInstanceId("service1");
        assertEquals(1, updatedService.size());
        assertNotEquals(null, updatedService.get(0).getSvcData());

        // Test with existing data - should return 409
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-data/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(409, mvcResult.getResponse().getStatus());

        // Clean up data
        configServicesRepository.deleteAll();
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataPut() throws Exception {
        // Clean up data
        configServicesRepository.deleteAll();

        String content = readFileContent("src/test/resources/service1-servicedata.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-data/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Test with empty service data
        ConfigServices service = new ConfigServices();
        service.setSvcInstanceId("service1");
        configServicesRepository.save(service);
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-data/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
        List<ConfigServices> updatedService = configServicesRepository.findBySvcInstanceId("service1");
        assertEquals(1, updatedService.size());
        assertNotEquals(null, updatedService.get(0).getSvcData());

        // Test with existing data - should return 204
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-data/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());

        // Clean up data
        configServicesRepository.deleteAll();
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusDelete() throws Exception {
        // Clean up data
        configServicesRepository.deleteAll();

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-status/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Load data
        loadServicesData("src/test/resources/service1.json");
        assertEquals(1, configServicesRepository.count());

        // Test with data
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-status/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
        List<ConfigServices> services = configServicesRepository.findBySvcInstanceId("service1");
        assertEquals(1, services.size());
        assertEquals(null, services.get(0).getServiceStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusGet() throws Exception {
        // Clean up data
        configServicesRepository.deleteAll();

        // Test with data
        loadServicesData("src/test/resources/service1.json");
        assert(configServicesRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-status/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with no data
        configServicesRepository.deleteAll();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-status/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusPost() throws Exception {
        // Clean up data
        configServicesRepository.deleteAll();

        String content = readFileContent("src/test/resources/service1-servicestatus.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-status/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Test with empty service data
        ConfigServices service = new ConfigServices();
        service.setSvcInstanceId("service1");
        configServicesRepository.save(service);
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-status/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
        List<ConfigServices> updatedService = configServicesRepository.findBySvcInstanceId("service1");
        assertEquals(1, updatedService.size());
        assertNotEquals(null, updatedService.get(0).getServiceStatus());

        // Test with existing data - should return 409
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-status/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(409, mvcResult.getResponse().getStatus());

        // Clean up data
        configServicesRepository.deleteAll();
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusPut() throws Exception {
        // Clean up data
        configServicesRepository.deleteAll();

        String content = readFileContent("src/test/resources/service1-servicestatus.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-status/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());

        // Test with empty service status
        ConfigServices service = new ConfigServices();
        service.setSvcInstanceId("service1");
        configServicesRepository.save(service);
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-status/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
        List<ConfigServices> updatedService = configServicesRepository.findBySvcInstanceId("service1");
        assertEquals(1, updatedService.size());
        assertNotEquals(null, updatedService.get(0).getServiceStatus());

        // Test with existing data - should return 204
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_SERVICES_SERVICE_URL+"service1/GENERIC-RESOURCE-API:service-status/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());

        // Clean up data
        configServicesRepository.deleteAll();
    }

    private String readFileContent(String path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        return content;
    }

    private void deleteData(String url) throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(url).contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
    }

    private void loadServicesData(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = readFileContent(path);
        GenericResourceApiServiceModelInfrastructure services = objectMapper.readValue(content, GenericResourceApiServiceModelInfrastructure.class);

        for (GenericResourceApiServicemodelinfrastructureService service : services.getService()) {
            ConfigServices newService = new ConfigServices();
            newService.setSvcInstanceId(service.getServiceInstanceId());
            newService.setSvcData(objectMapper.writeValueAsString(service.getServiceData()));
            newService.setServiceStatus(service.getServiceStatus());
            configServicesRepository.save(newService);
        }
    }

}