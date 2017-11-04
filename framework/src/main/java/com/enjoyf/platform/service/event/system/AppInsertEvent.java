package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.gameres.GameDeviceSet;
import com.enjoyf.platform.service.gameres.ResourceDomain;
import com.enjoyf.platform.service.content.ImageContentSet;

import java.util.Date;
import java.util.Set;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class AppInsertEvent extends SystemEvent {

    //通用
    private String resourceName;
    private ImageContentSet resourceIcon;
    private ImageContentSet resourceThumbimg;
    private String resourceDesc;
    private String resourceCategory;   //fps rpg
    private String resourceUrl;
    private GameDeviceSet deviceSet;//ipad iphone xbox ps3
    private String develop;//供应商或者产品提供商
    private String publishDate;//供应商或者产品提供商
    //for app
    private String referId;
    private String price; //价格
    private String resourceVersion;//版本
    private String fileSize;//大小
    private String language;//语言
    private Float currentRating;//当前评分
    private Integer currentRatingCount;
    private Float totalRating;//当前评分
    private Integer totalRatingCount;
    private Set<String> moreAppSets;
    private ImageContentSet screenShot;//

    //info
    private ActStatus removeStatus = ActStatus.UNACT;
    //
    private String createuno;
    private Date createdate;

    private ResourceDomain resourceDomain;
    private String iosDesc;//

    public AppInsertEvent() {
        super(SystemEventType.RESOURCE_IOS_INSERT);
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public ImageContentSet getResourceIcon() {
        return resourceIcon;
    }

    public void setResourceIcon(ImageContentSet resourceIcon) {
        this.resourceIcon = resourceIcon;
    }

    public ImageContentSet getResourceThumbimg() {
        return resourceThumbimg;
    }

    public void setResourceThumbimg(ImageContentSet resourceThumbimg) {
        this.resourceThumbimg = resourceThumbimg;
    }

    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    public String getResourceCategory() {
        return resourceCategory;
    }

    public void setResourceCategory(String resourceCategory) {
        this.resourceCategory = resourceCategory;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public GameDeviceSet getDeviceSet() {
        return deviceSet;
    }

    public void setDeviceSet(GameDeviceSet deviceSet) {
        this.deviceSet = deviceSet;
    }

    public String getDevelop() {
        return develop;
    }

    public void setDevelop(String develop) {
        this.develop = develop;
    }

    public String getReferId() {
        return referId;
    }

    public void setReferId(String referId) {
        this.referId = referId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
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

    public Float getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(Float currentRating) {
        this.currentRating = currentRating;
    }

    public Integer getCurrentRatingCount() {
        return currentRatingCount;
    }

    public void setCurrentRatingCount(Integer currentRatingCount) {
        this.currentRatingCount = currentRatingCount;
    }

    public Float getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Float totalRating) {
        this.totalRating = totalRating;
    }

    public Integer getTotalRatingCount() {
        return totalRatingCount;
    }

    public void setTotalRatingCount(Integer totalRatingCount) {
        this.totalRatingCount = totalRatingCount;
    }

    public Set<String> getMoreAppSets() {
        return moreAppSets;
    }

    public void setMoreAppSets(Set<String> moreAppSets) {
        this.moreAppSets = moreAppSets;
    }

    public ImageContentSet getScreenShot() {
        return screenShot;
    }

    public void setScreenShot(ImageContentSet screenShot) {
        this.screenShot = screenShot;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public String getCreateuno() {
        return createuno;
    }

    public void setCreateuno(String createuno) {
        this.createuno = createuno;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public ResourceDomain getResourceDomain() {
        return resourceDomain;
    }

    public void setResourceDomain(ResourceDomain resourceDomain) {
        this.resourceDomain = resourceDomain;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getIosDesc() {
        return iosDesc;
    }

    public void setIosDesc(String iosDesc) {
        this.iosDesc = iosDesc;
    }
}
