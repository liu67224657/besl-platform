package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by pengxu on 2017/3/22.
 */
public class UserCollect implements Serializable {
    private long id;
    private String profileId;
    private CollectType collectType;
    private Date createDate;
    private String appkey;
    private long contentId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public CollectType getCollectType() {
        return collectType;
    }

    public void setCollectType(CollectType collectType) {
        this.collectType = collectType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
