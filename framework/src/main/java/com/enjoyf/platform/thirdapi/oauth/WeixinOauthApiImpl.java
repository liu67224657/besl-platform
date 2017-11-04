package com.enjoyf.platform.thirdapi.oauth;

import com.enjoyf.platform.props.WeixinConfig;
import com.enjoyf.platform.service.account.AuthVersion;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.AuthParam;
import com.enjoyf.platform.thirdapi.AuthUrlParam;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.weixin.WeiXinAuth;
import com.enjoyf.platform.util.oauth.weixin.WeixinAuthService;
import com.enjoyf.platform.util.oauth.weixin.WeixinUserInfo;
import com.enjoyf.platform.util.oauth.weixin.WeixinUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class WeixinOauthApiImpl extends AbstractOauthApi {
    Logger logger = LoggerFactory.getLogger(WeixinOauthApiImpl.class);

    private WeixinAuthService authService = new WeixinAuthService();
    private WeixinUserService userService = new WeixinUserService();

    @Override
    public String getAuthorizeUrl(AuthUrlParam authUrlParam) throws ServiceException {
        String redirectUri = WeixinConfig.get().getRedirectUrl().trim();
//                + authUrlParam.generatorCallbackParam();

        try {
            redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        return authService.getWeiXinAuthUrl(redirectUri);
    }

    @Override
    public OAuthInfo getAccessToken(AuthParam authParam) throws ServiceException {

        OAuthInfo oauthInfo = null;
        try {
            WeiXinAuth auth = authService.getAccessToken(authParam.getAuthCode());
            if (auth == null) {
                return null;
            }

            WeixinUserInfo info = userService.getUserInfo(auth.getAccess_token(), auth.getOpenid());
            if (info == null) {
                return null;
            }

            String name = String.valueOf(info.getNickname());
            String figureUrl = info.getHeadimgurl();
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setOpenId(auth.getOpenid());
            tokenInfo.setOauthToken(auth.getAccess_token());
            tokenInfo.setExpire(String.valueOf(auth.getExpires_in()));
            tokenInfo.setRefreshToken(auth.getRefresh_token());

            //
            oauthInfo = new OAuthInfo();
            oauthInfo.setAuthVersion(AuthVersion.SYNC_AUTH_VERSION_AUTH20);
            oauthInfo.setAccessToken(tokenInfo);
            oauthInfo.setForignId(info.getUnionid());
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

            WeiXinAuth auth = authService.refreshToken(tokenInfo.getRefreshToken());
            if (auth == null) {
                return null;
            }

            WeixinUserInfo info = userService.getUserInfo(auth.getAccess_token(), auth.getOpenid());
            if (info == null) {
                return null;
            }
            tokenInfo = new TokenInfo();
            tokenInfo.setOpenId(auth.getOpenid());
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
