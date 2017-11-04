package com.enjoyf.platform.service.timeline;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.gson.Gson;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserTimeline implements Serializable {
    private Long itemId;
    private String profileId;
    private String domain;
    private String type;
    private String destProfileid;
    private Long destId;
    private String extendBody;
    private TimeLineActionType actionType;
    private String linekey;
    private Timestamp createTime;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDestProfileid() {
        return destProfileid;
    }

    public void setDestProfileid(String destProfileid) {
        this.destProfileid = destProfileid;
    }

    public Long getDestId() {
        return destId;
    }

    public void setDestId(Long destId) {
        this.destId = destId;
    }

    public String getExtendBody() {
        return extendBody;
    }

    public void setExtendBody(String extendBody) {
        this.extendBody = extendBody;
    }

    public TimeLineActionType getActionType() {
        return actionType;
    }

    public void setActionType(TimeLineActionType actionType) {
        this.actionType = actionType;
    }

    public String getLinekey() {
        return linekey;
    }

    public void setLinekey(String linekey) {
        this.linekey = linekey;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    public static UserTimeline toObject(String jsonStr) {
        return new Gson().fromJson(jsonStr, UserTimeline.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}


