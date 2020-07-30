package org.onap.sdnc.apps.ms.gra.data;

import org.hibernate.validator.constraints.Length;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRequestStatusEnumeration;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicestatusServiceStatus;

import javax.persistence.*;

@Entity(name="OPERATIONAL_GRA_SERVICES")
@Table(name="OPERATIONAL_GRA_SERVICES")
public class OperationalServices {
    @Id
    String svcInstanceId;

    @Lob
    @Column(columnDefinition = "clob")
    String svcData;

    // Service status fields
    String serviceStatusResponseCode;

    String serviceStatusResponseMessage;

    String serviceStatusFinalIndicator;

    String serviceStatusRequestStatus;

    String serviceStatusAction;

    String serviceStatusRpcName;

    String serviceStatusRpcAction;

    String serviceStatusResponseTimestamp;

    public String getSvcInstanceId() {
        return svcInstanceId;
    }

    public OperationalServices() {
        this.svcInstanceId = "";
        this.svcData = "";
    }

    public OperationalServices(String svcInstanceId, String svcData, GenericResourceApiServicestatusServiceStatus serviceStatus) {
        this.svcInstanceId = svcInstanceId;
        this.svcData = svcData;
        if (serviceStatus != null) {
            this.serviceStatusAction = serviceStatus.getAction();
            this.serviceStatusFinalIndicator = serviceStatus.getFinalIndicator();
            this.serviceStatusRequestStatus = serviceStatus.getRequestStatus().toString();
            this.serviceStatusResponseCode = serviceStatus.getResponseCode();
            this.serviceStatusResponseMessage = serviceStatus.getResponseMessage();
            this.serviceStatusResponseTimestamp = serviceStatus.getResponseTimestamp();
        }
    }

    public void setSvcInstanceId(String svcInstanceId) {
        this.svcInstanceId = svcInstanceId;
    }

    public String getSvcData() {
        return svcData;
    }

    public void setSvcData(String svcData) {
        this.svcData = svcData;
    }

    public String getServiceStatusResponseCode() {
        return serviceStatusResponseCode;
    }

    public void setServiceStatusResponseCode(String serviceStatusResponseCode) {
        this.serviceStatusResponseCode = serviceStatusResponseCode;
    }

    public String getServiceStatusResponseMessage() {
        return serviceStatusResponseMessage;
    }

    public void setServiceStatusResponseMessage(String serviceStatusResponseMessage) {
        this.serviceStatusResponseMessage = serviceStatusResponseMessage;
    }

    public String getServiceStatusFinalIndicator() {
        return serviceStatusFinalIndicator;
    }

    public void setServiceStatusFinalIndicator(String serviceStatusFinalIndicator) {
        this.serviceStatusFinalIndicator = serviceStatusFinalIndicator;
    }

    public String getServiceStatusRequestStatus() {
        return serviceStatusRequestStatus;
    }

    public void setServiceStatusRequestStatus(String serviceStatusRequestStatus) {
        this.serviceStatusRequestStatus = serviceStatusRequestStatus;
    }

    public String getServiceStatusAction() {
        return serviceStatusAction;
    }

    public void setServiceStatusAction(String serviceStatusAction) {
        this.serviceStatusAction = serviceStatusAction;
    }

    public String getServiceStatusRpcName() {
        return serviceStatusRpcName;
    }

    public void setServiceStatusRpcName(String serviceStatusRpcName) {
        this.serviceStatusRpcName = serviceStatusRpcName;
    }

    public String getServiceStatusRpcAction() {
        return serviceStatusRpcAction;
    }

    public void setServiceStatusRpcAction(String serviceStatusRpcAction) {
        this.serviceStatusRpcAction = serviceStatusRpcAction;
    }

    public String getServiceStatusResponseTimestamp() {
        return serviceStatusResponseTimestamp;
    }

    public void setServiceStatusResponseTimestamp(String serviceStatusResponseTimestamp) {
        this.serviceStatusResponseTimestamp = serviceStatusResponseTimestamp;
    }


    public GenericResourceApiServicestatusServiceStatus getServiceStatus() {
        GenericResourceApiServicestatusServiceStatus serviceStatus = new GenericResourceApiServicestatusServiceStatus();
        serviceStatus.setAction(serviceStatusAction);
        serviceStatus.setFinalIndicator(serviceStatusFinalIndicator);
        serviceStatus.setRequestStatus(GenericResourceApiRequestStatusEnumeration.fromValue(serviceStatusRequestStatus));
        serviceStatus.setResponseCode(serviceStatusResponseCode);
        serviceStatus.setResponseMessage(serviceStatusResponseMessage);
        serviceStatus.setResponseTimestamp(serviceStatusResponseTimestamp);

        return(serviceStatus);
    }

    public void setServiceStatus(GenericResourceApiServicestatusServiceStatus serviceStatus) {
        this.serviceStatusAction = serviceStatus.getAction();
        this.serviceStatusFinalIndicator = serviceStatus.getFinalIndicator();
        if (serviceStatus.getRequestStatus() != null) {
            this.serviceStatusRequestStatus = serviceStatus.getRequestStatus().toString();
        }

        this.serviceStatusResponseCode = serviceStatus.getResponseCode();
        this.serviceStatusResponseMessage = serviceStatus.getResponseMessage();
        this.serviceStatusResponseTimestamp = serviceStatus.getResponseTimestamp();
    }
}
