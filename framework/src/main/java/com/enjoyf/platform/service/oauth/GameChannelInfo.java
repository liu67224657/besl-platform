package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ericliu on 15/12/29.
 */
public class GameChannelInfo implements Serializable {
    private String infoId; //md5(appkey+appPlatform+channel)
    private String appkey;
    private String gamekey;
    private AppPlatform appPlatform;
    private String channel;

    private String channelKey;//渠道APPKEY
    private String channelAppId; //渠道AppId
    private String channelSecr;
    private String publickey;
    private String privatekey;

    private Date lastModifyTime;
    private String lastModifyUserId;
    private String lastModifyIp;

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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannelKey() {
        return channelKey;
    }

    public void setChannelKey(String channelKey) {
        this.channelKey = channelKey;
    }

    public String getChannelSecr() {
        return channelSecr;
    }

    public void setChannelSecr(String channelSecr) {
        this.channelSecr = channelSecr;
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }


    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
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

    public AppPlatform getAppPlatform() {
        return appPlatform;
    }

    public void setAppPlatform(AppPlatform appPlatform) {
        this.appPlatform = appPlatform;
    }

    public String getChannelAppId() {
        return channelAppId;
    }

    public void setChannelAppId(String channelAppId) {
        this.channelAppId = channelAppId;
    }

    public void calInfoId() {
        infoId = calInfoId(appkey, appPlatform, channel);
    }

    public static String calInfoId(String appkey, AppPlatform appPlatform, String channel) {
        return Md5Utils.md5(appkey + appPlatform.getCode() + channel);
    }


    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
