package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRelatedNetwork;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfnetworkdataSubnetsData;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiVnfNetworkData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-26T10:54:31.626-04:00")
@JsonRootName("vnf-network")
public class GenericResourceApiVnfNetworkData   {
  @JsonProperty("related-networks")
  private GenericResourceApiRelatedNetwork relatedNetworks = null;

  @JsonProperty("network-role")
  private String networkRole = null;

  @JsonProperty("is-trunked")
  private Boolean isTrunked = null;

  @JsonProperty("network-id")
  private String networkId = null;

  @JsonProperty("subnets-data")
  private GenericResourceApiVnfnetworkdataSubnetsData subnetsData = null;

  @JsonProperty("contrail-network-fqdn")
  private String contrailNetworkFqdn = null;

  @JsonProperty("network-name")
  private String networkName = null;

  @JsonProperty("segmentation-id")
  private String segmentationId = null;

  @JsonProperty("neutron-id")
  private String neutronId = null;

  public GenericResourceApiVnfNetworkData relatedNetworks(GenericResourceApiRelatedNetwork relatedNetworks) {
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

  public GenericResourceApiVnfNetworkData networkRole(String networkRole) {
    this.networkRole = networkRole;
    return this;
  }

  /**
   * A Network Role to which a VNF must connect
   * @return networkRole
  **/
  @ApiModelProperty(value = "A Network Role to which a VNF must connect")


  public String getNetworkRole() {
    return networkRole;
  }

  public void setNetworkRole(String networkRole) {
    this.networkRole = networkRole;
  }

  public GenericResourceApiVnfNetworkData isTrunked(Boolean isTrunked) {
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

  public GenericResourceApiVnfNetworkData networkId(String networkId) {
    this.networkId = networkId;
    return this;
  }

  /**
   * Unique Neutron UUID of an instance of the network role 
   * @return networkId
  **/
  @ApiModelProperty(value = "Unique Neutron UUID of an instance of the network role ")


  public String getNetworkId() {
    return networkId;
  }

  public void setNetworkId(String networkId) {
    this.networkId = networkId;
  }

  public GenericResourceApiVnfNetworkData subnetsData(GenericResourceApiVnfnetworkdataSubnetsData subnetsData) {
    this.subnetsData = subnetsData;
    return this;
  }

  /**
   * Get subnetsData
   * @return subnetsData
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiVnfnetworkdataSubnetsData getSubnetsData() {
    return subnetsData;
  }

  public void setSubnetsData(GenericResourceApiVnfnetworkdataSubnetsData subnetsData) {
    this.subnetsData = subnetsData;
  }

  public GenericResourceApiVnfNetworkData contrailNetworkFqdn(String contrailNetworkFqdn) {
    this.contrailNetworkFqdn = contrailNetworkFqdn;
    return this;
  }

  /**
   * contrail network policy object
   * @return contrailNetworkFqdn
  **/
  @ApiModelProperty(value = "contrail network policy object")


  public String getContrailNetworkFqdn() {
    return contrailNetworkFqdn;
  }

  public void setContrailNetworkFqdn(String contrailNetworkFqdn) {
    this.contrailNetworkFqdn = contrailNetworkFqdn;
  }

  public GenericResourceApiVnfNetworkData networkName(String networkName) {
    this.networkName = networkName;
    return this;
  }

  /**
   * Unique Neutron UUID of an instance of the network role 
   * @return networkName
  **/
  @ApiModelProperty(value = "Unique Neutron UUID of an instance of the network role ")


  public String getNetworkName() {
    return networkName;
  }

  public void setNetworkName(String networkName) {
    this.networkName = networkName;
  }

  public GenericResourceApiVnfNetworkData segmentationId(String segmentationId) {
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

  public GenericResourceApiVnfNetworkData neutronId(String neutronId) {
    this.neutronId = neutronId;
    return this;
  }

  /**
   * Unique Neutron UUID of an instance of the network role 
   * @return neutronId
  **/
  @ApiModelProperty(value = "Unique Neutron UUID of an instance of the network role ")


  public String getNeutronId() {
    return neutronId;
  }

  public void setNeutronId(String neutronId) {
    this.neutronId = neutronId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiVnfNetworkData genericResourceApiVnfNetworkData = (GenericResourceApiVnfNetworkData) o;
    return Objects.equals(this.relatedNetworks, genericResourceApiVnfNetworkData.relatedNetworks) &&
        Objects.equals(this.networkRole, genericResourceApiVnfNetworkData.networkRole) &&
        Objects.equals(this.isTrunked, genericResourceApiVnfNetworkData.isTrunked) &&
        Objects.equals(this.networkId, genericResourceApiVnfNetworkData.networkId) &&
        Objects.equals(this.subnetsData, genericResourceApiVnfNetworkData.subnetsData) &&
        Objects.equals(this.contrailNetworkFqdn, genericResourceApiVnfNetworkData.contrailNetworkFqdn) &&
        Objects.equals(this.networkName, genericResourceApiVnfNetworkData.networkName) &&
        Objects.equals(this.segmentationId, genericResourceApiVnfNetworkData.segmentationId) &&
        Objects.equals(this.neutronId, genericResourceApiVnfNetworkData.neutronId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relatedNetworks, networkRole, isTrunked, networkId, subnetsData, contrailNetworkFqdn, networkName, segmentationId, neutronId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiVnfNetworkData {\n");
    
    sb.append("    relatedNetworks: ").append(toIndentedString(relatedNetworks)).append("\n");
    sb.append("    networkRole: ").append(toIndentedString(networkRole)).append("\n");
    sb.append("    isTrunked: ").append(toIndentedString(isTrunked)).append("\n");
    sb.append("    networkId: ").append(toIndentedString(networkId)).append("\n");
    sb.append("    subnetsData: ").append(toIndentedString(subnetsData)).append("\n");
    sb.append("    contrailNetworkFqdn: ").append(toIndentedString(contrailNetworkFqdn)).append("\n");
    sb.append("    networkName: ").append(toIndentedString(networkName)).append("\n");
    sb.append("    segmentationId: ").append(toIndentedString(segmentationId)).append("\n");
    sb.append("    neutronId: ").append(toIndentedString(neutronId)).append("\n");
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

