package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServiceData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicestatusServiceStatus;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiServicemodelinfrastructureService
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-12T18:08:46.019-04:00")

@JsonRootName("service")
public class GenericResourceApiServicemodelinfrastructureService   {
  @JsonProperty("service-data")
  private GenericResourceApiServicedataServiceData serviceData = null;

  @JsonProperty("service-status")
  private GenericResourceApiServicestatusServiceStatus serviceStatus = null;

  @JsonProperty("service-instance-id")
  private String serviceInstanceId = null;

  public GenericResourceApiServicemodelinfrastructureService serviceData(GenericResourceApiServicedataServiceData serviceData) {
    this.serviceData = serviceData;
    return this;
  }

  /**
   * Get serviceData
   * @return serviceData
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicedataServiceData getServiceData() {
    return serviceData;
  }

  public void setServiceData(GenericResourceApiServicedataServiceData serviceData) {
    this.serviceData = serviceData;
  }

  public GenericResourceApiServicemodelinfrastructureService serviceStatus(GenericResourceApiServicestatusServiceStatus serviceStatus) {
    this.serviceStatus = serviceStatus;
    return this;
  }

  /**
   * Get serviceStatus
   * @return serviceStatus
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicestatusServiceStatus getServiceStatus() {
    return serviceStatus;
  }

  public void setServiceStatus(GenericResourceApiServicestatusServiceStatus serviceStatus) {
    this.serviceStatus = serviceStatus;
  }

  public GenericResourceApiServicemodelinfrastructureService serviceInstanceId(String serviceInstanceId) {
    this.serviceInstanceId = serviceInstanceId;
    return this;
  }

  /**
   * Keep as M
   * @return serviceInstanceId
  **/
  @ApiModelProperty(value = "Keep as M")


  public String getServiceInstanceId() {
    return serviceInstanceId;
  }

  public void setServiceInstanceId(String serviceInstanceId) {
    this.serviceInstanceId = serviceInstanceId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiServicemodelinfrastructureService genericResourceApiServicemodelinfrastructureService = (GenericResourceApiServicemodelinfrastructureService) o;
    return Objects.equals(this.serviceData, genericResourceApiServicemodelinfrastructureService.serviceData) &&
        Objects.equals(this.serviceStatus, genericResourceApiServicemodelinfrastructureService.serviceStatus) &&
        Objects.equals(this.serviceInstanceId, genericResourceApiServicemodelinfrastructureService.serviceInstanceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceData, serviceStatus, serviceInstanceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiServicemodelinfrastructureService {\n");
    
    sb.append("    serviceData: ").append(toIndentedString(serviceData)).append("\n");
    sb.append("    serviceStatus: ").append(toIndentedString(serviceStatus)).append("\n");
    sb.append("    serviceInstanceId: ").append(toIndentedString(serviceInstanceId)).append("\n");
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

