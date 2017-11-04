package com.enjoyf.platform.service.sync;

import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-8
 * Time: 下午1:28
 * To change this template use File | Settings | File Templates.
 */
public class ShareUserLog implements Serializable {

    private long shareLogId;
    private String profileUno;
    private LoginDomain loginDomain;
    private long shareBaseInfoId;
    private ShareType shareType;
    private String shareurl;


    private Date shareDate;
    private Date shareTime;

    public String getProfileUno() {
        return profileUno;
    }

    public void setProfileUno(String profileUno) {
        this.profileUno = profileUno;
    }

    public LoginDomain getLoginDomain() {
        return loginDomain;
    }

    public void setLoginDomain(LoginDomain loginDomain) {
        this.loginDomain = loginDomain;
    }

    public long getShareBaseInfoId() {
        return shareBaseInfoId;
    }

    public void setShareBaseInfoId(long shareBaseInfoId) {
        this.shareBaseInfoId = shareBaseInfoId;
    }

    public Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
    }

    public Date getShareTime() {
        return shareTime;
    }

    public void setShareTime(Date shareTime) {
        this.shareTime = shareTime;
    }

    public long getShareLogId() {
        return shareLogId;
    }

    public void setShareLogId(long shareLogId) {
        this.shareLogId = shareLogId;
    }

    public ShareType getShareType() {
        return shareType;
    }

    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }

    public String getShareurl() {
        return shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
