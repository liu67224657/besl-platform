package com.enjoyf.platform.service.notice.wiki;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by ericliu on 16/3/12.
 */
public class WikiNotice implements Serializable {
    private long noticeId;
    private String ownProfileId;
    private String destProfileId;
    private String destId;
    private String otherId;
    private WikiNoticeType noticeType;
    private String messageBody;
    private IntValidStatus removeStatus;
    private Date createTime;
    private String expStr;

    public long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(long noticeId) {
        this.noticeId = noticeId;
    }

    public String getOwnProfileId() {
        return ownProfileId;
    }

    public void setOwnProfileId(String ownProfileId) {
        this.ownProfileId = ownProfileId;
    }

    public String getDestProfileId() {
        return destProfileId;
    }

    public void setDestProfileId(String destProfileId) {
        this.destProfileId = destProfileId;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public WikiNoticeType getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(WikiNoticeType noticeType) {
        this.noticeType = noticeType;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public IntValidStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(IntValidStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getExpStr() {
        return expStr;
    }

    public void setExpStr(String expStr) {
        this.expStr = expStr;
    }

    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static WikiNotice jsonToObject(String jsonStr) {
        WikiNotice returnValue = null;

        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<WikiNotice>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
