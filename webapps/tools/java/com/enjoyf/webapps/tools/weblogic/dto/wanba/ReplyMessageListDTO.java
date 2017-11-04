package com.enjoyf.webapps.tools.weblogic.dto.wanba;

import com.enjoyf.platform.service.usercenter.Profile;

import java.util.Date;

/**
 * Created by zhimingli on 2016/11/15 0015.
 */
public class ReplyMessageListDTO {
    private Profile profile;

    private Long msgid;
    private String desc;
    private Date date;
    private String replydesc;
    private String answid;
    private String replyid;

    private String type;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Long getMsgid() {
        return msgid;
    }

    public void setMsgid(Long msgid) {
        this.msgid = msgid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReplydesc() {
        return replydesc;
    }

    public void setReplydesc(String replydesc) {
        this.replydesc = replydesc;
    }

    public String getAnswid() {
        return answid;
    }

    public void setAnswid(String answid) {
        this.answid = answid;
    }

    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
