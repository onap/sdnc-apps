package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiNetworkInstanceGroup;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiOperStatusData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRequestinformationRequestInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiSdncrequestheaderSdncRequestHeader;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceLevelOperStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceOperationInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceTopology;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataConsumedAllottedResources;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataForwardingPaths;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataNetworks;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataPnfs;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataProvidedAllottedResources;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataProvidedConfigurations;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfs;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceinformationServiceInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicerequestinputServiceRequestInput;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicetopologyServiceTopology;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiVnfcInstanceGroup;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiServicedataServiceData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("service-data")
public class GenericResourceApiServicedataServiceData   {
  @JsonProperty("service-level-oper-status")
  private GenericResourceApiOperStatusData serviceLevelOperStatus = null;

  @JsonProperty("request-information")
  private GenericResourceApiRequestinformationRequestInformation requestInformation = null;

  @JsonProperty("sdnc-request-header")
  private GenericResourceApiSdncrequestheaderSdncRequestHeader sdncRequestHeader = null;

  @JsonProperty("service-information")
  private GenericResourceApiServiceinformationServiceInformation serviceInformation = null;

  @JsonProperty("service-request-input")
  private GenericResourceApiServicerequestinputServiceRequestInput serviceRequestInput = null;

  @JsonProperty("service-topology")
  private GenericResourceApiServicetopologyServiceTopology serviceTopology = null;

  @JsonProperty("vnfc-instance-groups")
  private GenericResourceApiVnfcInstanceGroup vnfcInstanceGroups = null;

  @JsonProperty("provided-configurations")
  private GenericResourceApiServicedataServicedataProvidedConfigurations providedConfigurations = null;

  @JsonProperty("provided-allotted-resources")
  private GenericResourceApiServicedataServicedataProvidedAllottedResources providedAllottedResources = null;

  @JsonProperty("vnfs")
  private GenericResourceApiServicedataServicedataVnfs vnfs = null;

  @JsonProperty("forwarding-paths")
  private GenericResourceApiServicedataServicedataForwardingPaths forwardingPaths = null;

  @JsonProperty("network-instance-groups")
  private GenericResourceApiNetworkInstanceGroup networkInstanceGroups = null;

  @JsonProperty("networks")
  private GenericResourceApiServicedataServicedataNetworks networks = null;

  @JsonProperty("consumed-allotted-resources")
  private GenericResourceApiServicedataServicedataConsumedAllottedResources consumedAllottedResources = null;

  @JsonProperty("pnfs")
  private GenericResourceApiServicedataServicedataPnfs pnfs = null;

  public GenericResourceApiServicedataServiceData serviceLevelOperStatus(GenericResourceApiOperStatusData serviceLevelOperStatus) {
    this.serviceLevelOperStatus = serviceLevelOperStatus;
    return this;
  }

