package com.enjoyf.platform.service.sync;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-4
 * Time: 下午3:04
 * To change this template use File | Settings | File Templates.
 */
public class ShareTopic implements Serializable {
    private long shareTopicId;

    private long shareId;

    private String shareTopic;
    private ActStatus removeStatus = ActStatus.UNACT;

    private String createUserId;
    private Date createDate;
    private String createUserIp;

    private String lastModifyUserid;
    private Date lastModifyDate;
    private String lastModifyUserIp;

    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public String getShareTopic() {
        return shareTopic;
    }

    public void setShareTopic(String shareTopic) {
        this.shareTopic = shareTopic;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserIp() {
        return createUserIp;
    }

    public void setCreateUserIp(String createUserIp) {
        this.createUserIp = createUserIp;
    }

    public long getShareTopicId() {
        return shareTopicId;
    }

    public void setShareTopicId(long shareTopicId) {
        this.shareTopicId = shareTopicId;
    }

    public String getLastModifyUserid() {
        return lastModifyUserid;
    }

    public void setLastModifyUserid(String lastModifyUserid) {
        this.lastModifyUserid = lastModifyUserid;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getLastModifyUserIp() {
        return lastModifyUserIp;
    }

    public void setLastModifyUserIp(String lastModifyUserIp) {
        this.lastModifyUserIp = lastModifyUserIp;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
