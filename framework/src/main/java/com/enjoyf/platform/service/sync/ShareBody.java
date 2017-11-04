package com.enjoyf.platform.service.sync;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-4
 * Time: 下午3:03
 * To change this template use File | Settings | File Templates.
 */
public class ShareBody implements Serializable {
    private long shareBodyId;
    private long shareId;
    private String shareSubject;
    private String shareBody;
    private String picUrl;
    private ActStatus removeStatus = ActStatus.UNACT;

    private String createUserId;
    private Date createDate;
    private String createUserIp;

    private String lastModifyUserId;
    private Date lastModifyDate;
    private String lastModifyUserIp;

    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public String getShareSubject() {
        return shareSubject;
    }

    public void setShareSubject(String shareSubject) {
        this.shareSubject = shareSubject;
    }

    public String getShareBody() {
        return shareBody;
    }

    public void setShareBody(String shareBody) {
        this.shareBody = shareBody;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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

    public long getShareBodyId() {
        return shareBodyId;
    }

    public void setShareBodyId(long shareBodyId) {
        this.shareBodyId = shareBodyId;
    }

    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
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
