package com.enjoyf.platform.service.sync;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-4
 * Time: 下午3:03
 * To change this template use File | Settings | File Templates.
 */
public class ShareBaseInfo implements Serializable {
    private long shareId;

    private String shareKey;
    private String shareSource;
    private String directId;
    private Date expireDate;

    private ShareType shareType;
    private String displayStyle;

    private String createUserId;
    private Date createDate;
    private String createUserIp;

    private String lastModifyUserid;
    private Date lastModifyDate;
    private String lastModifyUserIp;

    private ActStatus removeStatus = ActStatus.UNACT;

    private ShareRewardType shareRewardType = ShareRewardType.getByValue(ShareRewardType.NONE);
    private int shareRewardPoint;
    private long shareRewardId;


    public Long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public String getShareSource() {
        return shareSource;
    }

    public void setShareSource(String shareSource) {
        this.shareSource = shareSource;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public ShareType getShareType() {
        return shareType;
    }

    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }

    public ShareRewardType getShareRewardType() {
        return shareRewardType;
    }

    public void setShareRewardType(ShareRewardType shareRewardType) {
        this.shareRewardType = shareRewardType;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserIp() {
        return createUserIp;
    }

    public void setCreateUserIp(String createUserIp) {
        this.createUserIp = createUserIp;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public int getShareRewardPoint() {
        return shareRewardPoint;
    }

    public void setShareRewardPoint(int shareRewardPoint) {
        this.shareRewardPoint = shareRewardPoint;
    }

    public long getShareRewardId() {
        return shareRewardId;
    }

    public void setShareRewardId(long shareRewardId) {
        this.shareRewardId = shareRewardId;
    }

    public String getShareKey() {
        return shareKey;
    }

    public void setShareKey(String shareKey) {
        this.shareKey = shareKey;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getLastModifyUserid() {
        return lastModifyUserid;
    }

    public void setLastModifyUserid(String lastModifyUserid) {
        this.lastModifyUserid = lastModifyUserid;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getLastModifyUserIp() {
        return lastModifyUserIp;
    }

    public void setLastModifyUserIp(String lastModifyUserIp) {
        this.lastModifyUserIp = lastModifyUserIp;
    }

    public String getDisplayStyle() {
        return displayStyle;
    }

    public void setDisplayStyle(String displayStyle) {
        this.displayStyle = displayStyle;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
