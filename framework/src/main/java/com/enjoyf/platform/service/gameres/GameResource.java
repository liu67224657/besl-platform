package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ImageContentSet;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class GameResource implements Serializable {
    //pk
    private long resourceId;

    //通用
//    private int categoryId;
    private String resourceName;
    private String gameCode;
    private ImageContentSet resourceIcon;

    //游戏关系 游戏属性
    private GameRelationSet gameRelationSet = new GameRelationSet();
    private GameProperties gameProperties;

    //
    private ResourceImageSet icon;
     @Deprecated
    private ImageContentSet resourceThumbimg;
    private String resourceDesc;
//    private String resourceCategory;   //fps rpg
    private GameCategorySet categorySet;   //fps rpg

    @Deprecated
    private String resourceUrl;
    private String logoSize;

    //
    private GameStyleSet styleSet;
    private GameDeviceSet deviceSet;//ipad iphone xbox ps3

//    private String device;//ipad iphone xbox ps3
    private String develop;//供应商或者产品提供商

    //for gameres
    private String synonyms;     //近义词，根据近义词，也可以搜到这个对应的条目；\n多近义词中间用逗号分隔
    private String publishCompany;
    private String playerNumber;//游戏人数
    private String playTime;//游戏时间
    private String operationstatus; //运营状态
    private String publishDate;

    private String lastUpdateDate;//版本更新时间
    private String buyLink;//游戏购买链接

    //for app
    private String referId;
    private String price; //价格
    private String resourceVersion;//版本
    private String fileSize;//大小
    private String language;//语言
    private String systemRequired;//系统需求

    @Deprecated
    private Float currentRating;//当前评分
    @Deprecated
    private Integer currentRatingCount;
    private Float totalRating;//当前评分
    private Integer totalRatingCount;
    private Set<String> moreAppSets;
    private ImageContentSet screenShot;//

    //
    private Integer playingSum;
    private Integer playedSum;

    //info
    private ActStatus removeStatus = ActStatus.UNACT;

    //
    private String createUserid;
    private Date createDate;

    private String modifyUserid;
    private Date modifyDate;

    //
    private ResourceDomain resourceDomain = ResourceDomain.GAME;
    private GameResourceStatus resourceStatus;
    private String encodeKey;
    private String seoKeyWords;
    private String seoDescription;

    //
    private String eventDesc;
    private GameMediaScoreSet gameMediaScoreSet;//媒体评分

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
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

    public ResourceImageSet getIcon() {
        return icon;
    }

    public void setIcon(ResourceImageSet icon) {
        this.icon = icon;
    }

    @Deprecated
    public ImageContentSet getResourceThumbimg() {
        return resourceThumbimg;
    }

     @Deprecated
    public void setResourceThumbimg(ImageContentSet resourceThumbimg) {
        this.resourceThumbimg = resourceThumbimg;
    }

    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

//    public String getResourceCategory() {
//        return resourceCategory;
//    }
//
//    public void setResourceCategory(String resourceCategory) {
//        this.resourceCategory = resourceCategory;
//    }

    public GameCategorySet getCategorySet() {
        return categorySet;
    }

    public void setCategorySet(GameCategorySet categorySet) {
        this.categorySet = categorySet;
    }

    @Deprecated
    public String getResourceUrl() {
        return resourceUrl;
    }

    @Deprecated
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getLogoSize() {
        return logoSize;
    }

    public void setLogoSize(String logoSize) {
        this.logoSize = logoSize;
    }

    public GameStyleSet getStyleSet() {
        return styleSet;
    }

    public void setStyleSet(GameStyleSet styleSet) {
        this.styleSet = styleSet;
    }

    public GameDeviceSet getDeviceSet() {
        return deviceSet;
    }

    public void setDeviceSet(GameDeviceSet deviceSet) {
        this.deviceSet = deviceSet;
    }

//    public String getDevice() {
//        return device;
//    }
//
//    public void setDevice(String device) {
//        this.device = device;
//    }

    public String getDevelop() {
        return develop;
    }

    public void setDevelop(String develop) {
        this.develop = develop;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public String getPublishCompany() {
        return publishCompany;
    }

    public void setPublishCompany(String publishCompany) {
        this.publishCompany = publishCompany;
    }

    public String getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(String playerNumber) {
        this.playerNumber = playerNumber;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    public String getOperationstatus() {
        return operationstatus;
    }

    public void setOperationstatus(String operationstatus) {
        this.operationstatus = operationstatus;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
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

    public String getSystemRequired() {
        return systemRequired;
    }

    public void setSystemRequired(String systemRequired) {
        this.systemRequired = systemRequired;
    }

    @Deprecated
    public Float getCurrentRating() {
        return currentRating;
    }

    @Deprecated
    public void setCurrentRating(Float currentRating) {
        this.currentRating = currentRating;
    }

    @Deprecated
    public Integer getCurrentRatingCount() {
        return currentRatingCount;
    }

    @Deprecated
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

    public Integer getPlayingSum() {
        return playingSum;
    }

    public void setPlayingSum(Integer playingSum) {
        this.playingSum = playingSum;
    }

    public Integer getPlayedSum() {
        return playedSum;
    }

    public void setPlayedSum(Integer playedSum) {
        this.playedSum = playedSum;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public String getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getModifyUserid() {
        return modifyUserid;
    }

    public void setModifyUserid(String modifyUserid) {
        this.modifyUserid = modifyUserid;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public GameResourceStatus getResourceStatus() {
        return resourceStatus;
    }

    public void setResourceStatus(GameResourceStatus resourceStatus) {
        this.resourceStatus = resourceStatus;
    }

    public String getEncodeKey() {
        return encodeKey;
    }

    public void setEncodeKey(String encodeKey) {
        this.encodeKey = encodeKey;
    }

    public ResourceDomain getResourceDomain() {
        return this.resourceDomain;
    }

    public void setResourceDomain(ResourceDomain resourceDomain) {
        this.resourceDomain = resourceDomain;
    }

    public String getSeoKeyWords() {
        return seoKeyWords;
    }

    public void setSeoKeyWords(String seoKeyWords) {
        this.seoKeyWords = seoKeyWords;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

//    public GameResourceLinkSet getResourceLinkSet() {
//        return resourceLinkSet;
//    }
//
//    public void setResourceLinkSet(GameResourceLinkSet resourceLinkSet) {
//        this.resourceLinkSet = resourceLinkSet;
//    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public GameRelationSet getGameRelationSet() {
        return gameRelationSet;
    }

    public void setGameRelationSet(GameRelationSet gameRelationSet) {
        this.gameRelationSet = gameRelationSet;
    }

    public GameProperties getGameProperties() {
        return gameProperties;
    }

    public void setGameProperties(GameProperties gameProperties) {
        this.gameProperties = gameProperties;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getBuyLink() {
        return buyLink;
    }

    public void setBuyLink(String buyLink) {
        this.buyLink = buyLink;
    }

    public GameMediaScoreSet getGameMediaScoreSet() {
        return gameMediaScoreSet;
    }

    public void setGameMediaScoreSet(GameMediaScoreSet gameMediaScoreSet) {
        this.gameMediaScoreSet = gameMediaScoreSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameResource that = (GameResource) o;

        if (resourceId != that.resourceId) return false;
        if (resourceDomain != null ? !resourceDomain.equals(that.resourceDomain) : that.resourceDomain != null)
            return false;
        if (resourceName != null ? !resourceName.equals(that.resourceName) : that.resourceName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (resourceId ^ (resourceId >>> 32));
        result = 31 * result + (resourceName != null ? resourceName.hashCode() : 0);
        result = 31 * result + (resourceDomain != null ? resourceDomain.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
