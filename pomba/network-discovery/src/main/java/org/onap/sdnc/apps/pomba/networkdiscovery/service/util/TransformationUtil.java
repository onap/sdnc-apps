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

import java.io.File;
import java.util.List;

public class TransformationUtil {

    private static final String CONFIG_JOLT = "config/jolt";

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

}