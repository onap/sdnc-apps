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

package org.onap.sdnc.apps.pomba.networkdiscovery.unittest.service;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.bazaarvoice.jolt.JsonUtils;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;

import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.jetty.util.security.Password;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.onap.pomba.common.datatypes.DataQuality;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.Attribute;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.NetworkDiscoveryNotification;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.NetworkDiscoveryResponse;
import org.onap.sdnc.apps.pomba.networkdiscovery.datamodel.Resource;
import org.onap.sdnc.apps.pomba.networkdiscovery.service.rs.RestService;
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
@TestPropertySource(properties = { "openstack.type.vserver.url=http://localhost:8774/v2.1/servers/{0}",
        "openstack.identity.url=http://localhost:5000/v3/auth/tokens",
        "enricher.keyStorePath=src/test/resources/client-cert-onap.p12",
        "enricher.keyStorePassword=OBF:1y0q1uvc1uum1uvg1pil1pjl1uuq1uvk1uuu1y10",
        "basicAuth.username=admin",
        "basicAuth.password=OBF:1u2a1toa1w8v1tok1u30" })

public class NetworkDiscoveryTest {
    private static final String V1 = "v1";
    private static final String APP = "junit";

    private static final String RESOURCE_TYPE_VSERVER = "vserver";
    private static final String CALLBACK_PATH = "/callback";

    private static final String AUTH = "Basic " + Base64.getEncoder().encodeToString((
            "admin:" + Password.deobfuscate("OBF:1u2a1toa1w8v1tok1u30")).getBytes());

    @Rule
    public WireMockRule identityRule = new WireMockRule(wireMockConfig().port(5000));

    @Rule
    public WireMockRule openstackRule = new WireMockRule(wireMockConfig().port(8774));

    @Rule
    public WireMockRule callbackRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Autowired
    private RestService service;

    private String transactionId = UUID.randomUUID().toString();
    private String requestId = UUID.randomUUID().toString();
    private HttpServletRequest httpRequest = Mockito.mock(HttpServletRequest.class);
    
    private static final String TEST_RESOURCES = "src/test/resources/jolt/";


