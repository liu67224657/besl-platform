package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-4
 * Time: 下午12:50
 * To change this template use File | Settings | File Templates.
 */
public class InstalledAppInfo implements Serializable{
    private long installedAppId;

    private String appKey;
    private String clientId;
    private String clientToken;
    private int platform;

    private String installInfo;

    private Date createTime;

    public long getInstalledAppId() {
        return installedAppId;
    }

    public void setInstalledAppId(long installedAppId) {
        this.installedAppId = installedAppId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getInstallInfo() {
        return installInfo;
    }

    public void setInstallInfo(String installInfo) {
        this.installInfo = installInfo;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
