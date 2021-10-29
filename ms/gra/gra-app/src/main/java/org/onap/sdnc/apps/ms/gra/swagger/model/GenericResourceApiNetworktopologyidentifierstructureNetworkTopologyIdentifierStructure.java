package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRelatedNetwork;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-23T12:02:01.518-04:00")
@JsonRootName("network-topology-identifier-structure")
public class GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure   {
  @JsonProperty("related-networks")
  private GenericResourceApiRelatedNetwork relatedNetworks = null;

  @JsonProperty("network-role")
  private String networkRole = null;

  @JsonProperty("is-trunked")
  private Boolean isTrunked = null;

  @JsonProperty("network-technology")
  private String networkTechnology = null;

  @JsonProperty("network-id")
  private String networkId = null;

  @JsonProperty("eipam-v4-address-plan")
  private String eipamV4AddressPlan = null;

  @JsonProperty("network-instance-group-id")
  private String networkInstanceGroupId = null;

  @JsonProperty("network-name")
  private String networkName = null;

  @JsonProperty("segmentation-id")
  private String segmentationId = null;

  @JsonProperty("eipam-v6-address-plan")
  private String eipamV6AddressPlan = null;

  @JsonProperty("network-type")
  private String networkType = null;

  public GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure relatedNetworks(GenericResourceApiRelatedNetwork relatedNetworks) {
    this.relatedNetworks = relatedNetworks;
    return this;
  }

  /**
   * Get relatedNetworks
   * @return relatedNetworks
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiRelatedNetwork getRelatedNetworks() {
    return relatedNetworks;
  }

  public void setRelatedNetworks(GenericResourceApiRelatedNetwork relatedNetworks) {
    this.relatedNetworks = relatedNetworks;
  }

  public GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure networkRole(String networkRole) {
    this.networkRole = networkRole;
    return this;
  }

  /**
   * Get networkRole
   * @return networkRole
  **/
  @ApiModelProperty(value = "")


  public String getNetworkRole() {
    return networkRole;
  }

  public void setNetworkRole(String networkRole) {
    this.networkRole = networkRole;
  }

  public GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure isTrunked(Boolean isTrunked) {
    this.isTrunked = isTrunked;
    return this;
  }

  /**
   * Get isTrunked
   * @return isTrunked
  **/
  @ApiModelProperty(value = "")


  public Boolean isIsTrunked() {
    return isTrunked;
  }

  public void setIsTrunked(Boolean isTrunked) {
    this.isTrunked = isTrunked;
  }

  public GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure networkTechnology(String networkTechnology) {
    this.networkTechnology = networkTechnology;
    return this;
  }

  /**
   * Get networkTechnology
   * @return networkTechnology
  **/
  @ApiModelProperty(value = "")


  public String getNetworkTechnology() {
    return networkTechnology;
  }

  public void setNetworkTechnology(String networkTechnology) {
    this.networkTechnology = networkTechnology;
  }

  public GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure networkId(String networkId) {
    this.networkId = networkId;
    return this;
  }

  /**
   * Get networkId
   * @return networkId
  **/
  @ApiModelProperty(value = "")


  public String getNetworkId() {
    return networkId;
  }

  public void setNetworkId(String networkId) {
    this.networkId = networkId;
  }

  public GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure eipamV4AddressPlan(String eipamV4AddressPlan) {
    this.eipamV4AddressPlan = eipamV4AddressPlan;
    return this;
  }

  /**
   * Get eipamV4AddressPlan
   * @return eipamV4AddressPlan
  **/
  @ApiModelProperty(value = "")


  public String getEipamV4AddressPlan() {
    return eipamV4AddressPlan;
  }

  public void setEipamV4AddressPlan(String eipamV4AddressPlan) {
    this.eipamV4AddressPlan = eipamV4AddressPlan;
  }

  public GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure networkInstanceGroupId(String networkInstanceGroupId) {
    this.networkInstanceGroupId = networkInstanceGroupId;
    return this;
  }

  /**
   * When network is part of network instance-Group-data & instance Group in A&AI
   * @return networkInstanceGroupId
  **/
  @ApiModelProperty(value = "When network is part of network instance-Group-data & instance Group in A&AI")


