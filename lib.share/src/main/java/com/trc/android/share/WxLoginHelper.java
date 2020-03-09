package com.trc.android.share;

import android.app.Application;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @author MaYongHui on 2016/6/28.
 */
public class WxLoginHelper {
    private static WxLoginHelper wxLoginHelper;
    private WeChatListener weChatListener;
    private IWXAPI iwxapi;

    public static WxLoginHelper getWxLoginHelper() {
        if (null == wxLoginHelper) {
            synchronized (WxLoginHelper.class) {
                if (null == wxLoginHelper) {
                    wxLoginHelper = new WxLoginHelper();
                }
            }
        }
        return wxLoginHelper;
    }

    public WeChatListener getWeChatListener() {
        return weChatListener;
    }

    public void regToWx(Application application, String appID) {
        iwxapi = WXAPIFactory.createWXAPI(application, null);
        iwxapi.registerApp(appID);
    }

    public void doAuth(WeChatListener weChatListener) {
        if (iwxapi == null) {
            throw new IllegalArgumentException("请先调用regToWx()方法初始化");
        }
        this.weChatListener = weChatListener;

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "alv_wx_login";
        iwxapi.sendReq(req);
    }

    public interface WeChatListener {
        void success(BaseResp resp);

        void other(BaseResp resp);

        void cancel();
    }
}
