package org.onap.sdnc.apps.ms.gra.swagger.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class GenericResourceApiNetworkinstancegroupNetworkInstanceGroupTest {

    @Test
    public void testNetworkInstanceGroup() {
        GenericResourceApiNetworkinstancegroupNetworkInstanceGroup netGroup1 = createTestInstance();
        GenericResourceApiNetworkinstancegroupNetworkInstanceGroup netGroup2 = createTestInstance();

        assertTrue(netGroup1.equals(netGroup2));
        assertEquals(netGroup1.toString(), netGroup2.toString());

    }


    private GenericResourceApiNetworkinstancegroupNetworkInstanceGroup createTestInstance() {
        GenericResourceApiNetworkinstancegroupNetworkInstanceGroup netGroup = new GenericResourceApiNetworkinstancegroupNetworkInstanceGroup();

        netGroup.setNetworkInstanceGroupFunction("function1");
        netGroup.networkInstanceGroupFunction(netGroup.getNetworkInstanceGroupFunction());
        netGroup.setNetworkInstanceGroupId("123");
        netGroup.networkInstanceGroupId(netGroup.getNetworkInstanceGroupId());
        netGroup.setServiceInstanceId("service123");
        netGroup.serviceInstanceId(netGroup.getServiceInstanceId());
        assertEquals("function1", netGroup.getNetworkInstanceGroupFunction());
        assertEquals("123", netGroup.getNetworkInstanceGroupId());
        assertEquals("service123", netGroup.getServiceInstanceId());

        GenericResourceApiOnapmodelinformationOnapModelInformation modelInfo = new GenericResourceApiOnapmodelinformationOnapModelInformation();
        modelInfo.setModelCustomizationUuid("123456");
        modelInfo.setModelInvariantUuid("123456");
        modelInfo.setModelName("model_t");
        modelInfo.setModelUuid("123456");
        modelInfo.setModelVersion("1");
        netGroup.setOnapModelInformation(modelInfo);
        netGroup.onapModelInformation(netGroup.getOnapModelInformation());
        assertEquals("123456", netGroup.getOnapModelInformation().getModelCustomizationUuid());
        assertEquals("123456", netGroup.getOnapModelInformation().getModelInvariantUuid());
        assertEquals("model_t", netGroup.getOnapModelInformation().getModelName());
        assertEquals("123456", netGroup.getOnapModelInformation().getModelUuid());
        assertEquals("1", netGroup.getOnapModelInformation().getModelVersion());

        GenericResourceApiNetworkinstancegroupNetworkinstancegroupAggregateRoutePolicy aggregateRoutePolicy = new GenericResourceApiNetworkinstancegroupNetworkinstancegroupAggregateRoutePolicy();
        aggregateRoutePolicy.setAggregateRoutePolicyName("agPolicy1");
        aggregateRoutePolicy.setIpv4AggregateRouteLevel("1");
        aggregateRoutePolicy.setIpv6AggregateRouteLevel("2");
        netGroup.setAggregateRoutePolicy(aggregateRoutePolicy);
        netGroup.aggregateRoutePolicy(netGroup.getAggregateRoutePolicy());
        assertEquals("agPolicy1", netGroup.getAggregateRoutePolicy().getAggregateRoutePolicyName());
        assertEquals("1", netGroup.getAggregateRoutePolicy().getIpv4AggregateRouteLevel());
        assertEquals("2", netGroup.getAggregateRoutePolicy().getIpv6AggregateRouteLevel());

        GenericResourceApiAggregateroutesAggregateRoutes agItem = new GenericResourceApiAggregateroutesAggregateRoutes();
        agItem.setCidrMask("12");
        agItem.setIpVersion("ipv4");
        agItem.setRouteId("route1");
        agItem.setStartAddress("127.0.0.0");
        netGroup.addAggregateRoutesItem(agItem);
        netGroup.setAggregateRoutes(netGroup.getAggregateRoutes());
        netGroup.aggregateRoutes(netGroup.getAggregateRoutes());
        List<GenericResourceApiAggregateroutesAggregateRoutes> agRoutes = netGroup.getAggregateRoutes();
        assertEquals(1, agRoutes.size());
        assertEquals("12", agRoutes.get(0).getCidrMask());
        assertEquals("ipv4", agRoutes.get(0).getIpVersion());
        assertEquals("route1", agRoutes.get(0).getRouteId());
        assertEquals("127.0.0.0", agRoutes.get(0).getStartAddress());

        GenericResourceApiSubnetsSubnets subnetItem = new GenericResourceApiSubnetsSubnets();
        subnetItem.setAddrFromStart(GenericResourceApiAddrFromStartEnumeration.Y);
        subnetItem.setCidrMask("12");
        subnetItem.setDhcpEnabled(GenericResourceApiDhcpEnabledEnumeration.Y);
        subnetItem.setDhcpStartAddress("10.1.1.1");
        subnetItem.setDhcpEndAddress("10.1.1.127");
        subnetItem.setGatewayAddress("10.1.1.255");
        subnetItem.setIpVersion("ipv4");
        subnetItem.setStartAddress("10.1.1.0");
        subnetItem.setSubnetName("subnet1");
        subnetItem.setSubnetRole("client");
        netGroup.addSubnetsItem(subnetItem);
        netGroup.setSubnets(netGroup.getSubnets());
        netGroup.subnets(netGroup.getSubnets());
        List<GenericResourceApiSubnetsSubnets> subnets = netGroup.getSubnets();
        assertEquals(1, subnets.size());
        assertEquals(GenericResourceApiAddrFromStartEnumeration.Y, subnets.get(0).getAddrFromStart());
        assertEquals("12", subnets.get(0).getCidrMask());
        assertEquals(GenericResourceApiDhcpEnabledEnumeration.Y, subnets.get(0).getDhcpEnabled());
        assertEquals("10.1.1.1", subnets.get(0).getDhcpStartAddress());
        assertEquals("10.1.1.127", subnets.get(0).getDhcpEndAddress());
        assertEquals("ipv4", subnets.get(0).getIpVersion());
        assertEquals("10.1.1.0", subnets.get(0).getStartAddress());
        assertEquals("subnet1", subnets.get(0).getSubnetName());
        assertEquals("client", subnets.get(0).getSubnetRole());

        GenericResourceApiNetworkinstancegroupNetworkinstancegroupSubnetAssignmentPolicy subnetAssignmentPolicy = new GenericResourceApiNetworkinstancegroupNetworkinstancegroupSubnetAssignmentPolicy();
        subnetAssignmentPolicy.setSubnetUse("optional");
        netGroup.setSubnetAssignmentPolicy(subnetAssignmentPolicy);
        netGroup.subnetAssignmentPolicy(netGroup.getSubnetAssignmentPolicy());
        assertEquals("optional", netGroup.getSubnetAssignmentPolicy().getSubnetUse());

        GenericResourceApiNetworkinstancegroupNetworkinstancegroupVpnBindingPolicy vpnBindingPolicy = new GenericResourceApiNetworkinstancegroupNetworkinstancegroupVpnBindingPolicy();
        vpnBindingPolicy.setGlobalRouteTarget("target1");
        vpnBindingPolicy.setRouteTargetRole("role1");
        vpnBindingPolicy.setVpnBinding("binding1");
        vpnBindingPolicy.setVpnName("vpn1");
        vpnBindingPolicy.setVpnPlatform("platform1");
        vpnBindingPolicy.setVpnType("type1");
        netGroup.setVpnBindingPolicy(vpnBindingPolicy);
        netGroup.vpnBindingPolicy(netGroup.getVpnBindingPolicy());
        assertEquals("target1", netGroup.getVpnBindingPolicy().getGlobalRouteTarget());
        assertEquals("role1", netGroup.getVpnBindingPolicy().getRouteTargetRole());
        assertEquals("binding1", netGroup.getVpnBindingPolicy().getVpnBinding());
        assertEquals("vpn1", netGroup.getVpnBindingPolicy().getVpnName());
        assertEquals("platform1", netGroup.getVpnBindingPolicy().getVpnPlatform());
        assertEquals("type1", netGroup.getVpnBindingPolicy().getVpnType());

        GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks networks = new GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks();
        GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork networkItem = new GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork();
        networkItem.setNetworkId("123");
        networkItem.setNetworkStatus("active");
        networkItem.setNeutronNetworkId("123");
        networks.addNetworkItem(networkItem);
        netGroup.setNetworks(networks);
        netGroup.networks(netGroup.getNetworks());
        assertEquals(1, netGroup.getNetworks().getNetwork().size());
        assertEquals("123", netGroup.getNetworks().getNetwork().get(0).getNetworkId());
        assertEquals("123", netGroup.getNetworks().getNetwork().get(0).getNeutronNetworkId());
        assertEquals("active", netGroup.getNetworks().getNetwork().get(0).getNetworkStatus());

        return(netGroup);
    }

}
