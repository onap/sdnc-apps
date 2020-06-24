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

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity(name = "OPERATIONAL_PRELOAD_DATA")
@Table(name="OPERATIONAL_PRELOAD_DATA")
@IdClass(PreloadDataKey.class)
public class OperationalPreloadData {

    @Id
    @Length(max = 100)
    @Column(length = 100)
    private String preloadId;

    @Id
    @Length(max = 25)
    @Column(length = 25)
    private String preloadType;

    @Lob
    @Column(columnDefinition = "clob")
    private String preloadData;

    public OperationalPreloadData() {
        this.preloadId = "";
        this.preloadType = "";
        this.preloadData = "";
    }

    public OperationalPreloadData(String preloadId, String preloadType, String preloadData) {
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
