package com.trc.android.share;


import com.umeng.socialize.PlatformConfig;


/**
 * @author Unknown  on 2016/1/11.
 */
public class UmengShareConfig {

    private static UmengShareConfig instance;
    private String wxAppId;
    private String wbAppId;
    private String qqAppId;
    private String ddAppId;
    private int defaultIconRes = R.drawable.share_logo;

    private UmengShareConfig() {
    }

    public static UmengShareConfig getInstance() {
        if (null == instance) {
            synchronized (UmengShareConfig.class) {
                if (null == instance) {
                    instance = new UmengShareConfig();
                }
            }
        }
        return instance;
    }

    public UmengShareConfig setWeixin(String appId, String appKey) {
        //微信 appid appsecret
        wxAppId = appId;
        PlatformConfig.setWeixin(appId, appKey);
        return this;
    }


    public UmengShareConfig setDefaultIconRes(int iconRes) {
        defaultIconRes = iconRes;
        return this;
    }

    public UmengShareConfig setWeibo(String appId, String appKey) {
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo(appId, appKey, "http://sns.whalecloud.com");
        wbAppId = appId;
        return this;
    }

    public UmengShareConfig setDingtalk(String appId) {
        //新浪微博 appkey appsecret
        PlatformConfig.setDing(appId);
        wbAppId = appId;
        return this;
    }

    public UmengShareConfig setQq(String appId, String appKey) {
        qqAppId = appId;
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone(appId, appKey);
        return this;
    }


    public String getWxAppId() {
        return wxAppId;
    }


    public String getWbAppId() {
        return wbAppId;
    }

    public String getQqAppId() {
        return qqAppId;
    }

    public String getDdAppId() {
        return ddAppId;
    }

    public int getDefaultIconRes() {
        return defaultIconRes;
    }
}
