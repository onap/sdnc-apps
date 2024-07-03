package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiContrailRouteAllottedResources
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-26T13:29:23.962-04:00")
@JsonRootName("contrail-route-allotted-resources")
public class GenericResourceApiContrailRouteAllottedResources   {
  @JsonProperty("contrail-route-allotted-resource")
  @Valid
  private List<GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource> contrailRouteAllottedResource = null;

  public GenericResourceApiContrailRouteAllottedResources contrailRouteAllottedResource(List<GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource> contrailRouteAllottedResource) {
    this.contrailRouteAllottedResource = contrailRouteAllottedResource;
    return this;
  }

  public GenericResourceApiContrailRouteAllottedResources addContrailRouteAllottedResourceItem(GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource contrailRouteAllottedResourceItem) {
    if (this.contrailRouteAllottedResource == null) {
      this.contrailRouteAllottedResource = new ArrayList<>();
    }
    this.contrailRouteAllottedResource.add(contrailRouteAllottedResourceItem);
    return this;
  }

  /**
   * Get contrailRouteAllottedResource
   * @return contrailRouteAllottedResource
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource> getContrailRouteAllottedResource() {
    return contrailRouteAllottedResource;
  }

  public void setContrailRouteAllottedResource(List<GenericResourceApiContrailrouteallottedresourcesContrailRouteAllottedResource> contrailRouteAllottedResource) {
    this.contrailRouteAllottedResource = contrailRouteAllottedResource;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiContrailRouteAllottedResources genericResourceApiContrailRouteAllottedResources = (GenericResourceApiContrailRouteAllottedResources) o;
    return Objects.equals(this.contrailRouteAllottedResource, genericResourceApiContrailRouteAllottedResources.contrailRouteAllottedResource);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contrailRouteAllottedResource);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiContrailRouteAllottedResources {\n");
    
    sb.append("    contrailRouteAllottedResource: ").append(toIndentedString(contrailRouteAllottedResource)).append("\n");
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

