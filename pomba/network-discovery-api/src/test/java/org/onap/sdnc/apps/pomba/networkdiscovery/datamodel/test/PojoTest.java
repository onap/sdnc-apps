/*
 *  ============LICENSE_START===================================================
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

package org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.onap.pomba.common.datatypes.DataQuality;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.Attribute;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.NetworkDiscoveryNotification;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.Resource;

public class PojoTest {

    @Test
    public void fromJson() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        NetworkDiscoveryNotification notification = objectMapper.readValue(
                ClassLoader.getSystemResourceAsStream("notification.json"),
                NetworkDiscoveryNotification.class);

        System.out.println(notification);

        assertEquals("1", notification.getRequestId());
        assertEquals(200, notification.getCode().intValue());
        assertEquals(true, notification.getAckFinalIndicator());
        assertEquals(1, notification.getResources().size());
        Resource vserver = notification.getResources().get(0);
        assertEquals("25fb07ab-0478-465e-a021-6384ac299671", vserver.getId());
        assertEquals("vserver", vserver.getType());
        assertEquals(DataQuality.Status.ok, vserver.getDataQuality().getStatus());
        assertNull(vserver.getDataQuality().getErrorText());

        Map<String, String> expectedValues = new HashMap<>();
        expectedValues.put("vserver-id", "25fb07ab-0478-465e-a021-6384ac299671");
        expectedValues.put("power-state", "1");
        expectedValues.put("vm-state", "active");
        expectedValues.put("status", "ACTIVE");
        expectedValues.put("host-status", "UNKNOWN");
        expectedValues.put("updated", "2017-11-20T04:26:13Z");
        expectedValues.put("disk-allocation-gb", ".010");
        expectedValues.put("memory-usage-mb", "null");
        expectedValues.put("cpu-util-percent", ".043");
        expectedValues.put("retrieval-timestamp", "2018-06-25 18:02:55 +0000");

        for (Attribute attribute : vserver.getAttributeList()) {
            assertEquals(expectedValues.remove(attribute.getName()), attribute.getValue());
            assertEquals(DataQuality.Status.ok, attribute.getDataQuality().getStatus());
        }
        assertEquals(0, expectedValues.size());
    }
}
