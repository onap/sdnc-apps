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

import static org.junit.Assert.assertThat;

import com.bazaarvoice.jolt.JsonUtils;
import com.bazaarvoice.jolt.exception.JsonUnmarshalException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.Attribute;
import org.onap.sdnc.apps.pomba.networkdiscovery.service.util.TransformationUtil;

public class TransformationUtilTest {

    private static final String TEST_RESOURCES = "src/test/resources/jolt/";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testTransformVServer() {

        Object sourceObject = JsonUtils.filepathToObject(TEST_RESOURCES + "vserver-input.json");

        String resultJson = TransformationUtil.transform(JsonUtils.toJsonString(sourceObject), "vserver");

        Object expectedObject = JsonUtils.filepathToObject(TEST_RESOURCES + "vserver-expected.json");

        Assert.assertEquals("Json transformation result does not match expected content",
                JsonUtils.toPrettyJsonString(expectedObject),
                JsonUtils.toPrettyJsonString(JsonUtils.jsonToObject(resultJson)));

    }

    @Test
    public void testTransformL3Network() {

        Object sourceObject = JsonUtils.filepathToObject(TEST_RESOURCES + "l3network-input.json");
        String resultJson = TransformationUtil.transform(JsonUtils.toJsonString(sourceObject), "l3-network");

        Object expectedObject = JsonUtils.filepathToObject(TEST_RESOURCES + "l3network-expected.json");

        Assert.assertEquals("Json transformation result does not match expected content",
                JsonUtils.toPrettyJsonString(expectedObject),
                JsonUtils.toPrettyJsonString(JsonUtils.jsonToObject(resultJson)));

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

    @Test
    public void testToAttributeList() {
        Map<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("name", "norm_bouygues");
        expectedAttributes.put("hostId", "ea1660efbbedda164379afacdc622305c4b88cebfb84119472d286a8");
        expectedAttributes.put("hostStatus", "UP");
        expectedAttributes.put("id", "2c311eae-f542-4173-8a01-582922abd495");
        expectedAttributes.put("status", "ACTIVE");
        expectedAttributes.put("vmState", "active");
        expectedAttributes.put("hostname", "norm-bouygues");
        expectedAttributes.put("inMaintenance", "true");
        expectedAttributes.put("imageId", "c0022890-d91f-422c-91c5-3866edeae768");
        expectedAttributes.put("tenantId", "15ad36d394e744838e947ca90609f805");
        expectedAttributes.put("host", "Setup-NCSO-OTT-E-C2");

        Object inputJson = JsonUtils.filepathToObject(TEST_RESOURCES + "vserver-expected.json");
        List<Attribute> resultAttributeList = TransformationUtil.toAttributeList(JsonUtils.toJsonString(inputJson));

        Map<String, String> resultAttributes = new HashMap<>();

        for (Attribute attribute : resultAttributeList) {
            resultAttributes.put(attribute.getName(), attribute.getValue());
        }
        assertThat(expectedAttributes, CoreMatchers.is(resultAttributes));
    }

    @Test
    public void testToAttributeListNullJsonValue() {
        Map<String, String> expectedAttributes = new HashMap<String, String>();
        expectedAttributes.put("name", "");

        String inputJson = "{\"server\": { \"name\": null }}";

        List<Attribute> resultAttributeList = TransformationUtil.toAttributeList(inputJson);

        Map<String, String> resultAttributes = new HashMap<>();

        for (Attribute attribute : resultAttributeList) {
            resultAttributes.put(attribute.getName(), attribute.getValue());
        }
        assertThat(expectedAttributes, CoreMatchers.is(resultAttributes));
    }

}
