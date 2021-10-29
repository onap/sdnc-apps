package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloaddataPreloadData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiPreloadmodelinformationPreloadList
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("preload-list")
public class GenericResourceApiPreloadmodelinformationPreloadList   {
  @JsonProperty("preload-data")
  private GenericResourceApiPreloaddataPreloadData preloadData = null;

  @JsonProperty("preload-type")
  private String preloadType = null;

  @JsonProperty("preload-id")
  private String preloadId = null;

  public GenericResourceApiPreloadmodelinformationPreloadList preloadData(GenericResourceApiPreloaddataPreloadData preloadData) {
    this.preloadData = preloadData;
    return this;
  }

  /**
   * Get preloadData
   * @return preloadData
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiPreloaddataPreloadData getPreloadData() {
    return preloadData;
  }

  public void setPreloadData(GenericResourceApiPreloaddataPreloadData preloadData) {
    this.preloadData = preloadData;
  }

  public GenericResourceApiPreloadmodelinformationPreloadList preloadType(String preloadType) {
    this.preloadType = preloadType;
    return this;
  }

  /**
   * network or vf-module
   * @return preloadType
  **/
  @ApiModelProperty(value = "network or vf-module")


  public String getPreloadType() {
    return preloadType;
  }

  public void setPreloadType(String preloadType) {
    this.preloadType = preloadType;
  }

  public GenericResourceApiPreloadmodelinformationPreloadList preloadId(String preloadId) {
    this.preloadId = preloadId;
    return this;
  }

  /**
   * vf-module-name or network name
   * @return preloadId
  **/
  @ApiModelProperty(value = "vf-module-name or network name")


  public String getPreloadId() {
    return preloadId;
  }

  public void setPreloadId(String preloadId) {
    this.preloadId = preloadId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiPreloadmodelinformationPreloadList genericResourceApiPreloadmodelinformationPreloadList = (GenericResourceApiPreloadmodelinformationPreloadList) o;
    return Objects.equals(this.preloadData, genericResourceApiPreloadmodelinformationPreloadList.preloadData) &&
        Objects.equals(this.preloadType, genericResourceApiPreloadmodelinformationPreloadList.preloadType) &&
        Objects.equals(this.preloadId, genericResourceApiPreloadmodelinformationPreloadList.preloadId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(preloadData, preloadType, preloadId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiPreloadmodelinformationPreloadList {\n");
    
    sb.append("    preloadData: ").append(toIndentedString(preloadData)).append("\n");
    sb.append("    preloadType: ").append(toIndentedString(preloadType)).append("\n");
    sb.append("    preloadId: ").append(toIndentedString(preloadId)).append("\n");
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

