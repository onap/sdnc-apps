package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadmodelinformationPreloadList;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiPreloadModelInformation
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("preload-information")
public class GenericResourceApiPreloadModelInformation   {
  @JsonProperty("preload-list")
  @Valid
  private List<GenericResourceApiPreloadmodelinformationPreloadList> preloadList = null;

  public GenericResourceApiPreloadModelInformation preloadList(List<GenericResourceApiPreloadmodelinformationPreloadList> preloadList) {
    this.preloadList = preloadList;
    return this;
  }

  public GenericResourceApiPreloadModelInformation addPreloadListItem(GenericResourceApiPreloadmodelinformationPreloadList preloadListItem) {
    if (this.preloadList == null) {
      this.preloadList = new ArrayList<>();
    }
    this.preloadList.add(preloadListItem);
    return this;
  }

  /**
   * Get preloadList
   * @return preloadList
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<GenericResourceApiPreloadmodelinformationPreloadList> getPreloadList() {
    return preloadList;
  }

  public void setPreloadList(List<GenericResourceApiPreloadmodelinformationPreloadList> preloadList) {
    this.preloadList = preloadList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiPreloadModelInformation genericResourceApiPreloadModelInformation = (GenericResourceApiPreloadModelInformation) o;
    return Objects.equals(this.preloadList, genericResourceApiPreloadModelInformation.preloadList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(preloadList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiPreloadModelInformation {\n");
    
    sb.append("    preloadList: ").append(toIndentedString(preloadList)).append("\n");
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

