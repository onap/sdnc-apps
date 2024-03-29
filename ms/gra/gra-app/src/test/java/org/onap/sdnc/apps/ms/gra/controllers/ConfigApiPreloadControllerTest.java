package org.onap.sdnc.apps.ms.gra.controllers;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.sdnc.apps.ms.gra.GenericResourceMsApp;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadData;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadDataRepository;
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
public class ConfigApiPreloadControllerTest {

    private final static String CONFIG_PRELOAD_URL = "/config/GENERIC-RESOURCE-API:preload-information/";
    private final static String CONFIG_PRELOAD_LIST_URL = "/config/GENERIC-RESOURCE-API:preload-information/preload-list/";


    @Autowired
    private MockMvc mvc;

    @Autowired
    ConfigPreloadDataRepository configPreloadDataRepository;

    @BeforeClass
    public static void setUp() throws Exception {
        System.out.println("ConfigApiPreloadControllerTest: Setting serviceLogicProperties, serviceLogicDirectory and sdnc.config.dir");
        System.setProperty("serviceLogicProperties", "src/test/resources/svclogic.properties");
        System.setProperty("serviceLogicDirectory", "src/test/resources/svclogic");
        System.setProperty("sdnc.config.dir", "src/test/resources");
   
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationDelete() throws Exception {

        // Clean up data
        configPreloadDataRepository.deleteAll();

        // Load test data
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
        // Clean up data
        configPreloadDataRepository.deleteAll();

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
        // Clean up data
        configPreloadDataRepository.deleteAll();

        String content = readFileContent("src/test/resources/preload1-net-model-info.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_PRELOAD_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());

        // Test with existing data - should return 409
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_PRELOAD_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(409, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());

        // Clean up data
        configPreloadDataRepository.deleteAll();

    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationPut() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        String content = readFileContent("src/test/resources/preload1-net-model-info.json");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_PRELOAD_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPost() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        String content = readFileContent("src/test/resources/preload1-net-list-item.json");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_PRELOAD_LIST_URL).contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configPreloadDataRepository.count());

    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeDelete() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        // Test with data
        loadData(CONFIG_PRELOAD_URL, "src/test/resources/preload1-net-model-info.json");
        assert(configPreloadDataRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_PRELOAD_LIST_URL+"preload1/network/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(0, configPreloadDataRepository.count());

        // Test without data
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_PRELOAD_LIST_URL+"preload1/network/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(0, configPreloadDataRepository.count());
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGet() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        // Test with data
        loadData(CONFIG_PRELOAD_URL, "src/test/resources/preload1-net-model-info.json");
        assert(configPreloadDataRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_PRELOAD_LIST_URL+"preload1/network/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with no data
        configPreloadDataRepository.deleteAll();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_PRELOAD_LIST_URL+"preload1/network/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypePreloadDataPreloadNetworkTopologyInformationGet() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        // Test with data
        loadData(CONFIG_PRELOAD_URL, "src/test/resources/preload1-net-model-info.json");
        assert(configPreloadDataRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_PRELOAD_LIST_URL+"preload1/network/preload-data/preload-network-topology-information/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with no data
        configPreloadDataRepository.deleteAll();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_PRELOAD_LIST_URL+"preload1/network/preload-data/preload-network-topology-information/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }


    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypePostNoData() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        String content = readFileContent("src/test/resources/preload1-net-list-item.json");

        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_PRELOAD_LIST_URL+"preload1/network/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());

    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypePostExistingData() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();
        // Test with data
        loadData(CONFIG_PRELOAD_URL, "src/test/resources/preload1-net-model-info.json");
        assert(configPreloadDataRepository.count() > 0);

        String content = readFileContent("src/test/resources/preload1-net-list-item.json");

        // Test with existing data - should return 409
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_PRELOAD_LIST_URL+"preload1/network/").contentType(MediaType.APPLICATION_JSON).content(content))
                .andReturn();
        assertEquals(409, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());

    }


    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypePut() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        String goodContent = readFileContent("src/test/resources/preload1-net-list-item.json");


        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_PRELOAD_LIST_URL+"preload1/network/").contentType(MediaType.APPLICATION_JSON).content(goodContent))
                .andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());

        // Test with existing data - should return 204
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_PRELOAD_LIST_URL+"preload1/network/").contentType(MediaType.APPLICATION_JSON).content(goodContent))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());

    }


    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataDelete() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        // Test with data
        loadData(CONFIG_PRELOAD_URL, "src/test/resources/preload1-net-model-info.json");
        assert(configPreloadDataRepository.count() > 0);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_PRELOAD_LIST_URL+"preload1/network/preload-data/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());

        // Test without data
        configPreloadDataRepository.deleteAll();
        assertEquals(0, configPreloadDataRepository.count());
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(CONFIG_PRELOAD_LIST_URL+"preload1/network/preload-data/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configPreloadDataRepository.count());

    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataGet() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        // Test with data
        loadData(CONFIG_PRELOAD_URL, "src/test/resources/preload1-net-model-info.json");
        assert(configPreloadDataRepository.count() > 0);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_PRELOAD_LIST_URL+"preload1/network/preload-data/").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        // Test with no data
        configPreloadDataRepository.deleteAll();
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(CONFIG_PRELOAD_LIST_URL+"preload1/network/preload-data").contentType(MediaType.APPLICATION_JSON).content(""))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataPostNoData() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        String goodContent = readFileContent("src/test/resources/preload1-net-preload-data.json");


        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_PRELOAD_LIST_URL+"preload1/network/preload-data/").contentType(MediaType.APPLICATION_JSON).content(goodContent))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configPreloadDataRepository.count());


    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataPostBadContent() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        String badContent = readFileContent("src/test/resources/preload1-net-model-info.json");

        // Load single entry with no preloadData
        ConfigPreloadData preloadData = new ConfigPreloadData();
        preloadData.setPreloadId("preload1");
        preloadData.setPreloadType("network");
        preloadData.setPreloadData(null);
        configPreloadDataRepository.save(preloadData);


    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataPostGoodData() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        String goodContent = readFileContent("src/test/resources/preload1-net-preload-data.json");

        // Load single entry with no preloadData
        ConfigPreloadData preloadData = new ConfigPreloadData();
        preloadData.setPreloadId("preload1");
        preloadData.setPreloadType("network");
        preloadData.setPreloadData(null);
        configPreloadDataRepository.save(preloadData);


        // Test with no existing preload data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_PRELOAD_LIST_URL+"preload1/network/preload-data/").contentType(MediaType.APPLICATION_JSON).content(goodContent))
                .andReturn();

        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataPostExistingRecord() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        String goodContent = readFileContent("src/test/resources/preload1-net-preload-data.json");


        // Test with data
        loadData(CONFIG_PRELOAD_URL, "src/test/resources/preload1-net-model-info.json");
        assert(configPreloadDataRepository.count() > 0);


        // Test with existing preload dat
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(CONFIG_PRELOAD_LIST_URL+"preload1/network/preload-data/").contentType(MediaType.APPLICATION_JSON).content(goodContent))
                .andReturn();
        assertEquals(409, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());
    }


    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataPutNoData() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        String goodContent = readFileContent("src/test/resources/preload1-net-preload-data.json");


        // Test with no data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_PRELOAD_LIST_URL+"preload1/network/preload-data/").contentType(MediaType.APPLICATION_JSON).content(goodContent))
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals(0, configPreloadDataRepository.count());
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataPutBadContent() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        String badContent = readFileContent("src/test/resources/preload1-net-model-info.json");

        // Load single entry with no preloadData
        ConfigPreloadData preloadData = new ConfigPreloadData();
        preloadData.setPreloadId("preload1");
        preloadData.setPreloadType("network");
        preloadData.setPreloadData(null);
        configPreloadDataRepository.save(preloadData);

    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataPutGoodData() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        String goodContent = readFileContent("src/test/resources/preload1-net-preload-data.json");

        // Load single entry with no preloadData
        ConfigPreloadData preloadData = new ConfigPreloadData();
        preloadData.setPreloadId("preload1");
        preloadData.setPreloadType("network");
        preloadData.setPreloadData(null);
        configPreloadDataRepository.save(preloadData);


        // Test with no existing preload data
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_PRELOAD_LIST_URL+"preload1/network/preload-data/").contentType(MediaType.APPLICATION_JSON).content(goodContent))
                .andReturn();

        assertEquals(201, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataPutExistingRecord() throws Exception {
        // Clean up data
        configPreloadDataRepository.deleteAll();

        String goodContent = readFileContent("src/test/resources/preload1-net-preload-data.json");


        // Test with data
        loadData(CONFIG_PRELOAD_URL, "src/test/resources/preload1-net-model-info.json");
        assert(configPreloadDataRepository.count() > 0);


        // Test with existing preload dat
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(CONFIG_PRELOAD_LIST_URL+"preload1/network/preload-data/").contentType(MediaType.APPLICATION_JSON).content(goodContent))
                .andReturn();
        assertEquals(204, mvcResult.getResponse().getStatus());
        assertEquals(1, configPreloadDataRepository.count());
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