package com.enjoyf.platform.service.weixin.req;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-5-14
 * Time: 下午4:42
 * To change this template use File | Settings | File Templates.
 * 文本消息
 */
public class ReqTextMessage extends ReqBaseMessage {
    // 消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
