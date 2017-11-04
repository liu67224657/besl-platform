package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.json.JsonObjectUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class UserLoginTokenInfo implements Serializable {
    private String accessToken;
    private String expire;
    private String refreshToken;

    private String oauthToken;
    private String oauthSecr;

    //for auth qv2
    private String openId;

    private static JsonObjectUtil<UserLoginTokenInfo> tokenJson = new JsonObjectUtil<UserLoginTokenInfo>();

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getOauthSecr() {
        return oauthSecr;
    }

    public void setOauthSecr(String oauthSecr) {
        this.oauthSecr = oauthSecr;
    }

    public String toJsonStr() {
        return tokenJson.toJsonStr(this);
    }

    public static UserLoginTokenInfo getByJsonStr(String jsonStr) {
        return tokenJson.parse(jsonStr, UserLoginTokenInfo.class);
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public static JsonObjectUtil<UserLoginTokenInfo> getTokenJson() {
        return tokenJson;
    }

    public static void setTokenJson(JsonObjectUtil<UserLoginTokenInfo> tokenJson) {
        UserLoginTokenInfo.tokenJson = tokenJson;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
