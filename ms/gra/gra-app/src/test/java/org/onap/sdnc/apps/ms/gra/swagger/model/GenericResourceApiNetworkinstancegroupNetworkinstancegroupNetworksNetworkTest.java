package org.onap.sdnc.apps.ms.gra.swagger.model;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;

public class GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetworkTest {

    @Test
    public void testCreateInstance() {
        GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork network = new GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork();
        
        network.setNetworkId("123");
        network.networkId("123");
        network.setNetworkStatus("active");
        network.networkStatus("active");
        network.setNeutronNetworkId("123");
        network.neutronNetworkId("123");
        network.setVlanTagId(123L);
        network.vlanTagId(123L);

        GenericResourceApiVpnbindingsVpnBindings vpnBinding = new GenericResourceApiVpnbindingsVpnBindings();
        vpnBinding.setAicZone("aic1");
        vpnBinding.setGlobalRouteTarget("target1");
        vpnBinding.setRouteTargetRole("role1");
        vpnBinding.setVpnBindingId("123");
        vpnBinding.setVpnName("name1");
        network.addVpnBindingsItem(vpnBinding);
        List<GenericResourceApiVpnbindingsVpnBindings> vpnBindings = network.getVpnBindings();
        network.setVpnBindings(vpnBindings);
        network.vpnBindings(vpnBindings);

        assertEquals("123", network.getNetworkId());
        assertEquals("active", network.getNetworkStatus());
        assertEquals("123", network.getNeutronNetworkId());
        assertEquals((Long) 123L, network.getVlanTagId());
        


    }
}
