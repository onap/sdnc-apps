package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiAggregateRoutes;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiAggregateroutesAggregateRoutes;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkinstancegroupNetworkinstancegroupAggregateRoutePolicy;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkinstancegroupNetworkinstancegroupSubnetAssignmentPolicy;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkinstancegroupNetworkinstancegroupVpnBindingPolicy;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiOnapModelInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiOnapmodelinformationOnapModelInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiSubnets;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiSubnetsSubnets;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiNetworkinstancegroupNetworkInstanceGroup
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-23T12:02:01.518-04:00")
@JsonRootName("network-instance-group")
public class GenericResourceApiNetworkinstancegroupNetworkInstanceGroup   {
  @JsonProperty("aggregate-routes")
  @Valid
  private List<GenericResourceApiAggregateroutesAggregateRoutes> aggregateRoutes = null;

  @JsonProperty("onap-model-information")
  private GenericResourceApiOnapmodelinformationOnapModelInformation onapModelInformation = null;

  @JsonProperty("subnets")
  @Valid
  private List<GenericResourceApiSubnetsSubnets> subnets = null;

  @JsonProperty("subnet-assignment-policy")
  private GenericResourceApiNetworkinstancegroupNetworkinstancegroupSubnetAssignmentPolicy subnetAssignmentPolicy = null;

  @JsonProperty("service-instance-id")
  private String serviceInstanceId = null;

  @JsonProperty("vpn-binding-policy")
  private GenericResourceApiNetworkinstancegroupNetworkinstancegroupVpnBindingPolicy vpnBindingPolicy = null;

  @JsonProperty("network-instance-group-id")
  private String networkInstanceGroupId = null;

  @JsonProperty("network-instance-group-function")
  private String networkInstanceGroupFunction = null;

  @JsonProperty("networks")
  private GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks networks = null;

  @JsonProperty("aggregate-route-policy")
  private GenericResourceApiNetworkinstancegroupNetworkinstancegroupAggregateRoutePolicy aggregateRoutePolicy = null;

  @JsonProperty("subnet-key-value")
  private String subnetKeyValue = null;

  public GenericResourceApiNetworkinstancegroupNetworkInstanceGroup aggregateRoutes(List<GenericResourceApiAggregateroutesAggregateRoutes> aggregateRoutes) {
    this.aggregateRoutes = aggregateRoutes;
    return this;
  }

  public GenericResourceApiNetworkinstancegroupNetworkInstanceGroup addAggregateRoutesItem(GenericResourceApiAggregateroutesAggregateRoutes aggregateRoutesItem) {
    if (this.aggregateRoutes == null) {
      this.aggregateRoutes = new ArrayList<>();
    }
    this.aggregateRoutes.add(aggregateRoutesItem);
    return this;
  }

  /**
   * Get aggregateRoutes
   * @return aggregateRoutes
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<GenericResourceApiAggregateroutesAggregateRoutes> getAggregateRoutes() {
    return aggregateRoutes;
  }

  public void setAggregateRoutes(List<GenericResourceApiAggregateroutesAggregateRoutes> aggregateRoutes) {
    this.aggregateRoutes = aggregateRoutes;
  }

  public GenericResourceApiNetworkinstancegroupNetworkInstanceGroup onapModelInformation(GenericResourceApiOnapmodelinformationOnapModelInformation onapModelInformation) {
    this.onapModelInformation = onapModelInformation;
    return this;
  }

  /**
   * Get onapModelInformation
   * @return onapModelInformation
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiOnapmodelinformationOnapModelInformation getOnapModelInformation() {
    return onapModelInformation;
  }

  public void setOnapModelInformation(GenericResourceApiOnapmodelinformationOnapModelInformation onapModelInformation) {
    this.onapModelInformation = onapModelInformation;
  }

  public GenericResourceApiNetworkinstancegroupNetworkInstanceGroup subnets(List<GenericResourceApiSubnetsSubnets> subnets) {
    this.subnets = subnets;
    return this;
  }

  public GenericResourceApiNetworkinstancegroupNetworkInstanceGroup addSubnetsItem(GenericResourceApiSubnetsSubnets subnetsItem) {
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

  public GenericResourceApiNetworkinstancegroupNetworkInstanceGroup subnetAssignmentPolicy(GenericResourceApiNetworkinstancegroupNetworkinstancegroupSubnetAssignmentPolicy subnetAssignmentPolicy) {
    this.subnetAssignmentPolicy = subnetAssignmentPolicy;
    return this;
  }

  /**
   * Get subnetAssignmentPolicy
   * @return subnetAssignmentPolicy
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiNetworkinstancegroupNetworkinstancegroupSubnetAssignmentPolicy getSubnetAssignmentPolicy() {
    return subnetAssignmentPolicy;
  }

  public void setSubnetAssignmentPolicy(GenericResourceApiNetworkinstancegroupNetworkinstancegroupSubnetAssignmentPolicy subnetAssignmentPolicy) {
    this.subnetAssignmentPolicy = subnetAssignmentPolicy;
  }

  public GenericResourceApiNetworkinstancegroupNetworkInstanceGroup serviceInstanceId(String serviceInstanceId) {
    this.serviceInstanceId = serviceInstanceId;
    return this;
  }

  /**
   * Get serviceInstanceId
   * @return serviceInstanceId
  **/
  @ApiModelProperty(value = "")