  /**
   * Get serviceLevelOperStatus
   * @return serviceLevelOperStatus
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiOperStatusData getServiceLevelOperStatus() {
    return serviceLevelOperStatus;
  }

  public void setServiceLevelOperStatus(GenericResourceApiOperStatusData serviceLevelOperStatus) {
    this.serviceLevelOperStatus = serviceLevelOperStatus;
  }

  public GenericResourceApiServicedataServiceData requestInformation(GenericResourceApiRequestinformationRequestInformation requestInformation) {
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

  public GenericResourceApiServicedataServiceData sdncRequestHeader(GenericResourceApiSdncrequestheaderSdncRequestHeader sdncRequestHeader) {
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

  public GenericResourceApiServicedataServiceData serviceInformation(GenericResourceApiServiceinformationServiceInformation serviceInformation) {
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

  public GenericResourceApiServicedataServiceData serviceRequestInput(GenericResourceApiServicerequestinputServiceRequestInput serviceRequestInput) {
    this.serviceRequestInput = serviceRequestInput;
    return this;
  }

  /**
   * Get serviceRequestInput
   * @return serviceRequestInput
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicerequestinputServiceRequestInput getServiceRequestInput() {
    return serviceRequestInput;
  }

  public void setServiceRequestInput(GenericResourceApiServicerequestinputServiceRequestInput serviceRequestInput) {
    this.serviceRequestInput = serviceRequestInput;
  }

  public GenericResourceApiServicedataServiceData serviceTopology(GenericResourceApiServicetopologyServiceTopology serviceTopology) {
    this.serviceTopology = serviceTopology;
    return this;
  }

  /**
   * Get serviceTopology
   * @return serviceTopology
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicetopologyServiceTopology getServiceTopology() {
    return serviceTopology;
  }

  public void setServiceTopology(GenericResourceApiServicetopologyServiceTopology serviceTopology) {
    this.serviceTopology = serviceTopology;
  }

  public GenericResourceApiServicedataServiceData vnfcInstanceGroups(GenericResourceApiVnfcInstanceGroup vnfcInstanceGroups) {
    this.vnfcInstanceGroups = vnfcInstanceGroups;
    return this;
  }

  /**
   * Get vnfcInstanceGroups
   * @return vnfcInstanceGroups
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiVnfcInstanceGroup getVnfcInstanceGroups() {
    return vnfcInstanceGroups;
  }

  public void setVnfcInstanceGroups(GenericResourceApiVnfcInstanceGroup vnfcInstanceGroups) {
    this.vnfcInstanceGroups = vnfcInstanceGroups;
  }

  public GenericResourceApiServicedataServiceData providedConfigurations(GenericResourceApiServicedataServicedataProvidedConfigurations providedConfigurations) {
    this.providedConfigurations = providedConfigurations;
    return this;
  }

  /**
   * Get providedConfigurations
   * @return providedConfigurations
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicedataServicedataProvidedConfigurations getProvidedConfigurations() {
    return providedConfigurations;
  }

  public void setProvidedConfigurations(GenericResourceApiServicedataServicedataProvidedConfigurations providedConfigurations) {
    this.providedConfigurations = providedConfigurations;
  }

  public GenericResourceApiServicedataServiceData providedAllottedResources(GenericResourceApiServicedataServicedataProvidedAllottedResources providedAllottedResources) {
    this.providedAllottedResources = providedAllottedResources;
    return this;
  }

  /**
   * Get providedAllottedResources
   * @return providedAllottedResources
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicedataServicedataProvidedAllottedResources getProvidedAllottedResources() {
    return providedAllottedResources;
  }

  public void setProvidedAllottedResources(GenericResourceApiServicedataServicedataProvidedAllottedResources providedAllottedResources) {
    this.providedAllottedResources = providedAllottedResources;
  }

  public GenericResourceApiServicedataServiceData vnfs(GenericResourceApiServicedataServicedataVnfs vnfs) {
    this.vnfs = vnfs;
    return this;
  }

  /**
   * Get vnfs
   * @return vnfs
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicedataServicedataVnfs getVnfs() {
    return vnfs;
  }

  public void setVnfs(GenericResourceApiServicedataServicedataVnfs vnfs) {
    this.vnfs = vnfs;
  }

  public GenericResourceApiServicedataServiceData forwardingPaths(GenericResourceApiServicedataServicedataForwardingPaths forwardingPaths) {
    this.forwardingPaths = forwardingPaths;
    return this;
  }

  /**
   * Get forwardingPaths
   * @return forwardingPaths
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicedataServicedataForwardingPaths getForwardingPaths() {
    return forwardingPaths;
  }

  public void setForwardingPaths(GenericResourceApiServicedataServicedataForwardingPaths forwardingPaths) {
    this.forwardingPaths = forwardingPaths;
  }

  public GenericResourceApiServicedataServiceData networkInstanceGroups(GenericResourceApiNetworkInstanceGroup networkInstanceGroups) {
    this.networkInstanceGroups = networkInstanceGroups;
    return this;
  }

  /**
   * Get networkInstanceGroups
   * @return networkInstanceGroups
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiNetworkInstanceGroup getNetworkInstanceGroups() {
    return networkInstanceGroups;
  }

  public void setNetworkInstanceGroups(GenericResourceApiNetworkInstanceGroup networkInstanceGroups) {
    this.networkInstanceGroups = networkInstanceGroups;
  }

  public GenericResourceApiServicedataServiceData networks(GenericResourceApiServicedataServicedataNetworks networks) {
    this.networks = networks;
    return this;
  }

  /**
   * Get networks
   * @return networks
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicedataServicedataNetworks getNetworks() {
    return networks;
  }

  public void setNetworks(GenericResourceApiServicedataServicedataNetworks networks) {
    this.networks = networks;
  }

  public GenericResourceApiServicedataServiceData consumedAllottedResources(GenericResourceApiServicedataServicedataConsumedAllottedResources consumedAllottedResources) {
    this.consumedAllottedResources = consumedAllottedResources;
    return this;
  }

  /**
   * Get consumedAllottedResources
   * @return consumedAllottedResources
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicedataServicedataConsumedAllottedResources getConsumedAllottedResources() {
    return consumedAllottedResources;
  }

  public void setConsumedAllottedResources(GenericResourceApiServicedataServicedataConsumedAllottedResources consumedAllottedResources) {
    this.consumedAllottedResources = consumedAllottedResources;
  }

  public GenericResourceApiServicedataServiceData pnfs(GenericResourceApiServicedataServicedataPnfs pnfs) {
    this.pnfs = pnfs;
    return this;
  }

  /**
   * Get pnfs
   * @return pnfs
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiServicedataServicedataPnfs getPnfs() {
    return pnfs;
  }

  public void setPnfs(GenericResourceApiServicedataServicedataPnfs pnfs) {
    this.pnfs = pnfs;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiServicedataServiceData genericResourceApiServicedataServiceData = (GenericResourceApiServicedataServiceData) o;
    return Objects.equals(this.serviceLevelOperStatus, genericResourceApiServicedataServiceData.serviceLevelOperStatus) &&
        Objects.equals(this.requestInformation, genericResourceApiServicedataServiceData.requestInformation) &&
        Objects.equals(this.sdncRequestHeader, genericResourceApiServicedataServiceData.sdncRequestHeader) &&
        Objects.equals(this.serviceInformation, genericResourceApiServicedataServiceData.serviceInformation) &&
        Objects.equals(this.serviceRequestInput, genericResourceApiServicedataServiceData.serviceRequestInput) &&
        Objects.equals(this.serviceTopology, genericResourceApiServicedataServiceData.serviceTopology) &&
        Objects.equals(this.vnfcInstanceGroups, genericResourceApiServicedataServiceData.vnfcInstanceGroups) &&
        Objects.equals(this.providedConfigurations, genericResourceApiServicedataServiceData.providedConfigurations) &&
        Objects.equals(this.providedAllottedResources, genericResourceApiServicedataServiceData.providedAllottedResources) &&
        Objects.equals(this.vnfs, genericResourceApiServicedataServiceData.vnfs) &&
        Objects.equals(this.forwardingPaths, genericResourceApiServicedataServiceData.forwardingPaths) &&
        Objects.equals(this.networkInstanceGroups, genericResourceApiServicedataServiceData.networkInstanceGroups) &&
        Objects.equals(this.networks, genericResourceApiServicedataServiceData.networks) &&
        Objects.equals(this.consumedAllottedResources, genericResourceApiServicedataServiceData.consumedAllottedResources) &&
        Objects.equals(this.pnfs, genericResourceApiServicedataServiceData.pnfs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceLevelOperStatus, requestInformation, sdncRequestHeader, serviceInformation, serviceRequestInput, serviceTopology, vnfcInstanceGroups, providedConfigurations, providedAllottedResources, vnfs, forwardingPaths, networkInstanceGroups, networks, consumedAllottedResources, pnfs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiServicedataServiceData {\n");
    
    sb.append("    serviceLevelOperStatus: ").append(toIndentedString(serviceLevelOperStatus)).append("\n");
    sb.append("    requestInformation: ").append(toIndentedString(requestInformation)).append("\n");
    sb.append("    sdncRequestHeader: ").append(toIndentedString(sdncRequestHeader)).append("\n");
    sb.append("    serviceInformation: ").append(toIndentedString(serviceInformation)).append("\n");
    sb.append("    serviceRequestInput: ").append(toIndentedString(serviceRequestInput)).append("\n");
    sb.append("    serviceTopology: ").append(toIndentedString(serviceTopology)).append("\n");
    sb.append("    vnfcInstanceGroups: ").append(toIndentedString(vnfcInstanceGroups)).append("\n");
    sb.append("    providedConfigurations: ").append(toIndentedString(providedConfigurations)).append("\n");
    sb.append("    providedAllottedResources: ").append(toIndentedString(providedAllottedResources)).append("\n");
    sb.append("    vnfs: ").append(toIndentedString(vnfs)).append("\n");
    sb.append("    forwardingPaths: ").append(toIndentedString(forwardingPaths)).append("\n");
    sb.append("    networkInstanceGroups: ").append(toIndentedString(networkInstanceGroups)).append("\n");
    sb.append("    networks: ").append(toIndentedString(networks)).append("\n");
    sb.append("    consumedAllottedResources: ").append(toIndentedString(consumedAllottedResources)).append("\n");
    sb.append("    pnfs: ").append(toIndentedString(pnfs)).append("\n");
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

