package com.enjoyf.platform.service.message;

import com.enjoyf.platform.service.joymeapp.AppEnterpriserType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppPushChannel;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-14
 * Time: 上午11:05
 * To change this template use File | Settings | File Templates.
 */
public class ClientDevice implements Serializable {

    private long deviceId;
    private String uno;
    private String clientId;
    private String clientToken;
    private AppPlatform platform;
    private String appId;
    private long lastMsgId;
    private AppPushChannel pushChannel;
    private String appVersion;
    private String appChannel;
    private String tags;
    private String advId;
    private String ip;
	private AppEnterpriserType enterpriserType;
    private String profileId;

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

    public AppPushChannel getPushChannel() {
        return pushChannel;
    }

    public void setPushChannel(AppPushChannel pushChannel) {
        this.pushChannel = pushChannel;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppChannel() {
        return appChannel;
    }

    public void setAppChannel(String appChannel) {
        this.appChannel = appChannel;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAdvId() {
        return advId;
    }

    public void setAdvId(String advId) {
        this.advId = advId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

	public AppEnterpriserType getEnterpriserType() {
		return enterpriserType;
	}

	public void setEnterpriserType(AppEnterpriserType enterpriserType) {
		this.enterpriserType = enterpriserType;
	}

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
