/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class SocialRemoveReplyBroadcastEvent extends SystemEvent {
    //
    private String replyUno;

    //
    private String replyId;

    //
    private String parentReplyUno;
    private String parentReplyId;

    //
    private String contentUno;
    private String contentId;

    //
    private Date replyDate;

    //
    public SocialRemoveReplyBroadcastEvent() {
        super(SystemEventType.SOCIAL_REPLY_REMOVE_BROADCAST);
    }

    public String getReplyUno() {
        return replyUno;
    }

    public void setReplyUno(String replyUno) {
        this.replyUno = replyUno;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getParentReplyUno() {
        return parentReplyUno;
    }

    public void setParentReplyUno(String parentReplyUno) {
        this.parentReplyUno = parentReplyUno;
    }

    public String getParentReplyId() {
        return parentReplyId;
    }

    public void setParentReplyId(String parentReplyId) {
        this.parentReplyId = parentReplyId;
    }

    public String getContentUno() {
        return contentUno;
    }

    public void setContentUno(String contentUno) {
        this.contentUno = contentUno;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return replyUno.hashCode();
    }
}
