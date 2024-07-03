package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiPortMirrorConfigurations
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-26T14:28:03.605-04:00")
@JsonRootName("port-mirror-configurations")
public class GenericResourceApiPortMirrorConfigurations   {
  @JsonProperty("port-mirror-configuration")
  @Valid
  private List<GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration> portMirrorConfiguration = null;

  public GenericResourceApiPortMirrorConfigurations portMirrorConfiguration(List<GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration> portMirrorConfiguration) {
    this.portMirrorConfiguration = portMirrorConfiguration;
    return this;
  }

  public GenericResourceApiPortMirrorConfigurations addPortMirrorConfigurationItem(GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration portMirrorConfigurationItem) {
    if (this.portMirrorConfiguration == null) {
      this.portMirrorConfiguration = new ArrayList<>();
    }
    this.portMirrorConfiguration.add(portMirrorConfigurationItem);
    return this;
  }

  /**
   * Get portMirrorConfiguration
   * @return portMirrorConfiguration
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration> getPortMirrorConfiguration() {
    return portMirrorConfiguration;
  }

  public void setPortMirrorConfiguration(List<GenericResourceApiPortmirrorconfigurationsPortMirrorConfiguration> portMirrorConfiguration) {
    this.portMirrorConfiguration = portMirrorConfiguration;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiPortMirrorConfigurations genericResourceApiPortMirrorConfigurations = (GenericResourceApiPortMirrorConfigurations) o;
    return Objects.equals(this.portMirrorConfiguration, genericResourceApiPortMirrorConfigurations.portMirrorConfiguration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(portMirrorConfiguration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiPortMirrorConfigurations {\n");
    
    sb.append("    portMirrorConfiguration: ").append(toIndentedString(portMirrorConfiguration)).append("\n");
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

