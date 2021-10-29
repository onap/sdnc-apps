package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnfVnfData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiServicedataServicedataVnfsVnf
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("vnf")
public class GenericResourceApiServicedataServicedataVnfsVnf   {
  @JsonProperty("vnf-data")
  private GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfData = null;

  @JsonProperty("vnf-id")
  private String vnfId = null;

  public GenericResourceApiServicedataServicedataVnfsVnf vnfData(GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfData) {
    this.vnfData = vnfData;
    return this;
  }

  /**
   * Get vnfData
   * @return vnfData
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicedataServicedataVnfsVnfVnfData getVnfData() {
    return vnfData;
  }

  public void setVnfData(GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfData) {
    this.vnfData = vnfData;
  }

  public GenericResourceApiServicedataServicedataVnfsVnf vnfId(String vnfId) {
    this.vnfId = vnfId;
    return this;
  }

  /**
   * Get vnfId
   * @return vnfId
  **/
  @ApiModelProperty(value = "")


  public String getVnfId() {
    return vnfId;
  }

  public void setVnfId(String vnfId) {
    this.vnfId = vnfId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiServicedataServicedataVnfsVnf genericResourceApiServicedataServicedataVnfsVnf = (GenericResourceApiServicedataServicedataVnfsVnf) o;
    return Objects.equals(this.vnfData, genericResourceApiServicedataServicedataVnfsVnf.vnfData) &&
        Objects.equals(this.vnfId, genericResourceApiServicedataServicedataVnfsVnf.vnfId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vnfData, vnfId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiServicedataServicedataVnfsVnf {\n");
    
    sb.append("    vnfData: ").append(toIndentedString(vnfData)).append("\n");
    sb.append("    vnfId: ").append(toIndentedString(vnfId)).append("\n");
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

