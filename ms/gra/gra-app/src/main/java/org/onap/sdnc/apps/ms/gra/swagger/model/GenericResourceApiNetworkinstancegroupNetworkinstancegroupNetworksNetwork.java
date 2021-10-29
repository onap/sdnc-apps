package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetworkCustomerBondingRequests;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVpnBindings;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVpnbindingsVpnBindings;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-23T12:02:01.518-04:00")
@JsonRootName("network")
public class GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork   {
  @JsonProperty("vpn-bindings")
  @Valid
  private List<GenericResourceApiVpnbindingsVpnBindings> vpnBindings = null;

  @JsonProperty("network-status")
  private String networkStatus = null;

  @JsonProperty("neutron-network-id")
  private String neutronNetworkId = null;

  @JsonProperty("network-id")
  private String networkId = null;

  @JsonProperty("customer-bonding-requests")
  private GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetworkCustomerBondingRequests customerBondingRequests = null;

  @JsonProperty("vlan-tag-id")
  private Long vlanTagId = null;

  public GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork vpnBindings(List<GenericResourceApiVpnbindingsVpnBindings> vpnBindings) {
    this.vpnBindings = vpnBindings;
    return this;
  }

  public GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork addVpnBindingsItem(GenericResourceApiVpnbindingsVpnBindings vpnBindingsItem) {
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

  public GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork networkStatus(String networkStatus) {
    this.networkStatus = networkStatus;
    return this;
  }

  /**
   * assigned, unassigned
   * @return networkStatus
  **/
  @ApiModelProperty(value = "assigned, unassigned")


  public String getNetworkStatus() {
    return networkStatus;
  }

  public void setNetworkStatus(String networkStatus) {
    this.networkStatus = networkStatus;
  }

  public GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork neutronNetworkId(String neutronNetworkId) {
    this.neutronNetworkId = neutronNetworkId;
    return this;
  }

  /**
   * Neutron-network-id assigned by PO/RO
   * @return neutronNetworkId
  **/
  @ApiModelProperty(value = "Neutron-network-id assigned by PO/RO")


  public String getNeutronNetworkId() {
    return neutronNetworkId;
  }

  public void setNeutronNetworkId(String neutronNetworkId) {
    this.neutronNetworkId = neutronNetworkId;
  }

  public GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork networkId(String networkId) {
    this.networkId = networkId;
    return this;
  }

  /**
   * Index into network-topology-identifier structure
   * @return networkId
  **/
  @ApiModelProperty(value = "Index into network-topology-identifier structure")


  public String getNetworkId() {
    return networkId;
  }

  public void setNetworkId(String networkId) {
    this.networkId = networkId;
  }

  public GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork customerBondingRequests(GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetworkCustomerBondingRequests customerBondingRequests) {
    this.customerBondingRequests = customerBondingRequests;
    return this;
  }

  /**
   * Get customerBondingRequests
   * @return customerBondingRequests
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetworkCustomerBondingRequests getCustomerBondingRequests() {
    return customerBondingRequests;
  }

  public void setCustomerBondingRequests(GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetworkCustomerBondingRequests customerBondingRequests) {
    this.customerBondingRequests = customerBondingRequests;
  }

  public GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork vlanTagId(Long vlanTagId) {
    this.vlanTagId = vlanTagId;
    return this;
  }

  /**
   * Get vlanTagId
   * @return vlanTagId
  **/
  @ApiModelProperty(value = "")


  public Long getVlanTagId() {
    return vlanTagId;
  }

  public void setVlanTagId(Long vlanTagId) {
    this.vlanTagId = vlanTagId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork genericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork = (GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork) o;
    return Objects.equals(this.vpnBindings, genericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork.vpnBindings) &&
        Objects.equals(this.networkStatus, genericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork.networkStatus) &&
        Objects.equals(this.neutronNetworkId, genericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork.neutronNetworkId) &&
        Objects.equals(this.networkId, genericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork.networkId) &&
        Objects.equals(this.customerBondingRequests, genericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork.customerBondingRequests) &&
        Objects.equals(this.vlanTagId, genericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork.vlanTagId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vpnBindings, networkStatus, neutronNetworkId, networkId, customerBondingRequests, vlanTagId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworksNetwork {\n");
    
    sb.append("    vpnBindings: ").append(toIndentedString(vpnBindings)).append("\n");
    sb.append("    networkStatus: ").append(toIndentedString(networkStatus)).append("\n");
    sb.append("    neutronNetworkId: ").append(toIndentedString(neutronNetworkId)).append("\n");
    sb.append("    networkId: ").append(toIndentedString(networkId)).append("\n");
    sb.append("    customerBondingRequests: ").append(toIndentedString(customerBondingRequests)).append("\n");
    sb.append("    vlanTagId: ").append(toIndentedString(vlanTagId)).append("\n");
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

