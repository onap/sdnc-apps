package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiLicenseinformationLicenseInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiOperStatusData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRequestinformationRequestInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiSdncrequestheaderSdncRequestHeader;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnfVnfdataVnfProvidedAllottedResources;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceinformationServiceInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfLevelOperStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfNetworkCollection;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfOperationInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfSubInterfaceGroup;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfTopology;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfinformationVnfInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfrequestinputVnfRequestInput;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnftopologyVnfTopology;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * GenericResourceApiServicedataServicedataVnfsVnfVnfData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-12T18:08:46.019-04:00")
@JsonRootName("vnf-data")
public class GenericResourceApiServicedataServicedataVnfsVnfVnfData   {
  @JsonProperty("vnf-level-oper-status")
  private GenericResourceApiOperStatusData vnfLevelOperStatus = null;

  @JsonProperty("license-information")
  private GenericResourceApiLicenseinformationLicenseInformation licenseInformation = null;

  @JsonProperty("request-information")
  private GenericResourceApiRequestinformationRequestInformation requestInformation = null;

  @JsonProperty("sdnc-request-header")
  private GenericResourceApiSdncrequestheaderSdncRequestHeader sdncRequestHeader = null;

  @JsonProperty("service-information")
  private GenericResourceApiServiceinformationServiceInformation serviceInformation = null;

  @JsonProperty("vnf-information")
  private GenericResourceApiVnfinformationVnfInformation vnfInformation = null;

  @JsonProperty("vnf-request-input")
  private GenericResourceApiVnfrequestinputVnfRequestInput vnfRequestInput = null;

  @JsonProperty("vnf-topology")
  private GenericResourceApiVnftopologyVnfTopology vnfTopology = null;

  @JsonProperty("vnf-network-collections")
  private GenericResourceApiVnfNetworkCollection vnfNetworkCollections = null;

  @JsonProperty("vnf-sub-interface-groups")
  private GenericResourceApiVnfSubInterfaceGroup vnfSubInterfaceGroups = null;

  @JsonProperty("vf-modules")
  private GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules vfModules = null;

  @JsonProperty("vnf-provided-allotted-resources")
  private GenericResourceApiServicedataServicedataVnfsVnfVnfdataVnfProvidedAllottedResources vnfProvidedAllottedResources = null;

  public GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfLevelOperStatus(GenericResourceApiOperStatusData vnfLevelOperStatus) {
    this.vnfLevelOperStatus = vnfLevelOperStatus;
    return this;
  }

