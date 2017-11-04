package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ericliu on 14/10/21.
 */
public class Token implements Serializable {

    private String token;
    private TokenType tokenType;
    private String profileKey;
    private int tokenExpires;
    private String uno;
    private long uid;
    private String profileId;
    private Date createTime;
    private String createIp;
    private String request_parameter;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getProfileKey() {
        return profileKey;
    }

    public void setProfileKey(String profileKey) {
        this.profileKey = profileKey;
    }

    public int getTokenExpires() {
        return tokenExpires;
    }

    public void setTokenExpires(int tokenExpires) {
        this.tokenExpires = tokenExpires;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
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

    public String getRequest_parameter() {
        return request_parameter;
    }

    public void setRequest_parameter(String request_parameter) {
        this.request_parameter = request_parameter;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
