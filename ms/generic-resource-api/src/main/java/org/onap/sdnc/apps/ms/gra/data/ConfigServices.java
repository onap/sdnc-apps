package org.onap.sdnc.apps.ms.gra.data;

import org.hibernate.validator.constraints.Length;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRequestStatusEnumeration;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServiceStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicestatusServiceStatus;

import javax.persistence.*;

@Entity(name="CONFIG_GRA_SERVICES")
@Table(name="CONFIG_GRA_SERVICES")
public class ConfigServices {
    @Id
    @Length(max = 100)
    @Column(length = 100)
    String svcInstanceId;

    @Lob
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


    public ConfigServices() {
        this.svcInstanceId = "";
        this.svcData = "";
    }

    public ConfigServices(String svcInstanceId, String svcData) {
        this.svcInstanceId = svcInstanceId;
        this.svcData = svcData;
    }

    public ConfigServices(String svcInstanceId, String svcData, GenericResourceApiServicestatusServiceStatus serviceStatus) {
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

    public String getSvcInstanceId() {
        return svcInstanceId;
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

    public void setServiceStatusResponseMessage(String servicesStatusResponseMessage) {
        this.serviceStatusResponseMessage = servicesStatusResponseMessage;
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

        if ((serviceStatusAction == null) &&
                (serviceStatusFinalIndicator == null) &&
                (serviceStatusRequestStatus == null) &&
                (serviceStatusResponseCode == null) &&
                (serviceStatusResponseMessage == null) &&
                (serviceStatusResponseTimestamp == null)) {
            return null;
        }

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
        if (serviceStatus == null) {
            this.serviceStatusAction = null;
            this.serviceStatusFinalIndicator = null;
            this.serviceStatusRequestStatus = null;
            this.serviceStatusResponseCode = null;
            this.serviceStatusResponseMessage = null;
            this.serviceStatusResponseTimestamp = null;
        } else {
            this.serviceStatusAction = serviceStatus.getAction();
            this.serviceStatusFinalIndicator = serviceStatus.getFinalIndicator();
            this.serviceStatusRequestStatus = serviceStatus.getRequestStatus().toString();
            this.serviceStatusResponseCode = serviceStatus.getResponseCode();
            this.serviceStatusResponseMessage = serviceStatus.getResponseMessage();
            this.serviceStatusResponseTimestamp = serviceStatus.getResponseTimestamp();
        }
    }
}