package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicemodelinfrastructureService;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiServiceModelInfrastructure
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-12T18:08:46.019-04:00")
@JsonRootName("services")
public class GenericResourceApiServiceModelInfrastructure   {
  @JsonProperty("service")
  @Valid
  private List<GenericResourceApiServicemodelinfrastructureService> service = null;

  public GenericResourceApiServiceModelInfrastructure service(List<GenericResourceApiServicemodelinfrastructureService> service) {
    this.service = service;
    return this;
  }

  public GenericResourceApiServiceModelInfrastructure addServiceItem(GenericResourceApiServicemodelinfrastructureService serviceItem) {
    if (this.service == null) {
      this.service = new ArrayList<>();
    }
    this.service.add(serviceItem);
    return this;
  }

  /**
   * Get service
   * @return service
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<GenericResourceApiServicemodelinfrastructureService> getService() {
    return service;
  }

  public void setService(List<GenericResourceApiServicemodelinfrastructureService> service) {
    this.service = service;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiServiceModelInfrastructure genericResourceApiServiceModelInfrastructure = (GenericResourceApiServiceModelInfrastructure) o;
    return Objects.equals(this.service, genericResourceApiServiceModelInfrastructure.service);
  }

  @Override
  public int hashCode() {
    return Objects.hash(service);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiServiceModelInfrastructure {\n");
    
    sb.append("    service: ").append(toIndentedString(service)).append("\n");
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

