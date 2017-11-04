package com.enjoyf.platform.service.event.client;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-19
 * Time: 下午7:37
 * To change this template use File | Settings | File Templates.
 */
public class ClientInstallEvent extends ClientEvent {
    private String clientId;
    private String appId;
    private int platform; //0-ios 1-android
    private String appVersion;// app 版本
    private int installType;//0-install 1-update
    private String channelCode;
    private String device;
    private String screen;

    private String osVersion;

    private String ip;
    private Date createDate;


    public ClientInstallEvent() {
        super(ClientEventType.INSTALL_EVENT);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getInstallType() {
        return installType;
    }

    public void setInstallType(int installType) {
        this.installType = installType;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
