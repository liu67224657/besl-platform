/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-8 上午10:18
 * Description:
 */
public class AuthAppTypeConfig {
    //the app type of an app.
    private AuthAppType appType;

    //the app type supported  types
    private Set<AuthGrantType> supportGrantTypes = new HashSet<AuthGrantType>();
    private Set<AuthResponseType> supportResponseTypes = new HashSet<AuthResponseType>();

    //
    private AuthTokenConfig accessTokenConfig;

    //
    private boolean supportRefreshToken = false;
    private AuthTokenConfig refreshTokenConfig;


    public AuthAppTypeConfig(AuthAppType appType) {
        this.appType = appType;
    }

    public AuthAppType getAppType() {
        return appType;
    }

    public Set<AuthGrantType> getSupportGrantTypes() {
        return supportGrantTypes;
    }

    public void setSupportGrantTypes(Set<AuthGrantType> supportGrantTypes) {
        this.supportGrantTypes = supportGrantTypes;
    }

    public Set<AuthResponseType> getSupportResponseTypes() {
        return supportResponseTypes;
    }

    public void setSupportResponseTypes(Set<AuthResponseType> supportResponseTypes) {
        this.supportResponseTypes = supportResponseTypes;
    }

    public AuthTokenConfig getAccessTokenConfig() {
        return accessTokenConfig;
    }

    public void setAccessTokenConfig(AuthTokenConfig accessTokenConfig) {
        this.accessTokenConfig = accessTokenConfig;
    }

    public boolean isSupportRefreshToken() {
        return supportRefreshToken;
    }

    public void setSupportRefreshToken(boolean supportRefreshToken) {
        this.supportRefreshToken = supportRefreshToken;
    }

    public AuthTokenConfig getRefreshTokenConfig() {
        return refreshTokenConfig;
    }

    public void setRefreshTokenConfig(AuthTokenConfig refreshTokenConfig) {
        this.refreshTokenConfig = refreshTokenConfig;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
