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
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NetworkDiscoveryNotification extends NetworkDiscoveryResponse {
    private List<Resource> resources;

    public List<Resource> getResources() {
        return this.resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public String toString() {
        return "NetworkDiscoveryResponse [requestId=" + getRequestId() + ", code=" + getCode() + ", message="
                + getMessage() + ", ackFinalIndicator=" + getAckFinalIndicator() + ", resources=" + this.resources
                + "]";
    }
}
