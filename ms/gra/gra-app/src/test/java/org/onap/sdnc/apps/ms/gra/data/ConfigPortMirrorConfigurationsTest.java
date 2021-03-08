package org.onap.sdnc.apps.ms.gra.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiConfigurationstatusConfigurationStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRequestStatusEnumeration;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRpcActionEnumeration;

public class ConfigPortMirrorConfigurationsTest {
    ConfigPortMirrorConfigurations portMirrorConfigurations;

    @Test
    public void testNoArgConstructor() {
        portMirrorConfigurations = new ConfigPortMirrorConfigurations();
        assertEquals("", portMirrorConfigurations.getConfigurationId());
        assertEquals("", portMirrorConfigurations.getPmcData());
    }

    @Test
    public void testTwoArgConstructor() {
        portMirrorConfigurations = new ConfigPortMirrorConfigurations("id1", "bogusData");
        assertEquals("id1", portMirrorConfigurations.getConfigurationId());
        assertEquals("bogusData", portMirrorConfigurations.getPmcData());
    }

    @Test
    public void testThreeArgConstructor() {
        GenericResourceApiConfigurationstatusConfigurationStatus configStatus = new GenericResourceApiConfigurationstatusConfigurationStatus();
        configStatus.setAction("assign");
        configStatus.setFinalIndicator("true");
        configStatus.setRequestStatus(GenericResourceApiRequestStatusEnumeration.SYNCCOMPLETE);
        configStatus.setResponseCode("200");
        configStatus.setResponseMessage("Test Message");
        configStatus.setRpcAction(GenericResourceApiRpcActionEnumeration.ASSIGN);
        configStatus.setRpcName("my-rpc");

        portMirrorConfigurations = new ConfigPortMirrorConfigurations("id1", "bogusData", configStatus);
        assertEquals("id1", portMirrorConfigurations.getConfigurationId());
        assertEquals("bogusData", portMirrorConfigurations.getPmcData());
        assertNotNull(portMirrorConfigurations.getPortMirrorConfigurationStatusRequestStatus());
        
    }

    @Test
    public void testGettersAndSetters() {
        GenericResourceApiConfigurationstatusConfigurationStatus configStatus = new GenericResourceApiConfigurationstatusConfigurationStatus();
        configStatus.setAction("assign");
        configStatus.setFinalIndicator("true");
        configStatus.setRequestStatus(GenericResourceApiRequestStatusEnumeration.SYNCCOMPLETE);
        configStatus.setResponseCode("200");
        configStatus.setResponseMessage("Test Message");
        configStatus.setRpcAction(GenericResourceApiRpcActionEnumeration.ASSIGN);
        configStatus.setRpcName("my-rpc");
        portMirrorConfigurations = new ConfigPortMirrorConfigurations();

        portMirrorConfigurations.setConfigurationId("id1");
        portMirrorConfigurations.setPmcData("bogusData");
        portMirrorConfigurations.setPortMirrorConfigurationStatus(configStatus);
        portMirrorConfigurations.setPortMirrorConfigurationStatusAction("assign");
        portMirrorConfigurations.setPortMirrorConfigurationStatusFinalIndicator("true");
        portMirrorConfigurations.setPortMirrorConfigurationStatusRequestStatus("SYNCCOMPLETE");
        portMirrorConfigurations.setPortMirrorConfigurationStatusResponseCode("200");
        portMirrorConfigurations.setPortMirrorConfigurationStatusResponseTimestamp("now");
        portMirrorConfigurations.setPortMirrorConfigurationStatusRpcAction("assign");
        portMirrorConfigurations.setPortMirrorConfigurationStatusRpcName("my-rpc");

        assertEquals("id1", portMirrorConfigurations.getConfigurationId());
        assertEquals("bogusData", portMirrorConfigurations.getPmcData());
        assertNotNull(portMirrorConfigurations.getPortMirrorConfigurationStatus());
        assertEquals("assign", portMirrorConfigurations.getPortMirrorConfigurationStatusAction());
        assertEquals("true", portMirrorConfigurations.getPortMirrorConfigurationStatusFinalIndicator());
        assertEquals("SYNCCOMPLETE", portMirrorConfigurations.getPortMirrorConfigurationStatusRequestStatus());
        assertEquals("200", portMirrorConfigurations.getPortMirrorConfigurationStatusResponseCode());
        assertEquals("now", portMirrorConfigurations.getPortMirrorConfigurationStatusResponseTimestamp());
        assertEquals("assign", portMirrorConfigurations.getPortMirrorConfigurationStatusRpcAction());
        assertEquals("my-rpc", portMirrorConfigurations.getPortMirrorConfigurationStatusRpcName());


    }
}
