package com.trc.android.share;

import java.io.Serializable;

public class ShareEvent implements Serializable {
    public ShareConstants.EventType eventType;
    public String platform;

    @Override
    public String toString() {
        return "Platform:" + platform + "  EventType:" + eventType;
    }
}
