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
package org.onap.sdnc.apps.pomba.servicedecomposition.exception;

import javax.ws.rs.core.Response.Status;

public class DiscoveryException extends Exception {
    public enum Error {
        FETCH_RESOURCE_FAILED("SD.0001", "A&AI query failed: %s"),
        RELATIONSHIP_LINK_PARSE_ERROR("SD.0002", "Error in parsing relationship link"),
        SERVICE_INSTANCE_NOT_FOUND("SD.0003", "Service Instance not Found"),
        SERVICE_RELATIONSHIP_PARSE_ERROR("SD.0004", "Error in parsing service-instance relationships: %s"),
        INVALID_URL("SD.0005", "Invalid request URL, missing parameter: service-instance-id"),
        MISSING_HEADER("SD.0006", "Missing header parameter: %s"),
        GENERAL_FAILURE("SD.0007", "An error occurred: %s"),
        UNAUTHORIZED("SD.00008", "Unauthorized");

        private final String responseCode;
        private final String message;

        private Error(String responseCode, String message) {
            this.responseCode = responseCode;
            this.message = message;
        }

        public String getMessage(Object... args) {
            if (args == null || args.length == 0) {
                return this.message;
            }
            return String.format(this.message, args);
        }

        public String getResponseCode() {
            return this.responseCode;
        }
    }

    private static final long serialVersionUID = -4874149714911165454L;

    private final Status httpStatus;
    private String responseCode;

    public DiscoveryException(Error error, Status httpStatus, Object... args) {
        super(error.getMessage(args));
        if (httpStatus == null) {
            throw new NullPointerException("httpStatus");
        }
        this.responseCode = error.getResponseCode();
        this.httpStatus = httpStatus;
    }

    public DiscoveryException(Error error, Exception cause, Object... args) {
        super(error.getMessage(args), cause);
        this.httpStatus = Status.INTERNAL_SERVER_ERROR;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public Status getHttpStatus() {
        return this.httpStatus;
    }
}
