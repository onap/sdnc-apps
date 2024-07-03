package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiConfigurationIdentifiers;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiConfigurationidentifiersConfigurationIdentifiers;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiOnapModelInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiOnapmodelinformationOnapModelInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiParam;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPortMirrorConfigurationAssignments;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPortmirrorconfigurationassignmentsPortMirrorConfigurationAssignments;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("port-mirror-configuration-topology")
public class GenericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology   {
  @JsonProperty("configuration-identifiers")
  private GenericResourceApiConfigurationidentifiersConfigurationIdentifiers configurationIdentifiers = null;

  @JsonProperty("onap-model-information")
  private GenericResourceApiOnapmodelinformationOnapModelInformation onapModelInformation = null;

  @JsonProperty("port-mirror-configuration-assignments")
  private GenericResourceApiPortmirrorconfigurationassignmentsPortMirrorConfigurationAssignments portMirrorConfigurationAssignments = null;

  @JsonProperty("port-mirror-configuration-parameters")
  private GenericResourceApiParam portMirrorConfigurationParameters = null;

  public GenericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology configurationIdentifiers(GenericResourceApiConfigurationidentifiersConfigurationIdentifiers configurationIdentifiers) {
    this.configurationIdentifiers = configurationIdentifiers;
    return this;
  }

  /**
   * Get configurationIdentifiers
   * @return configurationIdentifiers
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiConfigurationidentifiersConfigurationIdentifiers getConfigurationIdentifiers() {
    return configurationIdentifiers;
  }

  public void setConfigurationIdentifiers(GenericResourceApiConfigurationidentifiersConfigurationIdentifiers configurationIdentifiers) {
    this.configurationIdentifiers = configurationIdentifiers;
  }

  public GenericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology onapModelInformation(GenericResourceApiOnapmodelinformationOnapModelInformation onapModelInformation) {
    this.onapModelInformation = onapModelInformation;
    return this;
  }

  /**
   * Get onapModelInformation
   * @return onapModelInformation
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiOnapmodelinformationOnapModelInformation getOnapModelInformation() {
    return onapModelInformation;
  }

  public void setOnapModelInformation(GenericResourceApiOnapmodelinformationOnapModelInformation onapModelInformation) {
    this.onapModelInformation = onapModelInformation;
  }

  public GenericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology portMirrorConfigurationAssignments(GenericResourceApiPortmirrorconfigurationassignmentsPortMirrorConfigurationAssignments portMirrorConfigurationAssignments) {
    this.portMirrorConfigurationAssignments = portMirrorConfigurationAssignments;
    return this;
  }

  /**
   * Get portMirrorConfigurationAssignments
   * @return portMirrorConfigurationAssignments
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiPortmirrorconfigurationassignmentsPortMirrorConfigurationAssignments getPortMirrorConfigurationAssignments() {
    return portMirrorConfigurationAssignments;
  }

  public void setPortMirrorConfigurationAssignments(GenericResourceApiPortmirrorconfigurationassignmentsPortMirrorConfigurationAssignments portMirrorConfigurationAssignments) {
    this.portMirrorConfigurationAssignments = portMirrorConfigurationAssignments;
  }

  public GenericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology portMirrorConfigurationParameters(GenericResourceApiParam portMirrorConfigurationParameters) {
    this.portMirrorConfigurationParameters = portMirrorConfigurationParameters;
    return this;
  }

  /**
   * Get portMirrorConfigurationParameters
   * @return portMirrorConfigurationParameters
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiParam getPortMirrorConfigurationParameters() {
    return portMirrorConfigurationParameters;
  }

  public void setPortMirrorConfigurationParameters(GenericResourceApiParam portMirrorConfigurationParameters) {
    this.portMirrorConfigurationParameters = portMirrorConfigurationParameters;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology genericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology = (GenericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology) o;
    return Objects.equals(this.configurationIdentifiers, genericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology.configurationIdentifiers) &&
        Objects.equals(this.onapModelInformation, genericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology.onapModelInformation) &&
        Objects.equals(this.portMirrorConfigurationAssignments, genericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology.portMirrorConfigurationAssignments) &&
        Objects.equals(this.portMirrorConfigurationParameters, genericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology.portMirrorConfigurationParameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(configurationIdentifiers, onapModelInformation, portMirrorConfigurationAssignments, portMirrorConfigurationParameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiPortmirrorconfigurationtopologyPortMirrorConfigurationTopology {\n");
    
    sb.append("    configurationIdentifiers: ").append(toIndentedString(configurationIdentifiers)).append("\n");
    sb.append("    onapModelInformation: ").append(toIndentedString(onapModelInformation)).append("\n");
    sb.append("    portMirrorConfigurationAssignments: ").append(toIndentedString(portMirrorConfigurationAssignments)).append("\n");
    sb.append("    portMirrorConfigurationParameters: ").append(toIndentedString(portMirrorConfigurationParameters)).append("\n");
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

