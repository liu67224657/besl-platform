package com.enjoyf.platform.service.weixin.req;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-5-14
 * Time: 下午4:45
 * To change this template use File | Settings | File Templates.
 * 请求的 音频消息
 */
public class ReqVoiceMessage extends ReqBaseMessage{
    // 媒体ID
    private String MediaId;
    // 语音格式
    private String Format;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }
}
