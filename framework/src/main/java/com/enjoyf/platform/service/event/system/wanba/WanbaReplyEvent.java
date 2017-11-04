package com.enjoyf.platform.service.event.system.wanba;

import com.enjoyf.platform.service.event.system.SystemEvent;
import com.enjoyf.platform.service.event.system.SystemEventType;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.notice.wanba.WanbaReplyBody;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/23
 */
public class WanbaReplyEvent extends SystemEvent {
    //
    private String profileId;
    private String appkey = "";
    private NoticeType type;
    private WanbaReplyBody wanbaReplyBody;
    private Date createTime;
    private String destId;


    //
    public WanbaReplyEvent() {
        super(SystemEventType.WANBA_REPLY_NOTICE);
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


    public WanbaReplyBody getWanbaReplyBody() {
        return wanbaReplyBody;
    }

    public void setWanbaReplyBody(WanbaReplyBody wanbaReplyBody) {
        this.wanbaReplyBody = wanbaReplyBody;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
