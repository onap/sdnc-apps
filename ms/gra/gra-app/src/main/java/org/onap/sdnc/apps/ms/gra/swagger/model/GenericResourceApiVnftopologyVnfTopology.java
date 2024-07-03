package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiOnapModelInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiOnapmodelinformationOnapModelInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiParam;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRegionIdentifier;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfResourceAssignments;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfTopologyIdentifierStructure;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfresourceassignmentsVnfResourceAssignments;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnftopologyidentifierstructureVnfTopologyIdentifierStructure;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiVnftopologyVnfTopology
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("vnf-topology")
public class GenericResourceApiVnftopologyVnfTopology   {
  @JsonProperty("onap-model-information")
  private GenericResourceApiOnapmodelinformationOnapModelInformation onapModelInformation = null;

  @JsonProperty("aic-clli")
  private String aicClli = null;

  @JsonProperty("aic-cloud-region")
  private String aicCloudRegion = null;

  @JsonProperty("cloud-owner")
  private String cloudOwner = null;

  @JsonProperty("tenant")
  private String tenant = null;

  @JsonProperty("vnf-resource-assignments")
  private GenericResourceApiVnfresourceassignmentsVnfResourceAssignments vnfResourceAssignments = null;

  @JsonProperty("vnf-topology-identifier-structure")
  private GenericResourceApiVnftopologyidentifierstructureVnfTopologyIdentifierStructure vnfTopologyIdentifierStructure = null;

  @JsonProperty("vnf-parameters-data")
  private GenericResourceApiParam vnfParametersData = null;

  @JsonProperty("sdnc-generated-cloud-resources")
  private Boolean sdncGeneratedCloudResources = null;

