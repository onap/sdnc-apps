package org.onap.sdnc.apps.ms.gra.data;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;

@Entity(name="CONFIG_GRA_VNFS")
@Table(name="CONFIG_GRA_VNFS")
@IdClass(VnfsKey.class)
public class ConfigVnfs {
    @Id
    @Length(max = 100)
    @Column(length = 100)
    String svcInstanceId;

    @Id
    @Length(max = 100)
    @Column(length = 100)
    String vnfId;

    @Lob
    String vnfData;


    public ConfigVnfs() {
        this.svcInstanceId = "";
        this.vnfId = "";
        this.vnfData = "";
    }

    public ConfigVnfs(String svcInstanceId, String vnfId) {
        this.svcInstanceId = svcInstanceId;
        this.vnfId = vnfId;
        this.vnfData = "";
    }

    public ConfigVnfs(String svcInstanceId, String vnfId, String vnfData) {
        this.svcInstanceId = svcInstanceId;
        this.vnfId = vnfId;
        this.vnfData = vnfData;
    }

    public String getSvcInstanceId() {
        return svcInstanceId;
    }

    public void setSvcInstanceId(String svcInstanceId) {
        this.svcInstanceId = svcInstanceId;
    }

    public String getVnfId() {
        return vnfId;
    }

    public void setVnfId(String vnfId) {
        this.vnfId = vnfId;
    }

    public String getVnfData() {
        return vnfData;
    }

    public void setVnfData(String vnfData) {
        this.vnfData = vnfData;
    }
}
