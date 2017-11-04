package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-19
 * Time: 上午11:16
 * To change this template use File | Settings | File Templates.
 */
public class AppTips implements Serializable{

    private long tipsId;
    private String tipsTitle;
    private String tipsDescription;
    private String tipsPic;
    private String tipsUrl;
    private AppTipsType tipsType = AppTipsType.DEFAULT;

    private String appId;
    private int platform = AppPlatform.CLIENT.getCode();
    private Date updateTime;

    private ActStatus removeStatus = ActStatus.UNACT;
    private Date createDate;
    private String createIp;
    private String createUserId;
    private Date modifyDate;
    private String modifyIp;
    private String modifyUserId;

    public long getTipsId() {
        return tipsId;
    }

    public void setTipsId(long tipsId) {
        this.tipsId = tipsId;
    }

    public String getTipsTitle() {
        return tipsTitle;
    }

    public void setTipsTitle(String tipsTitle) {
        this.tipsTitle = tipsTitle;
    }

    public String getTipsDescription() {
        return tipsDescription;
    }

    public void setTipsDescription(String tipsDescription) {
        this.tipsDescription = tipsDescription;
    }

    public String getTipsPic() {
        return tipsPic;
    }

    public void setTipsPic(String tipsPic) {
        this.tipsPic = tipsPic;
    }

    public String getTipsUrl() {
        return tipsUrl;
    }

    public void setTipsUrl(String tipsUrl) {
        this.tipsUrl = tipsUrl;
    }

    public AppTipsType getTipsType() {
        return tipsType;
    }

    public void setTipsType(AppTipsType tipsType) {
        this.tipsType = tipsType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyIp() {
        return modifyIp;
    }

    public void setModifyIp(String modifyIp) {
        this.modifyIp = modifyIp;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
