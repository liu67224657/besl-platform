package com.enjoyf.platform.util.apk;

import java.util.HashSet;
import java.util.Set;

import com.enjoyf.platform.util.reflect.ReflectUtil;

public class APKInfo {

    private String slug;
    private String versionName;
    private Integer versionCode;
    private Boolean anyDensity;
    private Boolean smallScreens;
    private Boolean normalScreens;
    private Boolean largeScreens;
    private Boolean resizeable;
    private Integer minSDKVersion = 1; //default value
    private long size;
    private String md5hash;
    private Set<String> permissions = new HashSet<String>();


    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }


    public String getSlug() {
        return slug;
    }


    public void setSlug(String slug) {
        this.slug = slug;
    }


    public String getVersionName() {
        return versionName;
    }


    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }


    public Integer getVersionCode() {
        return versionCode;
    }


    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }


    public Boolean getAnyDensity() {
        return anyDensity;
    }


    public void setAnyDensity(Boolean anyDensity) {
        this.anyDensity = anyDensity;
    }


    public Boolean getSmallScreens() {
        return smallScreens;
    }


    public void setSmallScreens(Boolean smallScreens) {
        this.smallScreens = smallScreens;
    }


    public Boolean getNormalScreens() {
        return normalScreens;
    }


    public void setNormalScreens(Boolean normalScreens) {
        this.normalScreens = normalScreens;
    }


    public Boolean getLargeScreens() {
        return largeScreens;
    }


    public void setLargeScreens(Boolean largeScreens) {
        this.largeScreens = largeScreens;
    }


    public Boolean getResizeable() {
        return resizeable;
    }


    public void setResizeable(Boolean resizeable) {
        this.resizeable = resizeable;
    }


    public Integer getMinSDKVersion() {
        return minSDKVersion;
    }


    public void setMinSDKVersion(Integer minSDKVersion) {
        this.minSDKVersion = minSDKVersion;
    }


    public Set<String> getPermissions() {
        return permissions;
    }


    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }


    public long getSize() {
        return size;
    }


    public void setSize(long size) {
        this.size = size;
    }


    public String getMd5hash() {
        return md5hash;
    }


    public void setMd5hash(String md5hash) {
        this.md5hash = md5hash;
    }

}
