package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiAllottedResourceIdentifiers;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiAllottedresourceidentifiersAllottedResourceIdentifiers;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiContrailRouteAssignments;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiContrailrouteassignmentsContrailRouteAssignments;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiOnapModelInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiOnapmodelinformationOnapModelInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiParam;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiContrailroutetopologyContrailRouteTopology
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("contrail-route-topology")
public class GenericResourceApiContrailroutetopologyContrailRouteTopology   {
  @JsonProperty("allotted-resource-identifiers")
  private GenericResourceApiAllottedresourceidentifiersAllottedResourceIdentifiers allottedResourceIdentifiers = null;

  @JsonProperty("contrail-route-assignments")
  private GenericResourceApiContrailrouteassignmentsContrailRouteAssignments contrailRouteAssignments = null;

  @JsonProperty("onap-model-information")
  private GenericResourceApiOnapmodelinformationOnapModelInformation onapModelInformation = null;

  @JsonProperty("contrail-route-parameters")
  private GenericResourceApiParam contrailRouteParameters = null;

  public GenericResourceApiContrailroutetopologyContrailRouteTopology allottedResourceIdentifiers(GenericResourceApiAllottedresourceidentifiersAllottedResourceIdentifiers allottedResourceIdentifiers) {
    this.allottedResourceIdentifiers = allottedResourceIdentifiers;
    return this;
  }

  /**
   * Get allottedResourceIdentifiers
   * @return allottedResourceIdentifiers
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiAllottedresourceidentifiersAllottedResourceIdentifiers getAllottedResourceIdentifiers() {
    return allottedResourceIdentifiers;
  }

  public void setAllottedResourceIdentifiers(GenericResourceApiAllottedresourceidentifiersAllottedResourceIdentifiers allottedResourceIdentifiers) {
    this.allottedResourceIdentifiers = allottedResourceIdentifiers;
  }

  public GenericResourceApiContrailroutetopologyContrailRouteTopology contrailRouteAssignments(GenericResourceApiContrailrouteassignmentsContrailRouteAssignments contrailRouteAssignments) {
    this.contrailRouteAssignments = contrailRouteAssignments;
    return this;
  }

  /**
   * Get contrailRouteAssignments
   * @return contrailRouteAssignments
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiContrailrouteassignmentsContrailRouteAssignments getContrailRouteAssignments() {
    return contrailRouteAssignments;
  }

  public void setContrailRouteAssignments(GenericResourceApiContrailrouteassignmentsContrailRouteAssignments contrailRouteAssignments) {
    this.contrailRouteAssignments = contrailRouteAssignments;
  }

  public GenericResourceApiContrailroutetopologyContrailRouteTopology onapModelInformation(GenericResourceApiOnapmodelinformationOnapModelInformation onapModelInformation) {
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

  public GenericResourceApiContrailroutetopologyContrailRouteTopology contrailRouteParameters(GenericResourceApiParam contrailRouteParameters) {
    this.contrailRouteParameters = contrailRouteParameters;
    return this;
  }

  /**
   * Get contrailRouteParameters
   * @return contrailRouteParameters
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiParam getContrailRouteParameters() {
    return contrailRouteParameters;
  }

  public void setContrailRouteParameters(GenericResourceApiParam contrailRouteParameters) {
    this.contrailRouteParameters = contrailRouteParameters;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiContrailroutetopologyContrailRouteTopology genericResourceApiContrailroutetopologyContrailRouteTopology = (GenericResourceApiContrailroutetopologyContrailRouteTopology) o;
    return Objects.equals(this.allottedResourceIdentifiers, genericResourceApiContrailroutetopologyContrailRouteTopology.allottedResourceIdentifiers) &&
        Objects.equals(this.contrailRouteAssignments, genericResourceApiContrailroutetopologyContrailRouteTopology.contrailRouteAssignments) &&
        Objects.equals(this.onapModelInformation, genericResourceApiContrailroutetopologyContrailRouteTopology.onapModelInformation) &&
        Objects.equals(this.contrailRouteParameters, genericResourceApiContrailroutetopologyContrailRouteTopology.contrailRouteParameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allottedResourceIdentifiers, contrailRouteAssignments, onapModelInformation, contrailRouteParameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiContrailroutetopologyContrailRouteTopology {\n");
    
    sb.append("    allottedResourceIdentifiers: ").append(toIndentedString(allottedResourceIdentifiers)).append("\n");
    sb.append("    contrailRouteAssignments: ").append(toIndentedString(contrailRouteAssignments)).append("\n");
    sb.append("    onapModelInformation: ").append(toIndentedString(onapModelInformation)).append("\n");
    sb.append("    contrailRouteParameters: ").append(toIndentedString(contrailRouteParameters)).append("\n");
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

