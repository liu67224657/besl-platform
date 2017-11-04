package com.enjoyf.platform.service.event.system.wanba;

import com.enjoyf.platform.service.event.system.SystemEvent;
import com.enjoyf.platform.service.event.system.SystemEventType;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.notice.wanba.WanbaNoticeBodyType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/23
 */
public class WanbaQuestionNoticeEvent extends SystemEvent {
    //
    private String profileId;
    private String appkey = "";
    private NoticeType type;
    private long questionId;
    private long answerId;
    private String destProfileId;
    private WanbaNoticeBodyType bodyType;
    private Date createTime;
    private String extStr;

    //
    public WanbaQuestionNoticeEvent() {
        super(SystemEventType.WANBA_QUESTION_NOTICE);
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

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public String getDestProfileId() {
        return destProfileId;
    }

    public void setDestProfileId(String destProfileId) {
        this.destProfileId = destProfileId;
    }

    public WanbaNoticeBodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(WanbaNoticeBodyType bodyType) {
        this.bodyType = bodyType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getExtStr() {
        return extStr;
    }

    public void setExtStr(String extStr) {
        this.extStr = extStr;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
