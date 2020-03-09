package com.trc.android.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author ilioili  on 16/6/14.
 */
public class QqLoginHelper extends Activity {
    public static final String APP_ID = UmengShareConfig.getInstance().getQqAppId();
    public static final String SCOPE = "get_simple_userinfo";
    public static Tencent mTencent;
    private static OnUserInfoListener onUserInfoListener;
    private static OnUserLoginListener onUserLoginListener;

    private boolean mIsCanceled = true;


    private IUiListener iUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            try {
                mIsCanceled = false;
                JSONObject jsonObject = (JSONObject) o;
                String openid = jsonObject.getString("openid");
                String accessToken = jsonObject.getString("access_token");
                String expiresIn = jsonObject.get("expires_in") + "";
                mTencent.setAccessToken(accessToken, expiresIn);
                onUserLoginListener.onLoginSucceed(accessToken, expiresIn);
                mTencent.setOpenId(openid);
                if (null != onUserInfoListener) getUserInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                finish();
            }
        }

        @Override
        public void onError(UiError uiError) {
            mIsCanceled = false;
            onUserLoginListener.onFail();
            finish();
        }

        @Override
        public void onCancel() {
            onUserLoginListener.onUserCancel();
            finish();
        }
    };

    private void getUserInfo() {
        UserInfo info = new UserInfo(this, mTencent.getQQToken());
        info.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                try {
                    JSONObject jsonObject = (JSONObject) o;
                    String name = jsonObject.getString("nickname");
                    String icon = jsonObject.getString("figureurl_qq_2");
                    onUserInfoListener.onUserInfoObtained(name, icon, "QQ_" + mTencent.getOpenId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                onUserInfoListener.onFail(uiError.errorCode, uiError.errorMessage, uiError.errorDetail);
            }

            @Override
            public void onCancel() {
                onUserInfoListener.onUserCancel();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTencent = Tencent.createInstance(APP_ID, getApplicationContext());
        mTencent.login(this, SCOPE, iUiListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mIsCanceled) {
            onUserLoginListener.onUserCancel();
        }
    }

    public static void getAuth(Activity activity, OnUserLoginListener loginListener, OnUserInfoListener listener) {
        onUserInfoListener = listener;
        onUserLoginListener = loginListener;
        activity.startActivity(new Intent(activity, QqLoginHelper.class));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, iUiListener);
        }
        finish();
    }


    public interface OnUserLoginListener {
        void onLoginSucceed(String accessToken, String expiresIn);

        void onUserCancel();

        void onFail();
    }

    public interface OnUserInfoListener {
        void onUserInfoObtained(String name, String iconUrl, String id);

        void onUserCancel();

        void onFail(int code, String msg, String detail);
    }

}
