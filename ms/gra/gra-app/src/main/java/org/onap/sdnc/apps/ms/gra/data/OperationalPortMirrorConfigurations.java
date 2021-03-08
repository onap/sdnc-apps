package org.onap.sdnc.apps.ms.gra.data;

import org.hibernate.validator.constraints.Length;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiConfigurationstatusConfigurationStatus;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiRequestStatusEnumeration;

import javax.persistence.*;

@Entity(name="OPERATIONAL_GRA_PORT_MIRROR_CONFIGURATIONS")
@Table(name="OPERATIONAL_GRA_PORT_MIRROR_CONFIGURATIONS")
public class OperationalPortMirrorConfigurations {
    @Id
    @Length(max = 255)
    @Column(length = 255)
    String configurationId;

    @Lob
    String pmcData;

    // portMirrorConfiguration status fields
    String portMirrorConfigurationStatusResponseCode;

    String portMirrorConfigurationStatusResponseMessage;

    String portMirrorConfigurationStatusFinalIndicator;

    String portMirrorConfigurationStatusRequestStatus;

    String portMirrorConfigurationStatusAction;

    String portMirrorConfigurationStatusRpcName;

    String portMirrorConfigurationStatusRpcAction;

    String portMirrorConfigurationStatusResponseTimestamp;


    public OperationalPortMirrorConfigurations() {
        this.configurationId = "";
        this.pmcData = "";
    }

    public OperationalPortMirrorConfigurations(String configurationId, String pmcData) {
        this.configurationId = configurationId;
        this.pmcData = pmcData;
    }

    public OperationalPortMirrorConfigurations(String configurationId, String pmcData, GenericResourceApiConfigurationstatusConfigurationStatus portMirrorConfigurationStatus) {
        this.configurationId = configurationId;
        this.pmcData = pmcData;

        if (portMirrorConfigurationStatus != null) {
            this.portMirrorConfigurationStatusAction = portMirrorConfigurationStatus.getAction();
            this.portMirrorConfigurationStatusFinalIndicator = portMirrorConfigurationStatus.getFinalIndicator();
            this.portMirrorConfigurationStatusRequestStatus = portMirrorConfigurationStatus.getRequestStatus().toString();
            this.portMirrorConfigurationStatusResponseCode = portMirrorConfigurationStatus.getResponseCode();
            this.portMirrorConfigurationStatusResponseMessage = portMirrorConfigurationStatus.getResponseMessage();
            this.portMirrorConfigurationStatusResponseTimestamp = portMirrorConfigurationStatus.getResponseTimestamp();
        }
    }

    public String getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(String configurationId) {
        this.configurationId = configurationId;
    }

    public String getPmcData() {
        return pmcData;
    }

    public void setPmcData(String pmcData) {
        this.pmcData = pmcData;
    }

    public String getPortMirrorConfigurationStatusResponseCode() {
        return portMirrorConfigurationStatusResponseCode;
    }

    public void setPortMirrorConfigurationStatusResponseCode(String portMirrorConfigurationStatusResponseCode) {
        this.portMirrorConfigurationStatusResponseCode = portMirrorConfigurationStatusResponseCode;
    }

    public String getPortMirrorConfigurationStatusResponseMessage() {
        return portMirrorConfigurationStatusResponseMessage;
    }

    public void setPortMirrorConfigurationStatusResponseMessage(String portMirrorConfigurationsStatusResponseMessage) {
        this.portMirrorConfigurationStatusResponseMessage = portMirrorConfigurationsStatusResponseMessage;
    }

    public String getPortMirrorConfigurationStatusFinalIndicator() {
        return portMirrorConfigurationStatusFinalIndicator;
    }

    public void setPortMirrorConfigurationStatusFinalIndicator(String portMirrorConfigurationStatusFinalIndicator) {
        this.portMirrorConfigurationStatusFinalIndicator = portMirrorConfigurationStatusFinalIndicator;
    }

    public String getPortMirrorConfigurationStatusRequestStatus() {
        return portMirrorConfigurationStatusRequestStatus;
    }

    public void setPortMirrorConfigurationStatusRequestStatus(String portMirrorConfigurationStatusRequestStatus) {
        this.portMirrorConfigurationStatusRequestStatus = portMirrorConfigurationStatusRequestStatus;
    }

