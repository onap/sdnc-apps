package org.onap.sdnc.apps.ms.gra.data;

import java.io.Serializable;

public class PreloadDataKey implements Serializable {
    private String preloadId = "";
    private String preloadType = "";

    public PreloadDataKey() {
        this.preloadId = "";
        this.preloadType = "";
    }

    public PreloadDataKey(String preloadId, String preloadType) {
        this.preloadId = preloadId;
        this.preloadType = preloadType;
    }

    @Override
    public int hashCode() {
        return preloadId.hashCode() + preloadType.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PreloadDataKey &&
                preloadId.equals(((PreloadDataKey)obj).preloadId) &&
                preloadType.equals(((PreloadDataKey)obj).preloadType));
    }
}
