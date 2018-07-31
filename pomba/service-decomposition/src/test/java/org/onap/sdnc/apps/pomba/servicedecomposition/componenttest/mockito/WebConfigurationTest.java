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

package org.onap.sdnc.apps.pomba.servicedecomposition.componenttest.mockito;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.onap.sdnc.apps.pomba.servicedecomposition.WebConfiguration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class WebConfigurationTest {
    @Test
    public void runTest() {
        WebConfiguration webConfiguration = new WebConfiguration();
        WebMvcConfigurerAdapter webMvcConfigurerAdapter = webConfiguration.forwardToIndex();
        ViewResolverRegistry registry = mock(ViewResolverRegistry.class);
        webMvcConfigurerAdapter.configureViewResolvers(registry);
        assertEquals(registry.hasRegistrations(), false);
    }

}
