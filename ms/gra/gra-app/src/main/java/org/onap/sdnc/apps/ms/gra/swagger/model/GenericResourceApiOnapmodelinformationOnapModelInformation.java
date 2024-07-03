package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiOnapmodelinformationOnapModelInformation
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-26T09:20:20.871-04:00")
@JsonRootName("onap-model-information")
public class GenericResourceApiOnapmodelinformationOnapModelInformation   {
  @JsonProperty("model-name")
  private String modelName = null;

  @JsonProperty("model-version")
  private String modelVersion = null;

  @JsonProperty("model-customization-uuid")
  private String modelCustomizationUuid = null;

  @JsonProperty("model-uuid")
  private String modelUuid = null;

  @JsonProperty("model-invariant-uuid")
  private String modelInvariantUuid = null;

  public GenericResourceApiOnapmodelinformationOnapModelInformation modelName(String modelName) {
    this.modelName = modelName;
    return this;
  }

  /**
   * Get modelName
   * @return modelName
  **/
  @ApiModelProperty(value = "")


  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public GenericResourceApiOnapmodelinformationOnapModelInformation modelVersion(String modelVersion) {
    this.modelVersion = modelVersion;
    return this;
  }

  /**
   * Get modelVersion
   * @return modelVersion
  **/
  @ApiModelProperty(value = "")


  public String getModelVersion() {
    return modelVersion;
  }

  public void setModelVersion(String modelVersion) {
    this.modelVersion = modelVersion;
  }

  public GenericResourceApiOnapmodelinformationOnapModelInformation modelCustomizationUuid(String modelCustomizationUuid) {
    this.modelCustomizationUuid = modelCustomizationUuid;
    return this;
  }

  /**
   * customized resource for use within a given service.Would not be present at the service level but would be present for the resource level
   * @return modelCustomizationUuid
  **/
  @ApiModelProperty(value = "customized resource for use within a given service.Would not be present at the service level but would be present for the resource level")


  public String getModelCustomizationUuid() {
    return modelCustomizationUuid;
  }

  public void setModelCustomizationUuid(String modelCustomizationUuid) {
    this.modelCustomizationUuid = modelCustomizationUuid;
  }

  public GenericResourceApiOnapmodelinformationOnapModelInformation modelUuid(String modelUuid) {
    this.modelUuid = modelUuid;
    return this;
  }

  /**
   * identifies the uuid for this service or resource, which is version specific
   * @return modelUuid
  **/
  @ApiModelProperty(value = "identifies the uuid for this service or resource, which is version specific")


  public String getModelUuid() {
    return modelUuid;
  }

  public void setModelUuid(String modelUuid) {
    this.modelUuid = modelUuid;
  }

  public GenericResourceApiOnapmodelinformationOnapModelInformation modelInvariantUuid(String modelInvariantUuid) {
    this.modelInvariantUuid = modelInvariantUuid;
    return this;
  }

  /**
   * identifies the invariant uuid for this service or resource
   * @return modelInvariantUuid
  **/
  @ApiModelProperty(value = "identifies the invariant uuid for this service or resource")


  public String getModelInvariantUuid() {
    return modelInvariantUuid;
  }

  public void setModelInvariantUuid(String modelInvariantUuid) {
    this.modelInvariantUuid = modelInvariantUuid;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiOnapmodelinformationOnapModelInformation genericResourceApiOnapmodelinformationOnapModelInformation = (GenericResourceApiOnapmodelinformationOnapModelInformation) o;
    return Objects.equals(this.modelName, genericResourceApiOnapmodelinformationOnapModelInformation.modelName) &&
        Objects.equals(this.modelVersion, genericResourceApiOnapmodelinformationOnapModelInformation.modelVersion) &&
        Objects.equals(this.modelCustomizationUuid, genericResourceApiOnapmodelinformationOnapModelInformation.modelCustomizationUuid) &&
        Objects.equals(this.modelUuid, genericResourceApiOnapmodelinformationOnapModelInformation.modelUuid) &&
        Objects.equals(this.modelInvariantUuid, genericResourceApiOnapmodelinformationOnapModelInformation.modelInvariantUuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modelName, modelVersion, modelCustomizationUuid, modelUuid, modelInvariantUuid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiOnapmodelinformationOnapModelInformation {\n");
    
    sb.append("    modelName: ").append(toIndentedString(modelName)).append("\n");
    sb.append("    modelVersion: ").append(toIndentedString(modelVersion)).append("\n");
    sb.append("    modelCustomizationUuid: ").append(toIndentedString(modelCustomizationUuid)).append("\n");
    sb.append("    modelUuid: ").append(toIndentedString(modelUuid)).append("\n");
    sb.append("    modelInvariantUuid: ").append(toIndentedString(modelInvariantUuid)).append("\n");
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

