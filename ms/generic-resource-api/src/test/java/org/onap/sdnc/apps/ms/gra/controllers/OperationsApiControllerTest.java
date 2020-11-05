package org.onap.sdnc.apps.ms.gra.controllers;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.sdnc.apps.ms.gra.GenericResourceMsApp;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigServices;
import org.onap.sdnc.apps.ms.gra.data.ConfigServicesRepository;
import org.onap.sdnc.apps.ms.gra.data.OperationalServicesRepository;
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
public class OperationsApiControllerTest {

    private final static String PRELOAD_NETWORK_URL = "/operations/GENERIC-RESOURCE-API:preload-network-topology-operation/";
    private final static String PRELOAD_VFMODULE_URL = "/operations/GENERIC-RESOURCE-API:preload-vf-module-topology-operation/";
    private final static String SERVICE_TOPOLOGY_URL = "/operations/GENERIC-RESOURCE-API:service-topology-operation/";
    private final static String NETWORK_TOPOLOGY_URL = "/operations/GENERIC-RESOURCE-API:network-topology-operation/";
    private final static String VNF_TOPOLOGY_URL = "/operations/GENERIC-RESOURCE-API:vnf-topology-operation/";


    @Autowired
    private MockMvc mvc;

    @Autowired
    ConfigPreloadDataRepository configPreloadDataRepository;

    @Autowired
    ConfigServicesRepository configServicesRepository;

    @Autowired
    OperationalServicesRepository operationalServicesRepository;

    @BeforeClass
    public static void setUp() throws Exception {
        System.out.println("OperationsApiControllerTest: Setting serviceLogicProperties, serviceLogicDirectory and sdnc.config.dir");
        System.setProperty("serviceLogicProperties", "src/test/resources/svclogic.properties");
        System.setProperty("serviceLogicDirectory", "src/test/resources/svclogic");
        System.setProperty("sdnc.config.dir", "src/test/resources");
   
    }


    @Test
    public void operationsGENERICRESOURCEAPIpreloadNetworkTopologyOperationPost() throws Exception {

        // Remove any existing preload data
        configPreloadDataRepository.deleteAll();

        // Add invalid content
        String content = readFileContent("src/test/resources/preload1-rpc-vfmodule.json");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRELOAD_NETWORK_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(403, mvcResult.getResponse().getStatus());
        assertEquals(0, configPreloadDataRepository.count());

        // Add valid content
        content = readFileContent("src/test/resources/preload1-rpc-network.json");
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRELOAD_NETWORK_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());

    }

    @Test
    public void operationsGENERICRESOURCEAPIpreloadVfModuleTopologyOperationPost() throws Exception {
        // Remove any existing preload data
        configPreloadDataRepository.deleteAll();

        // Add invalid content
        String content = readFileContent("src/test/resources/preload1-rpc-network.json");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRELOAD_VFMODULE_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(403, mvcResult.getResponse().getStatus());
        assertEquals(0, configPreloadDataRepository.count());

        // Add valid content
        content = readFileContent("src/test/resources/preload1-rpc-vfmodule.json");
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRELOAD_VFMODULE_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());
    }

    @Test
    public void operationsGENERICRESOURCEAPIserviceTopologyOperationAssignPost() throws Exception {

        // Remove any existing service data
        configServicesRepository.deleteAll();
        operationalServicesRepository.deleteAll();

        // Add invalid content
        String content = readFileContent("src/test/resources/preload1-rpc-vfmodule.json");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(SERVICE_TOPOLOGY_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(0, configServicesRepository.count());
        assertEquals(0, operationalServicesRepository.count());

        // Add valid content
        content = readFileContent("src/test/resources/service-assign-rpc.json");
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(SERVICE_TOPOLOGY_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(1, configServicesRepository.count());
        assertEquals(0, operationalServicesRepository.count());

    }

    @Test
    public void operationsGENERICRESOURCEAPInetworkTopologyOperationAssignPost() throws Exception {

        // Remove any existing service data
        configServicesRepository.deleteAll();
        operationalServicesRepository.deleteAll();

        // Load services data
        loadServicesData("src/test/resources/service1.json");

        // Add invalid content
        String content = readFileContent("src/test/resources/preload1-rpc-vfmodule.json");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(NETWORK_TOPOLOGY_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Add valid content
        content = readFileContent("src/test/resources/network-assign-rpc.json");
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(NETWORK_TOPOLOGY_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

    }

    @Test
    public void operationsGENERICRESOURCEAPIvnfTopologyOperationAssignPost() throws Exception {

        // Remove any existing service data
        configServicesRepository.deleteAll();
        operationalServicesRepository.deleteAll();

        // Load services data
        loadServicesData("src/test/resources/service1.json");

        // Add invalid content
        String content = readFileContent("src/test/resources/preload1-rpc-vfmodule.json");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(VNF_TOPOLOGY_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Add valid content
        content = readFileContent("src/test/resources/vnf-assign-rpc.json");
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(VNF_TOPOLOGY_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

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

    private String readFileContent(String path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        return content;
    }
}