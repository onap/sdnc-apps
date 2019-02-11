/*
 * ============LICENSE_START===================================================
 * Copyright (c) 2018 Amdocs
 * ============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=====================================================
 */

package org.onap.sdnc.apps.pomba.networkdiscovery.unittest.service.util;

import com.bazaarvoice.jolt.JsonUtils;
import com.bazaarvoice.jolt.exception.JsonUnmarshalException;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.Resource;
import org.onap.sdnc.apps.pomba.networkdiscovery.service.util.TransformationUtil;

public class TransformationUtilTest {

    private static final String TEST_RESOURCES = "src/test/resources/jolt/";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testTransformVServer() {

        Object sourceObject = JsonUtils.filepathToObject(TEST_RESOURCES + "vserver-input.json");

        String resultJson = TransformationUtil.transform(JsonUtils.toJsonString(sourceObject), "vserver");

        Gson gson = new Gson();
        Resource resourceInst = gson.fromJson(resultJson, Resource.class);
        String resourceInstJson = gson.toJson(resourceInst);

        Object expectedObject = JsonUtils.filepathToObject(TEST_RESOURCES + "vserver-expected.json");

        Assert.assertEquals("Json transformation result does not match expected content",
                JsonUtils.toPrettyJsonString(expectedObject),
                JsonUtils.toPrettyJsonString(JsonUtils.jsonToObject(resourceInstJson)));

    }

    @Test
    public void testTransformL3Network() {

        Object sourceObject = JsonUtils.filepathToObject(TEST_RESOURCES + "l3network-input.json");
        String resultJson = TransformationUtil.transform(JsonUtils.toJsonString(sourceObject), "l3-network");

        Gson gson = new Gson();
        Resource resourceInst = gson.fromJson(resultJson, Resource.class);
        String resourceInstJson = gson.toJson(resourceInst);

        Object expectedObject = JsonUtils.filepathToObject(TEST_RESOURCES + "l3network-expected.json");

        Assert.assertEquals("Json transformation result does not match expected content",
                JsonUtils.toPrettyJsonString(expectedObject),
                JsonUtils.toPrettyJsonString(JsonUtils.jsonToObject(resourceInstJson)));

    }
    
    @Test
    public void testTransformPort() {

        Object sourceObject = JsonUtils.filepathToObject(TEST_RESOURCES + "port-input.json");
        String resultJson = TransformationUtil.transform(JsonUtils.toJsonString(sourceObject), "port");

        Gson gson = new Gson();
        Resource resourceInst = gson.fromJson(resultJson, Resource.class);
        String resourceInstJson = gson.toJson(resourceInst);

        Object expectedObject = JsonUtils.filepathToObject(TEST_RESOURCES + "port-expected.json");

        Assert.assertEquals("Json transformation result does not match expected content",
                JsonUtils.toPrettyJsonString(expectedObject),
                JsonUtils.toPrettyJsonString(JsonUtils.jsonToObject(resourceInstJson)));

    }

    @Test
    public void testTransformFailureFileNotFound() {

        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Unable to load JSON file");

        TransformationUtil.transform("{}", "foobar");
    }

    @Test
    public void testTransformFailureInvalidInputJson() {

        expectedEx.expect(JsonUnmarshalException.class);
        expectedEx.expectMessage("Unable to unmarshal JSON");

        TransformationUtil.transform("xxx", "foobar");
    }

}
