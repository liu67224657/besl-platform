package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-9
 * Time: 上午11:33
 * To change this template use File | Settings | File Templates.
 */
public class SocialActivity implements Serializable {

    private long activityId;
    private String title;
    private String description;
    //水印弹层图
    private String iosIcon;
    private String androidIcon;
    //广场图
    private String iosSmallPic;
    private String androidSmallPic;
    private String iosBigPic;
    private String androidBigPic;

    private int displayOrder;

    //使用数
    private int useSum;
    //评论数
    private int replySum;
    //点赞数
    private int agreeSum;
    //礼物数
    private int giftSum;

    private ValidStatus removeStatus = ValidStatus.INVALID;

    private SubscriptType subscriptType = SubscriptType.NULL;
    private Subscript subscript;

    private long shareId;

    //奖品 json
    private SocialAwardSet awardSet;

    private Date createDate;
    private String createIp;
    private String createUserId;

    private Date lastModifyDate;
    private String lastModifyIp;
    private String lastModifyUserId;

    private String bindStatus = ActStatus.UNACT.getCode();

    private SocialActivityType activityType=SocialActivityType.CONTENT;

    private int totals;

    private int redirectType = 1; // 0---WEBVIEW,1---NATIVE

    private String redirectUrl;

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIosIcon() {
        return iosIcon;
    }

    public void setIosIcon(String iosIcon) {
        this.iosIcon = iosIcon;
    }

    public String getAndroidIcon() {
        return androidIcon;
    }

    public void setAndroidIcon(String androidIcon) {
        this.androidIcon = androidIcon;
    }

    public String getIosSmallPic() {
        return iosSmallPic;
    }

    public void setIosSmallPic(String iosSmallPic) {
        this.iosSmallPic = iosSmallPic;
    }

    public String getAndroidSmallPic() {
        return androidSmallPic;
    }

    public void setAndroidSmallPic(String androidSmallPic) {
        this.androidSmallPic = androidSmallPic;
    }

    public String getIosBigPic() {
        return iosBigPic;
    }

    public void setIosBigPic(String iosBigPic) {
        this.iosBigPic = iosBigPic;
    }

    public String getAndroidBigPic() {
        return androidBigPic;
    }

    public void setAndroidBigPic(String androidBigPic) {
        this.androidBigPic = androidBigPic;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getUseSum() {
        return useSum;
    }

    public void setUseSum(int useSum) {
        this.useSum = useSum;
    }

    public ValidStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ValidStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Subscript getSubscript() {
        return subscript;
    }

    public void setSubscript(Subscript subscript) {
        this.subscript = subscript;
    }

    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getLastModifyIp() {
        return lastModifyIp;
    }

    public void setLastModifyIp(String lastModifyIp) {
        this.lastModifyIp = lastModifyIp;
    }

    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
    }

    public int getReplySum() {
        return replySum;
    }

    public void setReplySum(int replySum) {
        this.replySum = replySum;
    }

    public int getAgreeSum() {
        return agreeSum;
    }

    public void setAgreeSum(int agreeSum) {
        this.agreeSum = agreeSum;
    }

    public int getGiftSum() {
        return giftSum;
    }

    public void setGiftSum(int giftSum) {
        this.giftSum = giftSum;
    }

    public SocialAwardSet getAwardSet() {
        return awardSet;
    }

    public void setAwardSet(SocialAwardSet awardSet) {
        this.awardSet = awardSet;
    }

    public String getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(String bindStatus) {
        this.bindStatus = bindStatus;
    }

    public SubscriptType getSubscriptType() {
        return subscriptType;
    }

    public void setSubscriptType(SubscriptType subscriptType) {
        this.subscriptType = subscriptType;
    }

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public SocialActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(SocialActivityType activityType) {
        this.activityType = activityType;
    }

    public int getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(int redirectType) {
        this.redirectType = redirectType;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