    public NetworkDiscoveryTest() throws URISyntaxException {

    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testNoAuthHeader() throws Exception {
        // no Authorization header
        List<String> resourceIds = Arrays.asList(UUID.randomUUID().toString());
        Response response = this.service.findbyResourceIdAndType(this.httpRequest, V1, null, APP, this.transactionId,
                        this.requestId, RESOURCE_TYPE_VSERVER, resourceIds, getCallbackUrl());
        assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        // should get WWW-Authenticate header in response
        assertTrue(response.getHeaderString(HttpHeaders.WWW_AUTHENTICATE).startsWith("Basic realm"));
    }

    @Test
    public void testUnauthorized() throws Exception {
        String authorization = "Basic " + Base64.getEncoder().encodeToString("aaa:bbb".getBytes());
        // bad Authorization header
        List<String> resourceIds = Arrays.asList(UUID.randomUUID().toString());
        Response response = this.service.findbyResourceIdAndType(this.httpRequest, V1, authorization, APP,
                        this.transactionId, this.requestId, RESOURCE_TYPE_VSERVER, resourceIds, getCallbackUrl());
        assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        // should not get WWW-Authenticate header in response
        assertNull(response.getHeaderString(HttpHeaders.WWW_AUTHENTICATE));
    }

    @Test
    public void testNoVersion() throws Exception {
        // no Authorization header
        String authorization = "Basic " + Base64.getEncoder().encodeToString("aaa:bbb".getBytes());
        List<String> resourceIds = Arrays.asList(UUID.randomUUID().toString());
        Response response = this.service.findbyResourceIdAndType(this.httpRequest, null, authorization, APP,
                this.transactionId, this.requestId, RESOURCE_TYPE_VSERVER, resourceIds, getCallbackUrl());
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        // should get WWW-Authenticate header in response
        assertTrue(((String) response.getEntity()).contains("version"));
    }

    @Test
    public void testVerifyAppId() throws Exception {
        // no X-FromAppId header
        List<String> resourceIds = Arrays.asList(UUID.randomUUID().toString());
        Response response = this.service.findbyResourceIdAndType(this.httpRequest, V1, AUTH, null, this.transactionId,
                        this.requestId, RESOURCE_TYPE_VSERVER, resourceIds, getCallbackUrl());
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(((String) response.getEntity()).contains(ONAPLogConstants.Headers.PARTNER_NAME));
    }

    @Test
    public void testVerifyRequestId() throws Exception {
        List<String> resourceIds = Arrays.asList(UUID.randomUUID().toString());
        Response response = this.service.findbyResourceIdAndType(this.httpRequest, V1, AUTH, APP, this.transactionId,
                        null, RESOURCE_TYPE_VSERVER, resourceIds, getCallbackUrl());
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(((String) response.getEntity()).contains("requestId"));
    }

    @Test
    public void testVerifyNotificationUrl() throws Exception {
        List<String> resourceIds = Arrays.asList(UUID.randomUUID().toString());
        Response response = this.service.findbyResourceIdAndType(this.httpRequest, V1, AUTH, APP, this.transactionId,
                        this.requestId, RESOURCE_TYPE_VSERVER, resourceIds, null);
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(((String) response.getEntity()).contains("notificationURL"));
    }

    @Test
    public void testVerifyResourceIds() throws Exception {
        // no resourceIds list
        {
            List<String> resourceIds = null;
            Response response = this.service.findbyResourceIdAndType(this.httpRequest, V1, AUTH, APP,
                            this.transactionId, this.requestId, RESOURCE_TYPE_VSERVER, resourceIds, getCallbackUrl());
            assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
            assertTrue(((String) response.getEntity()).contains("resourceIds"));
        }

        // empty resourceId list
        {
            List<String> resourceIds = new ArrayList<>();
            Response response = this.service.findbyResourceIdAndType(this.httpRequest, V1, AUTH, APP,
                            this.transactionId, this.requestId, RESOURCE_TYPE_VSERVER, resourceIds, getCallbackUrl());
            assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
            assertTrue(((String) response.getEntity()).contains("resourceIds"));
        }
    }

    @Test
    public void testVerifyResourceType() throws Exception {
        // no resource type
        List<String> resourceIds = Arrays.asList(UUID.randomUUID().toString());
        Response response = this.service.findbyResourceIdAndType(this.httpRequest, V1, AUTH, APP, this.transactionId,
                        this.requestId, null, resourceIds, getCallbackUrl());
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(((String) response.getEntity()).contains("resourceType"));
    }

    @Test
    public void testVerifyInternalError() throws Exception {
        // no request
        List<String> resourceIds = Arrays.asList(UUID.randomUUID().toString());
        Response response = this.service.findbyResourceIdAndType(null, V1, AUTH, APP, this.transactionId,
                        this.requestId, null, resourceIds, getCallbackUrl());
        assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    public void testDiscoverVserver() throws Exception {
        String vserverId = UUID.randomUUID().toString();

        String resourcePath = MessageFormat.format("/v2.1/servers/{0}",
                new Object[] { vserverId });

        String identityPath = "/v3/auth/tokens";
      
        Object sourceObject = JsonUtils.filepathToObject(TEST_RESOURCES + "vserver-input.json");

        String openstackApiResponse = JsonUtils.toJsonString(sourceObject);

        this.openstackRule.stubFor(get(resourcePath).willReturn(okJson(openstackApiResponse)));

        this.identityRule.stubFor(post(identityPath).willReturn(okJson("{}").withHeader("X-Subject-Token", "tokenId")));
        
        this.callbackRule.stubFor(post(CALLBACK_PATH).willReturn(ok("Acknowledged")));

        Response response = this.service.findbyResourceIdAndType(this.httpRequest, V1, AUTH, APP, null, this.requestId,
                        RESOURCE_TYPE_VSERVER, Arrays.asList(vserverId), getCallbackUrl());

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        NetworkDiscoveryResponse entity = (NetworkDiscoveryResponse) response.getEntity();
        assertEquals(requestId, entity.getRequestId());
        assertEquals(Status.ACCEPTED.getStatusCode(), entity.getCode().intValue());
        assertEquals(Boolean.FALSE, entity.getAckFinalIndicator());

        List<ServeEvent> events = waitForRequests(this.callbackRule, 1, 10);
        LoggedRequest notificationRequest = events.get(0).getRequest();
        assertEquals(AUTH, notificationRequest.getHeader(HttpHeaders.AUTHORIZATION));
        String notificationJson = notificationRequest.getBodyAsString();

        ObjectMapper mapper = new ObjectMapper();
        AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
        mapper.setAnnotationIntrospector(introspector);
        NetworkDiscoveryNotification notification = mapper.readValue(notificationJson,
                        NetworkDiscoveryNotification.class);

        assertEquals(requestId, notification.getRequestId());
        assertEquals(Status.OK.getStatusCode(), notification.getCode().intValue());
        assertEquals(Boolean.TRUE, notification.getAckFinalIndicator());

        assertEquals(1, notification.getResources().size());
        Resource vserver = notification.getResources().get(0);
        assertEquals(vserverId, vserver.getId());
        assertEquals("vserver", vserver.getType());
        assertEquals(DataQuality.Status.ok, vserver.getDataQuality().getStatus());

        verifyAttribute(vserver.getAttributeList(), "status", "ACTIVE");
        verifyAttribute(vserver.getAttributeList(), "inMaintenance", "true");
        verifyAttribute(vserver.getAttributeList(), "hostname", "norm-bouygues");
        verifyAttribute(vserver.getAttributeList(), "vmState", "active");
    }
    
    @Test
    public void testDiscoverVserverFailure() throws Exception {
        String vserverId = UUID.randomUUID().toString();

        String resourcePath = MessageFormat.format("/v2.1/servers/{0}",
                new Object[] { vserverId });

        String identityPath = "/v3/auth/tokens";
 
        this.openstackRule.stubFor(get(resourcePath).willReturn(WireMock.notFound()));

        this.identityRule.stubFor(post(identityPath).willReturn(okJson("{}").withHeader("X-Subject-Token", "tokenId")));
        
        this.callbackRule.stubFor(post(CALLBACK_PATH).willReturn(ok("Acknowledged")));

        Response response = this.service.findbyResourceIdAndType(this.httpRequest, V1, AUTH, APP, null, this.requestId,
                        RESOURCE_TYPE_VSERVER, Arrays.asList(vserverId), getCallbackUrl());

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        NetworkDiscoveryResponse entity = (NetworkDiscoveryResponse) response.getEntity();
        assertEquals(requestId, entity.getRequestId());
        assertEquals(Status.ACCEPTED.getStatusCode(), entity.getCode().intValue());
        assertEquals(Boolean.FALSE, entity.getAckFinalIndicator());

        List<ServeEvent> events = waitForRequests(this.callbackRule, 1, 10);
        LoggedRequest notificationRequest = events.get(0).getRequest();
        assertEquals(AUTH, notificationRequest.getHeader(HttpHeaders.AUTHORIZATION));
        String notificationJson = notificationRequest.getBodyAsString();

        ObjectMapper mapper = new ObjectMapper();
        AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
        mapper.setAnnotationIntrospector(introspector);
        NetworkDiscoveryNotification notification = mapper.readValue(notificationJson,
                        NetworkDiscoveryNotification.class);

        assertEquals(requestId, notification.getRequestId());
        assertEquals(Status.OK.getStatusCode(), notification.getCode().intValue());
        assertEquals(Boolean.TRUE, notification.getAckFinalIndicator());

        assertEquals(1, notification.getResources().size());
        Resource vserver = notification.getResources().get(0);
        assertEquals(vserverId, vserver.getId());
        assertEquals("vserver", vserver.getType());
        assertEquals(DataQuality.Status.error, vserver.getDataQuality().getStatus());
        assertNull(vserver.getAttributeList());
    }


    /**
     * Verify API returns a final response indicating no discovery possible.
     */
    @Test
    public void testUnsupportedResourceType() throws Exception {

        String resourceType = "unsupported";
        List<String> resourceIds = Arrays.asList("dummyId");
        Response response = this.service.findbyResourceIdAndType(this.httpRequest, V1, AUTH, APP, this.transactionId,
                        this.requestId, resourceType, resourceIds, getCallbackUrl());
        assertEquals(Status.OK.getStatusCode(), response.getStatus());

        NetworkDiscoveryResponse entity = (NetworkDiscoveryResponse) response.getEntity();
        System.err.println("entity:" + entity);
        assertEquals(Boolean.TRUE, entity.getAckFinalIndicator());
        assertEquals(Status.NO_CONTENT.getStatusCode(), entity.getCode().intValue());
    }
    
    @Test
    public void testLoginFailure() throws Exception {
        String vserverId = UUID.randomUUID().toString();

        String identityPath = "/v3/auth/tokens";
      
        this.identityRule.stubFor(post(identityPath).willReturn(WireMock.unauthorized()));
        
        this.callbackRule.stubFor(post(CALLBACK_PATH).willReturn(ok("Acknowledged")));

        Response response = this.service.findbyResourceIdAndType(this.httpRequest, V1, AUTH, APP, null, this.requestId,
                        RESOURCE_TYPE_VSERVER, Arrays.asList(vserverId), getCallbackUrl());

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        NetworkDiscoveryResponse entity = (NetworkDiscoveryResponse) response.getEntity();
        assertEquals(requestId, entity.getRequestId());
        assertEquals(Status.ACCEPTED.getStatusCode(), entity.getCode().intValue());
        assertEquals(Boolean.FALSE, entity.getAckFinalIndicator());

        List<ServeEvent> events = waitForRequests(this.callbackRule, 1, 10);
        LoggedRequest notificationRequest = events.get(0).getRequest();
        assertEquals(AUTH, notificationRequest.getHeader(HttpHeaders.AUTHORIZATION));
        String notificationJson = notificationRequest.getBodyAsString();

        ObjectMapper mapper = new ObjectMapper();
        AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
        mapper.setAnnotationIntrospector(introspector);
        NetworkDiscoveryNotification notification = mapper.readValue(notificationJson,
                        NetworkDiscoveryNotification.class);

        assertEquals(requestId, notification.getRequestId());
        assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), notification.getCode().intValue());
        assertEquals(Boolean.TRUE, notification.getAckFinalIndicator());
        assertNull(notification.getResources());
    }


    private void verifyAttribute(List<Attribute> attributeList, String attrName, String attrValue) {
        for (Attribute attr : attributeList) {
            if (attr.getName().equals(attrName)) {
                assertEquals("Unexpected value for attribute " + attrName, attrValue, attr.getValue());
                return;
            }
        }
        fail("Attribute " + attrName + " not found");
    }

    private List<ServeEvent> waitForRequests(WireMockRule service, int minRequests, long timeoutSeconds)
                    throws InterruptedException {

        long remaining = timeoutSeconds * 1000L;
        long retryInterval = Math.min(remaining / 5, 1000);
        while (true) {
            List<ServeEvent> events = service.getAllServeEvents();
            if (events.size() >= minRequests) {
                return events;
            }
            if (remaining <= 0) {
                fail("Timeout waiting for " + minRequests + " requests");
            }
            Thread.sleep(retryInterval);
            remaining -= retryInterval;
        }
    }

    private String getCallbackUrl() {
        return "http://localhost:" + this.callbackRule.port() + CALLBACK_PATH;
    }
}
