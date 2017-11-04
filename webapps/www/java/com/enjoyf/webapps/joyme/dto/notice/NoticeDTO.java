package com.enjoyf.webapps.joyme.dto.notice;

import com.enjoyf.platform.service.notice.wiki.WikiNoticeBody;

import java.util.Date;

/**
 * Created by pengxu on 2016/12/14.
 */
public class NoticeDTO {
    private String profileId;
    private String appkey;
    private String noticeType;
    private WikiNoticeBody body;//json 对象
    private Date createTime;
    private String timeString;
    private String noticeTimeString;//

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public WikiNoticeBody getBody() {
        return body;
    }

    public void setBody(WikiNoticeBody body) {
        this.body = body;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getNoticeTimeString() {
        return noticeTimeString;
    }

    public void setNoticeTimeString(String noticeTimeString) {
        this.noticeTimeString = noticeTimeString;
    }
}
