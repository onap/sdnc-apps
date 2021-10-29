package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiHostRoutes;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiHostroutesHostRoutes;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkPolicy;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkTopologyIdentifierStructure;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkpolicyNetworkPolicy;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiProviderNetworkInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRouteTableReference;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRoutetablereferenceRouteTableReference;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiSubnets;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiSubnetsSubnets;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVpnBindings;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVpnbindingsVpnBindings;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("preload-network-topology-information")
public class GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation   {
  @JsonProperty("host-routes")
  @Valid
  private List<GenericResourceApiHostroutesHostRoutes> hostRoutes = null;

  @JsonProperty("network-policy")
  @Valid
  private List<GenericResourceApiNetworkpolicyNetworkPolicy> networkPolicy = null;

  @JsonProperty("network-topology-identifier-structure")
  private GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure networkTopologyIdentifierStructure = null;

  @JsonProperty("physical-network-name")
  private String physicalNetworkName = null;

  @JsonProperty("is-external-network")
  private Boolean isExternalNetwork = null;

  @JsonProperty("is-shared-network")
  private Boolean isSharedNetwork = null;

  @JsonProperty("is-provider-network")
  private Boolean isProviderNetwork = null;

  @JsonProperty("route-table-reference")
  @Valid
  private List<GenericResourceApiRoutetablereferenceRouteTableReference> routeTableReference = null;

  @JsonProperty("subnets")
  @Valid
  private List<GenericResourceApiSubnetsSubnets> subnets = null;

  @JsonProperty("vpn-bindings")
  @Valid
  private List<GenericResourceApiVpnbindingsVpnBindings> vpnBindings = null;

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation hostRoutes(List<GenericResourceApiHostroutesHostRoutes> hostRoutes) {
    this.hostRoutes = hostRoutes;
    return this;
  }

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation addHostRoutesItem(GenericResourceApiHostroutesHostRoutes hostRoutesItem) {
    if (this.hostRoutes == null) {
      this.hostRoutes = new ArrayList<>();
    }
    this.hostRoutes.add(hostRoutesItem);
    return this;
  }

  /**
   * Get hostRoutes
   * @return hostRoutes
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<GenericResourceApiHostroutesHostRoutes> getHostRoutes() {
    return hostRoutes;
  }

  public void setHostRoutes(List<GenericResourceApiHostroutesHostRoutes> hostRoutes) {
    this.hostRoutes = hostRoutes;
  }

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation networkPolicy(List<GenericResourceApiNetworkpolicyNetworkPolicy> networkPolicy) {
    this.networkPolicy = networkPolicy;
    return this;
  }

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation addNetworkPolicyItem(GenericResourceApiNetworkpolicyNetworkPolicy networkPolicyItem) {
    if (this.networkPolicy == null) {
      this.networkPolicy = new ArrayList<>();
    }
    this.networkPolicy.add(networkPolicyItem);
    return this;
  }

  /**
   * Get networkPolicy
   * @return networkPolicy
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<GenericResourceApiNetworkpolicyNetworkPolicy> getNetworkPolicy() {
    return networkPolicy;
  }

  public void setNetworkPolicy(List<GenericResourceApiNetworkpolicyNetworkPolicy> networkPolicy) {
    this.networkPolicy = networkPolicy;
  }

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation networkTopologyIdentifierStructure(GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure networkTopologyIdentifierStructure) {
    this.networkTopologyIdentifierStructure = networkTopologyIdentifierStructure;
    return this;
  }

  /**
   * Get networkTopologyIdentifierStructure
   * @return networkTopologyIdentifierStructure
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure getNetworkTopologyIdentifierStructure() {
    return networkTopologyIdentifierStructure;
  }

  public void setNetworkTopologyIdentifierStructure(GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure networkTopologyIdentifierStructure) {
    this.networkTopologyIdentifierStructure = networkTopologyIdentifierStructure;
  }

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation physicalNetworkName(String physicalNetworkName) {
    this.physicalNetworkName = physicalNetworkName;
    return this;
  }

  /**
   * Get physicalNetworkName
   * @return physicalNetworkName
  **/
  @ApiModelProperty(value = "")


  public String getPhysicalNetworkName() {
    return physicalNetworkName;
  }

  public void setPhysicalNetworkName(String physicalNetworkName) {
    this.physicalNetworkName = physicalNetworkName;
  }

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation isExternalNetwork(Boolean isExternalNetwork) {
    this.isExternalNetwork = isExternalNetwork;
    return this;
  }

  /**
   * Get isExternalNetwork
   * @return isExternalNetwork
  **/
  @ApiModelProperty(value = "")


  public Boolean isIsExternalNetwork() {
    return isExternalNetwork;
  }

  public void setIsExternalNetwork(Boolean isExternalNetwork) {
    this.isExternalNetwork = isExternalNetwork;
  }

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation isSharedNetwork(Boolean isSharedNetwork) {
    this.isSharedNetwork = isSharedNetwork;
    return this;
  }

  /**
   * Get isSharedNetwork
   * @return isSharedNetwork
  **/
  @ApiModelProperty(value = "")


  public Boolean isIsSharedNetwork() {
    return isSharedNetwork;
  }

