package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-9-17
 * Time: 上午11:46
 * To change this template use File | Settings | File Templates.
 */
public class ActivityTopMenu implements Serializable{

    private long activityTopMenuId;
    private String appKey;
    private Long channelId;
    private int platform;

    private String picUrl;
    private String linkUrl;
    private String menuName;
    private Integer menuType;
    private String menuDesc;
    private boolean isNew;
    private boolean isHot;
    private int displayOrder;
    private ValidStatus validStatus;

    private String createUserId;
    private String createIp;
    private Date createDate;
    private String lastModifyUserId;
    private String lastModifyIp;
    private Date lastModifyDate;

    /////////////////////////////////////
    private Integer redirectType;
    private AppTopMenuCategory category;

    private ActivityTopMenuParam param;


    public long getActivityTopMenuId() {
        return activityTopMenuId;
    }

    public void setActivityTopMenuId(long activityTopMenuId) {
        this.activityTopMenuId = activityTopMenuId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(boolean isHot) {
        this.isHot = isHot;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
    }

    public String getLastModifyIp() {
        return lastModifyIp;
    }

    public void setLastModifyIp(String lastModifyIp) {
        this.lastModifyIp = lastModifyIp;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public Integer getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(Integer redirectType) {
        this.redirectType = redirectType;
    }

    public AppTopMenuCategory getCategory() {
        return category;
    }

    public void setCategory(AppTopMenuCategory category) {
        this.category = category;
    }

    public ActivityTopMenuParam getParam() {
        return param;
    }

    public void setParam(ActivityTopMenuParam param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
