package com.trc.android.share;

import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * JiangyeLin on 2018/5/29
 */
public abstract class WxBaseCallBackActivity extends WXCallbackActivity {

    @Override
    public void onReq(BaseReq req) {
        super.onReq(req);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            //由于目前微信分享是通过友盟实现的，所以微信分享交还给友盟来处理回调
            super.onResp(baseResp);
        } else if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            //处理微信授权登录
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //授权成功
                    //String code = ((SendAuth.Resp) baseResp).code;
                    WxLoginHelper.getWxLoginHelper().getWeChatListener().success(baseResp);
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED: {
                    //用户拒绝授权
                    WxLoginHelper.getWxLoginHelper().getWeChatListener().other(baseResp);
                    break;
                }
                case BaseResp.ErrCode.ERR_USER_CANCEL: {
                    //用户取消授权
                    WxLoginHelper.getWxLoginHelper().getWeChatListener().cancel();
                    break;
                }
                default:
                    //其他异常情况
                    Log.i("WxBaseCallBackActivity", "onResp:Error " + baseResp.errCode + baseResp.errStr);
                    WxLoginHelper.getWxLoginHelper().getWeChatListener().other(baseResp);
            }
            this.finish();
        }
    }
}
