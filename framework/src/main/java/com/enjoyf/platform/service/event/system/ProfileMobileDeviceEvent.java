package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-14
 * Time: 上午11:42
 * To change this template use File | Settings | File Templates.
 */
public class ProfileMobileDeviceEvent extends SystemEvent{

    private String uno;
    private String clientId;
    private String clientToken;
    private int platform;
    private String appId;

    public ProfileMobileDeviceEvent() {
        super(SystemEventType.CHECK_PROFILE_MOBILE_DEVICE);
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

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return uno.hashCode();
    }
}
