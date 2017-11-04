package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-9
 * Time: 上午10:07
 * To change this template use File | Settings | File Templates.
 */
public class SocialTag implements Serializable {

    private long tagId;
    private String title;
    private String description;
    private String icon;
    //兴趣、位置
    private SocialTagType tagType;
    //管理员、自定义
    private SocialTagCategory tagCategory;

    private int displayOrder;

    private boolean isHot = false;

    private int useSum = 1;
    private int replySum;
    private int agreeSum;
    private int giftSum;

    private ValidStatus removeStatus = ValidStatus.VALID;

    private Date createDate;
    private String createIp;
    private String createUserId;

    private Date lastModifyDate;
    private String lastModifyIp;
    private String lastModifyUserId;

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public SocialTagType getTagType() {
        return tagType;
    }

    public void setTagType(SocialTagType tagType) {
        this.tagType = tagType;
    }

    public SocialTagCategory getTagCategory() {
        return tagCategory;
    }

    public void setTagCategory(SocialTagCategory tagCategory) {
        this.tagCategory = tagCategory;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public int getUseSum() {
        return useSum;
    }

    public void setUseSum(int useSum) {
        this.useSum = useSum;
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

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
