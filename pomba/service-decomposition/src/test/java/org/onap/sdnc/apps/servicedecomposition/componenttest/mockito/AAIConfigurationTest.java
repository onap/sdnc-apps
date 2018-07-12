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
package org.onap.sdnc.apps.servicedecomposition.componenttest.mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.onap.sdnc.apps.pomba.servicedecomposition.AAIConfiguration;

public class AAIConfigurationTest {

      AAIConfiguration aaiConfiguration = mock(AAIConfiguration.class);

       @Test
       public void testGetUrl() {
       String url = "www.google.com";
       when(aaiConfiguration.getURL()).thenReturn(url);
       assertEquals(aaiConfiguration.getURL().toString(),"www.google.com");
       }

       @Test
       public void testGetServiceInstancePathL() {
       String msg = "Hello User!";
       when(aaiConfiguration.getserviceInstancePathL()).thenReturn(msg);
       assertEquals(aaiConfiguration.getserviceInstancePathL().toString(),"Hello User!");
       }
}
