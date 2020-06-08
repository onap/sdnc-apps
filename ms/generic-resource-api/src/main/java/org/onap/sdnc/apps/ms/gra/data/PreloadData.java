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
package org.onap.sdnc.apps.ms.gra.data;

import javax.persistence.*;

@Entity(name = "PRELOAD_DATA")
@Table(name="PRELOAD_DATA")
public class PreloadData {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String preloadId;

    private String preloadType;

    @Lob
    private String preloadData;

    public PreloadData() {
        this.preloadId = "";
        this.preloadType = "";
        this.preloadData = "";
    }

    public PreloadData(String preloadId, String preloadType, String preloadData) {
        this.preloadId = preloadId;
        this.preloadType = preloadType;
        this.preloadData = preloadData;
    }

    public String getPreloadId() {
        return preloadId;
    }

    public void setPreloadId(String preloadId) {
        this.preloadId = preloadId;
    }

    public String getPreloadType() {
        return preloadType;
    }

    public void setPreloadType(String preloadType) {
        this.preloadType = preloadType;
    }

    public String getPreloadData() {
        return preloadData;
    }

    public void setPreloadData(String preloadData) {
        this.preloadData = preloadData;
    }
}
