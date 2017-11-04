package com.enjoyf.platform.thirdapi.oauth;

import com.enjoyf.platform.props.hotdeploy.QqV2Config;
import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.account.AuthVersion;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.AuthParam;
import com.enjoyf.platform.thirdapi.AuthUrlParam;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.qqv2.QqBlogService;
import com.enjoyf.platform.util.oauth.qqv2.*;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class QqV2OauthApiImpl extends AbstractOauthApi {
    Logger logger = LoggerFactory.getLogger(QqV2OauthApiImpl.class);

    private QqV2Oauth qqv2Oauth = new QqV2Oauth(QqV2Config.get().getClientId(), com.enjoyf.platform.props.hotdeploy.QqV2Config.get().getClientSecret());
    private QqV2UserService qqv2UserService = new QqV2UserService(com.enjoyf.platform.props.hotdeploy.QqV2Config.get().getClientId());

    @Override
    public String getAuthorizeUrl(AuthUrlParam authUrlParam) throws ServiceException {
        String redirectUri = QqV2Config.get().getRedirectUrl().trim();
        try {
            redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        return qqv2Oauth.getAuthorizeUrl(redirectUri);
    }

    @Override
    public OAuthInfo getAccessToken(AuthParam authParam) throws ServiceException {

        OAuthInfo oauthInfo = null;
        try {
            String redirectUri = com.enjoyf.platform.props.hotdeploy.QqV2Config.get().getRedirectUrl().trim() + new AuthUrlParam(authParam.getRurl(), authParam.isRl(), authParam.getRedirectType(), authParam.getAppKey()).generatorUrlParam();

            QqV2AuthToken accessToken = qqv2Oauth.getAccessToken(authParam.getAuthCode(), redirectUri);
            QqV2RequestAuthInfo requestAuthInfo = new QqV2RequestAuthInfo();
            requestAuthInfo.setAccessToken(accessToken.getAccessToken());
            requestAuthInfo.setOpenId(accessToken.getOpenid());

            QqUserInfo info = qqv2UserService.getUserInfo(requestAuthInfo, "json");
            String name = String.valueOf(info.getNickname());
            String figureUrl = String.valueOf(info.getFigureurl_qq_2() == null ? info.getFigureurl_qq_1() : info.getFigureurl_qq_2());
            if (StringUtil.isEmpty(name, false)) {
                return oauthInfo;
            }

            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setOpenId(accessToken.getOpenid());
            tokenInfo.setOauthToken(accessToken.getAccessToken());

            //
            oauthInfo = new OAuthInfo();
            oauthInfo.setAuthVersion(AuthVersion.SYNC_AUTH_VERSION_AUTH20);
            oauthInfo.setAccessToken(tokenInfo);
            oauthInfo.setSyncProvider(AccountDomain.SYNC_QQ);
            oauthInfo.setForignId(accessToken.getUnionId());
            oauthInfo.setForignName(name);
            oauthInfo.setUno(accessToken.getOpenid());
            if (!StringUtil.isEmpty(figureUrl)) {
                oauthInfo.setFigureUrl(figureUrl + ".jpeg");
            }
        } catch (JSONException e) {
            GAlerter.lab("get user accesstoken by qq oauth2.0 occured Exception.e: ", e);
        }
        return oauthInfo;
    }

    @Override
    public TokenInfo refreshToken(TokenInfo tokenInfo) throws ServiceException {
        return super.refreshToken(tokenInfo);
    }

    @Override
    public OAuthStatus syncContent(TokenInfo tokenInfo, SyncContent syncContent) throws ServiceException {
        String s = null;
        try {
            s = QqBlogService.get().addShare(tokenInfo.getOauthToken(), tokenInfo.getOpenId(), "json", syncContent);

            logger.debug(s);
        } catch (IOException e) {
            logger.error("getAuthorizeUrl occured IOException: ", e);
        } catch (InvalidKeyException e) {
            logger.error("getAuthorizeUrl occured InvalidKeyException: ", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("getAuthorizeUrl occured NoSuchAlgorithmException: ", e);
        }

        return OAuthStatus.OAUTH_SUCCESS;
    }

    @Override
    public AuthParam getAuthParamByRequest(HttpServletRequest request, String rurl, boolean requireLogin, String redirectType, String appKey) throws ServiceException {
        AuthParam returnObj = null;
        String code = request.getParameter("code");
        if (!StringUtil.isEmpty(code)) {
            returnObj = new AuthParam();
            returnObj.setAuthCode(code);
            returnObj.setRl(requireLogin);
            returnObj.setRurl(rurl);
            returnObj.setRedirectType(redirectType);
            returnObj.setAppKey(appKey);
        }
        return returnObj;
    }
}
