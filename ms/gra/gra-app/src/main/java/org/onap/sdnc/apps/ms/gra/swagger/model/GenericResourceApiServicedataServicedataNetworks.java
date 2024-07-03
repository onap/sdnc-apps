package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataNetworksNetwork;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiServicedataServicedataNetworks
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-23T12:02:01.518-04:00")
@JsonRootName("networks")
public class GenericResourceApiServicedataServicedataNetworks   {
  @JsonProperty("network")
  @Valid
  private List<GenericResourceApiServicedataServicedataNetworksNetwork> network = null;

  public GenericResourceApiServicedataServicedataNetworks network(List<GenericResourceApiServicedataServicedataNetworksNetwork> network) {
    this.network = network;
    return this;
  }

  public GenericResourceApiServicedataServicedataNetworks addNetworkItem(GenericResourceApiServicedataServicedataNetworksNetwork networkItem) {
    if (this.network == null) {
      this.network = new ArrayList<>();
    }
    this.network.add(networkItem);
    return this;
  }

  /**
   * Get network
   * @return network
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<GenericResourceApiServicedataServicedataNetworksNetwork> getNetwork() {
    return network;
  }

  public void setNetwork(List<GenericResourceApiServicedataServicedataNetworksNetwork> network) {
    this.network = network;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiServicedataServicedataNetworks genericResourceApiServicedataServicedataNetworks = (GenericResourceApiServicedataServicedataNetworks) o;
    return Objects.equals(this.network, genericResourceApiServicedataServicedataNetworks.network);
  }

  @Override
  public int hashCode() {
    return Objects.hash(network);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiServicedataServicedataNetworks {\n");
    
    sb.append("    network: ").append(toIndentedString(network)).append("\n");
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