  public String getNetworkInstanceGroupId() {
    return networkInstanceGroupId;
  }

  public void setNetworkInstanceGroupId(String networkInstanceGroupId) {
    this.networkInstanceGroupId = networkInstanceGroupId;
  }

  public GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure networkName(String networkName) {
    this.networkName = networkName;
    return this;
  }

  /**
   * Get networkName
   * @return networkName
  **/
  @ApiModelProperty(value = "")


  public String getNetworkName() {
    return networkName;
  }

  public void setNetworkName(String networkName) {
    this.networkName = networkName;
  }

  public GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure segmentationId(String segmentationId) {
    this.segmentationId = segmentationId;
    return this;
  }

  /**
   * Get segmentationId
   * @return segmentationId
  **/
  @ApiModelProperty(value = "")


  public String getSegmentationId() {
    return segmentationId;
  }

  public void setSegmentationId(String segmentationId) {
    this.segmentationId = segmentationId;
  }

  public GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure eipamV6AddressPlan(String eipamV6AddressPlan) {
    this.eipamV6AddressPlan = eipamV6AddressPlan;
    return this;
  }

  /**
   * Get eipamV6AddressPlan
   * @return eipamV6AddressPlan
  **/
  @ApiModelProperty(value = "")


  public String getEipamV6AddressPlan() {
    return eipamV6AddressPlan;
  }

  public void setEipamV6AddressPlan(String eipamV6AddressPlan) {
    this.eipamV6AddressPlan = eipamV6AddressPlan;
  }

  public GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure networkType(String networkType) {
    this.networkType = networkType;
    return this;
  }

  /**
   * Get networkType
   * @return networkType
  **/
  @ApiModelProperty(value = "")


  public String getNetworkType() {
    return networkType;
  }

  public void setNetworkType(String networkType) {
    this.networkType = networkType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure genericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure = (GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure) o;
    return Objects.equals(this.relatedNetworks, genericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure.relatedNetworks) &&
        Objects.equals(this.networkRole, genericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure.networkRole) &&
        Objects.equals(this.isTrunked, genericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure.isTrunked) &&
        Objects.equals(this.networkTechnology, genericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure.networkTechnology) &&
        Objects.equals(this.networkId, genericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure.networkId) &&
        Objects.equals(this.eipamV4AddressPlan, genericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure.eipamV4AddressPlan) &&
        Objects.equals(this.networkInstanceGroupId, genericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure.networkInstanceGroupId) &&
        Objects.equals(this.networkName, genericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure.networkName) &&
        Objects.equals(this.segmentationId, genericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure.segmentationId) &&
        Objects.equals(this.eipamV6AddressPlan, genericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure.eipamV6AddressPlan) &&
        Objects.equals(this.networkType, genericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure.networkType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relatedNetworks, networkRole, isTrunked, networkTechnology, networkId, eipamV4AddressPlan, networkInstanceGroupId, networkName, segmentationId, eipamV6AddressPlan, networkType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiNetworktopologyidentifierstructureNetworkTopologyIdentifierStructure {\n");
    
    sb.append("    relatedNetworks: ").append(toIndentedString(relatedNetworks)).append("\n");
    sb.append("    networkRole: ").append(toIndentedString(networkRole)).append("\n");
    sb.append("    isTrunked: ").append(toIndentedString(isTrunked)).append("\n");
    sb.append("    networkTechnology: ").append(toIndentedString(networkTechnology)).append("\n");
    sb.append("    networkId: ").append(toIndentedString(networkId)).append("\n");
    sb.append("    eipamV4AddressPlan: ").append(toIndentedString(eipamV4AddressPlan)).append("\n");
    sb.append("    networkInstanceGroupId: ").append(toIndentedString(networkInstanceGroupId)).append("\n");
    sb.append("    networkName: ").append(toIndentedString(networkName)).append("\n");
    sb.append("    segmentationId: ").append(toIndentedString(segmentationId)).append("\n");
    sb.append("    eipamV6AddressPlan: ").append(toIndentedString(eipamV6AddressPlan)).append("\n");
    sb.append("    networkType: ").append(toIndentedString(networkType)).append("\n");
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

