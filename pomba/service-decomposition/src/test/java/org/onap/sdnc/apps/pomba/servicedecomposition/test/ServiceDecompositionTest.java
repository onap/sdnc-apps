/*
 *  ============LICENSE_START===================================================
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

package org.onap.sdnc.apps.pomba.servicedecomposition.test;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.eclipse.jetty.util.security.Password;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.onap.sdnc.apps.pomba.servicedecomposition.service.rs.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@WebAppConfiguration
@SpringBootTest
@TestPropertySource(properties = {
        "aai.httpProtocol=http",
        "aai.serviceName=localhost",
        "aai.servicePort=8081",
        "basicAuth.username=admin",
        "basicAuth.password=OBF:1u2a1toa1w8v1tok1u30"
    })

public class ServiceDecompositionTest {

    private static final String AUTH = "Basic " + Base64.getEncoder().encodeToString((
            "admin:" + Password.deobfuscate("OBF:1u2a1toa1w8v1tok1u30")).getBytes());

    private HttpServletRequest httpRequest = Mockito.mock(HttpServletRequest.class);

    // TODO missing code coverage for VNFC resources

    @Rule
    public WireMockRule aai = new WireMockRule(wireMockConfig().port(8081));

    @Autowired
    private RestService service;

    /**
     * Simulation of the firewall demo service, using json captured from real A&AI queries.
     * The resource relationship looks something like.
     * <pre>
     * service instance
     * +- generic-vnf 1
     * |  +- vserver
     * +- generic-vnf 2
     *    +- network 1
     *    +- network 2
     * </pre>
     */
    @Test
    public void testDemoFirewallService() throws Exception {
        // setup A&AI responses
        addResponse(
                "/aai/v13/nodes/service-instance/c6456519-6acf-4adb-997c-3c363dd4caaf?depth=2",
                "junit/aai-service-instance.json");
        addResponse(
                "/aai/v13/network/generic-vnfs/generic-vnf/6700c313-fbb7-4cf9-ac70-0293ec56df68?depth=2",
                "junit/aai-generic-vnf1.json");
        addResponse(
                "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39?depth=2",
                "junit/aai-generic-vnf2.json");
        addResponse(
                "/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/CloudOwner/RegionOne/tenants/tenant"
                        + "/b49b830686654191bb1e952a74b014ad/vservers/vserver/b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a?depth=2",
                "junit/aai-vserver.json");
        addResponse(
                "/aai/v13/network/l3-networks/l3-network/HNP1d77c-1094-41ec-b7f3-94bb30951870?depth=2",
                "junit/aai-l3-network1.json");
        addResponse(
                "/aai/v13/network/l3-networks/l3-network/HNP1d77c-1094-41ec-b7f3-94bb30951872?depth=2",
                "junit/aai-l3-network2.json");

        final String serviceInstanceId = "c6456519-6acf-4adb-997c-3c363dd4caaf";
        final String vnfId1 = "6700c313-fbb7-4cf9-ac70-0293ec56df68";
        final String vnfId2 = "8a9ddb25-2e79-449c-a40d-5011bac0da39";
        final String vserverId = "b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a";
        final String networkId1 = "HNP1d77c-1094-41ec-b7f3-94bb30951870";
        final String networkId2 = "HNP1d77c-1094-41ec-b7f3-94bb30951872";

        Response response = this.service.getContext(httpRequest, AUTH, "network-discovery-context-builder", null,
                serviceInstanceId);
        assertEquals(200, response.getStatus());

        JSONObject serviceInstance = new JSONObject((String)response.getEntity());

        // verify two generic-vnfs added to service instance data
        verifyResource(serviceInstance, "generic-vnfs", "vnf-id", vnfId1);
        verifyResource(serviceInstance, "generic-vnfs", "vnf-id", vnfId2);

        JSONArray vnfs = serviceInstance.getJSONArray("generic-vnfs");
        for (int i = 0; i < vnfs.length(); i++) {
            JSONObject vnf = vnfs.getJSONObject(i);
            String vnfId = vnf.getString("vnf-id");
            switch (vnfId) {
                case vnfId1:
                    // verify vserver resource
                    verifyResource(vnf, "vservers", "vserver-id", vserverId);
                    break;
                case vnfId2:
                    // verify network resources
                    verifyResource(vnf, "l3-networks", "network-id", networkId1);
                    verifyResource(vnf, "l3-networks", "network-id", networkId2);
                    break;
                default:
                    fail("Unexpected generic-vnf " + vnfId);
            }
        }
    }

    @Test
    public void testDemoFirewallServiceWithL3Networks() throws Exception {
        // setup A&AI responses
        addResponse(
                "/aai/v13/nodes/service-instance/c6456519-6acf-4adb-997c-3c363dd4caaf?depth=2",
                "junit/aai-service-instance2.json");
        addResponse(
                "/aai/v13/network/generic-vnfs/generic-vnf/6700c313-fbb7-4cf9-ac70-0293ec56df68?depth=2",
                "junit/aai-generic-vnf1.json");
        addResponse(
                "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39?depth=2",
                "junit/aai-generic-vnf2.json");
        addResponse(
                "/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/CloudOwner/RegionOne/tenants/tenant"
                        + "/b49b830686654191bb1e952a74b014ad/vservers/vserver/b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a?depth=2",
                "junit/aai-vserver.json");
        addResponse(
                "/aai/v13/network/l3-networks/l3-network/HNP1d77c-1094-41ec-b7f3-94bb30951870?depth=2",
                "junit/aai-l3-network1.json");
        addResponse(
                "/aai/v13/network/l3-networks/l3-network/HNP1d77c-1094-41ec-b7f3-94bb30951872?depth=2",
                "junit/aai-l3-network2.json");

        final String serviceInstanceId = "c6456519-6acf-4adb-997c-3c363dd4caaf";
        final String vnfId1 = "6700c313-fbb7-4cf9-ac70-0293ec56df68";
        final String vnfId2 = "8a9ddb25-2e79-449c-a40d-5011bac0da39";
        final String vserverId = "b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a";
        final String networkId1 = "HNP1d77c-1094-41ec-b7f3-94bb30951870";
        final String networkId2 = "HNP1d77c-1094-41ec-b7f3-94bb30951872";

        Response response = this.service.getContext(httpRequest, AUTH, "network-discovery-context-builder", null,
                serviceInstanceId);
        assertEquals(200, response.getStatus());

        JSONObject serviceInstance = new JSONObject((String)response.getEntity());

        // verify two generic-vnfs added to service instance data
        verifyResource(serviceInstance, "generic-vnfs", "vnf-id", vnfId1);
        verifyResource(serviceInstance, "generic-vnfs", "vnf-id", vnfId2);
        verifyResource(serviceInstance, "l3-networks", "network-id", networkId1);

        JSONArray vnfs = serviceInstance.getJSONArray("generic-vnfs");
        for (int i = 0; i < vnfs.length(); i++) {
            JSONObject vnf = vnfs.getJSONObject(i);
            String vnfId = vnf.getString("vnf-id");
            switch (vnfId) {
                case vnfId1:
                    // verify vserver resource
                    verifyResource(vnf, "vservers", "vserver-id", vserverId);
                    break;
                case vnfId2:
                    // verify network resources
                    verifyResource(vnf, "l3-networks", "network-id", networkId1);
                    verifyResource(vnf, "l3-networks", "network-id", networkId2);
                    JSONObject vfmodules = vnf.getJSONObject("vf-modules");
                    JSONArray vfmoduleList = vfmodules.getJSONArray("vf-module");
                    for (int j = 0; j < vfmoduleList.length(); j++) {
                        JSONObject vfmodule = vfmoduleList.getJSONObject(j);
                        verifyResource(vfmodule, "l3-networks", "network-id", networkId1);
                    }
                    break;
                default:
                    fail("Unexpected generic-vnf " + vnfId);
            }
        }
    }

    @Test
    public void testNoAuthHeader() throws Exception {
        String fromAppId = "junit";
        String transactionId = null;
        String serviceInstanceId = "aServiceInstanceId";
        // no Authorization header
        Response response = this.service.getContext(httpRequest, null, fromAppId, transactionId,
                serviceInstanceId);
        assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        // should get WWW-Authenticate header in response
        assertTrue(response.getHeaderString(HttpHeaders.WWW_AUTHENTICATE).startsWith("Basic realm"));
    }

    @Test
    public void testUnauthorized() throws Exception {
        // bad credentials
        String authorization = "Basic " + Base64.getEncoder().encodeToString("aaa:bbb".getBytes());
        String fromAppId = "junit";
        String transactionId = null;
        String serviceInstanceId = "aServiceInstanceId";

        Response response = this.service.getContext(httpRequest, authorization, fromAppId,
                transactionId, serviceInstanceId);
        assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        // should not get WWW-Authenticate header in response
        assertNull(response.getHeaderString(HttpHeaders.WWW_AUTHENTICATE));
    }

    /** Fail if calling app name is missing. */
    @Test
    public void verifyFromAppId() throws Exception {
        String fromAppId = null;
        String transactionId = null;
        String serviceInstanceId = "someValue";

        Response response = this.service.getContext(httpRequest, AUTH, fromAppId, transactionId,
                serviceInstanceId);
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(((String)response.getEntity()).contains(ONAPLogConstants.Headers.PARTNER_NAME));
    }

    /** Fail if service instance id is missing. */
    @Test
    public void verifyServiceInstanceId() throws Exception {
        String fromAppId = "junit";
        String transactionId = null;
        String serviceInstanceId = null;

        Response response = this.service.getContext(httpRequest, AUTH, fromAppId, transactionId,
                serviceInstanceId);
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(((String)response.getEntity()).contains("service-instance-id"));
    }

    /** Unknown service-instance-id return HTTP 404. */
    @Test
    public void testInvalidServiceId() throws Exception {
        aai.stubFor(get("/aai/v13/nodes/service-instance/noSuchServiceId?depth=2").willReturn(notFound()));

        Response response =
                this.service.getContext(httpRequest, AUTH, "junit", null, "noSuchServiceId");

        assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    private void verifyResource(JSONObject parent, String arrayName, String field, String value) {
        JSONArray array = parent.getJSONArray(arrayName);
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            if (value.equals(item.getString(field))) {
                return;
            }
        }
        fail("Did not find " + field + "=" + value + " in " + arrayName + " array");
    }

    private void addResponse(String path, String classpathResource) throws IOException {
        String payload = readFully(ClassLoader.getSystemResourceAsStream(classpathResource));
        aai.stubFor(get(path).willReturn(okJson(payload)));
    }

    private String readFully(InputStream in) throws IOException {
        char[] cbuf = new char[1024];
        StringBuilder content = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(in, "UTF-8")) {
            int count;
            while ((count = reader.read(cbuf)) >= 0) {
                content.append(cbuf, 0, count);
            }
        }
        return content.toString();
    }
}