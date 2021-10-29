package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadNetworkTopologyInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadOperStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadVfModuleTopologyInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadoperstatusPreloadOperStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiPreloadvfmoduletopologyinformationPreloadVfModuleTopologyInformation;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiPreloaddataPreloadData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("preload-data")
public class GenericResourceApiPreloaddataPreloadData   {
  @JsonProperty("preload-network-topology-information")
  private GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation preloadNetworkTopologyInformation = null;

  @JsonProperty("preload-oper-status")
  private GenericResourceApiPreloadoperstatusPreloadOperStatus preloadOperStatus = null;

  @JsonProperty("preload-vf-module-topology-information")
  private GenericResourceApiPreloadvfmoduletopologyinformationPreloadVfModuleTopologyInformation preloadVfModuleTopologyInformation = null;

  public GenericResourceApiPreloaddataPreloadData preloadNetworkTopologyInformation(GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation preloadNetworkTopologyInformation) {
    this.preloadNetworkTopologyInformation = preloadNetworkTopologyInformation;
    return this;
  }

  /**
   * Get preloadNetworkTopologyInformation
   * @return preloadNetworkTopologyInformation
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation getPreloadNetworkTopologyInformation() {
    return preloadNetworkTopologyInformation;
  }

  public void setPreloadNetworkTopologyInformation(GenericResourceApiPreloadnetworktopologyinformationPreloadNetworkTopologyInformation preloadNetworkTopologyInformation) {
    this.preloadNetworkTopologyInformation = preloadNetworkTopologyInformation;
  }

  public GenericResourceApiPreloaddataPreloadData preloadOperStatus(GenericResourceApiPreloadoperstatusPreloadOperStatus preloadOperStatus) {
    this.preloadOperStatus = preloadOperStatus;
    return this;
  }

  /**
   * Get preloadOperStatus
   * @return preloadOperStatus
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiPreloadoperstatusPreloadOperStatus getPreloadOperStatus() {
    return preloadOperStatus;
  }

  public void setPreloadOperStatus(GenericResourceApiPreloadoperstatusPreloadOperStatus preloadOperStatus) {
    this.preloadOperStatus = preloadOperStatus;
  }

  public GenericResourceApiPreloaddataPreloadData preloadVfModuleTopologyInformation(GenericResourceApiPreloadvfmoduletopologyinformationPreloadVfModuleTopologyInformation preloadVfModuleTopologyInformation) {
    this.preloadVfModuleTopologyInformation = preloadVfModuleTopologyInformation;
    return this;
  }

  /**
   * Get preloadVfModuleTopologyInformation
   * @return preloadVfModuleTopologyInformation
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiPreloadvfmoduletopologyinformationPreloadVfModuleTopologyInformation getPreloadVfModuleTopologyInformation() {
    return preloadVfModuleTopologyInformation;
  }

  public void setPreloadVfModuleTopologyInformation(GenericResourceApiPreloadvfmoduletopologyinformationPreloadVfModuleTopologyInformation preloadVfModuleTopologyInformation) {
    this.preloadVfModuleTopologyInformation = preloadVfModuleTopologyInformation;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiPreloaddataPreloadData genericResourceApiPreloaddataPreloadData = (GenericResourceApiPreloaddataPreloadData) o;
    return Objects.equals(this.preloadNetworkTopologyInformation, genericResourceApiPreloaddataPreloadData.preloadNetworkTopologyInformation) &&
        Objects.equals(this.preloadOperStatus, genericResourceApiPreloaddataPreloadData.preloadOperStatus) &&
        Objects.equals(this.preloadVfModuleTopologyInformation, genericResourceApiPreloaddataPreloadData.preloadVfModuleTopologyInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(preloadNetworkTopologyInformation, preloadOperStatus, preloadVfModuleTopologyInformation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiPreloaddataPreloadData {\n");
    
    sb.append("    preloadNetworkTopologyInformation: ").append(toIndentedString(preloadNetworkTopologyInformation)).append("\n");
    sb.append("    preloadOperStatus: ").append(toIndentedString(preloadOperStatus)).append("\n");
    sb.append("    preloadVfModuleTopologyInformation: ").append(toIndentedString(preloadVfModuleTopologyInformation)).append("\n");
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

