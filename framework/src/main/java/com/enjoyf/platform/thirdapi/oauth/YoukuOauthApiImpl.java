package com.enjoyf.platform.thirdapi.oauth;

import com.enjoyf.platform.props.YoukuConfig;
import com.enjoyf.platform.service.account.AuthVersion;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.AuthParam;
import com.enjoyf.platform.thirdapi.AuthUrlParam;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.youku.YoukuAuth;
import com.enjoyf.platform.util.oauth.youku.YoukuAuthService;
import com.enjoyf.platform.util.oauth.youku.YoukuUser;
import com.enjoyf.platform.util.oauth.youku.YoukuUserService;
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
public class YoukuOauthApiImpl extends AbstractOauthApi {
    Logger logger = LoggerFactory.getLogger(YoukuOauthApiImpl.class);

    private YoukuAuthService authService = new YoukuAuthService();
    private YoukuUserService userService = new YoukuUserService();

    @Override
    public String getAuthorizeUrl(AuthUrlParam authUrlParam) throws ServiceException {
        return authService.getAuthorizeUrl(YoukuConfig.get().getRedirectUrl().trim());
    }

    @Override
    public OAuthInfo getAccessToken(AuthParam authParam) throws ServiceException {

        OAuthInfo oauthInfo = null;
        try {
            YoukuAuth auth = authService.getAccessToken(authParam.getAuthCode(), YoukuConfig.get().getRedirectUrl().trim());
            if (auth == null) {
                return null;
            }

            YoukuUser info = userService.getUserInfo(auth.getAccess_token());

            String name = String.valueOf(info.getName());
            String figureUrl = info.getAvatar_large();
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setOauthToken(auth.getAccess_token());
            tokenInfo.setExpire(String.valueOf(auth.getExpires_in()));
            tokenInfo.setRefreshToken(auth.getRefresh_token());

            //
            oauthInfo = new OAuthInfo();
            oauthInfo.setAuthVersion(AuthVersion.SYNC_AUTH_VERSION_AUTH20);
            oauthInfo.setAccessToken(tokenInfo);
            oauthInfo.setForignId(String.valueOf(info.getId()));
            oauthInfo.setForignName(name);
            if (!StringUtil.isEmpty(figureUrl)) {
                oauthInfo.setFigureUrl(figureUrl + ".jpeg");
            }
        } catch (Exception e) {
            GAlerter.lab("get user accesstoken by weixin oauth2.0 occured Exception.e: ", e);
        }
        return oauthInfo;
    }

    @Override
    public TokenInfo refreshToken(TokenInfo tokenInfo) throws ServiceException {
        OAuthInfo oauthInfo = null;
        try {

            YoukuAuth auth = authService.refreshToken(tokenInfo.getRefreshToken());
            if (auth == null) {
                return null;
            }

//            WeixinServUserInfo info = userService.getUserInfo(auth.getAccess_token(), auth.getOpenid());
//            if (info == null) {
//                return null;
//            }
            tokenInfo = new TokenInfo();
//            tokenInfo.setOpenId(auth.getOpenid());
            tokenInfo.setRefreshToken(auth.getRefresh_token());
            tokenInfo.setOauthToken(auth.getAccess_token());
            tokenInfo.setExpire(String.valueOf(auth.getExpires_in()));
            tokenInfo.setRefreshToken(auth.getRefresh_token());

        } catch (Exception e) {
            GAlerter.lab("get user accesstoken by weixin oauth2.0 occured Exception.e: ", e);
        }
        return tokenInfo;
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
