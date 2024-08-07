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

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import jakarta.transaction.Transactional;

@Transactional
public interface OperationalServicesRepository extends CrudRepository<OperationalServices, Long> {

    List<OperationalServices> findBySvcInstanceId(String svcInstanceId);
    long deleteBySvcInstanceId(String svcInstanceId);

}

