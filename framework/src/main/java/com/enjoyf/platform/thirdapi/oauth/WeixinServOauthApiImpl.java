package com.enjoyf.platform.thirdapi.oauth;

import com.enjoyf.platform.props.WeixinServConfig;
import com.enjoyf.platform.service.account.AuthVersion;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.AuthParam;
import com.enjoyf.platform.thirdapi.AuthUrlParam;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.weixinservice.WeiXinServAuth;
import com.enjoyf.platform.util.oauth.weixinservice.WeixinServAuthService;
import com.enjoyf.platform.util.oauth.weixinservice.WeixinServUserInfo;
import com.enjoyf.platform.util.oauth.weixinservice.WeixinServUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;



/**
 * <p/>
 * Description: 微信服务号的接口
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class WeixinServOauthApiImpl extends AbstractOauthApi {
    Logger logger = LoggerFactory.getLogger(WeixinServOauthApiImpl.class);

    private WeixinServAuthService authService = new WeixinServAuthService();
    private WeixinServUserService userService = new WeixinServUserService();

    @Override
    public String getAuthorizeUrl(AuthUrlParam authUrlParam) throws ServiceException {
        String redirectUri = WeixinServConfig.get().getRedirectUrl().trim();
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
            WeiXinServAuth auth = authService.getAccessToken(authParam.getAuthCode());
            if (auth == null) {
                return null;
            }

            WeixinServUserInfo info = userService.getUserInfo(auth.getAccess_token(), auth.getOpenid());
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
            oauthInfo.setForignId(info.getOpenId());
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

            WeiXinServAuth auth = authService.refreshToken(tokenInfo.getRefreshToken());
            if (auth == null) {
                return null;
            }

            WeixinServUserInfo info = userService.getUserInfo(auth.getAccess_token(), auth.getOpenid());
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
