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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ConfigPreloadDataRepository extends CrudRepository<ConfigPreloadData, Long> {

    List<ConfigPreloadData> findByPreloadIdAndPreloadType(String preloadId, String preloadType);
    long deleteByPreloadIdAndPreloadType(String preloadId, String preloadType);

}