  /**
   * Get vnfLevelOperStatus
   * @return vnfLevelOperStatus
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiOperStatusData getVnfLevelOperStatus() {
    return vnfLevelOperStatus;
  }

  public void setVnfLevelOperStatus(GenericResourceApiOperStatusData vnfLevelOperStatus) {
    this.vnfLevelOperStatus = vnfLevelOperStatus;
  }

  public GenericResourceApiServicedataServicedataVnfsVnfVnfData licenseInformation(GenericResourceApiLicenseinformationLicenseInformation licenseInformation) {
    this.licenseInformation = licenseInformation;
    return this;
  }

  /**
   * Get licenseInformation
   * @return licenseInformation
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiLicenseinformationLicenseInformation getLicenseInformation() {
    return licenseInformation;
  }

  public void setLicenseInformation(GenericResourceApiLicenseinformationLicenseInformation licenseInformation) {
    this.licenseInformation = licenseInformation;
  }

  public GenericResourceApiServicedataServicedataVnfsVnfVnfData requestInformation(GenericResourceApiRequestinformationRequestInformation requestInformation) {
    this.requestInformation = requestInformation;
    return this;
  }

  /**
   * Get requestInformation
   * @return requestInformation
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiRequestinformationRequestInformation getRequestInformation() {
    return requestInformation;
  }

  public void setRequestInformation(GenericResourceApiRequestinformationRequestInformation requestInformation) {
    this.requestInformation = requestInformation;
  }

  public GenericResourceApiServicedataServicedataVnfsVnfVnfData sdncRequestHeader(GenericResourceApiSdncrequestheaderSdncRequestHeader sdncRequestHeader) {
    this.sdncRequestHeader = sdncRequestHeader;
    return this;
  }

  /**
   * Get sdncRequestHeader
   * @return sdncRequestHeader
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiSdncrequestheaderSdncRequestHeader getSdncRequestHeader() {
    return sdncRequestHeader;
  }

  public void setSdncRequestHeader(GenericResourceApiSdncrequestheaderSdncRequestHeader sdncRequestHeader) {
    this.sdncRequestHeader = sdncRequestHeader;
  }

  public GenericResourceApiServicedataServicedataVnfsVnfVnfData serviceInformation(GenericResourceApiServiceinformationServiceInformation serviceInformation) {
    this.serviceInformation = serviceInformation;
    return this;
  }

  /**
   * Get serviceInformation
   * @return serviceInformation
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServiceinformationServiceInformation getServiceInformation() {
    return serviceInformation;
  }

  public void setServiceInformation(GenericResourceApiServiceinformationServiceInformation serviceInformation) {
    this.serviceInformation = serviceInformation;
  }

  public GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfInformation(GenericResourceApiVnfinformationVnfInformation vnfInformation) {
    this.vnfInformation = vnfInformation;
    return this;
  }

  /**
   * Get vnfInformation
   * @return vnfInformation
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiVnfinformationVnfInformation getVnfInformation() {
    return vnfInformation;
  }

  public void setVnfInformation(GenericResourceApiVnfinformationVnfInformation vnfInformation) {
    this.vnfInformation = vnfInformation;
  }

  public GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfRequestInput(GenericResourceApiVnfrequestinputVnfRequestInput vnfRequestInput) {
    this.vnfRequestInput = vnfRequestInput;
    return this;
  }

  /**
   * Get vnfRequestInput
   * @return vnfRequestInput
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiVnfrequestinputVnfRequestInput getVnfRequestInput() {
    return vnfRequestInput;
  }

  public void setVnfRequestInput(GenericResourceApiVnfrequestinputVnfRequestInput vnfRequestInput) {
    this.vnfRequestInput = vnfRequestInput;
  }

  public GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfTopology(GenericResourceApiVnftopologyVnfTopology vnfTopology) {
    this.vnfTopology = vnfTopology;
    return this;
  }

  /**
   * Get vnfTopology
   * @return vnfTopology
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiVnftopologyVnfTopology getVnfTopology() {
    return vnfTopology;
  }

  public void setVnfTopology(GenericResourceApiVnftopologyVnfTopology vnfTopology) {
    this.vnfTopology = vnfTopology;
  }

  public GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfNetworkCollections(GenericResourceApiVnfNetworkCollection vnfNetworkCollections) {
    this.vnfNetworkCollections = vnfNetworkCollections;
    return this;
  }

  /**
   * Get vnfNetworkCollections
   * @return vnfNetworkCollections
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiVnfNetworkCollection getVnfNetworkCollections() {
    return vnfNetworkCollections;
  }

  public void setVnfNetworkCollections(GenericResourceApiVnfNetworkCollection vnfNetworkCollections) {
    this.vnfNetworkCollections = vnfNetworkCollections;
  }

  public GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfSubInterfaceGroups(GenericResourceApiVnfSubInterfaceGroup vnfSubInterfaceGroups) {
    this.vnfSubInterfaceGroups = vnfSubInterfaceGroups;
    return this;
  }

  /**
   * Get vnfSubInterfaceGroups
   * @return vnfSubInterfaceGroups
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiVnfSubInterfaceGroup getVnfSubInterfaceGroups() {
    return vnfSubInterfaceGroups;
  }

  public void setVnfSubInterfaceGroups(GenericResourceApiVnfSubInterfaceGroup vnfSubInterfaceGroups) {
    this.vnfSubInterfaceGroups = vnfSubInterfaceGroups;
  }

  public GenericResourceApiServicedataServicedataVnfsVnfVnfData vfModules(GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules vfModules) {
    this.vfModules = vfModules;
    return this;
  }

  /**
   * Get vfModules
   * @return vfModules
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules getVfModules() {
    return vfModules;
  }

  public void setVfModules(GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules vfModules) {
    this.vfModules = vfModules;
  }

  public GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfProvidedAllottedResources(GenericResourceApiServicedataServicedataVnfsVnfVnfdataVnfProvidedAllottedResources vnfProvidedAllottedResources) {
    this.vnfProvidedAllottedResources = vnfProvidedAllottedResources;
    return this;
  }

  /**
   * Get vnfProvidedAllottedResources
   * @return vnfProvidedAllottedResources
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicedataServicedataVnfsVnfVnfdataVnfProvidedAllottedResources getVnfProvidedAllottedResources() {
    return vnfProvidedAllottedResources;
  }

  public void setVnfProvidedAllottedResources(GenericResourceApiServicedataServicedataVnfsVnfVnfdataVnfProvidedAllottedResources vnfProvidedAllottedResources) {
    this.vnfProvidedAllottedResources = vnfProvidedAllottedResources;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiServicedataServicedataVnfsVnfVnfData genericResourceApiServicedataServicedataVnfsVnfVnfData = (GenericResourceApiServicedataServicedataVnfsVnfVnfData) o;
    return Objects.equals(this.vnfLevelOperStatus, genericResourceApiServicedataServicedataVnfsVnfVnfData.vnfLevelOperStatus) &&
        Objects.equals(this.licenseInformation, genericResourceApiServicedataServicedataVnfsVnfVnfData.licenseInformation) &&
        Objects.equals(this.requestInformation, genericResourceApiServicedataServicedataVnfsVnfVnfData.requestInformation) &&
        Objects.equals(this.sdncRequestHeader, genericResourceApiServicedataServicedataVnfsVnfVnfData.sdncRequestHeader) &&
        Objects.equals(this.serviceInformation, genericResourceApiServicedataServicedataVnfsVnfVnfData.serviceInformation) &&
        Objects.equals(this.vnfInformation, genericResourceApiServicedataServicedataVnfsVnfVnfData.vnfInformation) &&
        Objects.equals(this.vnfRequestInput, genericResourceApiServicedataServicedataVnfsVnfVnfData.vnfRequestInput) &&
        Objects.equals(this.vnfTopology, genericResourceApiServicedataServicedataVnfsVnfVnfData.vnfTopology) &&
        Objects.equals(this.vnfNetworkCollections, genericResourceApiServicedataServicedataVnfsVnfVnfData.vnfNetworkCollections) &&
        Objects.equals(this.vnfSubInterfaceGroups, genericResourceApiServicedataServicedataVnfsVnfVnfData.vnfSubInterfaceGroups) &&
        Objects.equals(this.vfModules, genericResourceApiServicedataServicedataVnfsVnfVnfData.vfModules) &&
        Objects.equals(this.vnfProvidedAllottedResources, genericResourceApiServicedataServicedataVnfsVnfVnfData.vnfProvidedAllottedResources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vnfLevelOperStatus, licenseInformation, requestInformation, sdncRequestHeader, serviceInformation, vnfInformation, vnfRequestInput, vnfTopology, vnfNetworkCollections, vnfSubInterfaceGroups, vfModules, vnfProvidedAllottedResources);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiServicedataServicedataVnfsVnfVnfData {\n");
    
    sb.append("    vnfLevelOperStatus: ").append(toIndentedString(vnfLevelOperStatus)).append("\n");
    sb.append("    licenseInformation: ").append(toIndentedString(licenseInformation)).append("\n");
    sb.append("    requestInformation: ").append(toIndentedString(requestInformation)).append("\n");
    sb.append("    sdncRequestHeader: ").append(toIndentedString(sdncRequestHeader)).append("\n");
    sb.append("    serviceInformation: ").append(toIndentedString(serviceInformation)).append("\n");
    sb.append("    vnfInformation: ").append(toIndentedString(vnfInformation)).append("\n");
    sb.append("    vnfRequestInput: ").append(toIndentedString(vnfRequestInput)).append("\n");
    sb.append("    vnfTopology: ").append(toIndentedString(vnfTopology)).append("\n");
    sb.append("    vnfNetworkCollections: ").append(toIndentedString(vnfNetworkCollections)).append("\n");
    sb.append("    vnfSubInterfaceGroups: ").append(toIndentedString(vnfSubInterfaceGroups)).append("\n");
    sb.append("    vfModules: ").append(toIndentedString(vfModules)).append("\n");
    sb.append("    vnfProvidedAllottedResources: ").append(toIndentedString(vnfProvidedAllottedResources)).append("\n");
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

