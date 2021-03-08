package org.onap.sdnc.apps.ms.gra.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiAllottedresourcestatusAllottedResourceStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRequestStatusEnumeration;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRpcActionEnumeration;

public class OperationalContrailRouteAllottedResourcesTest {

    OperationalContrailRouteAllottedResources operationalContrailRouteAllottedResources;

    @Test
    public void testOneArgContructor() {
        // No arg constructor
        operationalContrailRouteAllottedResources = new OperationalContrailRouteAllottedResources();
        assertEquals("",operationalContrailRouteAllottedResources.getAllottedResourceId());
        assertEquals("", operationalContrailRouteAllottedResources.getArData());
    }

    @Test
    public void testTwoArgConstructor() {

        // 2 arg constructor
        operationalContrailRouteAllottedResources = new OperationalContrailRouteAllottedResources("id1", "bogusData");
        assertEquals("id1", operationalContrailRouteAllottedResources.getAllottedResourceId());
        assertEquals("bogusData", operationalContrailRouteAllottedResources.getArData());
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

        operationalContrailRouteAllottedResources = new OperationalContrailRouteAllottedResources("id1", "bogusData",resourceStatus);

        assertEquals("id1", operationalContrailRouteAllottedResources.getAllottedResourceId());
        assertEquals("bogusData", operationalContrailRouteAllottedResources.getArData());
        assertNotNull(operationalContrailRouteAllottedResources.getAllottedResourceStatus());
    }
    
    @Test
    public void testGetsAndSets() {
        operationalContrailRouteAllottedResources = new OperationalContrailRouteAllottedResources();
        GenericResourceApiAllottedresourcestatusAllottedResourceStatus resourceStatus = new GenericResourceApiAllottedresourcestatusAllottedResourceStatus();
        resourceStatus.setAction("assign");
        resourceStatus.setFinalIndicator("true");
        resourceStatus.setRequestStatus(GenericResourceApiRequestStatusEnumeration.SYNCCOMPLETE);
        resourceStatus.setResponseCode("200");
        resourceStatus.setResponseMessage("Test message");
        resourceStatus.setRpcAction(GenericResourceApiRpcActionEnumeration.ASSIGN);
        resourceStatus.setRpcName("my-rpc");

        operationalContrailRouteAllottedResources.setAllottedResourceId("id1");
        operationalContrailRouteAllottedResources.setAllottedResourceStatus(resourceStatus);
        operationalContrailRouteAllottedResources.setAllottedResourceStatusAction("assign");
        operationalContrailRouteAllottedResources.setAllottedResourceStatusFinalIndicator("true");
        operationalContrailRouteAllottedResources.setAllottedResourceStatusRequestStatus("SYNC_COMPLETE");
        operationalContrailRouteAllottedResources.setAllottedResourceStatusResponseCode("200");
        operationalContrailRouteAllottedResources.setAllottedResourceStatusResponseMessage("Test message");
        operationalContrailRouteAllottedResources.setAllottedResourceStatusResponseTimestamp("now");
        operationalContrailRouteAllottedResources.setAllottedResourceStatusRpcAction("assign");
        operationalContrailRouteAllottedResources.setAllottedResourceStatusRpcName("my-rpc");

        assertEquals("id1", operationalContrailRouteAllottedResources.getAllottedResourceId());
        assertNotNull(operationalContrailRouteAllottedResources.getAllottedResourceStatus());
        assertEquals("assign", operationalContrailRouteAllottedResources.getAllottedResourceStatusAction());
        assertEquals("true", operationalContrailRouteAllottedResources.getAllottedResourceStatusFinalIndicator());
        assertEquals("SYNC_COMPLETE", operationalContrailRouteAllottedResources.getAllottedResourceStatusRequestStatus());
        assertEquals("200", operationalContrailRouteAllottedResources.getAllottedResourceStatusResponseCode());
        assertEquals("Test message", operationalContrailRouteAllottedResources.getAllottedResourceStatusResponseMessage());
        assertEquals("now", operationalContrailRouteAllottedResources.getAllottedResourceStatusResponseTimestamp());
        assertEquals("assign", operationalContrailRouteAllottedResources.getAllottedResourceStatusRpcAction());
        assertEquals("my-rpc", operationalContrailRouteAllottedResources.getAllottedResourceStatusRpcName());
    }
}
