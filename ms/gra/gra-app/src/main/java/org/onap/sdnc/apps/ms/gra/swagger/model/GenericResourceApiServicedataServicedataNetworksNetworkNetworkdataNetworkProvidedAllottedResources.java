package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiServicedataServicedataNetworksNetworkNetworkdataNetworkProvidedAllottedResources
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-23T12:02:01.518-04:00")
@JsonRootName("network-provided-allotted-resources")
public class GenericResourceApiServicedataServicedataNetworksNetworkNetworkdataNetworkProvidedAllottedResources   {
  @JsonProperty("network-provided-ar-id")
  @Valid
  private List<String> networkProvidedArId = null;

  public GenericResourceApiServicedataServicedataNetworksNetworkNetworkdataNetworkProvidedAllottedResources networkProvidedArId(List<String> networkProvidedArId) {
    this.networkProvidedArId = networkProvidedArId;
    return this;
  }

  public GenericResourceApiServicedataServicedataNetworksNetworkNetworkdataNetworkProvidedAllottedResources addNetworkProvidedArIdItem(String networkProvidedArIdItem) {
    if (this.networkProvidedArId == null) {
      this.networkProvidedArId = new ArrayList<>();
    }
    this.networkProvidedArId.add(networkProvidedArIdItem);
    return this;
  }

  /**
   * List of allotted resources using capacity from this network
   * @return networkProvidedArId
  **/
  @ApiModelProperty(value = "List of allotted resources using capacity from this network")


  public List<String> getNetworkProvidedArId() {
    return networkProvidedArId;
  }

  public void setNetworkProvidedArId(List<String> networkProvidedArId) {
    this.networkProvidedArId = networkProvidedArId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiServicedataServicedataNetworksNetworkNetworkdataNetworkProvidedAllottedResources genericResourceApiServicedataServicedataNetworksNetworkNetworkdataNetworkProvidedAllottedResources = (GenericResourceApiServicedataServicedataNetworksNetworkNetworkdataNetworkProvidedAllottedResources) o;
    return Objects.equals(this.networkProvidedArId, genericResourceApiServicedataServicedataNetworksNetworkNetworkdataNetworkProvidedAllottedResources.networkProvidedArId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(networkProvidedArId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiServicedataServicedataNetworksNetworkNetworkdataNetworkProvidedAllottedResources {\n");
    
    sb.append("    networkProvidedArId: ").append(toIndentedString(networkProvidedArId)).append("\n");
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

