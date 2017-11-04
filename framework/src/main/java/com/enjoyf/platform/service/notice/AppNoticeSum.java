package com.enjoyf.platform.service.notice;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by ericliu on 16/3/12.
 */
public class AppNoticeSum implements Serializable {
    private String profileId;//玩家id
    private String appkey;//appkey
    private String type;//app 约定好的type
    private String value;//数字
    private int dtype;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getDtype() {
        return dtype;
    }

    public void setDtype(int dtype) {
        this.dtype = dtype;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static AppNoticeSum toObject(String jsonStr) {
        return new Gson().fromJson(jsonStr, AppNoticeSum.class);
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
