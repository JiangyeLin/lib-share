package com.trc.android.share;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trc.android.share.utils.FileUtil;
import com.trc.android.share.utils.ImgUtil;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.trc.android.share.ShareConstants.EventType;
import static com.trc.android.share.ShareConstants.PLATFORM_BROWSER;
import static com.trc.android.share.ShareConstants.PLATFORM_CLIPBOARD;
import static com.trc.android.share.ShareConstants.PLATFORM_COVER;
import static com.trc.android.share.ShareConstants.PLATFORM_DINGDING;
import static com.trc.android.share.ShareConstants.PLATFORM_MINA;
import static com.trc.android.share.ShareConstants.PLATFORM_QQ;
import static com.trc.android.share.ShareConstants.PLATFORM_QZONE;
import static com.trc.android.share.ShareConstants.PLATFORM_WEIBO;
import static com.trc.android.share.ShareConstants.PLATFORM_WX_FRIEND;
import static com.trc.android.share.ShareConstants.PLATFORM_WX_TIMELINE;
import static com.trc.android.share.ShareConstants.PLAT_FORMS;
import static com.trc.android.share.ShareConstants.SHARE_RESPONSE_OBJ_KEY;
import static com.trc.android.share.ShareConstants.TYPE_TEXT;

public class CustomShareActivity extends Activity implements OnClickListener {


    public static final String INTENT_KEY_SHARE_ITEM = "shareItem";
    private ShareItem shareItem;
    private TextView tvContent;
    private TextView tvTitle;
    private boolean isFirstResumeCompleted;

    static Intent newIntent(Context context, ShareItem shareItem) {
        Intent it = new Intent(context, CustomShareActivity.class);
        it.putExtra(INTENT_KEY_SHARE_ITEM, shareItem);
        return it;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        findViewById(R.id.shareBoard).setVisibility(View.GONE);
        int id = v.getId();
        if (id == R.id.bgView || id == R.id.btn_cancel) {
            onBackPressed();
        } else if (id == R.id.clipboard) {
            copyToClipboard();
        } else {
            if (R.id.wexin == id) {
                shareToPlatForms(ShareConstants.PLATFORM_WX_FRIEND);
            } else if (R.id.weixinpengyouquan == id) {
                shareToPlatForms(ShareConstants.PLATFORM_WX_TIMELINE);
            } else if (R.id.sina == id) {
                shareToPlatForms(ShareConstants.PLATFORM_WEIBO);
            } else if (R.id.qq == id) {
                shareToPlatForms(ShareConstants.PLATFORM_QQ);
            } else if (R.id.qzone == id) {
                shareToPlatForms(ShareConstants.PLATFORM_QZONE);
            } else if (id == R.id.dingding) {
                shareToPlatForms(ShareConstants.PLATFORM_DINGDING);
            } else if (id == R.id.photoalbum) {
                shareToPlatForms(ShareConstants.PLATFORM_COVER);
            } else if (id == R.id.browser) {
                shareToPlatForms(ShareConstants.PLATFORM_BROWSER);
            }
        }
    }

