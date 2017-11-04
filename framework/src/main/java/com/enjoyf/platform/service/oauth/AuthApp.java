/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppSecret;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-7 下午12:02
 * Description:
 */
public class AuthApp implements Serializable {
    //the app primary key.
    private String appId;

    //the key.
    private String appKey;

    private String appName;

    //the app type
    private AuthAppType appType = AuthAppType.INTERNAL_CLIENT;

    private AppPlatform platform;


    //the app detail info.
    private AuthAppDetail appDetail;

    //
    private ValidStatus validStatus = ValidStatus.VALID;

    //the
    private Date createDate;
    private String createIp;

    //the status info.
    private ActStatus auditStatus = ActStatus.UNACT;

    //
    private Date auditDate;
    private String auditIp;

    private String profileKey;

    private int displayMy;//是否显示我的 模块 0=不显示  1=显示
    private int displayRed; //是否显示我的模块小红点  0=不显示 1=显示
    private Date modifyDate;// 更新时间

    private boolean defadOpen;
    private String defadUrl;
    private boolean shake_open = true;

    private AppSecret appSecret;


    private String depositCallback;

    private String gameKey;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public AuthAppType getAppType() {
        return appType;
    }

    public void setAppType(AuthAppType appType) {
        this.appType = appType;
    }

    public AuthAppDetail getAppDetail() {
        return appDetail;
    }

    public void setAppDetail(AuthAppDetail appDetail) {
        this.appDetail = appDetail;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
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

    public ActStatus getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(ActStatus auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditIp() {
        return auditIp;
    }

    public void setAuditIp(String auditIp) {
        this.auditIp = auditIp;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public AppPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(AppPlatform platform) {
        this.platform = platform;
    }

    public int getDisplayMy() {
        return displayMy;
    }

    public void setDisplayMy(int displayMy) {
        this.displayMy = displayMy;
    }

    public int getDisplayRed() {
        return displayRed;
    }

    public void setDisplayRed(int displayRed) {
        this.displayRed = displayRed;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return appId == null ? 0 : appId.hashCode();
    }

    public String getProfileKey() {
        return profileKey;
    }

    public void setProfileKey(String profileKey) {
        this.profileKey = profileKey;
    }

    public boolean getDefadOpen() {
        return defadOpen;
    }

    public void setDefadOpen(boolean defadOpen) {
        this.defadOpen = defadOpen;
    }

    public String getDefadUrl() {
        return defadUrl;
    }

    public void setDefadUrl(String defadUrl) {
        this.defadUrl = defadUrl;
    }

    public boolean isShake_open() {
        return shake_open;
    }

    public void setShake_open(boolean shake_open) {
        this.shake_open = shake_open;
    }

    public AppSecret getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(AppSecret appSecret) {
        this.appSecret = appSecret;
    }

    public String getDepositCallback() {
        return depositCallback;
    }

    public void setDepositCallback(String depositCallback) {
        this.depositCallback = depositCallback;
    }

    public String getGameKey() {
        return gameKey;
    }

    public void setGameKey(String gameKey) {
        this.gameKey = gameKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof AuthApp) {
            return appId.equals(((AuthApp) obj).getAppId());
        } else {
            return false;
        }
    }

}
