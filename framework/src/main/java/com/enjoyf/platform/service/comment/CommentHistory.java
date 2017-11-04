package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-11
 * Time: 上午11:08
 * To change this template use File | Settings | File Templates.
 */
public class CommentHistory implements Serializable{

    private long historyId;
    private String profileId;
    private String objectId;
    private int actionTimes;
    private Date actionDate;
    private CommentHistoryType historyType;
    private String actionIp;
    private CommentDomain domain;
    private String commentId;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getActionTimes() {
        return actionTimes;
    }

    public void setActionTimes(int actionTimes) {
        this.actionTimes = actionTimes;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionIp() {
        return actionIp;
    }

    public void setActionIp(String actionIp) {
        this.actionIp = actionIp;
    }

    public CommentDomain getDomain() {
        return domain;
    }

    public void setDomain(CommentDomain domain) {
        this.domain = domain;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public CommentHistoryType getHistoryType() {
        return historyType;
    }

    public void setHistoryType(CommentHistoryType historyType) {
        this.historyType = historyType;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
