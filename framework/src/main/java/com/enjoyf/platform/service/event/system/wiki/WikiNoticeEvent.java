package com.enjoyf.platform.service.event.system.wiki;

import com.enjoyf.platform.service.event.system.SystemEvent;
import com.enjoyf.platform.service.event.system.SystemEventType;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.notice.wiki.WikiNoticeBody;

import java.util.Date;

/**
 * Created by pengxu on 2016/12/6.
 */
public class WikiNoticeEvent extends SystemEvent {
    private String profileId;
    private String appkey="default";//todo modify constants
    private NoticeType type;
    private Date createTime;
    private WikiNoticeBody body;
    private String destId;

    public WikiNoticeEvent() {
        super(SystemEventType.WIKI_NOTICE_EVENT);
    }

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

    public NoticeType getType() {
        return type;
    }

    public void setType(NoticeType type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public WikiNoticeBody getBody() {
        return body;
    }

    public void setBody(WikiNoticeBody body) {
        this.body = body;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }
}
