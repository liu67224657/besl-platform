package com.enjoyf.platform.thirdapi.oauth;

import com.enjoyf.platform.props.QWeiboAuthV2Config;
import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.account.AuthVersion;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.AuthParam;
import com.enjoyf.platform.thirdapi.AuthUrlParam;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.oauth.qweibov2.*;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;


/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class QWeiboV2OauthApiImpl extends AbstractOauthApi {
    Logger logger = LoggerFactory.getLogger(QWeiboV2OauthApiImpl.class);

    private QWeiboAuthV2Config config = QWeiboAuthV2Config.get();

    /**
     * @return
     * @throws com.enjoyf.platform.service.service.ServiceException
     *
     */
    public String getAuthorizeUrl(AuthUrlParam authUrlParam) throws ServiceException {
        String redirectUri = config.getRedirectUrl().trim() + authUrlParam.generatorUrlParam();

        OAuth OAuth = new OAuth(config.getQweiboApiKey(), config.getQweiboApiSecret());
        return OAuth.getAuthorizeUrl(redirectUri);
    }


    public OAuthInfo getAccessToken(AuthParam authParam) throws ServiceException {
        String redirectUri = config.getRedirectUrl().trim() +new AuthUrlParam(authParam.getRurl(),authParam.isRl(),authParam.getRedirectType(),authParam.getAppKey()).generatorUrlParam();
        OAuth OAuth = new OAuth(config.getQweiboApiKey(), config.getQweiboApiSecret());
        OAuthInfo oauthInfo = null;
        try {
            QweiboAuthToken qweiboAuthToken = OAuth.getAccessToken(authParam.getAuthCode(), redirectUri);
            if (StringUtil.isEmpty(qweiboAuthToken.getOpenId()) || StringUtil.isEmpty(qweiboAuthToken.getName())) {
                return oauthInfo;
            }

            RequestAuthInfo authInfo = new RequestAuthInfo();
            authInfo.setAccessToken(qweiboAuthToken.getAccessToken());
            authInfo.setClientId(config.getQweiboApiKey());
            authInfo.setClientScr(config.getQweiboApiSecret());
            authInfo.setOpenId(qweiboAuthToken.getOpenId());
            authInfo.setOpenKey(authParam.getExtString02());
            authInfo.setName(qweiboAuthToken.getName());

            UserService userService = new UserService();
            JSONObject jsonObject = userService.getUserInfo(authInfo, "json");
//            String openId = String.valueOf(jsonObject.get("openid"));
//            String name = StringUtil.isEmpty(String.valueOf(jsonObject.get("nick"))) ? String.valueOf(jsonObject.get("name")) : String.valueOf(jsonObject.get("nick"));

            String head = String.valueOf(jsonObject.get("head"));

            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setOpenId(authInfo.getOpenId());
            tokenInfo.setAccessToken(qweiboAuthToken.getAccessToken());
            tokenInfo.setExpire(String.valueOf(qweiboAuthToken.getExpires()));
            tokenInfo.setRefreshToken(qweiboAuthToken.getRefreshToken());

            //
            oauthInfo = new OAuthInfo();
            oauthInfo.setAccessToken(tokenInfo);
            oauthInfo.setAuthVersion(AuthVersion.SYNC_AUTH_VERSION_AUTH20);
            oauthInfo.setAccessToken(tokenInfo);
            oauthInfo.setSyncProvider(AccountDomain.SYNC_QQ_WEIBO);
            oauthInfo.setForignId(authInfo.getOpenId());
            oauthInfo.setForignName(authInfo.getName());
            if (!StringUtil.isEmpty(head)) {
                oauthInfo.setFigureUrl(head + "/100.jpeg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return oauthInfo;
    }

    @Override
    public TokenInfo refreshToken(TokenInfo tokenInfo) throws ServiceException {

        OAuth oAuth = new OAuth(config.getQweiboApiKey(), config.getQweiboApiSecret());

        try {
            QweiboAuthToken qweiboAuthToken = oAuth.refreshToken(tokenInfo.getRefreshToken());

            tokenInfo.setAccessToken(qweiboAuthToken.getAccessToken());
            tokenInfo.setExpire(String.valueOf(qweiboAuthToken.getExpires()));
            tokenInfo.setRefreshToken(qweiboAuthToken.getRefreshToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tokenInfo;
    }

    @Override
    public OAuthStatus syncContent(TokenInfo tokenInfo, SyncContent syncContent) throws ServiceException {
        OAuthStatus oAuthStatus = OAuthStatus.OAUTH_SUCCESS;

        TweetService tweetService = new TweetService();
        try {

            RequestAuthInfo authInfo = new RequestAuthInfo();
            authInfo.setAccessToken(tokenInfo.getAccessToken());
            authInfo.setClientId(config.getQweiboApiKey());
            authInfo.setClientScr(config.getQweiboApiSecret());
            authInfo.setOpenId(tokenInfo.getOpenId());

            if (StringUtil.isEmpty(syncContent.getSyncImg())) {
                tweetService.add(authInfo, syncContent.getSyncText(), "json");
            } else {
                tweetService.addPic(authInfo, syncContent.getSyncText(), syncContent.getSyncImg(), "json");
            }

        } catch (Exception e) {
            return OAuthStatus.OAUTH_TOKEN_EXPIRE;
        }

        return oAuthStatus;
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
            returnObj.setExtString01(request.getParameter("openid"));
            returnObj.setExtString02(request.getParameter("openkey"));
            returnObj.setRedirectType(redirectType);
            returnObj.setAppKey(appKey);
        }
        return returnObj;
    }
}
