package org.onap.sdnc.apps.ms.gra.data;

import org.hibernate.validator.constraints.Length;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiAllottedresourcestatusAllottedResourceStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRequestStatusEnumeration;

import javax.persistence.*;

@Entity(name="OPERATIONAL_GRA_CONTRAIL_ROUTE_ALLOTTED_RESOURCES")
@Table(name="OPERATIONAL_GRA_CONTRAIL_ROUTE_ALLOTTED_RESOURCES")
public class OperationalContrailRouteAllottedResources {
    @Id
    @Length(max = 255)
    @Column(length = 255)
    String allottedResourceId;

    @Lob
    String arData;

    // allottedResource status fields
    String allottedResourceStatusResponseCode;

    String allottedResourceStatusResponseMessage;

    String allottedResourceStatusFinalIndicator;

    String allottedResourceStatusRequestStatus;

    String allottedResourceStatusAction;

    String allottedResourceStatusRpcName;

    String allottedResourceStatusRpcAction;

    String allottedResourceStatusResponseTimestamp;


    public OperationalContrailRouteAllottedResources() {
        this.allottedResourceId = "";
        this.arData = "";
    }

    public OperationalContrailRouteAllottedResources(String allottedResourceId, String arData) {
        this.allottedResourceId = allottedResourceId;
        this.arData = arData;
    }

    public OperationalContrailRouteAllottedResources(String allottedResourceId, String arData, GenericResourceApiAllottedresourcestatusAllottedResourceStatus allottedResourceStatus) {
        this.allottedResourceId = allottedResourceId;
        this.arData = arData;

        if (allottedResourceStatus != null) {
            this.allottedResourceStatusAction = allottedResourceStatus.getAction();
            this.allottedResourceStatusFinalIndicator = allottedResourceStatus.getFinalIndicator();
            this.allottedResourceStatusRequestStatus = allottedResourceStatus.getRequestStatus().toString();
            this.allottedResourceStatusResponseCode = allottedResourceStatus.getResponseCode();
            this.allottedResourceStatusResponseMessage = allottedResourceStatus.getResponseMessage();
            this.allottedResourceStatusResponseTimestamp = allottedResourceStatus.getResponseTimestamp();
        }
    }

    public String getAllottedResourceId() {
        return allottedResourceId;
    }

    public void setAllottedResourceId(String allottedResourceId) {
        this.allottedResourceId = allottedResourceId;
    }

    public String getArData() {
        return arData;
    }

    public void setArData(String arData) {
        this.arData = arData;
    }

    public String getAllottedResourceStatusResponseCode() {
        return allottedResourceStatusResponseCode;
    }

    public void setAllottedResourceStatusResponseCode(String allottedResourceStatusResponseCode) {
        this.allottedResourceStatusResponseCode = allottedResourceStatusResponseCode;
    }

    public String getAllottedResourceStatusResponseMessage() {
        return allottedResourceStatusResponseMessage;
    }

    public void setAllottedResourceStatusResponseMessage(String allottedResourcesStatusResponseMessage) {
        this.allottedResourceStatusResponseMessage = allottedResourcesStatusResponseMessage;
    }

    public String getAllottedResourceStatusFinalIndicator() {
        return allottedResourceStatusFinalIndicator;
    }

    public void setAllottedResourceStatusFinalIndicator(String allottedResourceStatusFinalIndicator) {
        this.allottedResourceStatusFinalIndicator = allottedResourceStatusFinalIndicator;
    }

    public String getAllottedResourceStatusRequestStatus() {
        return allottedResourceStatusRequestStatus;
    }

    public void setAllottedResourceStatusRequestStatus(String allottedResourceStatusRequestStatus) {
        this.allottedResourceStatusRequestStatus = allottedResourceStatusRequestStatus;
    }

    public String getAllottedResourceStatusAction() {
        return allottedResourceStatusAction;
    }

    public void setAllottedResourceStatusAction(String allottedResourceStatusAction) {
        this.allottedResourceStatusAction = allottedResourceStatusAction;
    }

    public String getAllottedResourceStatusRpcName() {
        return allottedResourceStatusRpcName;
    }

    public void setAllottedResourceStatusRpcName(String allottedResourceStatusRpcName) {
        this.allottedResourceStatusRpcName = allottedResourceStatusRpcName;
    }

    public String getAllottedResourceStatusRpcAction() {
        return allottedResourceStatusRpcAction;
    }

    public void setAllottedResourceStatusRpcAction(String allottedResourceStatusRpcAction) {
        this.allottedResourceStatusRpcAction = allottedResourceStatusRpcAction;
    }

    public String getAllottedResourceStatusResponseTimestamp() {
        return allottedResourceStatusResponseTimestamp;
    }

    public void setAllottedResourceStatusResponseTimestamp(String allottedResourceStatusResponseTimestamp) {
        this.allottedResourceStatusResponseTimestamp = allottedResourceStatusResponseTimestamp;
    }

    public GenericResourceApiAllottedresourcestatusAllottedResourceStatus getAllottedResourceStatus() {

        if ((allottedResourceStatusAction == null) &&
                (allottedResourceStatusFinalIndicator == null) &&
                (allottedResourceStatusRequestStatus == null) &&
                (allottedResourceStatusResponseCode == null) &&
                (allottedResourceStatusResponseMessage == null) &&
                (allottedResourceStatusResponseTimestamp == null)) {
            return null;
        }

        GenericResourceApiAllottedresourcestatusAllottedResourceStatus allottedResourceStatus = new GenericResourceApiAllottedresourcestatusAllottedResourceStatus();
        allottedResourceStatus.setAction(allottedResourceStatusAction);
        allottedResourceStatus.setFinalIndicator(allottedResourceStatusFinalIndicator);
        allottedResourceStatus.setRequestStatus(GenericResourceApiRequestStatusEnumeration.fromValue(allottedResourceStatusRequestStatus));
        allottedResourceStatus.setResponseCode(allottedResourceStatusResponseCode);
        allottedResourceStatus.setResponseMessage(allottedResourceStatusResponseMessage);
        allottedResourceStatus.setResponseTimestamp(allottedResourceStatusResponseTimestamp);

        return(allottedResourceStatus);
    }

    public void setAllottedResourceStatus(GenericResourceApiAllottedresourcestatusAllottedResourceStatus allottedResourceStatus) {
        if (allottedResourceStatus == null) {
            this.allottedResourceStatusAction = null;
            this.allottedResourceStatusFinalIndicator = null;
            this.allottedResourceStatusRequestStatus = null;
            this.allottedResourceStatusResponseCode = null;
            this.allottedResourceStatusResponseMessage = null;
            this.allottedResourceStatusResponseTimestamp = null;
        } else {
            this.allottedResourceStatusAction = allottedResourceStatus.getAction();
            this.allottedResourceStatusFinalIndicator = allottedResourceStatus.getFinalIndicator();
            this.allottedResourceStatusRequestStatus = allottedResourceStatus.getRequestStatus().toString();
            this.allottedResourceStatusResponseCode = allottedResourceStatus.getResponseCode();
            this.allottedResourceStatusResponseMessage = allottedResourceStatus.getResponseMessage();
            this.allottedResourceStatusResponseTimestamp = allottedResourceStatus.getResponseTimestamp();
        }
    }
}