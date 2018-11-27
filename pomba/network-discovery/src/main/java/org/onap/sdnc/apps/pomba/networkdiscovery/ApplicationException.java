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
package org.onap.sdnc.apps.pomba.networkdiscovery;

import javax.ws.rs.core.Response.Status;

public class ApplicationException extends Exception {
    public enum Error {
        GENERAL_FAILURE("NET.0001", "An error occurred: %s"),
        MISSING_PARAM("NET.0002", "Missing required parameter %s"),
        UNAUTHORIZED("NET.0003", "Unauthorized");

        private final String responseCode;
        private final String message;

        private Error(String responseCode, String message) {
            this.responseCode = responseCode;
            this.message = message;
        }

        public String getMessage(Object... args) {
            return String.format(this.message, args);
        }

        public String getResponseCode() {
            return this.responseCode;
        }
    }

    private static final long serialVersionUID = -4874149714911165454L;

    private final Status httpStatus;
    private final String responseCode;

    public ApplicationException(Error errorCode, Status httpStatus, Object... args) {
        super(errorCode.getMessage(args));
        if (httpStatus == null) {
            throw new NullPointerException("httpStatus");
        }

        this.responseCode = errorCode.getResponseCode();
        this.httpStatus = httpStatus;
    }

    public Status getHttpStatus() {
        return this.httpStatus;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

}
