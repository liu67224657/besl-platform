package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-24
 * Time: 下午4:31
 * To change this template use File | Settings | File Templates.
 */
public class AppStatInstallLog implements Serializable {
    private long appInstallLogId;
    private long appId;
    private AppPlatform appPlatform;
    private long destAppId;
    private String destAppName;
    private int destInstallCount;
    private long appSeqId;
    private Date createTime;
    private Date createDate;
    private int displayorder;

    public long getAppInstallLogId() {
        return appInstallLogId;
    }

    public void setAppInstallLogId(long appInstallLogId) {
        this.appInstallLogId = appInstallLogId;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public AppPlatform getAppPlatform() {
        return appPlatform;
    }

    public void setAppPlatform(AppPlatform appPlatform) {
        this.appPlatform = appPlatform;
    }

    public long getDestAppId() {
        return destAppId;
    }

    public void setDestAppId(long destAppId) {
        this.destAppId = destAppId;
    }

    public String getDestAppName() {
        return destAppName;
    }

    public void setDestAppName(String destAppName) {
        this.destAppName = destAppName;
    }

    public int getDestInstallCount() {
        return destInstallCount;
    }

    public void setDestInstallCount(int destInstallCount) {
        this.destInstallCount = destInstallCount;
    }

    public long getAppSeqId() {
        return appSeqId;
    }

    public void setAppSeqId(long appSeqId) {
        this.appSeqId = appSeqId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(int displayorder) {
        this.displayorder = displayorder;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
