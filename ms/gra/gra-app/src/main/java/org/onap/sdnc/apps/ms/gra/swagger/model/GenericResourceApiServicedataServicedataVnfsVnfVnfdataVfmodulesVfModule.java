package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfmoduleVfModuleData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("vf-module")
public class GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule   {
  @JsonProperty("vf-module-data")
  private GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfmoduleVfModuleData vfModuleData = null;

  @JsonProperty("vf-module-id")
  private String vfModuleId = null;

  public GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule vfModuleData(GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfmoduleVfModuleData vfModuleData) {
    this.vfModuleData = vfModuleData;
    return this;
  }

  /**
   * Get vfModuleData
   * @return vfModuleData
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfmoduleVfModuleData getVfModuleData() {
    return vfModuleData;
  }

  public void setVfModuleData(GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfmoduleVfModuleData vfModuleData) {
    this.vfModuleData = vfModuleData;
  }

  public GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule vfModuleId(String vfModuleId) {
    this.vfModuleId = vfModuleId;
    return this;
  }

  /**
   * Get vfModuleId
   * @return vfModuleId
  **/
  @ApiModelProperty(value = "")


  public String getVfModuleId() {
    return vfModuleId;
  }

  public void setVfModuleId(String vfModuleId) {
    this.vfModuleId = vfModuleId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule genericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule = (GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule) o;
    return Objects.equals(this.vfModuleData, genericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule.vfModuleData) &&
        Objects.equals(this.vfModuleId, genericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule.vfModuleId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vfModuleData, vfModuleId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule {\n");
    
    sb.append("    vfModuleData: ").append(toIndentedString(vfModuleData)).append("\n");
    sb.append("    vfModuleId: ").append(toIndentedString(vfModuleId)).append("\n");
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

