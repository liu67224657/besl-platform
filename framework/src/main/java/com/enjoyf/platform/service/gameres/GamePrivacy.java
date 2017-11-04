package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 游戏权限对象
 * User: ericliu
 * Date: 13-4-25
 * Time: 上午9:16
 * To change this template use File | Settings | File Templates.
 */
public class GamePrivacy implements Serializable {
    //squence．
    private long resourceId;

    private ResourceDomain resourceDomain;

    //the game level．
    private GamePrivacyLevel privacyLevel;

    //
    private String uno;

    //create info．
    private Date createDate;
    private String createUserid;

    //last update info．
    private Date updateDate;
    private String updateUserid;


    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceDomain getResourceDomain() {
        return resourceDomain;
    }

    public void setResourceDomain(ResourceDomain resourceDomain) {
        this.resourceDomain = resourceDomain;
    }

    public GamePrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(GamePrivacyLevel privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUserid() {
        return updateUserid;
    }

    public void setUpdateUserid(String updateUserid) {
        this.updateUserid = updateUserid;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