  public String getServiceInstanceId() {
    return serviceInstanceId;
  }

  public void setServiceInstanceId(String serviceInstanceId) {
    this.serviceInstanceId = serviceInstanceId;
  }

  public GenericResourceApiNetworkinstancegroupNetworkInstanceGroup vpnBindingPolicy(GenericResourceApiNetworkinstancegroupNetworkinstancegroupVpnBindingPolicy vpnBindingPolicy) {
    this.vpnBindingPolicy = vpnBindingPolicy;
    return this;
  }

  /**
   * Get vpnBindingPolicy
   * @return vpnBindingPolicy
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiNetworkinstancegroupNetworkinstancegroupVpnBindingPolicy getVpnBindingPolicy() {
    return vpnBindingPolicy;
  }

  public void setVpnBindingPolicy(GenericResourceApiNetworkinstancegroupNetworkinstancegroupVpnBindingPolicy vpnBindingPolicy) {
    this.vpnBindingPolicy = vpnBindingPolicy;
  }

  public GenericResourceApiNetworkinstancegroupNetworkInstanceGroup networkInstanceGroupId(String networkInstanceGroupId) {
    this.networkInstanceGroupId = networkInstanceGroupId;
    return this;
  }

  /**
   * Also a Key in Network Instance Group in A&AI
   * @return networkInstanceGroupId
  **/
  @ApiModelProperty(value = "Also a Key in Network Instance Group in A&AI")


  public String getNetworkInstanceGroupId() {
    return networkInstanceGroupId;
  }

  public void setNetworkInstanceGroupId(String networkInstanceGroupId) {
    this.networkInstanceGroupId = networkInstanceGroupId;
  }

  public GenericResourceApiNetworkinstancegroupNetworkInstanceGroup networkInstanceGroupFunction(String networkInstanceGroupFunction) {
    this.networkInstanceGroupFunction = networkInstanceGroupFunction;
    return this;
  }

  /**
   * Get networkInstanceGroupFunction
   * @return networkInstanceGroupFunction
  **/
  @ApiModelProperty(value = "")


  public String getNetworkInstanceGroupFunction() {
    return networkInstanceGroupFunction;
  }

  public void setNetworkInstanceGroupFunction(String networkInstanceGroupFunction) {
    this.networkInstanceGroupFunction = networkInstanceGroupFunction;
  }

  public GenericResourceApiNetworkinstancegroupNetworkInstanceGroup networks(GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks networks) {
    this.networks = networks;
    return this;
  }

  /**
   * Get networks
   * @return networks
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks getNetworks() {
    return networks;
  }

  public void setNetworks(GenericResourceApiNetworkinstancegroupNetworkinstancegroupNetworks networks) {
    this.networks = networks;
  }

  public GenericResourceApiNetworkinstancegroupNetworkInstanceGroup aggregateRoutePolicy(GenericResourceApiNetworkinstancegroupNetworkinstancegroupAggregateRoutePolicy aggregateRoutePolicy) {
    this.aggregateRoutePolicy = aggregateRoutePolicy;
    return this;
  }

  /**
   * Get aggregateRoutePolicy
   * @return aggregateRoutePolicy
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiNetworkinstancegroupNetworkinstancegroupAggregateRoutePolicy getAggregateRoutePolicy() {
    return aggregateRoutePolicy;
  }

  public void setAggregateRoutePolicy(GenericResourceApiNetworkinstancegroupNetworkinstancegroupAggregateRoutePolicy aggregateRoutePolicy) {
    this.aggregateRoutePolicy = aggregateRoutePolicy;
  }

  public GenericResourceApiNetworkinstancegroupNetworkInstanceGroup subnetKeyValue(String subnetKeyValue) {
    this.subnetKeyValue = subnetKeyValue;
    return this;
  }

  /**
   * key-value provided to IPAM when  creating shared subnet
   * @return subnetKeyValue
  **/
  @ApiModelProperty(value = "key-value provided to IPAM when  creating shared subnet")


