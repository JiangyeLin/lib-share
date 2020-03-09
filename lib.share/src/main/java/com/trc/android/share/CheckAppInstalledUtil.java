package com.trc.android.share;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * @author HuangMing on 2016/9/10.
 */
class CheckAppInstalledUtil {

    /**
     * 检测相关应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        boolean appInstalled = false;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(packageName, 0);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
        }
        return appInstalled;
    }

    /**
     * 判断微信是否安装
     *
     * @param context
     * @return
     */
    public static boolean isWeChatInstalled(Context context) {
        return isAppInstalled(context, "com.tencent.mm");
    }

    /**
     * 判断QQ是否安装
     *
     * @param context
     * @return
     */
    public static boolean isQQInstalled(Context context) {
        return isAppInstalled(context, "com.tencent.mobileqq");
    }

    /**
     * 判断微博是否安装
     *
     * @param context
     * @return
     */
    public static boolean isWeiBo(Context context) {
        return isAppInstalled(context, "com.sina.weibo");
    }

    public static boolean isDingdingInstall(Context context){
        return isAppInstalled(context, "com.alibaba.android.rimet");
    }
}
