package com.trc.android.share;

import android.net.Uri;

import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.trc.android.share.ShareConstants.PLATFORM_DINGDING;
import static com.trc.android.share.ShareConstants.PLATFORM_QQ;
import static com.trc.android.share.ShareConstants.PLATFORM_QZONE;
import static com.trc.android.share.ShareConstants.PLATFORM_WEIBO;
import static com.trc.android.share.ShareConstants.PLATFORM_WX_FRIEND;
import static com.trc.android.share.ShareConstants.PLATFORM_WX_TIMELINE;

class ParamsUtil {
    public static ShareItem parseUri(Uri uri) {
        String params = uri.getQueryParameter("params");
        String json = SafeBase64.decodeString(params);
        return getShareItemModel(json);
    }


    private static ShareItem getShareItemModel(String json) {
        try {
            ShareItem shareItem = new ShareItem();
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("title"))
                shareItem.title = jsonObject.getString("title");
            if (jsonObject.has("link"))
                shareItem.link = jsonObject.getString("link");
            if (jsonObject.has("content"))
                shareItem.content = jsonObject.getString("content");
            if (jsonObject.has("icon"))
                shareItem.icon = jsonObject.getString("icon");
            if (jsonObject.has("shareBoardTitle"))
                shareItem.shareBoardTitle = jsonObject.getString("shareBoardTitle");
            if (jsonObject.has("shareBoardContent"))
                shareItem.shareBoardContent = jsonObject.getString("shareBoardContent");
            if (jsonObject.has("shareType"))
                shareItem.shareType = jsonObject.getString("shareType");
            if (jsonObject.has("platforms")) {
                JSONArray jsonArray = jsonObject.getJSONArray("platforms");
                int length = jsonArray.length();
                shareItem.platforms = new String[length];
                for (int i = 0; i < length; i++) {
                    shareItem.platforms[i] = jsonArray.getString(i);
                }
            }

            //添加小程序特定参数
            if (jsonObject.has("minaType")){
                shareItem.minaType=jsonObject.optInt("minaType");
            }
            if (jsonObject.has("minaId")) {
                shareItem.minaId = jsonObject.optString("minaId");
            }
            if (jsonObject.has("minaPath")){
                shareItem.minaPath=jsonObject.optString("minaPath");
            }
            if (jsonObject.has("minaWithShareTicket")){
                shareItem.minaWithShareTicket=jsonObject.optBoolean("minaWithShareTicket");
            }

            return shareItem;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String platformConvert(SHARE_MEDIA shareMedia) {
        switch (shareMedia) {
            case QQ:
                return PLATFORM_QQ;
            case QZONE:
                return PLATFORM_QZONE;
            case DINGTALK:
                return PLATFORM_DINGDING;
            case WEIXIN:
                return PLATFORM_WX_FRIEND;
            case WEIXIN_CIRCLE:
                return PLATFORM_WX_TIMELINE;
            case SINA:
                return PLATFORM_WEIBO;
            default:
                return null;
        }
    }
}
