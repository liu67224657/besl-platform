package com.enjoyf.platform.webapps.common.multimedia.appFetcher;

import com.enjoyf.platform.service.gameres.GameDevice;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Set;

/**
 * <p/>
 * Description:从app网站得到的封装类用于页面显示
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class IOSAppEntity implements Serializable {
    private String appid;
    private String appName;
    private String icon;
    private String price;
    private String fileSize;
    private String appCategory;
    private String publishDate;
    private String appVersion;
    private String desc;

    private String language;
    private String develop;
    private String resourceUrl;
    private Set<GameDevice> deviceSet;

    private float currentRating;
    private int currentRatingCount;
    private float totalRating;
    private int totalRatingCount;

    private Set<String> moreAppSets;
    private Set<String> screenShot;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAppCategory() {
        return appCategory;
    }

    public void setAppCategory(String appCategory) {
        this.appCategory = appCategory;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDevelop() {
        return develop;
    }

    public void setDevelop(String develop) {
        this.develop = develop;
    }

    public Set<String> getMoreAppSets() {
        return moreAppSets;
    }

    public void setMoreAppSets(Set<String> moreAppSets) {
        this.moreAppSets = moreAppSets;
    }

    public Set<String> getScreenShot() {
        return screenShot;
    }

    public void setScreenShot(Set<String> screenShot) {
        this.screenShot = screenShot;
    }

    public Set<GameDevice> getDeviceSet() {
        return deviceSet;
    }

    public void setDeviceSet(Set<GameDevice> deviceSet) {
        this.deviceSet = deviceSet;
    }

    public float getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(float currentRating) {
        this.currentRating = currentRating;
    }

    public int getCurrentRatingCount() {
        return currentRatingCount;
    }

    public void setCurrentRatingCount(int currentRatingCount) {
        this.currentRatingCount = currentRatingCount;
    }

    public float getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(float totalRating) {
        this.totalRating = totalRating;
    }

    public int getTotalRatingCount() {
        return totalRatingCount;
    }

    public void setTotalRatingCount(int totalRatingCount) {
        this.totalRatingCount = totalRatingCount;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
