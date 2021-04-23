package org.onap.sdnc.apps.ms.gra.data;

import java.io.Serializable;

public class NetworksKey implements Serializable {
    private String svcInstanceId = "";
    private String networkId = "";

    public NetworksKey() {
        this.svcInstanceId = "";
        this.networkId = "";
    }

    public NetworksKey(String svcInstanceId, String networkId) {
        this.svcInstanceId = svcInstanceId;
        this.networkId = networkId;
    }

    @Override
    public int hashCode() {
        return svcInstanceId.hashCode() + networkId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof NetworksKey &&
                svcInstanceId.equals(((NetworksKey)obj).svcInstanceId) &&
                networkId.equals(((NetworksKey)obj).networkId));
    }
}
