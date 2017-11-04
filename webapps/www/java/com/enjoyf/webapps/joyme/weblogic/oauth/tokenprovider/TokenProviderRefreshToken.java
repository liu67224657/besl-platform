/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.joyme.weblogic.oauth.tokenprovider;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.OAuthHotdeployConfig;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppTypeConfig;
import com.enjoyf.platform.service.oauth.AuthGrantType;
import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.service.oauth.AuthTokenType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.webapps.common.oauth.OAuthException;
import com.enjoyf.platform.webapps.common.oauth.OAuthTokenGenerator;
import com.enjoyf.webapps.joyme.weblogic.oauth.OAuthTokenProvider;
import com.enjoyf.webapps.joyme.weblogic.oauth.TokenAccessRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-8 下午6:00
 * Description:
 */
public class TokenProviderRefreshToken implements OAuthTokenProvider {
    //
    private Logger logger = LoggerFactory.getLogger(TokenProviderRefreshToken.class);

    //
    private OAuthHotdeployConfig config = HotdeployConfigFactory.get().getConfig(OAuthHotdeployConfig.class);

    @Override
    public AuthToken accessToken(TokenAccessRequest request) throws OAuthException {
        AuthToken returnValue = null;

        //get the app
        AuthApp app = null;
        try {
            app = OAuthServiceSngl.get().getApp(request.getAppId());
        } catch (Exception e) {
            throw OAuthException.TEMPORARILY_UNAVAILABLE;
        }

        if (app == null || !app.getAppKey().equals(request.getAppKey())) {
            throw OAuthException.INVALID_CLIENT;
        }

        //
        AuthAppTypeConfig appTypeConfig = config.getAppTypeConfig(app.getAppType());
        if (appTypeConfig == null) {
            throw OAuthException.INVALID_CLIENT;
        }

        if (!appTypeConfig.getSupportGrantTypes().contains(AuthGrantType.REFRESH_TOKEN)) {
            throw OAuthException.UNSUPPORTED_GRANT_TYPE;
        }

        //
        AuthToken refreshToken = null;
        try {
            refreshToken = OAuthServiceSngl.get().getToken(request.getRefreshToken(), AuthTokenType.REFRESH);
        } catch (Exception e) {
            throw OAuthException.TEMPORARILY_UNAVAILABLE;
        }

        if (refreshToken == null || (refreshToken.getExpireDate() != null && refreshToken.getExpireDate().before(new Date()))) {
            throw OAuthException.ACCESS_DENIED;
        }

        //assign
        returnValue = OAuthTokenGenerator.generateAccessToken(app, refreshToken.getCredentialId());

        return returnValue;
    }
}
