package io.trchain.cube;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.trc.com.androidshare.R;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.trc.android.share.QqLoginHelper;
import com.trc.android.share.ShareApi;
import com.trc.android.share.ShareCallback;
import com.trc.android.share.ShareConstants;
import com.trc.android.share.ShareEvent;
import com.trc.android.share.UmengShareConfig;
import com.trc.android.share.WxLoginHelper;
import com.umeng.commonsdk.UMConfigure;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UMConfigure.init(this, "5ae2d1278f4a9d4fe900005c", "mina-test", UMConfigure.DEVICE_TYPE_PHONE, "d1e868835e2241c3952ec87d1bcd1bd4");
        UmengShareConfig.getInstance()
                .setQq("1106773075", "2RS5sF8XWhbzkaT5")
                .setWeibo("3263870905", "3a36fd30369281ea80edab0938a90a2d")
                .setDingtalk("dingoae6x8yuhewefgcuvw")
                .setWeixin("wxd74b2564118425d9", "10279a4499cad978c2e2e3080625d7bc");
    }

    public void onClick(View view) {
        Uri uri=Uri.parse("tlkj://share?params=eyJpY29uIjoiaHR0cHM6Ly9pbWcudGZhYnJpYy5jb20vMTU1OTczNDA4OTM3OT9pbWFnZVZpZXcyLzIvdy8xMDAyL2gvMTI1MSZlPTE1NTk3Mzc2OTAmdG9rZW49bjZ4NUVpOXFNOEVxSG1RX3pxYlo2dThUNzVOSm00c3I3SjE5dFZQNjpyN2p5NTJYeGNlalEyYnhFSHZmQ1RsMzJkMHM9Iiwic2hhcmVUeXBlIjoicGljIiwicGxhdGZvcm1zIjpbInd4RnJpZW5kIl19");
        //Uri uri = Uri.parse("trmall://share?params=ew0KICAgICJjb250ZW50IjogIuaIkeWcqOazsOeEtuWfjuWPkeeOsOS6huS4gOS4quS4jemUmeeahOWVhuWTge+8jOi1tuW/q+adpeeci+eci+WQpyIsIA0KICAgICJzaGFyZVR5cGUiOiAidGV4dCIsIA0KICAgICJwbGF0Zm9ybXMiOiBbDQogICAgICAgICJ3eEZyaWVuZCIsIA0KICAgICAgICAid3hUaW1lbGluZSIsIA0KICAgICAgICAid2VpYm8iLCANCiAgICAgICAgInFxIiwNCiAgICAgICAgInF6b25lIiwgDQogICAgICAgICJkaW5nZGluZyIsIA0KICAgICAgICAiY2xpcGJvYXJkIg0KICAgIF0NCn0=");
        ShareApi.share(this, uri, new ShareCallback() {
            @Override
            public void onShareEvent(ShareEvent shareEvent) {
                System.out.println(shareEvent);
                Toast.makeText(MainActivity.this, shareEvent.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickShareTextToWexin(View view) {
        Uri uri = Uri.parse("trmall://share?params=ewogICAgImNvbnRlbnQiOiAi5oiR5Zyo5rOw54S25Z+O5Y+R546w5LqG5LiA5Liq5LiN6ZSZ55qE5ZWG5ZOB77yM6LW25b+r5p2l55yL55yL5ZCnIiwgCiAgICAic2hhcmVUeXBlIjogInRleHQiLCAKICAgICJwbGF0Zm9ybXMiOiBbCiAgICAgICAgInd4RnJpZW5kIgogICAgXQp9");
        ShareApi.share(this, uri, new ShareCallback() {
            @Override
            public void onShareEvent(ShareEvent shareEvent) {
                System.out.println(shareEvent);
                Toast.makeText(MainActivity.this, shareEvent.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickShareLinkToDefaultPlatforms(View view) {
        Uri uri = Uri.parse("trmall://share?params=eyJ0aXRsZSI6Ilx1NjJmY1x1OTBhZVx1N2Y4ZVx1NTk4NiBcdTY1ZTVcdTY3MmMgXHU4ZDQ0XHU3NTFmXHU1ODAyRXR0dXNhaXNcdTgyN2VcdTY3NWNcdTdlYjFcdTUzZWZcdTcyMzFcdTczMmJcdTU0YWFcdTgxNmVcdTdlYTJcdTRlMDlcdTgyNzIgXHU2YTU4XHU4MjcyXC9cdTdjODlcdTgyNzIgMi40ZyIsImNvbnRlbnQiOiJcdTYyMTFcdTU3MjhcdTZjZjBcdTcxMzZcdTU3Y2VcdTUzZDFcdTczYjBcdTRlODZcdTRlMDBcdTRlMmFcdTRlMGRcdTk1MTlcdTc2ODRcdTU1NDZcdTU0YzFcdWZmMGNcdThkNzZcdTVmZWJcdTY3NjVcdTc3MGJcdTc3MGJcdTU0MjciLCJsaW5rIjoiaHR0cHM6XC9cL20udGFpcmFubWFsbC5jb21cL2l0ZW0/aXRlbV9pZD01MzA4NiIsImljb24iOiJodHRwczpcL1wvaW1hZ2UudGFpcmFubWFsbC5jb21cL0ZvVDhSUmhiYXRmenRLQTNWYnRId25CSTNRZVEifQ==");
        ShareApi.share(this, uri, new ShareCallback() {
            @Override
            public void onShareEvent(ShareEvent shareEvent) {
                System.out.println(shareEvent);
                Toast.makeText(MainActivity.this, shareEvent.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickShareTextToQq(View view) {
        String content = "Python的3.0版本，常被称为Python 3000，或简称Py3k。相对于Python的早期版本，这是一个较大的升级。为了不带入过多的累赘，Python 3.0在设计的时候没有考虑向下兼容。\n" +
                "Python 介绍及安装教程我们在Python 2.X版本的教程中已有介绍，这里就不再赘述。\n" +
                "你也可以点击 Python2.x与3\u200B\u200B.x版本区别 来查看两者的不同。";
        ShareApi.builder()
                .context(this)
                .content(content)
                .shareType(ShareConstants.TYPE_TEXT)
                .platforms(ShareConstants.PLATFORM_QQ, ShareConstants.PLATFORM_QZONE)
                .share(new ShareCallback() {
                    @Override
                    public void onShareEvent(ShareEvent shareEvent) {
                        System.out.println(shareEvent);
                        Toast.makeText(MainActivity.this, shareEvent.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onClickShareTextToDingding(View view) {
        String content = "Python的3.0版本，常被称为Python 3000，或简称Py3k。相对于Python的早期版本，这是一个较大的升级。为了不带入过多的累赘，Python 3.0在设计的时候没有考虑向下兼容。\n" +
                "Python 介绍及安装教程我们在Python 2.X版本的教程中已有介绍，这里就不再赘述。\n" +
                "你也可以点击 Python2.x与3\u200B\u200B.x版本区别 来查看两者的不同。";
        ShareApi.builder()
                .context(this)
                .content(content)
                .shareType(ShareConstants.TYPE_TEXT)
                .platforms(ShareConstants.PLATFORM_DINGDING)
                .share();
    }

    public void onClickLoginToWechat(View view) {
        WxLoginHelper.getWxLoginHelper().regToWx(this.getApplication(), "wxd74b2564118425d9");
        WxLoginHelper.getWxLoginHelper().doAuth(new WxLoginHelper.WeChatListener() {
            @Override
            public void success(BaseResp resp) {
                String code = ((SendAuth.Resp) resp).code;
                Log.d("WeChatListener", "success: " + code);
                Toast.makeText(getApplicationContext(), "登录成功 code=" + code, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void other(BaseResp resp) {
                Log.d("WeChatListener", "other: ");
                Toast.makeText(getApplicationContext(), "登录异常啦", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cancel() {
                Toast.makeText(getApplicationContext(), "用户取消登录啦", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickLoginToQQ(View view) {
        QqLoginHelper.getAuth(this, new QqLoginHelper.OnUserLoginListener() {
            @Override
            public void onLoginSucceed(String accessToken, String expiresIn) {
                Log.d("OnUserLoginListener", "onLoginSucceed: ");
            }

            @Override
            public void onUserCancel() {
                Log.d("OnUserLoginListener", "onUserCancel: ");
            }

            @Override
            public void onFail() {
                Log.d("OnUserLoginListener", "onFail: ");
            }
        }, new QqLoginHelper.OnUserInfoListener() {
            @Override
            public void onUserInfoObtained(String name, String iconUrl, String id) {
                String info = String.format("name=%1$s iconUrl=%2$s id=%3$s", name, iconUrl, id);
                Toast.makeText(MainActivity.this, "获取用户信息成功：" + info, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUserCancel() {
                Log.d("OnUserInfoListener", "onUserCancel: ");
            }

            @Override
            public void onFail(int code, String msg, String detail) {
                Log.d("OnUserInfoListener", "onFail: " + code + msg + detail);
            }
        });
    }

    public void onClickShareLocalImg(View view) {
        ShareApi.builder()
                .context(this)
                .shareType(ShareConstants.TYPE_PIC)
                .icon(R.drawable.share_logo)
                .platforms(ShareConstants.PLATFORM_WX_FRIEND,
                        ShareConstants.PLATFORM_WX_TIMELINE,
                        ShareConstants.PLATFORM_WEIBO,
                        ShareConstants.PLATFORM_DINGDING,
                        ShareConstants.PLATFORM_QQ)
                .share(new ShareCallback() {
                    @Override
                    public void onShareEvent(ShareEvent shareEvent) {
                        System.out.println(shareEvent);
                        Toast.makeText(MainActivity.this, shareEvent.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onClickShareNetImg(View view) {
        ShareApi.builder()
                .context(this)
                .shareType(ShareConstants.TYPE_PIC)
                .icon("http://www.xinhuanet.com/photo/2018-06/13/1122981636_15288895116391n.jpg")
                .share(new ShareCallback() {
                    @Override
                    public void onShareEvent(ShareEvent shareEvent) {
                        System.out.println(shareEvent);
                        Toast.makeText(MainActivity.this, shareEvent.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onClickShareLargeImg(View view) {
        ShareApi.builder()
                .context(this)
                .shareType(ShareConstants.TYPE_PIC)
                .icon("https://mfbl-apk.oss-cn-beijing.aliyuncs.com/1559714734317.jpeg")
                .share(new ShareCallback() {
                    @Override
                    public void onShareEvent(ShareEvent shareEvent) {
                        System.out.println(shareEvent);
                        Toast.makeText(MainActivity.this, shareEvent.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*分享到小程序*/
    public void onClickShareMINA(View view){
        ShareApi.builder()
                .context(this)
                .icon(R.drawable.share_logo)
                .title("分享到小程序")
                .platforms(ShareConstants.PLATFORM_MINA)
                .content("分享到小程序测试")
                .link("http://www.baidu.com")
                .minaId("gh_c8db80a80a89")
                .minaPath("pages/index/index")
                .minaType(1)
                .share(new ShareCallback() {
                    @Override
                    public void onShareEvent(ShareEvent shareEvent) {
                        Log.d("onShareEvent", "onShareEvent: 分享回调"+shareEvent.eventType);
                    }
                });
    }
}
