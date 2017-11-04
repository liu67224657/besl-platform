/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-7 下午1:22
 * Description:
 */
public class AuthToken implements Serializable {
    //the unique key.
    private String token;

    //
    private AuthTokenType tokenType;

    //the related crediate.
    private String credentialId;

    //
    private String appId;

    //
    private String refreshToken;

    //
    private Date expireDate;

    //
    private Date createDate;

    //
    private int accessTimes = 0;

    ///////////////////////////////////////////

    public AuthToken() {
    }

    public AuthToken(String token) {
        this.token = token;
    }

    ///////////////////////////////////////////
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AuthTokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(AuthTokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getAccessTimes() {
        return accessTimes;
    }

    public void setAccessTimes(int accessTimes) {
        this.accessTimes = accessTimes;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return token == null ? 0 : token.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof AuthToken) {
            return token.equals(((AuthToken) obj).getToken());
        } else {
            return false;
        }
    }
}