  public String getSubnetKeyValue() {
    return subnetKeyValue;
  }

  public void setSubnetKeyValue(String subnetKeyValue) {
    this.subnetKeyValue = subnetKeyValue;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiNetworkinstancegroupNetworkInstanceGroup genericResourceApiNetworkinstancegroupNetworkInstanceGroup = (GenericResourceApiNetworkinstancegroupNetworkInstanceGroup) o;
    return Objects.equals(this.aggregateRoutes, genericResourceApiNetworkinstancegroupNetworkInstanceGroup.aggregateRoutes) &&
        Objects.equals(this.onapModelInformation, genericResourceApiNetworkinstancegroupNetworkInstanceGroup.onapModelInformation) &&
        Objects.equals(this.subnets, genericResourceApiNetworkinstancegroupNetworkInstanceGroup.subnets) &&
        Objects.equals(this.subnetAssignmentPolicy, genericResourceApiNetworkinstancegroupNetworkInstanceGroup.subnetAssignmentPolicy) &&
        Objects.equals(this.serviceInstanceId, genericResourceApiNetworkinstancegroupNetworkInstanceGroup.serviceInstanceId) &&
        Objects.equals(this.vpnBindingPolicy, genericResourceApiNetworkinstancegroupNetworkInstanceGroup.vpnBindingPolicy) &&
        Objects.equals(this.networkInstanceGroupId, genericResourceApiNetworkinstancegroupNetworkInstanceGroup.networkInstanceGroupId) &&
        Objects.equals(this.networkInstanceGroupFunction, genericResourceApiNetworkinstancegroupNetworkInstanceGroup.networkInstanceGroupFunction) &&
        Objects.equals(this.networks, genericResourceApiNetworkinstancegroupNetworkInstanceGroup.networks) &&
        Objects.equals(this.aggregateRoutePolicy, genericResourceApiNetworkinstancegroupNetworkInstanceGroup.aggregateRoutePolicy) &&
        Objects.equals(this.subnetKeyValue, genericResourceApiNetworkinstancegroupNetworkInstanceGroup.subnetKeyValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aggregateRoutes, onapModelInformation, subnets, subnetAssignmentPolicy, serviceInstanceId, vpnBindingPolicy, networkInstanceGroupId, networkInstanceGroupFunction, networks, aggregateRoutePolicy, subnetKeyValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiNetworkinstancegroupNetworkInstanceGroup {\n");
    
    sb.append("    aggregateRoutes: ").append(toIndentedString(aggregateRoutes)).append("\n");
    sb.append("    onapModelInformation: ").append(toIndentedString(onapModelInformation)).append("\n");
    sb.append("    subnets: ").append(toIndentedString(subnets)).append("\n");
    sb.append("    subnetAssignmentPolicy: ").append(toIndentedString(subnetAssignmentPolicy)).append("\n");
    sb.append("    serviceInstanceId: ").append(toIndentedString(serviceInstanceId)).append("\n");
    sb.append("    vpnBindingPolicy: ").append(toIndentedString(vpnBindingPolicy)).append("\n");
    sb.append("    networkInstanceGroupId: ").append(toIndentedString(networkInstanceGroupId)).append("\n");
    sb.append("    networkInstanceGroupFunction: ").append(toIndentedString(networkInstanceGroupFunction)).append("\n");
    sb.append("    networks: ").append(toIndentedString(networks)).append("\n");
    sb.append("    aggregateRoutePolicy: ").append(toIndentedString(aggregateRoutePolicy)).append("\n");
    sb.append("    subnetKeyValue: ").append(toIndentedString(subnetKeyValue)).append("\n");
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

