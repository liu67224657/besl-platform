package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ericliu on 15/12/29.
 */
public class GameChannelConfig implements Serializable {
    private String configId; //md5(appkey+appPlatform+channel+version)
    private String appkey;
    private String gamekey;
    private AppPlatform appPlatform;
    private String channel;
    private String version;

    private boolean debug;
    private Date lastModifyTime;
    private String lastModifyUserId;
    private String lastModifyIp;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getGamekey() {
        return gamekey;
    }

    public void setGamekey(String gamekey) {
        this.gamekey = gamekey;
    }

    public AppPlatform getAppPlatform() {
        return appPlatform;
    }

    public void setAppPlatform(AppPlatform appPlatform) {
        this.appPlatform = appPlatform;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
    }

    public String getLastModifyIp() {
        return lastModifyIp;
    }

    public void setLastModifyIp(String lastModifyIp) {
        this.lastModifyIp = lastModifyIp;
    }


    public void calConfigId() {
        configId = calConfigId(appkey, appPlatform, channel, version);
    }

    public static String calConfigId(String appkey, AppPlatform appPlatform, String channel, String version) {
        return Md5Utils.md5(appkey + appPlatform.getCode() + channel + version);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
