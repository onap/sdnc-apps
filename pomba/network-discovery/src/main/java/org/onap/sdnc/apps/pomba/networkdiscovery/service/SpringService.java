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
package org.onap.sdnc.apps.pomba.networkdiscovery.service;

import java.util.List;
import org.onap.logging.ref.slf4j.ONAPLogAdapter;
import org.onap.sdnc.apps.pomba.networkdiscovery.ApplicationException;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.NetworkDiscoveryResponse;

public interface SpringService {

    public NetworkDiscoveryResponse findbyResourceIdAndType(String transactionId,
                                                            String requestId,
                                                            String resourceType,
                                                            List<String> resourceIds,
                                                            String notificationURL,
                                                            String notificationAuthorization,
                                                            ONAPLogAdapter adapter)
    throws ApplicationException;
}
