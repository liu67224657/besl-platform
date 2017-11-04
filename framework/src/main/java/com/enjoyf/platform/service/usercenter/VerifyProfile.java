package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/13
 */
public class VerifyProfile implements Serializable {
    private String profileId;
    private String nick;
    private String description;
    private Long verifyType;   //认证ID
    private String verifyTitle;//认证类型名称
    private String appkey;//用户来源
    private int askPoint = 0;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(Long verifyType) {
        this.verifyType = verifyType;
    }

    public static VerifyProfile getByJson(String json) {
        return new Gson().fromJson(json, VerifyProfile.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public int getAskPoint() {
        return askPoint;
    }

    public void setAskPoint(int askPoint) {
        this.askPoint = askPoint;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getVerifyTitle() {
        return verifyTitle;
    }

    public void setVerifyTitle(String verifyTitle) {
        this.verifyTitle = verifyTitle;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
