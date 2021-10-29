package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiVpnbindingsVpnBindings
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-23T12:02:01.518-04:00")
@JsonRootName("vpn-bindings")
public class GenericResourceApiVpnbindingsVpnBindings   {
  @JsonProperty("vpn-name")
  private String vpnName = null;

  @JsonProperty("vpn-binding-id")
  private String vpnBindingId = null;

  @JsonProperty("route-target-role")
  private String routeTargetRole = null;

  @JsonProperty("aic-zone")
  private String aicZone = null;

  @JsonProperty("global-route-target")
  private String globalRouteTarget = null;

  public GenericResourceApiVpnbindingsVpnBindings vpnName(String vpnName) {
    this.vpnName = vpnName;
    return this;
  }

  /**
   * Get vpnName
   * @return vpnName
  **/
  @ApiModelProperty(value = "")


  public String getVpnName() {
    return vpnName;
  }

  public void setVpnName(String vpnName) {
    this.vpnName = vpnName;
  }

  public GenericResourceApiVpnbindingsVpnBindings vpnBindingId(String vpnBindingId) {
    this.vpnBindingId = vpnBindingId;
    return this;
  }

  /**
   * Get vpnBindingId
   * @return vpnBindingId
  **/
  @ApiModelProperty(value = "")


  public String getVpnBindingId() {
    return vpnBindingId;
  }

  public void setVpnBindingId(String vpnBindingId) {
    this.vpnBindingId = vpnBindingId;
  }

  public GenericResourceApiVpnbindingsVpnBindings routeTargetRole(String routeTargetRole) {
    this.routeTargetRole = routeTargetRole;
    return this;
  }

  /**
   * Get routeTargetRole
   * @return routeTargetRole
  **/
  @ApiModelProperty(value = "")


  public String getRouteTargetRole() {
    return routeTargetRole;
  }

  public void setRouteTargetRole(String routeTargetRole) {
    this.routeTargetRole = routeTargetRole;
  }

  public GenericResourceApiVpnbindingsVpnBindings aicZone(String aicZone) {
    this.aicZone = aicZone;
    return this;
  }

  /**
   * AIC Zone the route target associated with
   * @return aicZone
  **/
  @ApiModelProperty(value = "AIC Zone the route target associated with")


  public String getAicZone() {
    return aicZone;
  }

  public void setAicZone(String aicZone) {
    this.aicZone = aicZone;
  }

  public GenericResourceApiVpnbindingsVpnBindings globalRouteTarget(String globalRouteTarget) {
    this.globalRouteTarget = globalRouteTarget;
    return this;
  }

  /**
   * Get globalRouteTarget
   * @return globalRouteTarget
  **/
  @ApiModelProperty(value = "")


  public String getGlobalRouteTarget() {
    return globalRouteTarget;
  }

  public void setGlobalRouteTarget(String globalRouteTarget) {
    this.globalRouteTarget = globalRouteTarget;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiVpnbindingsVpnBindings genericResourceApiVpnbindingsVpnBindings = (GenericResourceApiVpnbindingsVpnBindings) o;
    return Objects.equals(this.vpnName, genericResourceApiVpnbindingsVpnBindings.vpnName) &&
        Objects.equals(this.vpnBindingId, genericResourceApiVpnbindingsVpnBindings.vpnBindingId) &&
        Objects.equals(this.routeTargetRole, genericResourceApiVpnbindingsVpnBindings.routeTargetRole) &&
        Objects.equals(this.aicZone, genericResourceApiVpnbindingsVpnBindings.aicZone) &&
        Objects.equals(this.globalRouteTarget, genericResourceApiVpnbindingsVpnBindings.globalRouteTarget);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vpnName, vpnBindingId, routeTargetRole, aicZone, globalRouteTarget);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiVpnbindingsVpnBindings {\n");
    
    sb.append("    vpnName: ").append(toIndentedString(vpnName)).append("\n");
    sb.append("    vpnBindingId: ").append(toIndentedString(vpnBindingId)).append("\n");
    sb.append("    routeTargetRole: ").append(toIndentedString(routeTargetRole)).append("\n");
    sb.append("    aicZone: ").append(toIndentedString(aicZone)).append("\n");
    sb.append("    globalRouteTarget: ").append(toIndentedString(globalRouteTarget)).append("\n");
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

