package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiConfigurationStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiConfigurationstatusConfigurationStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPortmirrorconfigurationsPortmirrorconfigurationConfigurationData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("port-mirror-configuration")
public class GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration   {
  @JsonProperty("configuration-status")
  private GenericResourceApiConfigurationstatusConfigurationStatus configurationStatus = null;

  @JsonProperty("configuration-id")
  private String configurationId = null;

  @JsonProperty("configuration-data")
  private GenericResourceApiPortmirrorconfigurationsPortmirrorconfigurationConfigurationData configurationData = null;

  public GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration configurationStatus(GenericResourceApiConfigurationstatusConfigurationStatus configurationStatus) {
    this.configurationStatus = configurationStatus;
    return this;
  }

  /**
   * Get configurationStatus
   * @return configurationStatus
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiConfigurationstatusConfigurationStatus getConfigurationStatus() {
    return configurationStatus;
  }

  public void setConfigurationStatus(GenericResourceApiConfigurationstatusConfigurationStatus configurationStatus) {
    this.configurationStatus = configurationStatus;
  }

  public GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration configurationId(String configurationId) {
    this.configurationId = configurationId;
    return this;
  }

  /**
   * Get configurationId
   * @return configurationId
  **/
  @ApiModelProperty(value = "")


  public String getConfigurationId() {
    return configurationId;
  }

  public void setConfigurationId(String configurationId) {
    this.configurationId = configurationId;
  }

  public GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration configurationData(GenericResourceApiPortmirrorconfigurationsPortmirrorconfigurationConfigurationData configurationData) {
    this.configurationData = configurationData;
    return this;
  }

  /**
   * Get configurationData
   * @return configurationData
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiPortmirrorconfigurationsPortmirrorconfigurationConfigurationData getConfigurationData() {
    return configurationData;
  }

  public void setConfigurationData(GenericResourceApiPortmirrorconfigurationsPortmirrorconfigurationConfigurationData configurationData) {
    this.configurationData = configurationData;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration genericResourceApiPortmirrorconfigurationsPortMirrorConfiguration = (GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration) o;
    return Objects.equals(this.configurationStatus, genericResourceApiPortmirrorconfigurationsPortMirrorConfiguration.configurationStatus) &&
        Objects.equals(this.configurationId, genericResourceApiPortmirrorconfigurationsPortMirrorConfiguration.configurationId) &&
        Objects.equals(this.configurationData, genericResourceApiPortmirrorconfigurationsPortMirrorConfiguration.configurationData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(configurationStatus, configurationId, configurationData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration {\n");
    
    sb.append("    configurationStatus: ").append(toIndentedString(configurationStatus)).append("\n");
    sb.append("    configurationId: ").append(toIndentedString(configurationId)).append("\n");
    sb.append("    configurationData: ").append(toIndentedString(configurationData)).append("\n");
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

