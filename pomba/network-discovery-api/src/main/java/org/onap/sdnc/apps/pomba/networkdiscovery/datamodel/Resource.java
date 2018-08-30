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
package org.onap.sdnc.apps.pomba.networkdiscovery.datamodel;

import java.util.List;
import org.onap.pomba.common.datatypes.DataQuality;

public class Resource {
    private String id;
    private String name;
    private String type;
    private DataQuality dataQuality;
    private List<Attribute> attributeList;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Attribute> getAttributeList() {
        return this.attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    public DataQuality getDataQuality() {
        return this.dataQuality;
    }

    public void setDataQuality(DataQuality dataQuality) {
        this.dataQuality = dataQuality;
    }

    @Override
    public String toString() {
        return "Resource [id=" + this.id + ", name=" + this.name + ", type=" + this.type + ", dataQuality="
                + this.dataQuality + ", attributeList=" + this.attributeList + "]";
    }
}
