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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.sdnc.apps.ms.gra.data.PreloadData;
import org.onap.sdnc.apps.ms.gra.data.PreloadDataRepository;
import org.onap.sdnc.apps.ms.gra.swagger.ConfigApi;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadModelInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloaddataPreloadData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadmodelinformationPreloadList;
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

@Controller
@ComponentScan(basePackages = {"org.onap.sdnc.apps.ms.gra.springboot.*"})
@EntityScan("org.onap.sdnc.apps.ms.gra.springboot.*")
public class ConfigApiController implements ConfigApi {
    private static final Logger log = LoggerFactory.getLogger(ConfigApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private PreloadDataRepository preloadDataRepository;

    @Autowired
    public ConfigApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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
        preloadDataRepository.deleteAll();
        return (new ResponseEntity<>(HttpStatus.OK));
    }

    @Override
    public ResponseEntity<GenericResourceApiPreloadModelInformation> configGENERICRESOURCEAPIpreloadInformationGet() {
        GenericResourceApiPreloadModelInformation genericResourceApiPreloadModelInformation = new GenericResourceApiPreloadModelInformation();

        preloadDataRepository.findAll().forEach(preloadData -> {
            GenericResourceApiPreloadmodelinformationPreloadList preloadListItem = new GenericResourceApiPreloadmodelinformationPreloadList();

            preloadListItem.setPreloadId(preloadData.getPreloadId());
            preloadListItem.setPreloadType(preloadData.getPreloadType());
            try {
                preloadListItem.setPreloadData(objectMapper.readValue(preloadData.getPreloadData(), GenericResourceApiPreloaddataPreloadData.class));
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
                    preloadDataRepository.save(new PreloadData(curItem.getPreloadId(), curItem.getPreloadType(), objectMapper.writeValueAsString(curItem.getPreloadData())));
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
            preloadDataRepository.save(new PreloadData(preloadListItem.getPreloadId(), preloadListItem.getPreloadType(), objectMapper.writeValueAsString(preloadListItem.getPreloadData())));
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeDelete(String preloadId, String preloadType) {
        List<PreloadData> preloadData = preloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);

        if (preloadData != null) {
            Iterator<PreloadData> iter = preloadData.iterator();
            while (iter.hasNext()) {
                preloadDataRepository.delete(iter.next());
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResourceApiPreloadmodelinformationPreloadList> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGet(String preloadId, String preloadType) {
        List<PreloadData> preloadData = preloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);
        if (preloadData != null) {
            if (!preloadData.isEmpty()) {
                PreloadData preloadDataItem = preloadData.get(0);
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
            preloadDataRepository.save(new PreloadData(preloadId, preloadType, objectMapper.writeValueAsString(preloadListItem.getPreloadData())));
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Void> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataDelete(String preloadId, String preloadType) {
        List<PreloadData> preloadData = preloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);

        if (preloadData != null) {
            Iterator<PreloadData> iter = preloadData.iterator();

            while (iter.hasNext()) {
                preloadDataRepository.delete(iter.next());
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResourceApiPreloaddataPreloadData> configGENERICRESOURCEAPIpreloadInformationGENERICRESOURCEAPIpreloadListPreloadIdPreloadTypeGENERICRESOURCEAPIpreloadDataGet(String preloadId, String preloadType) {
        List<PreloadData> preloadData = preloadDataRepository.findByPreloadIdAndPreloadType(preloadId, preloadType);
        if (preloadData != null) {
            if (!preloadData.isEmpty()) {
                PreloadData preloadDataItem = preloadData.get(0);
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
        try {
            preloadDataRepository.save(new PreloadData(preloadId, preloadType, objectMapper.writeValueAsString(preloadData)));
        } catch (JsonProcessingException e) {
            log.error("Cannot convert preload data", e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
