package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-27
 * Time: 上午10:35
 * To change this template use File | Settings | File Templates.
 */
public class SocialWatermark implements Serializable{

    private long watermarkId;
    private String title;
    private String description;
    private String iosIcon;
    private String androidIcon;
    private String iosPic;
    private String androidPic;
    private SubscriptType subscriptType = SubscriptType.NULL;
    private Subscript subscript;
    private ValidStatus removeStatus = ValidStatus.INVALID;
    private int displayOrder;
    private int useSum = 0;

    private Date createDate;
    private String createIp;
    private String createUserId;
    private Date modifyDate;
    private String modifyIp;
    private String modifyUserId;

    private long activityId;

    public long getWatermarkId() {
        return watermarkId;
    }

    public void setWatermarkId(long watermarkId) {
        this.watermarkId = watermarkId;
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

    public String getIosPic() {
        return iosPic;
    }

    public void setIosPic(String iosPic) {
        this.iosPic = iosPic;
    }

    public String getAndroidPic() {
        return androidPic;
    }

    public void setAndroidPic(String androidPic) {
        this.androidPic = androidPic;
    }

    public Subscript getSubscript() {
        return subscript;
    }

    public void setSubscript(Subscript subscript) {
        this.subscript = subscript;
    }

    public ValidStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ValidStatus removeStatus) {
        this.removeStatus = removeStatus;
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

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyIp() {
        return modifyIp;
    }

    public void setModifyIp(String modifyIp) {
        this.modifyIp = modifyIp;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public int getUseSum() {
        return useSum;
    }

    public void setUseSum(int useSum) {
        this.useSum = useSum;
    }

    public SubscriptType getSubscriptType() {
        return subscriptType;
    }

    public void setSubscriptType(SubscriptType subscriptType) {
        this.subscriptType = subscriptType;
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

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