    private void copyToClipboard() {
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb != null) {
            if (TYPE_TEXT.equals(shareItem.shareType)) {
                cmb.setPrimaryClip(ClipData.newPlainText("ShareUrl", shareItem.content));
            } else {
                cmb.setPrimaryClip(ClipData.newPlainText("ShareUrl", shareItem.link));
            }
            toast("复制完成");
            finish();
        } else {
            toast("复制失败");
        }
    }

    //根据平台触发分享
    private void shareToPlatForms(String platform) {
        //设置分享参数
        final ShareAction action = new ShareAction(CustomShareActivity.this);
        if (TYPE_TEXT.equals(shareItem.shareType)) {//纯文本发送消息
            if (PLATFORM_QQ.equals(platform)) {//QQ官方SDK不支持纯文本分享
                shareQQ(shareItem.content);
                return;
            } else {
                action.withText(shareItem.content);
            }
        } else {
            UMImage image = createUmengImage(shareItem);
            if (null != image) action.withMedia(image);

            //默认图文分享链接
            if (!ShareConstants.TYPE_PIC.equals(shareItem.shareType)
                    && !TextUtils.isEmpty(shareItem.link)) {

                UMWeb web = new UMWeb(shareItem.link);
                if (!TextUtils.isEmpty(shareItem.title)) {
                    web.setTitle(shareItem.title);//标题
                }
                web.setThumb(image);
                web.setDescription(shareItem.content);
                action.withMedia(web);
            }

            if (ShareConstants.TYPE_PIC.equals(shareItem.shareType) && PLATFORM_WX_FRIEND.equals(platform) && shareItem.icon.startsWith("http")) {
                // TODO: 2019/6/5 微信好友分享图片小图显示不对

                File path = new File(getExternalCacheDir(), "image");
                if (!path.exists()) path.mkdirs();
                File file = new File(path, shareItem.icon.hashCode() + ".jpg");
                final String filePath = file.getAbsolutePath();
                FileUtil.download(shareItem.icon, file, new FileUtil.DownloadListener() {
                    @Override
                    public void onSuccess() {
                        try {
                            Bitmap bmp = ImgUtil.getBitmap(filePath);

                            /*UMImage image = new UMImage(getApplicationContext(), bmp);
                            action.withMedia(image);
                            action.setCallback(shareListener);
                            ShareEvent shareEvent = new ShareEvent();
                            shareEvent.eventType = EventType.CLICK_EVENT;
                            action.setPlatform(SHARE_MEDIA.WEIXIN);
                            action.share();
                            showProgressDlg(getApplicationContext());
                            if (null != shareItem.broadCastToken)
                                sendBroadcast(shareEvent);*/

                            // Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_share_logo);
                            //初始化 WXImageObject 和 WXMediaMessage 对象
                            WXImageObject imgObj = new WXImageObject(bmp);
                            WXMediaMessage msg = new WXMediaMessage();
                            msg.mediaObject = imgObj;

                            //设置缩略图
                            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 160, 200, true);
                            bmp.recycle();
                            //msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

                            //构造一个Req
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = System.currentTimeMillis() + "img";
                            req.message = msg;
                            req.scene = SendMessageToWX.Req.WXSceneSession;
                            //调用api接口，发送数据到微信
                            IWXAPI api = WXAPIFactory.createWXAPI(getApplicationContext(), UmengShareConfig.getInstance().getWxAppId(), true);
                            api.sendReq(req);
                            finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });

                return;
            }
        }

        action.setCallback(shareListener);
        ShareEvent shareEvent = new ShareEvent();
        shareEvent.eventType = EventType.CLICK_EVENT;

        if (PLATFORM_WX_FRIEND.equals(platform)) {
            shareEvent.platform = ShareConstants.PLATFORM_WX_FRIEND;
            if (!CheckAppInstalledUtil.isWeChatInstalled(this)) {
                toast("请先安装微信客户端");
                finish();
                return;
            }
            action.setPlatform(SHARE_MEDIA.WEIXIN);
        } else if (PLATFORM_WX_TIMELINE.equals(platform)) {
            shareEvent.platform = ShareConstants.PLATFORM_WX_TIMELINE;
            if (!CheckAppInstalledUtil.isWeChatInstalled(this)) {
                toast("请先安装微信客户端");
                finish();
                return;
            }
            action.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
        } else if (PLATFORM_WEIBO.equals(platform)) {
            shareEvent.platform = ShareConstants.PLATFORM_WEIBO;
            if (!CheckAppInstalledUtil.isWeiBo(this)) {
                toast("请先安装微博客户端");
                finish();
                return;
            }
            action.setPlatform(SHARE_MEDIA.SINA);
        } else if (PLATFORM_QQ.equals(platform)) {
            shareEvent.platform = ShareConstants.PLATFORM_QQ;
            if (!CheckAppInstalledUtil.isQQInstalled(this)) {
                toast("请先安装QQ客户端");
                finish();
                return;
            }
            action.setPlatform(SHARE_MEDIA.QQ);
        } else if (PLATFORM_QZONE.equals(platform)) {
            shareEvent.platform = ShareConstants.PLATFORM_QZONE;
            if (!CheckAppInstalledUtil.isQQInstalled(this)) {
                toast("请先安装QQ客户端");
                finish();
                return;
            }
            action.setPlatform(SHARE_MEDIA.QZONE);
        } else if (PLATFORM_DINGDING.equals(platform)) {
            shareEvent.platform = ShareConstants.PLATFORM_DINGDING;
            if (!CheckAppInstalledUtil.isDingdingInstall(this)) {
                toast("请先安装钉钉客户端");
                finish();
                return;
            }
            action.setPlatform(SHARE_MEDIA.DINGTALK);
        } else if (PLATFORM_CLIPBOARD.equals(platform)) {
            //复制到剪贴板
            shareEvent.platform = ShareConstants.PLATFORM_CLIPBOARD;
            copyToClipboard();
        } else if (PLATFORM_COVER.equals(platform)) {
            // TODO: 2018/6/22 电商需要一个生成封面的类别，先单独给他回传出去，后续调整
            shareEvent.platform = ShareConstants.PLATFORM_COVER;
            if (null != shareItem.broadCastToken)
                sendBroadcast(shareEvent);
            finish();
            return;
        } else if (PLATFORM_BROWSER.equals(platform)) {
            //  分享至浏览器
            if (TextUtils.isEmpty(shareItem.link)) {
                toast("请先设置分享链接");
                finish();
                return;
            }

            Uri uri = Uri.parse(shareItem.link);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

            shareEvent.platform = ShareConstants.PLATFORM_BROWSER;
            if (null != shareItem.broadCastToken)
                sendBroadcast(shareEvent);
            finish();
            return;
        } else if (PLATFORM_MINA.equals(platform)) {
            UMMin umMin = new UMMin(shareItem.link); //兼容低版本的网页链接
            umMin.setThumb(createUmengImage(shareItem)); // 小程序消息封面图片
            umMin.setTitle(shareItem.title);   // 小程序消息title
            umMin.setDescription(shareItem.content);  // 小程序消息描述
            umMin.setPath(shareItem.minaPath);    //小程序页面路径
            umMin.setUserName(shareItem.minaId);   // 小程序原始id,在微信平台查询
            if (1 == shareItem.minaType) {
                //开发版
                Config.setMiniTest();
            } else if ((2 == shareItem.minaType)) {
                //体验版
                Config.setMiniPreView();
            }
            action.withMedia(umMin);
            //小程序只能分享到微信好友
            action.setPlatform(SHARE_MEDIA.WEIXIN);
            shareEvent.platform = ShareConstants.PLATFORM_MINA;
        } else {
            //没有匹配到对应的平台，回传出去让调用方自行处理
            shareEvent.platform = platform;
            if (null != shareItem.broadCastToken)
                sendBroadcast(shareEvent);
            finish();
            return;
        }
        action.share();
        showProgressDlg(this);
        if (null != shareItem.broadCastToken)
            sendBroadcast(shareEvent);
    }

    private void sendBroadcast(ShareEvent event) {
        Intent intent = new Intent(shareItem.broadCastToken);
        intent.putExtra(SHARE_RESPONSE_OBJ_KEY, event);
        sendBroadcast(intent);
    }

    /**
     * umShareListener为回调监听
     */
    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onCancel(SHARE_MEDIA shareMedia) {
            sendBroadcastIfNecessary(shareMedia, EventType.CANCEL_EVENT);
            finish();
        }

        @Override
        public void onError(SHARE_MEDIA shareMedia, Throwable throwable) {
            sendBroadcastIfNecessary(shareMedia, EventType.SHARE_FAIL_EVENT);
            toast("分享失败，不好意思啦");
            finish();
        }

        @Override
        public void onStart(SHARE_MEDIA shareMedia) {
            sendBroadcastIfNecessary(shareMedia, EventType.SHARE_START_EVENT);
        }

        @Override
        public void onResult(SHARE_MEDIA shareMedia) {
            sendBroadcastIfNecessary(shareMedia, EventType.SHARE_SUCCESS_EVENT);
            finish();
        }
    };

    private void sendBroadcastIfNecessary(SHARE_MEDIA shareMedia, EventType cancelEvent) {
        if (null != shareItem.broadCastToken) {
            ShareEvent shareEvent = new ShareEvent();
            shareEvent.eventType = cancelEvent;
            shareEvent.platform = ParamsUtil.platformConvert(shareMedia);
            sendBroadcast(shareEvent);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareItem = (ShareItem) getIntent().getSerializableExtra(INTENT_KEY_SHARE_ITEM);
        requestNecessaryPermissions();
        if (null != shareItem.platforms && shareItem.platforms.length == 1) {
            shareDirectly();
        } else {
            setContentView(R.layout.share_activity);
            initViews();
            setUpShareBoard();
        }
    }

    private void requestNecessaryPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//("读写权限被拒绝，有可能会造成qq分享失败，可以到设置里手动打开");
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    private void shareDirectly() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //直接唤起分享
                shareToPlatForms(shareItem.platforms[0]);
            }
        }, 25);
    }

    protected void initViews() {
        tvContent = findViewById(R.id.tv_content);
        tvTitle = findViewById(R.id.tv_title);
    }

    //设置分享面板
    private void setUpShareBoard() {
        if (TextUtils.isEmpty(shareItem.shareBoardTitle)) tvTitle.setVisibility(View.GONE);
        else tvTitle.setText(shareItem.shareBoardTitle);
        if (TextUtils.isEmpty(shareItem.shareBoardContent)) tvContent.setVisibility(View.GONE);
        else tvContent.setText(shareItem.shareBoardContent);
        ShareSimpleGridLayout viewGroup = findViewById(R.id.grid_container);
        Map<String, View> map = new HashMap<>(PLAT_FORMS.length);
        for (int i = 0; i < PLAT_FORMS.length; i++) {
            map.put(PLAT_FORMS[i], viewGroup.getChildAt(i));
            viewGroup.getChildAt(i).setTag(PLAT_FORMS[i]);
        }
        if (null != shareItem.platforms) {
            viewGroup.removeAllViews();
            for (String key : shareItem.platforms) {
                View child = map.get(key);
                if (child != null) {
                    //目前Uri有可能传的平台不支持，防止崩溃
                    child.setVisibility(View.VISIBLE);
                    viewGroup.addView(child);
                }
            }
            if (shareItem.platforms.length < 4) {
                viewGroup.setColumn(shareItem.platforms.length);
            } else {
                viewGroup.setColumn(3);
            }
        }
    }

    @Override
    protected void onResume() {//跳转到第三方有可能不会回调页面，最终回来到时候认为分享成功
        super.onResume();
        if (isFirstResumeCompleted) {
            if (null != shareItem.broadCastToken) {
                ShareEvent shareEvent = new ShareEvent();
                shareEvent.eventType = EventType.UNKNOWN_EVENT;
                sendBroadcast(shareEvent);
            }
            finish();
        } else {
            isFirstResumeCompleted = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopProgressDlg();
        UMShareAPI.get(this).release();
    }


    private ProgressDialog progressDlg = null;

    /**
     * 启动进度条
     */
    private void showProgressDlg(Context ctx) {

        if (null == progressDlg) {
            if (ctx == null) return;
            progressDlg = new ProgressDialog(ctx);
        }
        //设置进度条样式
        progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //提示的消息
        progressDlg.setMessage("正在加载...");
        progressDlg.setIndeterminate(false);
        progressDlg.setCancelable(true);
        progressDlg.show();

    }

    /**
     * 结束进度条
     */
    public void stopProgressDlg() {
        if (null != progressDlg && progressDlg.isShowing()) {
            progressDlg.dismiss();
            progressDlg = null;
        }
    }

    private void toast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (null != shareItem.broadCastToken) {
            ShareEvent shareEvent = new ShareEvent();
            shareEvent.eventType = EventType.CANCEL_EVENT;
            sendBroadcast(shareEvent);
        }
    }


    public void shareQQ(String content) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        try {
            sendIntent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
            Intent chooserIntent = Intent.createChooser(sendIntent, "选择分享途径");
            if (null != chooserIntent) {
                startActivity(chooserIntent);
            } else if (null != sendIntent.resolveActivity(getPackageManager())) {
                startActivity(sendIntent);
            }
            sendBroadcastIfNecessary(SHARE_MEDIA.QQ, EventType.CLICK_EVENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UMImage createUmengImage(ShareItem shareItem) {
        String iconUrl = shareItem.icon;
        UMImage image = null;
        if (null != shareItem.file) {
            image = new UMImage(CustomShareActivity.this, shareItem.file);
        } else if (!TextUtils.isEmpty(iconUrl)) {
            if (iconUrl.startsWith("http")) {
                image = new UMImage(CustomShareActivity.this, iconUrl);
                image.compressStyle = UMImage.CompressStyle.QUALITY;
            } else if (iconUrl.startsWith("res")) {
                String idStr = Uri.parse(iconUrl).getHost();
                image = new UMImage(CustomShareActivity.this, BitmapFactory.decodeResource(getResources(), Integer.parseInt(idStr)));
            }
        } else {
            //是否设置默认图片
            int defaultIconRes = UmengShareConfig.getInstance().getDefaultIconRes();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), defaultIconRes);
            if (null == bitmap) {
                throw new IllegalArgumentException("默认图片设置失败");
            }
            image = new UMImage(CustomShareActivity.this, bitmap);
        }
        return image;
    }
}
