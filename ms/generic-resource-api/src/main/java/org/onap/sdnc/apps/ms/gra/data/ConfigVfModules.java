package org.onap.sdnc.apps.ms.gra.data;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity(name="CONFIG_GRA_VF_MODULES")
@Table(name="CONFIG_GRA_VF_MODULES")
@IdClass(VfModulesKey.class)
public class ConfigVfModules {
    @Id
    @Length(max = 100)
    @Column(length = 100)
    String svcInstanceId;

    @Id
    @Length(max = 100)
    @Column(length = 100)
    String vnfId;

    @Id
    @Length(max = 100)
    @Column(length = 100)
    String vfModuleId;

    @Lob
    String vfModuleData;


    public ConfigVfModules() {
        this.svcInstanceId = "";
        this.vnfId = "";
        this.vfModuleId = "";
        this.vfModuleData = "";
    }

    public ConfigVfModules(String svcInstanceId, String vnfId, String vfModuleId, String vfModuleData) {
        this.svcInstanceId = svcInstanceId;
        this.vnfId = vnfId;
        this.vfModuleId = vfModuleId;
        this.vfModuleData = vfModuleData;
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

    public String getVfModuleId() {
        return vfModuleId;
    }

    public void setVfModuleId(String vfModuleId) {
        this.vfModuleId = vfModuleId;
    }

    public String getVfModuleData() {
        return vfModuleData;
    }

    public void setVfModuleData(String vfModuleData) {
        this.vfModuleData = vfModuleData;
    }

  
}