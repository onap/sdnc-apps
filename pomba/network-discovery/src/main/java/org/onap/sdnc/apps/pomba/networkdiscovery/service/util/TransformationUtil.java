/*
 * Copyright 2014 Bazaarvoice, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onap.sdnc.apps.pomba.networkdiscovery.service.util;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.onap.pomba.common.datatypes.DataQuality;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.Attribute;

public class TransformationUtil {

    private static final String CONFIG_JOLT = "config/jolt";
    private static final String EMPTY_STRING = "";

    private TransformationUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Transforms the sourceJson using the jolt specification for the given resourceType.
     * 
     * @param sourceJson
     * @param resourceType
     * @return
     */
    public static String transform(String sourceJson, String resourceType) {

        Object sourceObject = JsonUtils.jsonToObject(sourceJson);

        List<Object> chainrSpecJSON = JsonUtils.filepathToList(CONFIG_JOLT + File.separator + resourceType + ".json");
        Chainr chainr = Chainr.fromSpec(chainrSpecJSON);
        Object output = chainr.transform(sourceObject);

        return JsonUtils.toJsonString(output);
    }

    /**
     * Converts the second level of JsonElements from the given json to a list of
     * Network Discovery Attributes.
     * 
     * @param json
     * @return
     */
    public static List<Attribute> toAttributeList(String json) {

        JsonParser parser = new JsonParser();
        JsonElement elem = parser.parse(json);

        Set<Entry<String, JsonElement>> entrySet = elem.getAsJsonObject().entrySet();

        List<Attribute> result = new ArrayList<>();

        Iterator<Entry<String, JsonElement>> iter = entrySet.iterator();
        while (iter.hasNext()) {
            Entry<String, JsonElement> next = iter.next();

            JsonElement vserverElem = next.getValue();
            Set<Entry<String, JsonElement>> vserverEntrySet = vserverElem.getAsJsonObject().entrySet();
            Iterator<Entry<String, JsonElement>> vserverIter = vserverEntrySet.iterator();
            while (vserverIter.hasNext()) {
                Entry<String, JsonElement> vserverNext = vserverIter.next();
                Attribute attr = new Attribute();
                attr.setName(vserverNext.getKey());
                if (vserverNext.getValue().isJsonNull()) {
                    attr.setValue(EMPTY_STRING);
                } else {
                    attr.setValue(vserverNext.getValue().getAsString());
                }
                attr.setDataQuality(DataQuality.ok());
                result.add(attr);
            }
        }
        return result;
    }

}