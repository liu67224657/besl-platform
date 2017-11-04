package com.enjoyf.webapps.joyme.dto.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-7-30 下午2:02
 * Description:
 */
public class AppResourceDTO {
    private long appId;
    private String appName;
    private String pkgname;
    private String desc;
    private String icon;
    private String size;

    private int displayOrder;
    private int lastDisplayOrder;

     private AppCategoryDTO appCategory;
    private List<String> tags;
    private List<DownLoadLinkDTO> downLoadUrls;

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public AppCategoryDTO getAppCategory() {
        return appCategory;
    }

    public void setAppCategory(AppCategoryDTO appCategory) {
        this.appCategory = appCategory;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getLastDisplayOrder() {
        return lastDisplayOrder;
    }

    public void setLastDisplayOrder(int lastDisplayOrder) {
        this.lastDisplayOrder = lastDisplayOrder;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<DownLoadLinkDTO> getDownLoadUrls() {
        return downLoadUrls;
    }

    public void setDownLoadUrls(List<DownLoadLinkDTO> downLoadUrls) {
        this.downLoadUrls = downLoadUrls;
    }

    public String getPkgname() {
        return pkgname;
    }

    public void setPkgname(String pkgname) {
        this.pkgname = pkgname;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
