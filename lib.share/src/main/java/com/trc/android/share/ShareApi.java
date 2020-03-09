package com.trc.android.share;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

/**
 * 具体文档可以查看
 * {@link "http://wiki.trc.com/pages/viewpage.action?pageId=7568934"}
 */
public class ShareApi {

    public static void share(Context context, @NonNull String uri, @Nullable ShareCallback callback) {
        share(context, Uri.parse(uri), callback);
    }

    public static void share(Context context, Uri uri) {
        share(context, uri, null);
    }

    public static void share(Context context, Uri uri, @Nullable ShareCallback callback) {
        share(context, ParamsUtil.parseUri(uri), callback);
    }

    private static void share(Context context, ShareItem shareItem, @Nullable final ShareCallback callback) {
        if (null != callback) {
            shareItem.broadCastToken = ShareConstants.SHARE_BROADCAST_ACTION_PREFIX + System.currentTimeMillis();
            //如果需要监听一些事件：点击对应平台分享的事件、分享成功事件、分享失败事件、分享取消事件，及对应平台信息
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ShareEvent shareEvent = (ShareEvent) intent.getSerializableExtra(ShareConstants.SHARE_RESPONSE_OBJ_KEY);
                    callback.onShareEvent(shareEvent);
                    switch (shareEvent.eventType) {
                        case CANCEL_EVENT:
                        case SHARE_FAIL_EVENT:
                        case SHARE_SUCCESS_EVENT:
                        case UNKNOWN_EVENT:
                            context.unregisterReceiver(this);
                    }
                }
            }, new IntentFilter(shareItem.broadCastToken));
        }
        Intent intent = CustomShareActivity.newIntent(context, shareItem);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        public Builder() {
            shareItem = new ShareItem();
        }

        Context context;

        ShareItem shareItem;

        ShareCallback shareCallback;

        /**
         * @param title 分享标题，仅在{@link ShareConstants#TYPE_DEFAULT} 模式下生效
         * @return
         */
        public Builder title(String title) {
            shareItem.title = title;
            return this;
        }

        /**
         * @param link 分享链接,仅在{@link ShareConstants#TYPE_DEFAULT} 模式下生效
         * @return
         */
        public Builder link(String link) {
            shareItem.link = link;
            return this;
        }

        /**
         * @param content 分享文本
         * @return
         */
        public Builder content(String content) {
            shareItem.content = content;
            return this;
        }

        /**
         * @param icon 图片URL,目前只支持网络链接，如果你想要传本地图片的话，请调用
         *             {@link #icon(int)}或者{@link #icon(File)}
         * @return
         */
        public Builder icon(String icon) {
            shareItem.icon = icon;
            return this;
        }

        /**
         * @param icon 本地图片{@link #icon(String)}
         * @return
         */
        public Builder icon(@DrawableRes int icon) {
            shareItem.icon = "res://" + icon;
            return this;
        }

        /**
         * @param file 图片文件{@link #icon(String)}
         * @return
         */
        public Builder icon(File file) {
            shareItem.file = file;
            return this;
        }

        /**
         * @param shareBoardTitle 分享面板的标题，选填
         * @return
         */
        public Builder shareBoardTitle(String shareBoardTitle) {
            shareItem.shareBoardTitle = shareBoardTitle;
            return this;
        }

        /**
         * @param shareBoardContent 分享面板上面的二级说明,选填
         * @return
         */
        public Builder shareBoardContent(String shareBoardContent) {
            shareItem.shareBoardContent = shareBoardContent;
            return this;
        }

        /**
         * @param shareType 分享类别
         *                  {@link ShareConstants#TYPE_DEFAULT}
         *                  {@link ShareConstants#TYPE_PIC}
         *                  {@link ShareConstants#TYPE_TEXT}
         *                  若不传，则按 {@link ShareConstants#TYPE_DEFAULT}处理
         */
        public Builder shareType(@ShareConstants.TypeDef String shareType) {
            shareItem.shareType = shareType;
            return this;
        }

        /**
         * @param platforms 分享平台
         *                  可不传，如果不传会显示sdk默认平台
         *                  <p>
         *                  具体可以传的平台请参阅{@link ShareConstants}
         *                  如果通过uri设置的话，
         * @return
         */
        public Builder platforms(@ShareConstants.PlatformDef String... platforms) {
            if (null == platforms[0]) return this;
            shareItem.platforms = platforms;
            return this;
        }


        /**
         *  小程序 分享类型
         * */
        public Builder minaType(int minaType){
            shareItem.minaType=minaType;
            return this;
        }

        /**
         *  小程序id
         * */
        public Builder minaId(String minaId){
            shareItem.minaId=minaId;
            return this;
        }

        /**
         *  小程序 分享类型
         * */
        public Builder minaPath(String minaPath){
            shareItem.minaPath=minaPath;
            return this;
        }

        /**
         *  小程序 是否使用带shareTicket的分享
         *  默认false，非必须
         * */
        public Builder minaWithShareTicket(boolean minaWithShareTicket){
            shareItem.minaWithShareTicket=minaWithShareTicket;
            return this;
        }

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder shareCallback(ShareCallback shareCallback) {
            this.shareCallback = shareCallback;
            return this;
        }

        public void share() {
            ShareApi.share(context, shareItem, shareCallback);
        }

        public void share(ShareCallback shareCallback) {
            this.shareCallback = shareCallback;
            ShareApi.share(context, shareItem, shareCallback);
        }

    }


}
