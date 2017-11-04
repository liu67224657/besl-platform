package com.enjoyf.platform.util.oauth.qqv2;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class QqV2RequestAuthInfo {
    private String accessToken;
    private String openId;
    private String openKey;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOpenKey() {
        return openKey;
    }

    public void setOpenKey(String openKey) {
        this.openKey = openKey;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
