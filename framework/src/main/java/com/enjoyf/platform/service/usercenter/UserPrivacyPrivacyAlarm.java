package com.enjoyf.platform.service.usercenter;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by ericliu on 14/10/22.
 * 提醒设置
 */
public class UserPrivacyPrivacyAlarm implements Serializable {
    //0关闭 1开启
    private String userat;
    private String comment;
    private String agreement;
    private String follow;
    private String systeminfo;

    public String getUserat() {
        return userat;
    }

    public void setUserat(String userat) {
        this.userat = userat;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getSysteminfo() {
        return systeminfo;
    }

    public void setSysteminfo(String systeminfo) {
        this.systeminfo = systeminfo;
    }

    public static UserPrivacyPrivacyAlarm getByJson(String json) {
        return new Gson().fromJson(json, UserPrivacyPrivacyAlarm.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
