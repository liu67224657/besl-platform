/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.webapps.common.oauth;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.OAuthHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppTypeConfig;
import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.service.oauth.AuthTokenType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-11 下午9:39
 * Description:
 */
public class OAuthTokenGenerator {
    private static Logger logger= LoggerFactory.getLogger(OAuthTokenGenerator.class);

    //
    private static OAuthHotdeployConfig oauthHotdeployConfig = HotdeployConfigFactory.get().getConfig(OAuthHotdeployConfig.class);

    public static AuthToken generateAccessToken(String appId, String credentialId) throws OAuthException {
        if(logger.isDebugEnabled()){
            logger.debug(" generateAccessToken appId:"+appId+" , credentialId:"+credentialId);
        }

        AuthApp app = null;

        //find the app.
        try {
            app = OAuthServiceSngl.get().getApp(appId);
        } catch (ServiceException e) {
            //
            GAlerter.lab(OAuthTokenGenerator.class.getName() + " generateAccessToken getApp occured ServiceException,e:", e);
            throw OAuthException.TEMPORARILY_UNAVAILABLE;
        }

        //the app status.
        if (app == null || !ValidStatus.VALID.equals(app.getValidStatus()) || !ActStatus.ACTED.equals(app.getAuditStatus())) {
            throw OAuthException.INVALID_CLIENT;
        }

        return generateAccessToken(app, credentialId);
    }

    //
    public static AuthToken generateAccessToken(AuthApp app, String credentialId) throws OAuthException {

        //get the app type config.
        AuthAppTypeConfig appTypeConfig = oauthHotdeployConfig.getAppTypeConfig(app.getAppType());

        if (appTypeConfig == null) {
            throw OAuthException.INVALID_CLIENT;
        }

        AuthToken returnValue = null;

        //
        try {
            //
            AuthToken accessToken = new AuthToken();

            //
            accessToken.setAppId(app.getAppId());
            accessToken.setCreateDate(new Date());
            accessToken.setToken(UUID.randomUUID().toString());
            accessToken.setTokenType(AuthTokenType.ACCESS);
            accessToken.setCredentialId(credentialId);
            accessToken.setAccessTimes(0);

            if (appTypeConfig.getAccessTokenConfig().getExpireTimeSecs() >= 0) {
                accessToken.setExpireDate(new Date(System.currentTimeMillis() + 1000l * appTypeConfig.getAccessTokenConfig().getExpireTimeSecs()));
            }

            //the refresh token.
            if (appTypeConfig.isSupportRefreshToken()) {
                AuthToken refreshToken = new AuthToken();

                //
                refreshToken.setAppId(app.getAppId());

                refreshToken.setCreateDate(new Date());
                refreshToken.setToken(UUID.randomUUID().toString());
                refreshToken.setTokenType(AuthTokenType.REFRESH);
                refreshToken.setCredentialId(credentialId);
                refreshToken.setAccessTimes(0);
                if (appTypeConfig.getRefreshTokenConfig().getExpireTimeSecs() >= 0) {
                    refreshToken.setExpireDate(new Date(System.currentTimeMillis() + 1000l * appTypeConfig.getRefreshTokenConfig().getExpireTimeSecs()));
                }

                //
                refreshToken = OAuthServiceSngl.get().applyToken(refreshToken);

                ///
                accessToken.setRefreshToken(refreshToken.getToken());
            }

            returnValue = OAuthServiceSngl.get().applyToken(accessToken);
        } catch (Exception e) {
            throw OAuthException.TEMPORARILY_UNAVAILABLE;
        }

        return returnValue;
    }
}
