package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfNetworkData;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-26T09:20:20.871-04:00")
@JsonRootName("vnf-networks")
public class GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks   {
  @JsonProperty("vnf-network")
  @Valid
  private List<GenericResourceApiVnfNetworkData> vnfNetwork = null;

  public GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks vnfNetwork(List<GenericResourceApiVnfNetworkData> vnfNetwork) {
    this.vnfNetwork = vnfNetwork;
    return this;
  }

  public GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks addVnfNetworkItem(GenericResourceApiVnfNetworkData vnfNetworkItem) {
    if (this.vnfNetwork == null) {
      this.vnfNetwork = new ArrayList<>();
    }
    this.vnfNetwork.add(vnfNetworkItem);
    return this;
  }

  /**
   * Get vnfNetwork
   * @return vnfNetwork
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<GenericResourceApiVnfNetworkData> getVnfNetwork() {
    return vnfNetwork;
  }

  public void setVnfNetwork(List<GenericResourceApiVnfNetworkData> vnfNetwork) {
    this.vnfNetwork = vnfNetwork;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks genericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks = (GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks) o;
    return Objects.equals(this.vnfNetwork, genericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks.vnfNetwork);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vnfNetwork);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiVnfresourceassignmentsVnfresourceassignmentsVnfNetworks {\n");
    
    sb.append("    vnfNetwork: ").append(toIndentedString(vnfNetwork)).append("\n");
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

