package com.enjoyf.platform.service.point.pointwall;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by tonydiao on 2014/11/27.
 * <p/>
 * 关联关系表
 */
public class PointwallWallApp implements Serializable {

    private long wallAppId;
    private long appId;
    private String appkey;
    private int platform;
    private int displayOrder;
    private int hotStatus;
    private int pointAmount;

    //todo 为什么不是枚举类？为什么还是大写的Status 用于tools后台管理,Status为'removed'或者'invalid'的app不会在wap页面显示
    private String Status;


    private String packageName;
    private String appName;
    private String appIcon;
    private String appDesc;
    private String downloadUrl;
    private String size;
    private String score;
    private String recommend;


    //用于wap页面显示,如果该app在当前clientid已经下载,其值为1,已经获得了积分,其值为2,未下载,其值为0
    private int installStatus;


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getInstallStatus() {
        return installStatus;
    }

    public void setInstallStatus(int installStatus) {
        this.installStatus = installStatus;
    }

    public int getHotStatus() {
        return hotStatus;
    }

    public void setHotStatus(int hotStatus) {
        this.hotStatus = hotStatus;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getWallAppId() {
        return wallAppId;
    }

    public void setWallAppId(long wallAppId) {
        this.wallAppId = wallAppId;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(int pointAmount) {
        this.pointAmount = pointAmount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}



