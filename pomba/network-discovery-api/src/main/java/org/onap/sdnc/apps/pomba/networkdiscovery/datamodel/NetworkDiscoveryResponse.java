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

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NetworkDiscoveryResponse {
    /** The originating request identifier */
    private String requestId;
    /** Result code - 200 indicates success */
    private Integer code;
    private String message;

    /**
     * @return false if further asynchronous notifications will follow.
     */
    private Boolean ackFinalIndicator;

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer responseCode) {
        this.code = responseCode;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String responseMessage) {
        this.message = responseMessage;
    }

    public Boolean getAckFinalIndicator() {
        return this.ackFinalIndicator;
    }

    public void setAckFinalIndicator(Boolean ackFinalIndicator) {
        this.ackFinalIndicator = ackFinalIndicator;
    }

    @Override
    public String toString() {
        return "NetworkDiscoveryResponse [requestId=" + this.requestId + ", code=" + this.code + ", message="
                + this.message + ", ackFinalIndicator=" + this.ackFinalIndicator
                + "]";
    }
}
