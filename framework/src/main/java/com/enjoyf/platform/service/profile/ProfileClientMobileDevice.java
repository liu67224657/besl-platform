package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-14
 * Time: 上午11:05
 * To change this template use File | Settings | File Templates.
 */
public class ProfileClientMobileDevice implements Serializable {

    private long deviceId;
    private String uno;
    private String clientId;
    private String clientToken;
    private AppPlatform platform;
    private String appId;
    private long lastMsgId;

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
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

    public AppPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(AppPlatform platform) {
        this.platform = platform;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public long getLastMsgId() {
        return lastMsgId;
    }

    public void setLastMsgId(long lastMsgId) {
        this.lastMsgId = lastMsgId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