    public String getPortMirrorConfigurationStatusAction() {
        return portMirrorConfigurationStatusAction;
    }

    public void setPortMirrorConfigurationStatusAction(String portMirrorConfigurationStatusAction) {
        this.portMirrorConfigurationStatusAction = portMirrorConfigurationStatusAction;
    }

    public String getPortMirrorConfigurationStatusRpcName() {
        return portMirrorConfigurationStatusRpcName;
    }

    public void setPortMirrorConfigurationStatusRpcName(String portMirrorConfigurationStatusRpcName) {
        this.portMirrorConfigurationStatusRpcName = portMirrorConfigurationStatusRpcName;
    }

    public String getPortMirrorConfigurationStatusRpcAction() {
        return portMirrorConfigurationStatusRpcAction;
    }

    public void setPortMirrorConfigurationStatusRpcAction(String portMirrorConfigurationStatusRpcAction) {
        this.portMirrorConfigurationStatusRpcAction = portMirrorConfigurationStatusRpcAction;
    }

    public String getPortMirrorConfigurationStatusResponseTimestamp() {
        return portMirrorConfigurationStatusResponseTimestamp;
    }

    public void setPortMirrorConfigurationStatusResponseTimestamp(String portMirrorConfigurationStatusResponseTimestamp) {
        this.portMirrorConfigurationStatusResponseTimestamp = portMirrorConfigurationStatusResponseTimestamp;
    }

    public GenericResourceApiConfigurationstatusConfigurationStatus getPortMirrorConfigurationStatus() {

        if ((portMirrorConfigurationStatusAction == null) &&
                (portMirrorConfigurationStatusFinalIndicator == null) &&
                (portMirrorConfigurationStatusRequestStatus == null) &&
                (portMirrorConfigurationStatusResponseCode == null) &&
                (portMirrorConfigurationStatusResponseMessage == null) &&
                (portMirrorConfigurationStatusResponseTimestamp == null)) {
            return null;
        }

        GenericResourceApiConfigurationstatusConfigurationStatus portMirrorConfigurationStatus = new GenericResourceApiConfigurationstatusConfigurationStatus();
        portMirrorConfigurationStatus.setAction(portMirrorConfigurationStatusAction);
        portMirrorConfigurationStatus.setFinalIndicator(portMirrorConfigurationStatusFinalIndicator);
        portMirrorConfigurationStatus.setRequestStatus(GenericResourceApiRequestStatusEnumeration.fromValue(portMirrorConfigurationStatusRequestStatus));
        portMirrorConfigurationStatus.setResponseCode(portMirrorConfigurationStatusResponseCode);
        portMirrorConfigurationStatus.setResponseMessage(portMirrorConfigurationStatusResponseMessage);
        portMirrorConfigurationStatus.setResponseTimestamp(portMirrorConfigurationStatusResponseTimestamp);

        return(portMirrorConfigurationStatus);
    }

    public void setPortMirrorConfigurationStatus(GenericResourceApiConfigurationstatusConfigurationStatus portMirrorConfigurationStatus) {
        if (portMirrorConfigurationStatus == null) {
            this.portMirrorConfigurationStatusAction = null;
            this.portMirrorConfigurationStatusFinalIndicator = null;
            this.portMirrorConfigurationStatusRequestStatus = null;
            this.portMirrorConfigurationStatusResponseCode = null;
            this.portMirrorConfigurationStatusResponseMessage = null;
            this.portMirrorConfigurationStatusResponseTimestamp = null;
        } else {
            this.portMirrorConfigurationStatusAction = portMirrorConfigurationStatus.getAction();
            this.portMirrorConfigurationStatusFinalIndicator = portMirrorConfigurationStatus.getFinalIndicator();
            this.portMirrorConfigurationStatusRequestStatus = portMirrorConfigurationStatus.getRequestStatus().toString();
            this.portMirrorConfigurationStatusResponseCode = portMirrorConfigurationStatus.getResponseCode();
            this.portMirrorConfigurationStatusResponseMessage = portMirrorConfigurationStatus.getResponseMessage();
            this.portMirrorConfigurationStatusResponseTimestamp = portMirrorConfigurationStatus.getResponseTimestamp();
        }
    }
}
