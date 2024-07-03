package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataNetworksNetworkNetworkData;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiServicedataServicedataNetworksNetwork
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-23T12:02:01.518-04:00")
@JsonRootName("network")
public class GenericResourceApiServicedataServicedataNetworksNetwork   {
  @JsonProperty("network-id")
  private String networkId = null;

  @JsonProperty("network-data")
  private GenericResourceApiServicedataServicedataNetworksNetworkNetworkData networkData = null;

  public GenericResourceApiServicedataServicedataNetworksNetwork networkId(String networkId) {
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

  public GenericResourceApiServicedataServicedataNetworksNetwork networkData(GenericResourceApiServicedataServicedataNetworksNetworkNetworkData networkData) {
    this.networkData = networkData;
    return this;
  }

  /**
   * Get networkData
   * @return networkData
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicedataServicedataNetworksNetworkNetworkData getNetworkData() {
    return networkData;
  }

  public void setNetworkData(GenericResourceApiServicedataServicedataNetworksNetworkNetworkData networkData) {
    this.networkData = networkData;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiServicedataServicedataNetworksNetwork genericResourceApiServicedataServicedataNetworksNetwork = (GenericResourceApiServicedataServicedataNetworksNetwork) o;
    return Objects.equals(this.networkId, genericResourceApiServicedataServicedataNetworksNetwork.networkId) &&
        Objects.equals(this.networkData, genericResourceApiServicedataServicedataNetworksNetwork.networkData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(networkId, networkData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiServicedataServicedataNetworksNetwork {\n");
    
    sb.append("    networkId: ").append(toIndentedString(networkId)).append("\n");
    sb.append("    networkData: ").append(toIndentedString(networkData)).append("\n");
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

