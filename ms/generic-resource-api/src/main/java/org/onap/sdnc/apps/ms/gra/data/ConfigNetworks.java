package org.onap.sdnc.apps.ms.gra.data;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity(name="CONFIG_GRA_NETWORKS")
@Table(name="CONFIG_GRA_NETWORKS")
@IdClass(NetworksKey.class)
public class ConfigNetworks {
    @Id
    @Length(max = 100)
    @Column(length = 100)
    String svcInstanceId;

    @Id
    @Length(max = 100)
    @Column(length = 100)
    String networkId;

    @Lob
    String networkData;


    public ConfigNetworks() {
        this.svcInstanceId = "";
        this.networkId = "";
        this.networkData = "";
    }

    public ConfigNetworks(String svcInstanceId, String networkId) {
        this.svcInstanceId = svcInstanceId;
        this.networkId = networkId;
        this.networkData = "";
    }

    public ConfigNetworks(String svcInstanceId, String networkId, String networkData) {
        this.svcInstanceId = svcInstanceId;
        this.networkId = networkId;
        this.networkData = networkData;
    }



    public String getSvcInstanceId() {
        return svcInstanceId;
    }

    public void setSvcInstanceId(String svcInstanceId) {
        this.svcInstanceId = svcInstanceId;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getNetworkData() {
        return networkData;
    }

    public void setNetworkData(String networkData) {
        this.networkData = networkData;
    }


}