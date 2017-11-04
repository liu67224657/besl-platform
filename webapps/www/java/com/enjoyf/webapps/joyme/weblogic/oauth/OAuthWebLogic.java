package com.enjoyf.webapps.joyme.weblogic.oauth;

import com.enjoyf.platform.service.oauth.*;
import com.enjoyf.platform.webapps.common.oauth.OAuthException;
import com.enjoyf.webapps.joyme.weblogic.oauth.tokenprovider.TokenProviderFactory;
import org.springframework.stereotype.Service;


@Service(value = "oauthWebLogic")
public class OAuthWebLogic {

    public AuthToken accessToken(TokenAccessRequest request) throws OAuthException {
        if (request.getGrantType() == null) {
            throw OAuthException.INVALID_GRANT;
        }

        return TokenProviderFactory.factory(request.getGrantType()).accessToken(request);
    }

    public AuthToken getToken(String token, AuthTokenType tokenType) throws OAuthException {
        AuthToken returnValue = null;

        try {
            returnValue = OAuthServiceSngl.get().getToken(token, tokenType);
        } catch (Exception e) {
            //
            throw OAuthException.TEMPORARILY_UNAVAILABLE;
        }

        return returnValue;
    }

    public AuthApp getApp(String appId) throws OAuthException {
        AuthApp returnValue = null;

        try {
            returnValue = OAuthServiceSngl.get().getApp(appId);
        } catch (Exception e) {
            //
            throw OAuthException.TEMPORARILY_UNAVAILABLE;
        }

        return returnValue;
    }

    public OAuthInfo create(OAuthInfo oAuthInfo) throws OAuthException {
        OAuthInfo retunrnValue = null;
        try {
            retunrnValue = OAuthServiceSngl.get().create(oAuthInfo);
        } catch (Exception e) {
            throw OAuthException.OAUTHINFO_CREATE;
        }
        return retunrnValue;
    }

    public OAuthInfo getOAuthInfoByRereshToken(String refreshToken, String appKey) throws OAuthException {
        OAuthInfo returnValue = null;
        try {
            returnValue = OAuthServiceSngl.get().getOAuthInfoByRereshToken(refreshToken, appKey);
        } catch (Exception e) {
            throw OAuthException.GET_OAUTHINFO_BYRERESHTOKEN;
        }
        return returnValue;
    }

    public OAuthInfo getOAuthInfoByRereshTokenInter(String refreshToken, String appKey) throws OAuthException {
        OAuthInfo returnValue = null;
        try {
            returnValue = OAuthServiceSngl.get().getOAuthInfoByRereshToken(refreshToken, appKey);
        } catch (Exception e) {
            throw OAuthException.GET_OAUTHINFO_BYRERESHTOKEN;
        }
        return returnValue;
    }

    public OAuthInfo getOAuthInfoByAccessToken(String accessToken, String appKey, String isInterceptor) throws OAuthException {
        OAuthInfo returnValue = null;
        try {
            returnValue = OAuthServiceSngl.get().getOAuthInfoByAccessToken(accessToken, appKey, isInterceptor);
        } catch (Exception e) {
            throw OAuthException.GET_OAUTHINFO_BYACCESSTOKEN;
        }
        return returnValue;
    }

    public OAuthInfo generaterOauthInfo(String uno, String appKey) throws OAuthException {
        OAuthInfo returnValue = null;
        try {
            returnValue = OAuthServiceSngl.get().generaterOauthInfo(uno, appKey);
        } catch (Exception e) {
            throw OAuthException.GET_OAUTHINFO_BYACCESSTOKEN;
        }
        return returnValue;
    }
}
