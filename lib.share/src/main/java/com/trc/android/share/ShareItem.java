package com.trc.android.share;

import java.io.File;
import java.io.Serializable;

class ShareItem implements Serializable {
    public String title;
    public String link;
    public String content;
    public String icon;
    public String shareBoardTitle;
    public String shareBoardContent;
    public File file;
    public String shareType;
    String[] platforms;
    //如果不为空则意味着需要发送广播事件
    public String broadCastToken;


    /**
     *  小程序分享 新增字段
     * */

    public int minaType;
    public String minaId;
    public String minaPath;
    public boolean minaWithShareTicket;

    ShareItem() {
    }


}
