package com.trc.android.share;

import androidx.annotation.StringDef;

public class ShareConstants {
    public static final String SHARE_BROADCAST_ACTION_PREFIX = "SHARE_BROADCAST_ACTION_PREFIX";
    public static final String SHARE_RESPONSE_OBJ_KEY = "SHARE_ITEM";

    public static final String PLATFORM_WX_FRIEND = "wxFriend";
    public static final String PLATFORM_WX_TIMELINE = "wxTimeline";
    public static final String PLATFORM_WEIBO = "weibo";
    public static final String PLATFORM_QQ = "qq";
    public static final String PLATFORM_QZONE = "qzone";
    public static final String PLATFORM_DINGDING = "dingding";
    public static final String PLATFORM_CLIPBOARD = "clipboard";
    public static final String PLATFORM_COVER = "photoAlbum"; //生成封面
    public static final String PLATFORM_BROWSER = "browser";    //分享至浏览器
    public static final String PLATFORM_MINA = "mina";   //分享到小程序

    public static final String[] PLAT_FORMS = {PLATFORM_WX_FRIEND, PLATFORM_WX_TIMELINE, PLATFORM_WEIBO, PLATFORM_QQ, PLATFORM_QZONE,
            PLATFORM_DINGDING, PLATFORM_CLIPBOARD, PLATFORM_COVER, PLATFORM_BROWSER,PLATFORM_MINA};

    @StringDef({PLATFORM_WX_FRIEND, PLATFORM_WX_TIMELINE, PLATFORM_WEIBO, PLATFORM_QQ, PLATFORM_QZONE, PLATFORM_DINGDING, PLATFORM_CLIPBOARD, PLATFORM_COVER, PLATFORM_BROWSER,PLATFORM_MINA})
    public @interface PlatformDef {

    }

    public static final String TYPE_DEFAULT = "default";
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_PIC = "pic";//纯图片分享

    /**
     * 分享支持的type
     */
    @StringDef({TYPE_TEXT, TYPE_DEFAULT, TYPE_PIC})
    public @interface TypeDef {

    }

    public enum EventType {
        CLICK_EVENT, SHARE_SUCCESS_EVENT, SHARE_FAIL_EVENT, CANCEL_EVENT, UNKNOWN_EVENT, SHARE_START_EVENT
    }
}
