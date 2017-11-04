package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.text.TextJsonItem;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-12
 * Time: 下午2:51
 * To change this template use File | Settings | File Templates.
 */
public class Activity implements Serializable {
    private long activityId;
    private long gameDbId;
    private String activitySubject;

    //服文本
    private String activityDesc;
    private List<TextJsonItem> textJsonItemsList;

    //积分兑换标题描述
    private String subDesc;

    private String activityPicUrl;

    private ActivityType activityType;
    private ActivityRelations activityRelations;

    private String gameName;
    private String gameIconUrl;
    private String gameProduct;
    private String gameUrl;

    private String qrUrl;//一对多的下载地址
    private String iosDownloadUrl;
    private double iosSizeMB;
    private String androidDownloadUrl;
    private double androidSizeMB;

    private Date startTime;
    private Date endTime;
    private MobileExclusive isWeixinExclusive;
    private int hotActivity; //是否是热门活动 0=不是 1=是
    private ActStatus removeStatus = ActStatus.UNACT;

    private String createUserId;
    private Date createTime;
    private String createIp;
    private String lastModifyUserId;
    private Date lastModifyTime;
    private String lastModifyIp;
    private ActivityCategoryType category;
    private int displayOrder;
    private Date eventDate;
    private long shareId;//主动分享Id

    private ActivityGoodsCategory goodsCategory;

    private String firstLetter;
    private ActivityPlatform activityPlatform;
    private ActivityCooperation cooperation;
    private GoodsActionType goodsActionType = GoodsActionType.WWW;
    private String bgPic;
    private int reserveType;//预订状态 0=可兑换 1=可预订

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public String getActivitySubject() {
        return activitySubject;
    }

    public void setActivitySubject(String activitySubject) {
        this.activitySubject = activitySubject;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }


    public List<TextJsonItem> getTextJsonItemsList() {
        return textJsonItemsList;
    }

    public void setTextJsonItemsList(List<TextJsonItem> textJsonItemsList) {
        this.textJsonItemsList = textJsonItemsList;
    }

    public String getActivityPicUrl() {
        return activityPicUrl;
    }

    public void setActivityPicUrl(String activityPicUrl) {
        this.activityPicUrl = activityPicUrl;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public ActivityRelations getActivityRelations() {
        return activityRelations;
    }

    public void setActivityRelations(ActivityRelations activityRelations) {
        this.activityRelations = activityRelations;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameIconUrl() {
        return gameIconUrl;
    }

    public void setGameIconUrl(String gameIconUrl) {
        this.gameIconUrl = gameIconUrl;
    }

    public String getGameProduct() {
        return gameProduct;
    }

    public void setGameProduct(String gameProduct) {
        this.gameProduct = gameProduct;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public String getIosDownloadUrl() {
        return iosDownloadUrl;
    }

    public void setIosDownloadUrl(String iosDownloadUrl) {
        this.iosDownloadUrl = iosDownloadUrl;
    }

    public double getIosSizeMB() {
        return iosSizeMB;
    }

    public void setIosSizeMB(double iosSizeMB) {
        this.iosSizeMB = iosSizeMB;
    }

    public String getAndroidDownloadUrl() {
        return androidDownloadUrl;
    }

    public void setAndroidDownloadUrl(String androidDownloadUrl) {
        this.androidDownloadUrl = androidDownloadUrl;
    }

    public double getAndroidSizeMB() {
        return androidSizeMB;
    }

    public void setAndroidSizeMB(double androidSizeMB) {
        this.androidSizeMB = androidSizeMB;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getLastModifyIp() {
        return lastModifyIp;
    }

    public void setLastModifyIp(String lastModifyIp) {
        this.lastModifyIp = lastModifyIp;
    }

    public ActivityCategoryType getCategory() {
        return category;
    }

    public void setCategory(ActivityCategoryType category) {
        this.category = category;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getSubDesc() {
        return subDesc;
    }

    public void setSubDesc(String subDesc) {
        this.subDesc = subDesc;
    }

    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public ActivityGoodsCategory getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(ActivityGoodsCategory goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public ActivityPlatform getActivityPlatform() {
        return activityPlatform;
    }

    public void setActivityPlatform(ActivityPlatform activityPlatform) {
        this.activityPlatform = activityPlatform;
    }

    public ActivityCooperation getCooperation() {
        return cooperation;
    }

    public void setCooperation(ActivityCooperation cooperation) {
        this.cooperation = cooperation;
    }

    public long getGameDbId() {
        return gameDbId;
    }

    public void setGameDbId(long gameDbId) {
        this.gameDbId = gameDbId;
    }

    public MobileExclusive getWeixinExclusive() {
        return isWeixinExclusive;
    }

    public void setWeixinExclusive(MobileExclusive weixinExclusive) {
        isWeixinExclusive = weixinExclusive;
    }

    public int getHotActivity() {
        return hotActivity;
    }

    public void setHotActivity(int hotActivity) {
        this.hotActivity = hotActivity;
    }

    public GoodsActionType getGoodsActionType() {
        return goodsActionType;
    }

    public void setGoodsActionType(GoodsActionType goodsActionType) {
        this.goodsActionType = goodsActionType;
    }

    public String getBgPic() {
        return bgPic;
    }

    public void setBgPic(String bgPic) {
        this.bgPic = bgPic;
    }

    public int getReserveType() {
        return reserveType;
    }

    public void setReserveType(int reserveType) {
        this.reserveType = reserveType;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
