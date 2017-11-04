package com.enjoyf.platform.service.usercenter;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

public class UserPrivacy implements Serializable {
    private String profileId;
    private UserPrivacyPrivacyAlarm alarmSetting;
    private UserPrivacyFunction functionSetting;
    private Date createtime;
    private String createip;
    private Date updatetime;
    private String updateip;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public UserPrivacyPrivacyAlarm getAlarmSetting() {
        return alarmSetting;
    }

    public void setAlarmSetting(UserPrivacyPrivacyAlarm alarmSetting) {
        this.alarmSetting = alarmSetting;
    }

    public UserPrivacyFunction getFunctionSetting() {
        return functionSetting;
    }

    public void setFunctionSetting(UserPrivacyFunction functionSetting) {
        this.functionSetting = functionSetting;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getCreateip() {
        return createip;
    }

    public void setCreateip(String createip) {
        this.createip = createip;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdateip() {
        return updateip;
    }

    public void setUpdateip(String updateip) {
        this.updateip = updateip;
    }

    public static UserPrivacy getByJson(String json) {
        return new Gson().fromJson(json, UserPrivacy.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}


