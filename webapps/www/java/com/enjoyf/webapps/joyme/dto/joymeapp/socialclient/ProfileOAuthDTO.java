package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

import com.enjoyf.platform.service.oauth.OAuthInfo;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-4-15
 * Time: 下午8:16
 * To change this template use File | Settings | File Templates.
 */
public class ProfileOAuthDTO {

    private ProfileDTO profile;

    private OAuthInfo oauth;

    private String login_type; //登录类型

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

    public OAuthInfo getOauth() {
        return oauth;
    }

    public void setOauth(OAuthInfo oauth) {
        this.oauth = oauth;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }
}
