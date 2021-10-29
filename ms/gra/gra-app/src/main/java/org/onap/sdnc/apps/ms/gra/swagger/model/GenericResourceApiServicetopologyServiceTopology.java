package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.gson.annotations.JsonAdapter;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiOnapModelInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiOnapmodelinformationOnapModelInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceAssignments;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceParameters;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceTopologyIdentifier;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceassignmentsServiceAssignments;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceparametersServiceParameters;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicetopologyidentifierServiceTopologyIdentifier;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiServicetopologyServiceTopology
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("service-topology")
public class GenericResourceApiServicetopologyServiceTopology   {
  @JsonProperty("onap-model-information")
  private GenericResourceApiOnapmodelinformationOnapModelInformation onapModelInformation = null;

  @JsonProperty("service-assignments")
  private GenericResourceApiServiceassignmentsServiceAssignments serviceAssignments = null;

  @JsonProperty("service-parameters")
  private GenericResourceApiServiceparametersServiceParameters serviceParameters = null;

  @JsonProperty("service-topology-identifier")
  private GenericResourceApiServicetopologyidentifierServiceTopologyIdentifier serviceTopologyIdentifier = null;

  public GenericResourceApiServicetopologyServiceTopology onapModelInformation(GenericResourceApiOnapmodelinformationOnapModelInformation onapModelInformation) {
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

  public GenericResourceApiServicetopologyServiceTopology serviceAssignments(GenericResourceApiServiceassignmentsServiceAssignments serviceAssignments) {
    this.serviceAssignments = serviceAssignments;
    return this;
  }

  /**
   * Get serviceAssignments
   * @return serviceAssignments
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServiceassignmentsServiceAssignments getServiceAssignments() {
    return serviceAssignments;
  }

  public void setServiceAssignments(GenericResourceApiServiceassignmentsServiceAssignments serviceAssignments) {
    this.serviceAssignments = serviceAssignments;
  }

  public GenericResourceApiServicetopologyServiceTopology serviceParameters(GenericResourceApiServiceparametersServiceParameters serviceParameters) {
    this.serviceParameters = serviceParameters;
    return this;
  }

  /**
   * Get serviceParameters
   * @return serviceParameters
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServiceparametersServiceParameters getServiceParameters() {
    return serviceParameters;
  }

  public void setServiceParameters(GenericResourceApiServiceparametersServiceParameters serviceParameters) {
    this.serviceParameters = serviceParameters;
  }

  public GenericResourceApiServicetopologyServiceTopology serviceTopologyIdentifier(GenericResourceApiServicetopologyidentifierServiceTopologyIdentifier serviceTopologyIdentifier) {
    this.serviceTopologyIdentifier = serviceTopologyIdentifier;
    return this;
  }

  /**
   * Get serviceTopologyIdentifier
   * @return serviceTopologyIdentifier
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicetopologyidentifierServiceTopologyIdentifier getServiceTopologyIdentifier() {
    return serviceTopologyIdentifier;
  }

  public void setServiceTopologyIdentifier(GenericResourceApiServicetopologyidentifierServiceTopologyIdentifier serviceTopologyIdentifier) {
    this.serviceTopologyIdentifier = serviceTopologyIdentifier;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiServicetopologyServiceTopology genericResourceApiServicetopologyServiceTopology = (GenericResourceApiServicetopologyServiceTopology) o;
    return Objects.equals(this.onapModelInformation, genericResourceApiServicetopologyServiceTopology.onapModelInformation) &&
        Objects.equals(this.serviceAssignments, genericResourceApiServicetopologyServiceTopology.serviceAssignments) &&
        Objects.equals(this.serviceParameters, genericResourceApiServicetopologyServiceTopology.serviceParameters) &&
        Objects.equals(this.serviceTopologyIdentifier, genericResourceApiServicetopologyServiceTopology.serviceTopologyIdentifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(onapModelInformation, serviceAssignments, serviceParameters, serviceTopologyIdentifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiServicetopologyServiceTopology {\n");
    
    sb.append("    onapModelInformation: ").append(toIndentedString(onapModelInformation)).append("\n");
    sb.append("    serviceAssignments: ").append(toIndentedString(serviceAssignments)).append("\n");
    sb.append("    serviceParameters: ").append(toIndentedString(serviceParameters)).append("\n");
    sb.append("    serviceTopologyIdentifier: ").append(toIndentedString(serviceTopologyIdentifier)).append("\n");
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

