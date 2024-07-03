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
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVfModuleAssignments;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVfModuleTopologyIdentifier;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVfmoduleassignmentsVfModuleAssignments;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVfmoduletopologyidentifierVfModuleTopologyIdentifier;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiVfmoduletopologyVfModuleTopology
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("vf-module-topology")
public class GenericResourceApiVfmoduletopologyVfModuleTopology   {
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

  @JsonProperty("vf-module-assignments")
  private GenericResourceApiVfmoduleassignmentsVfModuleAssignments vfModuleAssignments = null;

  @JsonProperty("vf-module-topology-identifier")
  private GenericResourceApiVfmoduletopologyidentifierVfModuleTopologyIdentifier vfModuleTopologyIdentifier = null;

  @JsonProperty("vf-module-parameters")
  private GenericResourceApiParam vfModuleParameters = null;

  @JsonProperty("sdnc-generated-cloud-resources")
  private Boolean sdncGeneratedCloudResources = null;

  public GenericResourceApiVfmoduletopologyVfModuleTopology onapModelInformation(GenericResourceApiOnapmodelinformationOnapModelInformation onapModelInformation) {
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

  public GenericResourceApiVfmoduletopologyVfModuleTopology aicClli(String aicClli) {
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

  public GenericResourceApiVfmoduletopologyVfModuleTopology aicCloudRegion(String aicCloudRegion) {
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

  public GenericResourceApiVfmoduletopologyVfModuleTopology cloudOwner(String cloudOwner) {
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

  public GenericResourceApiVfmoduletopologyVfModuleTopology tenant(String tenant) {
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

  public GenericResourceApiVfmoduletopologyVfModuleTopology vfModuleAssignments(GenericResourceApiVfmoduleassignmentsVfModuleAssignments vfModuleAssignments) {
    this.vfModuleAssignments = vfModuleAssignments;
    return this;
  }

  /**
   * Get vfModuleAssignments
   * @return vfModuleAssignments
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiVfmoduleassignmentsVfModuleAssignments getVfModuleAssignments() {
    return vfModuleAssignments;
  }

  public void setVfModuleAssignments(GenericResourceApiVfmoduleassignmentsVfModuleAssignments vfModuleAssignments) {
    this.vfModuleAssignments = vfModuleAssignments;
  }

  public GenericResourceApiVfmoduletopologyVfModuleTopology vfModuleTopologyIdentifier(GenericResourceApiVfmoduletopologyidentifierVfModuleTopologyIdentifier vfModuleTopologyIdentifier) {
    this.vfModuleTopologyIdentifier = vfModuleTopologyIdentifier;
    return this;
  }

  /**
   * Get vfModuleTopologyIdentifier
   * @return vfModuleTopologyIdentifier
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiVfmoduletopologyidentifierVfModuleTopologyIdentifier getVfModuleTopologyIdentifier() {
    return vfModuleTopologyIdentifier;
  }

  public void setVfModuleTopologyIdentifier(GenericResourceApiVfmoduletopologyidentifierVfModuleTopologyIdentifier vfModuleTopologyIdentifier) {
    this.vfModuleTopologyIdentifier = vfModuleTopologyIdentifier;
  }

  public GenericResourceApiVfmoduletopologyVfModuleTopology vfModuleParameters(GenericResourceApiParam vfModuleParameters) {
    this.vfModuleParameters = vfModuleParameters;
    return this;
  }

  /**
   * Get vfModuleParameters
   * @return vfModuleParameters
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiParam getVfModuleParameters() {
    return vfModuleParameters;
  }

  public void setVfModuleParameters(GenericResourceApiParam vfModuleParameters) {
    this.vfModuleParameters = vfModuleParameters;
  }

  public GenericResourceApiVfmoduletopologyVfModuleTopology sdncGeneratedCloudResources(Boolean sdncGeneratedCloudResources) {
    this.sdncGeneratedCloudResources = sdncGeneratedCloudResources;
    return this;
  }

  /**
   * Indicate if source is sdnc-generated-cloud-resources.When true, the parameters are literal HEAT template parameter names;When false, the parameters need to be converted to HEAT format
   * @return sdncGeneratedCloudResources
  **/
  @ApiModelProperty(value = "Indicate if source is sdnc-generated-cloud-resources.When true, the parameters are literal HEAT template parameter names;When false, the parameters need to be converted to HEAT format")


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
    GenericResourceApiVfmoduletopologyVfModuleTopology genericResourceApiVfmoduletopologyVfModuleTopology = (GenericResourceApiVfmoduletopologyVfModuleTopology) o;
    return Objects.equals(this.onapModelInformation, genericResourceApiVfmoduletopologyVfModuleTopology.onapModelInformation) &&
        Objects.equals(this.aicClli, genericResourceApiVfmoduletopologyVfModuleTopology.aicClli) &&
        Objects.equals(this.aicCloudRegion, genericResourceApiVfmoduletopologyVfModuleTopology.aicCloudRegion) &&
        Objects.equals(this.cloudOwner, genericResourceApiVfmoduletopologyVfModuleTopology.cloudOwner) &&
        Objects.equals(this.tenant, genericResourceApiVfmoduletopologyVfModuleTopology.tenant) &&
        Objects.equals(this.vfModuleAssignments, genericResourceApiVfmoduletopologyVfModuleTopology.vfModuleAssignments) &&
        Objects.equals(this.vfModuleTopologyIdentifier, genericResourceApiVfmoduletopologyVfModuleTopology.vfModuleTopologyIdentifier) &&
        Objects.equals(this.vfModuleParameters, genericResourceApiVfmoduletopologyVfModuleTopology.vfModuleParameters) &&
        Objects.equals(this.sdncGeneratedCloudResources, genericResourceApiVfmoduletopologyVfModuleTopology.sdncGeneratedCloudResources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(onapModelInformation, aicClli, aicCloudRegion, cloudOwner, tenant, vfModuleAssignments, vfModuleTopologyIdentifier, vfModuleParameters, sdncGeneratedCloudResources);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiVfmoduletopologyVfModuleTopology {\n");
    
    sb.append("    onapModelInformation: ").append(toIndentedString(onapModelInformation)).append("\n");
    sb.append("    aicClli: ").append(toIndentedString(aicClli)).append("\n");
    sb.append("    aicCloudRegion: ").append(toIndentedString(aicCloudRegion)).append("\n");
    sb.append("    cloudOwner: ").append(toIndentedString(cloudOwner)).append("\n");
    sb.append("    tenant: ").append(toIndentedString(tenant)).append("\n");
    sb.append("    vfModuleAssignments: ").append(toIndentedString(vfModuleAssignments)).append("\n");
    sb.append("    vfModuleTopologyIdentifier: ").append(toIndentedString(vfModuleTopologyIdentifier)).append("\n");
    sb.append("    vfModuleParameters: ").append(toIndentedString(vfModuleParameters)).append("\n");
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

