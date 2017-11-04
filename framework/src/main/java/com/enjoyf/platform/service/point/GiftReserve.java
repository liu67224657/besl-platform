package com.enjoyf.platform.service.point;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-6-24
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class GiftReserve implements Serializable {
    private Long giftReserveId;
    private Long aid;
    private String uno;
    private String giftName;
    private Date createTime;
    private String createIp;
    private ValidStatus validStatus;
    private LoginDomain loginDomain;
    private String profileId;
    private String appkey;

    public Long getGiftReserveId() {
        return giftReserveId;
    }

    public void setGiftReserveId(Long giftReserveId) {
        this.giftReserveId = giftReserveId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
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

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public LoginDomain getLoginDomain() {
        return loginDomain;
    }

    public void setLoginDomain(LoginDomain loginDomain) {
        this.loginDomain = loginDomain;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    @Override
    public String toString() {
        return  ReflectUtil.fieldsToString(this);
    }
}
