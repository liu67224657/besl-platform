package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.usercenter.activityuser.ActivityActionType;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityObjectType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/10
 * Description:
 */
public class ActivityUserEvent extends SystemEvent {

    private String appkey;
    private String subkey = "def";
    private String profileId;
    private String uno;
    private ActivityActionType actionType;
    private ActivityObjectType objectType;
    private String objectId;
    private Date actionTime;


    public ActivityUserEvent() {
        super(SystemEventType.ACTIVITY_USER);
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSubkey() {
        return subkey;
    }

    public void setSubkey(String subkey) {
        this.subkey = subkey;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public ActivityActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActivityActionType actionType) {
        this.actionType = actionType;
    }

    public ActivityObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ActivityObjectType objectType) {
        this.objectType = objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}