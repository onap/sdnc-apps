package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiAllottedResourceStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiAllottedresourcestatusAllottedResourceStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiContrailrouteallottedresourcesContrailrouteallottedresourceAllottedResourceData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("contrail-route-allotted-resource")
public class GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource   {
  @JsonProperty("allotted-resource-status")
  private GenericResourceApiAllottedresourcestatusAllottedResourceStatus allottedResourceStatus = null;

  @JsonProperty("allotted-resource-id")
  private String allottedResourceId = null;

  @JsonProperty("allotted-resource-data")
  private GenericResourceApiContrailrouteallottedresourcesContrailrouteallottedresourceAllottedResourceData allottedResourceData = null;

  public GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource allottedResourceStatus(GenericResourceApiAllottedresourcestatusAllottedResourceStatus allottedResourceStatus) {
    this.allottedResourceStatus = allottedResourceStatus;
    return this;
  }

  /**
   * Get allottedResourceStatus
   * @return allottedResourceStatus
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiAllottedresourcestatusAllottedResourceStatus getAllottedResourceStatus() {
    return allottedResourceStatus;
  }

  public void setAllottedResourceStatus(GenericResourceApiAllottedresourcestatusAllottedResourceStatus allottedResourceStatus) {
    this.allottedResourceStatus = allottedResourceStatus;
  }

  public GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource allottedResourceId(String allottedResourceId) {
    this.allottedResourceId = allottedResourceId;
    return this;
  }

  /**
   * Get allottedResourceId
   * @return allottedResourceId
  **/
  @ApiModelProperty(value = "")


  public String getAllottedResourceId() {
    return allottedResourceId;
  }

  public void setAllottedResourceId(String allottedResourceId) {
    this.allottedResourceId = allottedResourceId;
  }

  public GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource allottedResourceData(GenericResourceApiContrailrouteallottedresourcesContrailrouteallottedresourceAllottedResourceData allottedResourceData) {
    this.allottedResourceData = allottedResourceData;
    return this;
  }

  /**
   * Get allottedResourceData
   * @return allottedResourceData
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiContrailrouteallottedresourcesContrailrouteallottedresourceAllottedResourceData getAllottedResourceData() {
    return allottedResourceData;
  }

  public void setAllottedResourceData(GenericResourceApiContrailrouteallottedresourcesContrailrouteallottedresourceAllottedResourceData allottedResourceData) {
    this.allottedResourceData = allottedResourceData;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource genericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource = (GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource) o;
    return Objects.equals(this.allottedResourceStatus, genericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource.allottedResourceStatus) &&
        Objects.equals(this.allottedResourceId, genericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource.allottedResourceId) &&
        Objects.equals(this.allottedResourceData, genericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource.allottedResourceData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allottedResourceStatus, allottedResourceId, allottedResourceData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource {\n");
    
    sb.append("    allottedResourceStatus: ").append(toIndentedString(allottedResourceStatus)).append("\n");
    sb.append("    allottedResourceId: ").append(toIndentedString(allottedResourceId)).append("\n");
    sb.append("    allottedResourceData: ").append(toIndentedString(allottedResourceData)).append("\n");
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

