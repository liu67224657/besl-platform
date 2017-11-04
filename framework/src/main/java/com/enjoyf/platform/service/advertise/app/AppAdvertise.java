package com.enjoyf.platform.service.advertise.app;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-6-7
 * Time: 下午12:03
 * To change this template use File | Settings | File Templates.
 */
public class AppAdvertise implements Serializable {

    private long advertiseId;
    private String advertiseName;
    private String advertiseDesc;
    private String url;
    private String picUrl1;   //ios 4
    private String picUrl2;   //ios 5 、安卓

    private Date createTime;
    private String createUser;
    private String creatIp;
    private AppPlatform appPlatform = AppPlatform.IOS;
    private ActStatus removeStatus = ActStatus.UNACT;
    private Integer appAdvertiseRedirectType = AppAdvertiseRedirectType.WEBVIEW.getCode();

    private AppAdvertiseType appAdvertiseType = AppAdvertiseType.DEFAULT;
    private String extstring;//扩展字段

    //non db
    private Date startDate;
    private Date endDate;
    private long publishId;
    private PublishParam publishParam;

    public long getAdvertiseId() {
        return advertiseId;
    }

    public void setAdvertiseId(long advertiseId) {
        this.advertiseId = advertiseId;
    }

    public String getAdvertiseName() {
        return advertiseName;
    }

    public void setAdvertiseName(String advertiseName) {
        this.advertiseName = advertiseName;
    }

    public String getAdvertiseDesc() {
        return advertiseDesc;
    }

    public void setAdvertiseDesc(String advertiseDesc) {
        this.advertiseDesc = advertiseDesc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicUrl1() {
        return picUrl1;
    }

    public void setPicUrl1(String picUrl1) {
        this.picUrl1 = picUrl1;
    }

    public String getPicUrl2() {
        return picUrl2;
    }

    public void setPicUrl2(String picUrl2) {
        this.picUrl2 = picUrl2;
    }

    public AppPlatform getAppPlatform() {
        return appPlatform;
    }

    public void setAppPlatform(AppPlatform appPlatform) {
        this.appPlatform = appPlatform;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreatIp() {
        return creatIp;
    }

    public void setCreatIp(String creatIp) {
        this.creatIp = creatIp;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getPublishId() {
        return publishId;
    }

    public void setPublishId(long publishId) {
        this.publishId = publishId;
    }

    public PublishParam getPublishParam() {
        return publishParam;
    }

    public void setPublishParam(PublishParam publishParam) {
        this.publishParam = publishParam;
    }

    public Integer getAppAdvertiseRedirectType() {
        return appAdvertiseRedirectType;
    }

    public void setAppAdvertiseRedirectType(Integer appAdvertiseRedirectType) {
        this.appAdvertiseRedirectType = appAdvertiseRedirectType;
    }

    public AppAdvertiseType getAppAdvertiseType() {
        return appAdvertiseType;
    }

    public void setAppAdvertiseType(AppAdvertiseType appAdvertiseType) {
        this.appAdvertiseType = appAdvertiseType;
    }

    public String getExtstring() {
        return extstring;
    }

    public void setExtstring(String extstring) {
        this.extstring = extstring;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
