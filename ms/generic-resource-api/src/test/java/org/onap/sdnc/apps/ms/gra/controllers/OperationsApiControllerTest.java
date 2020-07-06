package org.onap.sdnc.apps.ms.gra.controllers;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.sdnc.apps.ms.gra.core.GenericResourceMsApp;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigServicesRepository;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={GenericResourceMsApp.class})
@AutoConfigureMockMvc
@Transactional
public class OperationsApiControllerTest {

    private final static String PRELOAD_NETWORK_URL = "/operations/GENERIC-RESOURCE-API:preload-network-topology-operation/";
    private final static String PRELOAD_VFMODULE_URL = "/operations/GENERIC-RESOURCE-API:preload-vf-module-topology-operation/";


    @Autowired
    private MockMvc mvc;

    @Autowired
    ConfigPreloadDataRepository configPreloadDataRepository;

    @BeforeClass
    public static void setUp() throws Exception {
        System.setProperty("serviceLogicProperties", "src/test/resources/svclogic.properties");
        System.setProperty("serviceLogicDirectory", "target/docker-stage/opt/onap/sdnc/svclogic/graphs/generic-resource-api");
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

    private String readFileContent(String path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        return content;
    }
}