  public GenericResourceApiVnftopologyVnfTopology onapModelInformation(GenericResourceApiOnapmodelinformationOnapModelInformation onapModelInformation) {
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

  public GenericResourceApiVnftopologyVnfTopology aicClli(String aicClli) {
    this.aicClli = aicClli;
    return this;
  }

  /**
   * Not expected to be used
   * @return aicClli
  **/
  @ApiModelProperty(value = "Not expected to be used")


  public String getAicClli() {
    return aicClli;
  }

  public void setAicClli(String aicClli) {
    this.aicClli = aicClli;
  }

  public GenericResourceApiVnftopologyVnfTopology aicCloudRegion(String aicCloudRegion) {
    this.aicCloudRegion = aicCloudRegion;
    return this;
  }

  /**
   * The AIC cloud region which maps to contrail versions
   * @return aicCloudRegion
  **/
  @ApiModelProperty(value = "The AIC cloud region which maps to contrail versions")


  public String getAicCloudRegion() {
    return aicCloudRegion;
  }

  public void setAicCloudRegion(String aicCloudRegion) {
    this.aicCloudRegion = aicCloudRegion;
  }

  public GenericResourceApiVnftopologyVnfTopology cloudOwner(String cloudOwner) {
    this.cloudOwner = cloudOwner;
    return this;
  }

  /**
   * Get cloudOwner
   * @return cloudOwner
  **/
  @ApiModelProperty(value = "")


  public String getCloudOwner() {
    return cloudOwner;
  }

  public void setCloudOwner(String cloudOwner) {
    this.cloudOwner = cloudOwner;
  }

  public GenericResourceApiVnftopologyVnfTopology tenant(String tenant) {
    this.tenant = tenant;
    return this;
  }

  /**
   * Get tenant
   * @return tenant
  **/
  @ApiModelProperty(value = "")


  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  public GenericResourceApiVnftopologyVnfTopology vnfResourceAssignments(GenericResourceApiVnfresourceassignmentsVnfResourceAssignments vnfResourceAssignments) {
    this.vnfResourceAssignments = vnfResourceAssignments;
    return this;
  }

  /**
   * Get vnfResourceAssignments
   * @return vnfResourceAssignments
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiVnfresourceassignmentsVnfResourceAssignments getVnfResourceAssignments() {
    return vnfResourceAssignments;
  }

  public void setVnfResourceAssignments(GenericResourceApiVnfresourceassignmentsVnfResourceAssignments vnfResourceAssignments) {
    this.vnfResourceAssignments = vnfResourceAssignments;
  }

  public GenericResourceApiVnftopologyVnfTopology vnfTopologyIdentifierStructure(GenericResourceApiVnftopologyidentifierstructureVnfTopologyIdentifierStructure vnfTopologyIdentifierStructure) {
    this.vnfTopologyIdentifierStructure = vnfTopologyIdentifierStructure;
    return this;
  }

  /**
   * Get vnfTopologyIdentifierStructure
   * @return vnfTopologyIdentifierStructure
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiVnftopologyidentifierstructureVnfTopologyIdentifierStructure getVnfTopologyIdentifierStructure() {
    return vnfTopologyIdentifierStructure;
  }

  public void setVnfTopologyIdentifierStructure(GenericResourceApiVnftopologyidentifierstructureVnfTopologyIdentifierStructure vnfTopologyIdentifierStructure) {
    this.vnfTopologyIdentifierStructure = vnfTopologyIdentifierStructure;
  }

  public GenericResourceApiVnftopologyVnfTopology vnfParametersData(GenericResourceApiParam vnfParametersData) {
    this.vnfParametersData = vnfParametersData;
    return this;
  }

  /**
   * Get vnfParametersData
   * @return vnfParametersData
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiParam getVnfParametersData() {
    return vnfParametersData;
  }

  public void setVnfParametersData(GenericResourceApiParam vnfParametersData) {
    this.vnfParametersData = vnfParametersData;
  }

  public GenericResourceApiVnftopologyVnfTopology sdncGeneratedCloudResources(Boolean sdncGeneratedCloudResources) {
    this.sdncGeneratedCloudResources = sdncGeneratedCloudResources;
    return this;
  }

  /**
   * Indicate if source is sdnc generated cloud param. When true, the parameters are literal HEAT template parameter names;When false, the parameters need to be converted to HEAT format
   * @return sdncGeneratedCloudResources
  **/
  @ApiModelProperty(value = "Indicate if source is sdnc generated cloud param. When true, the parameters are literal HEAT template parameter names;When false, the parameters need to be converted to HEAT format")


  public Boolean isSdncGeneratedCloudResources() {
    return sdncGeneratedCloudResources;
  }

  public void setSdncGeneratedCloudResources(Boolean sdncGeneratedCloudResources) {
    this.sdncGeneratedCloudResources = sdncGeneratedCloudResources;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiVnftopologyVnfTopology genericResourceApiVnftopologyVnfTopology = (GenericResourceApiVnftopologyVnfTopology) o;
    return Objects.equals(this.onapModelInformation, genericResourceApiVnftopologyVnfTopology.onapModelInformation) &&
        Objects.equals(this.aicClli, genericResourceApiVnftopologyVnfTopology.aicClli) &&
        Objects.equals(this.aicCloudRegion, genericResourceApiVnftopologyVnfTopology.aicCloudRegion) &&
        Objects.equals(this.cloudOwner, genericResourceApiVnftopologyVnfTopology.cloudOwner) &&
        Objects.equals(this.tenant, genericResourceApiVnftopologyVnfTopology.tenant) &&
        Objects.equals(this.vnfResourceAssignments, genericResourceApiVnftopologyVnfTopology.vnfResourceAssignments) &&
        Objects.equals(this.vnfTopologyIdentifierStructure, genericResourceApiVnftopologyVnfTopology.vnfTopologyIdentifierStructure) &&
        Objects.equals(this.vnfParametersData, genericResourceApiVnftopologyVnfTopology.vnfParametersData) &&
        Objects.equals(this.sdncGeneratedCloudResources, genericResourceApiVnftopologyVnfTopology.sdncGeneratedCloudResources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(onapModelInformation, aicClli, aicCloudRegion, cloudOwner, tenant, vnfResourceAssignments, vnfTopologyIdentifierStructure, vnfParametersData, sdncGeneratedCloudResources);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiVnftopologyVnfTopology {\n");
    
    sb.append("    onapModelInformation: ").append(toIndentedString(onapModelInformation)).append("\n");
    sb.append("    aicClli: ").append(toIndentedString(aicClli)).append("\n");
    sb.append("    aicCloudRegion: ").append(toIndentedString(aicCloudRegion)).append("\n");
    sb.append("    cloudOwner: ").append(toIndentedString(cloudOwner)).append("\n");
    sb.append("    tenant: ").append(toIndentedString(tenant)).append("\n");
    sb.append("    vnfResourceAssignments: ").append(toIndentedString(vnfResourceAssignments)).append("\n");
    sb.append("    vnfTopologyIdentifierStructure: ").append(toIndentedString(vnfTopologyIdentifierStructure)).append("\n");
    sb.append("    vnfParametersData: ").append(toIndentedString(vnfParametersData)).append("\n");
    sb.append("    sdncGeneratedCloudResources: ").append(toIndentedString(sdncGeneratedCloudResources)).append("\n");
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

