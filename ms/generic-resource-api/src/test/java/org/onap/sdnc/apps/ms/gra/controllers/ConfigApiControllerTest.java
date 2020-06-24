package org.onap.sdnc.apps.ms.gra.controllers;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.sdnc.apps.ms.gra.core.GenericResourceMsApp;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadData;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigServicesRepository;
import org.onap.sdnc.apps.ms.gra.swagger.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={GenericResourceMsApp.class})
@AutoConfigureMockMvc
public class ConfigApiControllerTest {

    private final static String CONFIG_PRELOAD_URL = "/config/GENERIC-RESOURCE-API:preload-information/";

    @Autowired
    private MockMvc mvc;

    @Autowired
    ConfigPreloadDataRepository configPreloadDataRepository;

    @Autowired
    ConfigServicesRepository configServicesRepository;

    @BeforeClass
    public static void setUp() throws Exception {
        System.setProperty("serviceLogicProperties", "src/test/resources/svclogic.properties");
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationDelete() throws Exception {


        // Test with data
        loadData(CONFIG_PRELOAD_URL, "src/test/resources/preload1-net-model-info.json");

        assertEquals(1, configPreloadDataRepository.count());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_PRELOAD_URL).contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(0, configPreloadDataRepository.count());

        // Test with no data
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_PRELOAD_URL).contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGet() throws Exception {

        // Test with data
        loadData(CONFIG_PRELOAD_URL, "src/test/resources/preload1-net-model-info.json");
        assert(configPreloadDataRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_PRELOAD_URL).contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with no data
        configPreloadDataRepository.deleteAll();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_PRELOAD_URL).contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());

    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationPost() throws Exception {


        String content = readFileContent("src/test/resources/preload1-net-model-info.json");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_PRELOAD_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());

        // Clean up data
        configPreloadDataRepository.deleteAll();

    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationPut() throws Exception {

        String content = readFileContent("src/test/resources/preload1-net-model-info.json");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_PRELOAD_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());

        // Clean up data
        configPreloadDataRepository.deleteAll();


    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPost() throws Exception {
        String content = readFileContent("src/test/resources/preload1-net-model-info.json");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_PRELOAD_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());

        // Clean up data
        configPreloadDataRepository.deleteAll();
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeDelete() {


    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGet() {
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypePost() {
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataDelete() {
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataGet() {
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataPost() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesDelete() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGet() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesPost() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesPut() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIservicePost() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdDelete() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGet() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdPost() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdPut() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataDelete() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataGet() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataPost() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceDataPut() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusDelete() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusGet() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusPost() {
    }

    @Test
    public void configGENERICRESOURCEAPIservicesGENERICRESOURCEAPIserviceServiceInstanceIdGENERICRESOURCEAPIserviceStatusPut() {
    }

    private String readFileContent(String path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        return content;
    }

    private void deleteData(String url) throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(url).contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
    }

    private void loadData(String url, String path) throws Exception {
        String content = readFileContent(path);
        mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();


    }
}