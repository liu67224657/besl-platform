package com.enjoyf.platform.service.weixin.req;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-5-14
 * Time: 下午4:43
 * To change this template use File | Settings | File Templates.
 * 图片消息
 */
public class ReqImageMessage extends ReqBaseMessage {
    // 图片链接
    private String PicUrl;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }
}
