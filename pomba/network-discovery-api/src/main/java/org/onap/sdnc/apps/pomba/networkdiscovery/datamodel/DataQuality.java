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

public class DataQuality {
    public static enum Status {
        ok, error
    }

    private Status status;
    private String errorText;

    
    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getErrorText() {
        return this.errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }


    public static DataQuality ok() {
        // as a non-mutable class, it is not safe to define a constant for this
        DataQuality result = new DataQuality();
        result.setStatus(Status.ok);
        return result;
    }

    public static DataQuality error(String text) {
        // as a non-mutable class, it is not safe to define a constant for this
        DataQuality result = new DataQuality();
        result.setStatus(Status.error);
        result.setErrorText(text);
        return result;
    }

    @Override
    public String toString() {
        return "DataQuality [status=" + this.status + ", errorText=" + this.errorText + "]";
    }
}
