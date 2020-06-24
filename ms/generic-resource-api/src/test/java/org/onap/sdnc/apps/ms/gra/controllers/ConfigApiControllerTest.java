package org.onap.sdnc.apps.ms.gra.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadData;
import org.onap.sdnc.apps.ms.gra.data.ConfigPreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigServicesRepository;
import org.onap.sdnc.apps.ms.gra.swagger.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={ConfigApiController.class})
public class ConfigApiControllerTest {


    @Autowired
    ConfigApiController apiController;

    @Autowired
    ConfigPreloadDataRepository configPreloadDataRepository;

    @Autowired
    ConfigServicesRepository configServicesRepository;


    @Test
    public void configGENERICRESOURCEAPIpreloadInformationDelete() {
        ResponseEntity<Void> responseEntity = apiController.configGENERICRESOURCEAPIpreloadInformationDelete();


       assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
       assertEquals(0, configPreloadDataRepository.count());
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGet() {
        ResponseEntity<GenericResourceApiPreloadModelInformation> responseEntity = apiController.configGENERICRESOURCEAPIpreloadInformationGet();
        assert(responseEntity.getStatusCode() == HttpStatus.OK || responseEntity.getStatusCode() == HttpStatus.NOT_FOUND);
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationPost() throws IOException {

        configPreloadDataRepository.deleteAll();


        ResponseEntity<Void> respEntity = apiController.configGENERICRESOURCEAPIpreloadInformationPost(readPreloadModelInfo());


        assertEquals(HttpStatus.OK, respEntity.getStatusCode());
        assertEquals(1, configPreloadDataRepository.count());

    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationPut() throws IOException {
        configPreloadDataRepository.deleteAll();
        assertEquals(0, configPreloadDataRepository.count());

        ResponseEntity<Void> respEntity = apiController.configGENERICRESOURCEAPIpreloadInformationPut(readPreloadModelInfo());

        assertEquals(HttpStatus.OK, respEntity.getStatusCode());
        assertEquals(1, configPreloadDataRepository.count());

    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPost() throws IOException {
        configPreloadDataRepository.deleteAll();
        assertEquals(0, configPreloadDataRepository.count());

        ResponseEntity<Void> respEntity = apiController.configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPost(readPreloadListItem());

        assertEquals(HttpStatus.OK, respEntity.getStatusCode());
        assertEquals(1, configPreloadDataRepository.count());
    }

    @Test
    public void configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeDelete() {
        configPreloadDataRepository.deleteAll();
        assertEquals(0, configPreloadDataRepository.count());

        ConfigPreloadData preloadData = new ConfigPreloadData();
        String preloadId = "test1";
        String preloadType = "network";

        preloadData.setPreloadId(preloadId);
        preloadData.setPreloadType(preloadType);
        configPreloadDataRepository.save(preloadData);
        assertEquals(1, configPreloadDataRepository.count());

        ResponseEntity<Void> respEntity = apiController.configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeDelete(preloadId, preloadType);
        assertEquals(HttpStatus.OK, respEntity.getStatusCode());
        assertEquals(0, configPreloadDataRepository.count());

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

    private GenericResourceApiPreloadModelInformation readPreloadModelInfo() throws IOException {

        String path = "src/test/resources/preload-net-model-info.json";
        String content = new String(Files.readAllBytes(Paths.get(path)));
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(content, GenericResourceApiPreloadModelInformation.class);
    }

    private GenericResourceApiPreloadmodelinformationPreloadList readPreloadListItem() throws IOException {

        String path = "src/test/resources/preload-net-list-item.json";
        String content = new String(Files.readAllBytes(Paths.get(path)));
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(content, GenericResourceApiPreloadmodelinformationPreloadList.class);
    }
}