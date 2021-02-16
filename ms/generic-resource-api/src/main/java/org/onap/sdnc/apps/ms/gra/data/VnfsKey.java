package org.onap.sdnc.apps.ms.gra.data;

import java.io.Serializable;

public class VnfsKey implements Serializable {
    private String svcInstanceId = "";
    private String vnfId = "";

    public VnfsKey() {
        this.svcInstanceId = "";
        this.vnfId = "";
    }

    public VnfsKey(String svcInstanceId, String vnfId) {
        this.svcInstanceId = svcInstanceId;
        this.vnfId = vnfId;
    }

    @Override
    public int hashCode() {
        return svcInstanceId.hashCode() + vnfId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof VnfsKey &&
                svcInstanceId.equals(((VnfsKey)obj).svcInstanceId) &&
                vnfId.equals(((VnfsKey)obj).vnfId));
    }
}
