package org.onap.sdnc.apps.ms.gra.data;

import java.io.Serializable;

public class VfModulesKey implements Serializable {
    private String svcInstanceId = "";
    private String vnfId = "";
    private String vfModuleId = "";

    public VfModulesKey() {
        this.svcInstanceId = "";
        this.vnfId = "";
        this.vfModuleId = "";
    }

    public VfModulesKey(String svcInstanceId, String vnfId, String vfModuleId) {
        this.svcInstanceId = svcInstanceId;
        this.vnfId = vnfId;
        this.vfModuleId = vfModuleId;
    }

    @Override
    public int hashCode() {
        return svcInstanceId.hashCode() + vnfId.hashCode()+vfModuleId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof VfModulesKey &&
                svcInstanceId.equals(((VfModulesKey)obj).svcInstanceId) &&
                vnfId.equals(((VfModulesKey)obj).vnfId) &&
                vfModuleId.equals(((VfModulesKey)obj).vfModuleId));
    }
}