  public void setIsSharedNetwork(Boolean isSharedNetwork) {
    this.isSharedNetwork = isSharedNetwork;
  }

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation isProviderNetwork(Boolean isProviderNetwork) {
    this.isProviderNetwork = isProviderNetwork;
    return this;
  }

  /**
   * Get isProviderNetwork
   * @return isProviderNetwork
  **/
  @ApiModelProperty(value = "")


  public Boolean isIsProviderNetwork() {
    return isProviderNetwork;
  }

  public void setIsProviderNetwork(Boolean isProviderNetwork) {
    this.isProviderNetwork = isProviderNetwork;
  }

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation routeTableReference(List<GenericResourceApiRoutetablereferenceRouteTableReference> routeTableReference) {
    this.routeTableReference = routeTableReference;
    return this;
  }

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation addRouteTableReferenceItem(GenericResourceApiRoutetablereferenceRouteTableReference routeTableReferenceItem) {
    if (this.routeTableReference == null) {
      this.routeTableReference = new ArrayList<>();
    }
    this.routeTableReference.add(routeTableReferenceItem);
    return this;
  }

  /**
   * Get routeTableReference
   * @return routeTableReference
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<GenericResourceApiRoutetablereferenceRouteTableReference> getRouteTableReference() {
    return routeTableReference;
  }

  public void setRouteTableReference(List<GenericResourceApiRoutetablereferenceRouteTableReference> routeTableReference) {
    this.routeTableReference = routeTableReference;
  }

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation subnets(List<GenericResourceApiSubnetsSubnets> subnets) {
    this.subnets = subnets;
    return this;
  }

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation addSubnetsItem(GenericResourceApiSubnetsSubnets subnetsItem) {
    if (this.subnets == null) {
      this.subnets = new ArrayList<>();
    }
    this.subnets.add(subnetsItem);
    return this;
  }

  /**
   * Get subnets
   * @return subnets
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<GenericResourceApiSubnetsSubnets> getSubnets() {
    return subnets;
  }

  public void setSubnets(List<GenericResourceApiSubnetsSubnets> subnets) {
    this.subnets = subnets;
  }

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation vpnBindings(List<GenericResourceApiVpnbindingsVpnBindings> vpnBindings) {
    this.vpnBindings = vpnBindings;
    return this;
  }

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation addVpnBindingsItem(GenericResourceApiVpnbindingsVpnBindings vpnBindingsItem) {
    if (this.vpnBindings == null) {
      this.vpnBindings = new ArrayList<>();
    }
    this.vpnBindings.add(vpnBindingsItem);
    return this;
  }

  /**
   * Get vpnBindings
   * @return vpnBindings
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<GenericResourceApiVpnbindingsVpnBindings> getVpnBindings() {
    return vpnBindings;
  }

  public void setVpnBindings(List<GenericResourceApiVpnbindingsVpnBindings> vpnBindings) {
    this.vpnBindings = vpnBindings;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation genericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation = (GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation) o;
    return Objects.equals(this.hostRoutes, genericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation.hostRoutes) &&
        Objects.equals(this.networkPolicy, genericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation.networkPolicy) &&
        Objects.equals(this.networkTopologyIdentifierStructure, genericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation.networkTopologyIdentifierStructure) &&
        Objects.equals(this.physicalNetworkName, genericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation.physicalNetworkName) &&
        Objects.equals(this.isExternalNetwork, genericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation.isExternalNetwork) &&
        Objects.equals(this.isSharedNetwork, genericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation.isSharedNetwork) &&
        Objects.equals(this.isProviderNetwork, genericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation.isProviderNetwork) &&
        Objects.equals(this.routeTableReference, genericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation.routeTableReference) &&
        Objects.equals(this.subnets, genericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation.subnets) &&
        Objects.equals(this.vpnBindings, genericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation.vpnBindings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hostRoutes, networkPolicy, networkTopologyIdentifierStructure, physicalNetworkName, isExternalNetwork, isSharedNetwork, isProviderNetwork, routeTableReference, subnets, vpnBindings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation {\n");
    
    sb.append("    hostRoutes: ").append(toIndentedString(hostRoutes)).append("\n");
    sb.append("    networkPolicy: ").append(toIndentedString(networkPolicy)).append("\n");
    sb.append("    networkTopologyIdentifierStructure: ").append(toIndentedString(networkTopologyIdentifierStructure)).append("\n");
    sb.append("    physicalNetworkName: ").append(toIndentedString(physicalNetworkName)).append("\n");
    sb.append("    isExternalNetwork: ").append(toIndentedString(isExternalNetwork)).append("\n");
    sb.append("    isSharedNetwork: ").append(toIndentedString(isSharedNetwork)).append("\n");
    sb.append("    isProviderNetwork: ").append(toIndentedString(isProviderNetwork)).append("\n");
    sb.append("    routeTableReference: ").append(toIndentedString(routeTableReference)).append("\n");
    sb.append("    subnets: ").append(toIndentedString(subnets)).append("\n");
    sb.append("    vpnBindings: ").append(toIndentedString(vpnBindings)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

