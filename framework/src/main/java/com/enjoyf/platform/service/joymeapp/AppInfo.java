package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-24
 * Time: 下午12:06
 * To change this template use File | Settings | File Templates.
 */
public class AppInfo implements Serializable {
    private long appInfoId;
    private String appName;
    private String appKey;
    private String packageName;
    private AppInfoType appInfoType;
    private AppPlatform appPlatform;
    private int recommend;
    private Date createDate;
    private String createUserId;
    private String createIp;
    private boolean isSearch;
    private boolean isCommplete;

    private String modifyIp;
    private String modifyUserId;
    private Date modifyDate;
    private ActStatus removeStatus = ActStatus.UNACT;

    //礼包中心开关
    private String version;   //版本号
    private boolean hasGift = false;  //是否有礼包中心
    private AppChannelType channelType;  //渠道

    public long getAppInfoId() {
        return appInfoId;
    }

    public void setAppInfoId(long appInfoId) {
        this.appInfoId = appInfoId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public AppInfoType getAppInfoType() {
        return appInfoType;
    }

    public void setAppInfoType(AppInfoType appInfoType) {
        this.appInfoType = appInfoType;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public boolean getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }

    public boolean getIsCommplete() {
        return isCommplete;
    }

    public void setIsCommplete(boolean isCommplete) {
        this.isCommplete = isCommplete;
    }

    public AppPlatform getAppPlatform() {
        return appPlatform;
    }

    public void setAppPlatform(AppPlatform appPlatform) {
        this.appPlatform = appPlatform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean getHasGift() {
        return hasGift;
    }

    public void setHasGift(boolean hasGift) {
        this.hasGift = hasGift;
    }

    public AppChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(AppChannelType channelType) {
        this.channelType = channelType;
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

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
