package org.onap.sdnc.apps.ms.gra.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRequestStatusEnumeration;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRpcActionEnumeration;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GenericResourceApiServicestatusServiceStatus
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-07-13T13:26:44.236-04:00")
@JsonRootName("service-status")
public class GenericResourceApiServicestatusServiceStatus   {
  @JsonProperty("response-code")
  private String responseCode = null;

  @JsonProperty("response-message")
  private String responseMessage = null;

  @JsonProperty("action")
  private String action = null;

  @JsonProperty("response-timestamp")
  private String responseTimestamp = null;

  @JsonProperty("request-status")
  private GenericResourceApiRequestStatusEnumeration requestStatus = null;

  @JsonProperty("final-indicator")
  private String finalIndicator = null;

  @JsonProperty("rpc-name")
  private String rpcName = null;

  @JsonProperty("rpc-action")
  private GenericResourceApiRpcActionEnumeration rpcAction = null;

  public GenericResourceApiServicestatusServiceStatus responseCode(String responseCode) {
    this.responseCode = responseCode;
    return this;
  }

  /**
   * Get responseCode
   * @return responseCode
  **/
  @ApiModelProperty(value = "")


  public String getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(String responseCode) {
    this.responseCode = responseCode;
  }

  public GenericResourceApiServicestatusServiceStatus responseMessage(String responseMessage) {
    this.responseMessage = responseMessage;
    return this;
  }

  /**
   * Get responseMessage
   * @return responseMessage
  **/
  @ApiModelProperty(value = "")


  public String getResponseMessage() {
    return responseMessage;
  }

  public void setResponseMessage(String responseMessage) {
    this.responseMessage = responseMessage;
  }

  public GenericResourceApiServicestatusServiceStatus action(String action) {
    this.action = action;
    return this;
  }

  /**
   * value would one of possible request-actions; match the list in service-data oper-status
   * @return action
  **/
  @ApiModelProperty(value = "value would one of possible request-actions; match the list in service-data oper-status")


  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public GenericResourceApiServicestatusServiceStatus responseTimestamp(String responseTimestamp) {
    this.responseTimestamp = responseTimestamp;
    return this;
  }

  /**
   * Get responseTimestamp
   * @return responseTimestamp
  **/
  @ApiModelProperty(value = "")


  public String getResponseTimestamp() {
    return responseTimestamp;
  }

  public void setResponseTimestamp(String responseTimestamp) {
    this.responseTimestamp = responseTimestamp;
  }

  public GenericResourceApiServicestatusServiceStatus requestStatus(GenericResourceApiRequestStatusEnumeration requestStatus) {
    this.requestStatus = requestStatus;
    return this;
  }

  /**
   * Get requestStatus
   * @return requestStatus
  **/
  @ApiModelProperty(value = "")

  @Valid

  public GenericResourceApiRequestStatusEnumeration getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(GenericResourceApiRequestStatusEnumeration requestStatus) {
    this.requestStatus = requestStatus;
  }

  public GenericResourceApiServicestatusServiceStatus finalIndicator(String finalIndicator) {
    this.finalIndicator = finalIndicator;
    return this;
  }

  /**
   * Get finalIndicator
   * @return finalIndicator
  **/
  @ApiModelProperty(value = "")


  public String getFinalIndicator() {
    return finalIndicator;
  }

  public void setFinalIndicator(String finalIndicator) {
    this.finalIndicator = finalIndicator;
  }

  public GenericResourceApiServicestatusServiceStatus rpcName(String rpcName) {
    this.rpcName = rpcName;
    return this;
  }

  /**
   * Get rpcName
   * @return rpcName
  **/
  @ApiModelProperty(value = "")


  public String getRpcName() {
    return rpcName;
  }

  public void setRpcName(String rpcName) {
    this.rpcName = rpcName;
  }

  public GenericResourceApiServicestatusServiceStatus rpcAction(GenericResourceApiRpcActionEnumeration rpcAction) {
    this.rpcAction = rpcAction;
    return this;
  }

  /**
   * this is the svc-action from the incoming request
   * @return rpcAction
  **/
  @ApiModelProperty(value = "this is the svc-action from the incoming request")

  @Valid

  public GenericResourceApiRpcActionEnumeration getRpcAction() {
    return rpcAction;
  }

  public void setRpcAction(GenericResourceApiRpcActionEnumeration rpcAction) {
    this.rpcAction = rpcAction;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericResourceApiServicestatusServiceStatus genericResourceApiServicestatusServiceStatus = (GenericResourceApiServicestatusServiceStatus) o;
    return Objects.equals(this.responseCode, genericResourceApiServicestatusServiceStatus.responseCode) &&
        Objects.equals(this.responseMessage, genericResourceApiServicestatusServiceStatus.responseMessage) &&
        Objects.equals(this.action, genericResourceApiServicestatusServiceStatus.action) &&
        Objects.equals(this.responseTimestamp, genericResourceApiServicestatusServiceStatus.responseTimestamp) &&
        Objects.equals(this.requestStatus, genericResourceApiServicestatusServiceStatus.requestStatus) &&
        Objects.equals(this.finalIndicator, genericResourceApiServicestatusServiceStatus.finalIndicator) &&
        Objects.equals(this.rpcName, genericResourceApiServicestatusServiceStatus.rpcName) &&
        Objects.equals(this.rpcAction, genericResourceApiServicestatusServiceStatus.rpcAction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseCode, responseMessage, action, responseTimestamp, requestStatus, finalIndicator, rpcName, rpcAction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenericResourceApiServicestatusServiceStatus {\n");
    
    sb.append("    responseCode: ").append(toIndentedString(responseCode)).append("\n");
    sb.append("    responseMessage: ").append(toIndentedString(responseMessage)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    responseTimestamp: ").append(toIndentedString(responseTimestamp)).append("\n");
    sb.append("    requestStatus: ").append(toIndentedString(requestStatus)).append("\n");
    sb.append("    finalIndicator: ").append(toIndentedString(finalIndicator)).append("\n");
    sb.append("    rpcName: ").append(toIndentedString(rpcName)).append("\n");
    sb.append("    rpcAction: ").append(toIndentedString(rpcAction)).append("\n");
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

