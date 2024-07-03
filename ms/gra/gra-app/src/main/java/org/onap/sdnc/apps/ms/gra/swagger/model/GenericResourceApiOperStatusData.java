package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;

import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModelProperty;

/**
 * GenericResourceApiOperStatusData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-23T12:02:01.518-04:00")
@JsonRootName("oper-status")
public class GenericResourceApiOperStatusData   {
  @JsonProperty("last-action")
  private GenericResourceApiLastActionEnumeration lastAction = null;

  @JsonProperty("modify-timestamp")
  private String modifyTimestamp = null;

  @JsonProperty("last-order-status")
  private GenericResourceApiLastOrderStatusEnumeration lastOrderStatus = null;

  @JsonProperty("create-timestamp")
  private String createTimestamp = null;

  @JsonProperty("order-status")
  private GenericResourceApiOrderStatusEnumeration orderStatus = null;

  @JsonProperty("last-rpc-action")
  private GenericResourceApiLastRpcActionEnumeration lastRpcAction = null;

  @JsonProperty("last-svc-request-id")
  private String lastSvcRequestId = null;

  public GenericResourceApiOperStatusData lastAction(GenericResourceApiLastActionEnumeration lastAction) {
    this.lastAction = lastAction;
    return this;
  }

  /**
   * should be list of possible request-actions
   * @return lastAction
  **/
  @ApiModelProperty(value = "should be list of possible request-actions")

  @Valid

  public GenericResourceApiLastActionEnumeration getLastAction() {
    return lastAction;
  }

  public void setLastAction(GenericResourceApiLastActionEnumeration lastAction) {
    this.lastAction = lastAction;
  }

  public GenericResourceApiOperStatusData modifyTimestamp(String modifyTimestamp) {
    this.modifyTimestamp = modifyTimestamp;
    return this;
  }

  /**
   * Get modifyTimestamp
   * @return modifyTimestamp
  **/
  @ApiModelProperty(value = "")


  public String getModifyTimestamp() {
    return modifyTimestamp;
  }

  public void setModifyTimestamp(String modifyTimestamp) {
    this.modifyTimestamp = modifyTimestamp;
  }

  public GenericResourceApiOperStatusData lastOrderStatus(GenericResourceApiLastOrderStatusEnumeration lastOrderStatus) {
    this.lastOrderStatus = lastOrderStatus;
    return this;
  }

  /**
   * Get lastOrderStatus
   * @return lastOrderStatus
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiLastOrderStatusEnumeration getLastOrderStatus() {
    return lastOrderStatus;
  }

  public void setLastOrderStatus(GenericResourceApiLastOrderStatusEnumeration lastOrderStatus) {
    this.lastOrderStatus = lastOrderStatus;
  }

  public GenericResourceApiOperStatusData createTimestamp(String createTimestamp) {
    this.createTimestamp = createTimestamp;
    return this;
  }

  /**
   * Not currently populated in service data.
   * @return createTimestamp
  **/
  @ApiModelProperty(value = "Not currently populated in service data.")


  public String getCreateTimestamp() {
    return createTimestamp;
  }

  public void setCreateTimestamp(String createTimestamp) {
    this.createTimestamp = createTimestamp;
  }

  public GenericResourceApiOperStatusData orderStatus(GenericResourceApiOrderStatusEnumeration orderStatus) {
    this.orderStatus = orderStatus;
    return this;
  }

  /**
   * TBD - do we need Request failed statuses? RequestFailed | InProgressTimeout
   * @return orderStatus
  **/
  @ApiModelProperty(value = "TBD - do we need Request failed statuses? RequestFailed | InProgressTimeout")

  @Valid

  public GenericResourceApiOrderStatusEnumeration getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(GenericResourceApiOrderStatusEnumeration orderStatus) {
    this.orderStatus = orderStatus;
  }

  public GenericResourceApiOperStatusData lastRpcAction(GenericResourceApiLastRpcActionEnumeration lastRpcAction) {
    this.lastRpcAction = lastRpcAction;
    return this;
  }

  /**
   * Get lastRpcAction
   * @return lastRpcAction
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiLastRpcActionEnumeration getLastRpcAction() {
    return lastRpcAction;
  }

  public void setLastRpcAction(GenericResourceApiLastRpcActionEnumeration lastRpcAction) {
    this.lastRpcAction = lastRpcAction;
  }

  public GenericResourceApiOperStatusData lastSvcRequestId(String lastSvcRequestId) {
    this.lastSvcRequestId = lastSvcRequestId;
    return this;
  }

  /**
   * Not currently populated in service data.
   * @return lastSvcRequestId
  **/
  @ApiModelProperty(value = "Not currently populated in service data.")


  public String getLastSvcRequestId() {
    return lastSvcRequestId;
  }

  public void setLastSvcRequestId(String lastSvcRequestId) {
    this.lastSvcRequestId = lastSvcRequestId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiOperStatusData genericResourceApiOperStatusData = (GenericResourceApiOperStatusData) o;
    return Objects.equals(this.lastAction, genericResourceApiOperStatusData.lastAction) &&
        Objects.equals(this.modifyTimestamp, genericResourceApiOperStatusData.modifyTimestamp) &&
        Objects.equals(this.lastOrderStatus, genericResourceApiOperStatusData.lastOrderStatus) &&
        Objects.equals(this.createTimestamp, genericResourceApiOperStatusData.createTimestamp) &&
        Objects.equals(this.orderStatus, genericResourceApiOperStatusData.orderStatus) &&
        Objects.equals(this.lastRpcAction, genericResourceApiOperStatusData.lastRpcAction) &&
        Objects.equals(this.lastSvcRequestId, genericResourceApiOperStatusData.lastSvcRequestId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lastAction, modifyTimestamp, lastOrderStatus, createTimestamp, orderStatus, lastRpcAction, lastSvcRequestId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiOperStatusData {\n");
    
    sb.append("    lastAction: ").append(toIndentedString(lastAction)).append("\n");
    sb.append("    modifyTimestamp: ").append(toIndentedString(modifyTimestamp)).append("\n");
    sb.append("    lastOrderStatus: ").append(toIndentedString(lastOrderStatus)).append("\n");
    sb.append("    createTimestamp: ").append(toIndentedString(createTimestamp)).append("\n");
    sb.append("    orderStatus: ").append(toIndentedString(orderStatus)).append("\n");
    sb.append("    lastRpcAction: ").append(toIndentedString(lastRpcAction)).append("\n");
    sb.append("    lastSvcRequestId: ").append(toIndentedString(lastSvcRequestId)).append("\n");
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

