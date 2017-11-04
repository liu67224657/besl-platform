package com.enjoyf.platform.util.joymeapp;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Created by zhimingli
 * Date: 2014/12/18
 * Time: 14:49
 */
public class AppClientCommonParam implements Serializable {
    private int platform;//客户端平台(平台 0--ios,1--android)
    private String version;//客户端当前版本
    private String appkey;//该应用appkey(服务器端该应用唯一ID)
    private String clientid;//设备ID
    private String channelid;//渠道的code值
    private String token;
    private String uno;
    private String uid;

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
