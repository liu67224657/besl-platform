/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-26 下午5:56
 * Description:
 */
public class ContentReplyQueryEntry implements Serializable {
    private String uno;
    private String contentId;
    private String replyId;

    public ContentReplyQueryEntry(String uno, String contentId, String replyId) {
        this.uno = uno;
        this.contentId = contentId;
        this.replyId = replyId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    @Override
    public int hashCode() {
        return replyId != null ? replyId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
