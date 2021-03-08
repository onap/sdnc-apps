package org.onap.sdnc.apps.ms.gra.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiAllottedresourcestatusAllottedResourceStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRequestStatusEnumeration;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRpcActionEnumeration;

public class ConfigContrailRouteAllottedResourcesTest {

    ConfigContrailRouteAllottedResources configContrailRouteAllottedResources;

    @Test
    public void testOneArgContructor() {
        // No arg constructor
        configContrailRouteAllottedResources = new ConfigContrailRouteAllottedResources();
        assertEquals("",configContrailRouteAllottedResources.getAllottedResourceId());
        assertEquals("", configContrailRouteAllottedResources.getArData());
    }

    @Test
    public void testTwoArgConstructor() {

        // 2 arg constructor
        configContrailRouteAllottedResources = new ConfigContrailRouteAllottedResources("id1", "bogusData");
        assertEquals("id1", configContrailRouteAllottedResources.getAllottedResourceId());
        assertEquals("bogusData", configContrailRouteAllottedResources.getArData());
    }

    @Test
    public void testThreeArgConstructor() {
        // 3 arg constructor
        GenericResourceApiAllottedresourcestatusAllottedResourceStatus resourceStatus = new GenericResourceApiAllottedresourcestatusAllottedResourceStatus();
        resourceStatus.setAction("assign");
        resourceStatus.setFinalIndicator("true");
        resourceStatus.setRequestStatus(GenericResourceApiRequestStatusEnumeration.SYNCCOMPLETE);
        resourceStatus.setResponseCode("200");
        resourceStatus.setResponseMessage("Test message");
        resourceStatus.setRpcAction(GenericResourceApiRpcActionEnumeration.ASSIGN);
        resourceStatus.setRpcName("my-rpc");

        configContrailRouteAllottedResources = new ConfigContrailRouteAllottedResources("id1", "bogusData",resourceStatus);

        assertEquals("id1", configContrailRouteAllottedResources.getAllottedResourceId());
        assertEquals("bogusData", configContrailRouteAllottedResources.getArData());
        assertNotNull(configContrailRouteAllottedResources.getAllottedResourceStatus());
    }
    
    @Test
    public void testGetsAndSets() {
        configContrailRouteAllottedResources = new ConfigContrailRouteAllottedResources();
        GenericResourceApiAllottedresourcestatusAllottedResourceStatus resourceStatus = new GenericResourceApiAllottedresourcestatusAllottedResourceStatus();
        resourceStatus.setAction("assign");
        resourceStatus.setFinalIndicator("true");
        resourceStatus.setRequestStatus(GenericResourceApiRequestStatusEnumeration.SYNCCOMPLETE);
        resourceStatus.setResponseCode("200");
        resourceStatus.setResponseMessage("Test message");
        resourceStatus.setRpcAction(GenericResourceApiRpcActionEnumeration.ASSIGN);
        resourceStatus.setRpcName("my-rpc");

        configContrailRouteAllottedResources.setAllottedResourceId("id1");
        configContrailRouteAllottedResources.setAllottedResourceStatus(resourceStatus);
        configContrailRouteAllottedResources.setAllottedResourceStatusAction("assign");
        configContrailRouteAllottedResources.setAllottedResourceStatusFinalIndicator("true");
        configContrailRouteAllottedResources.setAllottedResourceStatusRequestStatus("SYNC_COMPLETE");
        configContrailRouteAllottedResources.setAllottedResourceStatusResponseCode("200");
        configContrailRouteAllottedResources.setAllottedResourceStatusResponseMessage("Test message");
        configContrailRouteAllottedResources.setAllottedResourceStatusResponseTimestamp("now");
        configContrailRouteAllottedResources.setAllottedResourceStatusRpcAction("assign");
        configContrailRouteAllottedResources.setAllottedResourceStatusRpcName("my-rpc");

        assertEquals("id1", configContrailRouteAllottedResources.getAllottedResourceId());
        assertNotNull(configContrailRouteAllottedResources.getAllottedResourceStatus());
        assertEquals("assign", configContrailRouteAllottedResources.getAllottedResourceStatusAction());
        assertEquals("true", configContrailRouteAllottedResources.getAllottedResourceStatusFinalIndicator());
        assertEquals("SYNC_COMPLETE", configContrailRouteAllottedResources.getAllottedResourceStatusRequestStatus());
        assertEquals("200", configContrailRouteAllottedResources.getAllottedResourceStatusResponseCode());
        assertEquals("Test message", configContrailRouteAllottedResources.getAllottedResourceStatusResponseMessage());
        assertEquals("now", configContrailRouteAllottedResources.getAllottedResourceStatusResponseTimestamp());
        assertEquals("assign", configContrailRouteAllottedResources.getAllottedResourceStatusRpcAction());
        assertEquals("my-rpc", configContrailRouteAllottedResources.getAllottedResourceStatusRpcName());
    }
}
