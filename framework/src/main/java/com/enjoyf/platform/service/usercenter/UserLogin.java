package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ericliu on 14/10/21.
 */
public class UserLogin implements Serializable{

    private String loginId;
    private String loginKey;
    private String loginPassword;
    private String passwdTime; //用户修改密码时候的timestamp，创建时候=createTiem，老用户=0 用于密码校验
    private String loginName;
    private LoginDomain loginDomain;
    private String uno;
    private Date createTime;
    private String createIp;

    private TokenInfo tokenInfo;
    private String authCode;
    private Date authTime;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginKey() {
        return loginKey;
    }

    public void setLoginKey(String loginKey) {
        this.loginKey = loginKey;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public LoginDomain getLoginDomain() {
        return loginDomain;
    }

    public void setLoginDomain(LoginDomain loginDomain) {
        this.loginDomain = loginDomain;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
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

    public String getPasswdTime() {
        return passwdTime;
    }

    public void setPasswdTime(String passwdTime) {
        this.passwdTime = passwdTime;
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public Date getAuthTime() {
        return authTime;
    }

    public void setAuthTime(Date authTime) {
        this.authTime = authTime;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
