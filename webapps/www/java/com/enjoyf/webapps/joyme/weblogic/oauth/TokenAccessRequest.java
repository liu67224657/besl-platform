/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.joyme.weblogic.oauth;

import com.enjoyf.platform.service.oauth.AuthGrantType;
import com.enjoyf.platform.service.oauth.AuthResponseType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-10 下午3:23
 * Description:
 */
public class TokenAccessRequest {
    private String appId;
    private String appKey;

    private AuthGrantType grantType;
    private AuthResponseType responseType;

    private String redr;

    private String code;

    private String refreshToken;

    private String loginName;
    private String loginPwd;

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

    public AuthGrantType getGrantType() {
        return grantType;
    }

    public void setGrantType(AuthGrantType grantType) {
        this.grantType = grantType;
    }

    public AuthResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(AuthResponseType responseType) {
        this.responseType = responseType;
    }

    public String getRedr() {
        return redr;
    }

    public void setRedr(String redr) {
        this.redr = redr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
