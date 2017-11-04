package com.enjoyf.platform.thirdapi.oauth;

import com.enjoyf.platform.props.SinaWeiboConfig;
import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.account.AuthVersion;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.AuthParam;
import com.enjoyf.platform.service.sync.SyncServiceException;
import com.enjoyf.platform.thirdapi.AuthUrlParam;
import com.enjoyf.platform.thirdapi.friends.FriendUrlParam;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.weibo4j.*;
import com.enjoyf.platform.util.oauth.weibo4j.http.AccessToken;
import com.enjoyf.platform.util.oauth.weibo4j.http.ImageItem;
import com.enjoyf.platform.util.oauth.weibo4j.model.User;
import com.enjoyf.platform.util.oauth.weibo4j.model.WeiboException;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONException;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;


/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SinaOauthApiImpl extends AbstractOauthApi {
    Logger logger = LoggerFactory.getLogger(SinaOauthApiImpl.class);

    /**
     * 得到sina微博验证的authorizeUrl地址
     */
    public String getAuthorizeUrl(AuthUrlParam authUrlParam) throws ServiceException {
        try {
            String redirectUri = SinaWeiboConfig.get().getRedirectUri().trim();
//                + authUrlParam.generatorUrlParam();

            try {
                redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
            } catch (UnsupportedEncodingException e) {

            }


            return new Oauth().authorize("code", redirectUri);
        } catch (WeiboException e) {
            logger.debug(e.getMessage());
            throw new ServiceException(SyncServiceException.SYNC_GET_AUTHURL_SERVICE_EXCEPTION, "sina get auth url error");
        }

    }

    public OAuthInfo getAccessToken(AuthParam authParam) throws ServiceException {

        try {

            Oauth oauth = new Oauth();
            AccessToken token = oauth.getAccessTokenByCode(authParam.getAuthCode());

            Weibo weibo = new Weibo();
            weibo.setToken(token.getAccessToken());
            Account account = new Account();
            JSONObject jsonIdObject = account.getUid();
            if (jsonIdObject == null) {
                return null;
            }
            Object idObject = null;
            try {
                idObject = jsonIdObject.get("uid");
            } catch (JSONException e) {

            }
            if (idObject == null) {
                return null;
            }
            String uid = String.valueOf(idObject);

            Users users = new Users();
            User user = users.showUserById(uid);
            if (user == null) {
                return null;
            }

            OAuthInfo OAuthInfo = new OAuthInfo();

            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setAccessToken(token.getAccessToken());
            tokenInfo.setExpire(token.getExpireIn());
            tokenInfo.setRefreshToken(token.getRefreshToken());

            OAuthInfo.setAccessToken(tokenInfo);
            OAuthInfo.setForignId(String.valueOf(uid));
            OAuthInfo.setForignName(user.getScreenName());
            OAuthInfo.setAuthVersion(AuthVersion.SYNC_AUTH_VERSION_AUTH20);
            OAuthInfo.setAccessToken(tokenInfo);
            OAuthInfo.setSyncProvider(AccountDomain.SYNC_SINA_WEIBO);
            if (!StringUtil.isEmpty(String.valueOf(user.getavatarLarge()))) {
                OAuthInfo.setFigureUrl(String.valueOf(user.getavatarLarge()) + ".jpeg");
            } else {
                if (user.getProfileImageURL() != null) {
                    String figurl = user.getProfileImageURL().getProtocol() + "://" + user.getProfileImageURL().getHost() + user.getProfileImageURL().getPath();
                    figurl = figurl.replace("/50/", "/180/");
                    OAuthInfo.setFigureUrl(figurl);
                }
            }
            return OAuthInfo;
        } catch (WeiboException e) {
            logger.error(e.getMessage(), e);
            GAlerter.lan(this.getClass() + "------------method:getAccessToken,  WeiboException errorCode=" + e.getErrorCode() + "--error=" + e.getError());
            throw new ServiceException(SyncServiceException.SYNC_AUTH_FALIED_SERVICE_EXCEPTION, "auth failed.authParam:" + authParam);
        }
    }

    @Override
    public TokenInfo refreshToken(TokenInfo tokenInfo) throws ServiceException {
        try {
            AccessToken refreshToken = new Oauth().refreshAccessToken(tokenInfo.getRefreshToken());

            tokenInfo.setAccessToken(refreshToken.getAccessToken());
            tokenInfo.setExpire(refreshToken.getExpireIn());
            tokenInfo.setRefreshToken(refreshToken.getRefreshToken());

            return tokenInfo;
        } catch (WeiboException e) {
            logger.debug(e.getMessage());
            throw new ServiceException(SyncServiceException.SYNC_AUTH_FALIED_SERVICE_EXCEPTION, " sina2.0 refresh token falied." + e.getError());
        }

    }

    @Override
    public OAuthStatus syncContent(TokenInfo tokenInfo, SyncContent syncContent) throws ServiceException {
        OAuthStatus status = OAuthStatus.OAUTH_SUCCESS;

        Weibo weibo = new Weibo();
        weibo.setToken(tokenInfo.getAccessToken());
        Timeline timeline = new Timeline();
        try {
            if (StringUtil.isEmpty(syncContent.getSyncImg())) {
                timeline.UpdateStatus(syncContent.getSyncText());
            } else {
                try {
                    HttpByteData picData = HttpURLUtils.getByte(syncContent.getSyncImg(), !syncContent.getSyncImg().startsWith("http://"));
                    timeline.UploadStatus(syncContent.getSyncText(), new ImageItem(picData.getData()));
                } catch (IOException e) {
                    GAlerter.lan(this.getClass().getName() + " UploadStatus occured IOException,src: " + syncContent.getSyncImg() + ", e:", e);
                }
            }

        } catch (WeiboException e) {
            GAlerter.lan(this.getClass().getName() + " UploadStatus occured WeiboException, e:", e);
            //重新获取refreshToken 存入新token发送文章
            if (e.getErrorCode() == 21327 && !StringUtil.isEmpty(tokenInfo.getRefreshToken())) {
                status = OAuthStatus.OAUTH_TOKEN_EXPIRE;
                return status;
            }
            status = OAuthStatus.OAUTH_TOKEN_FAILED;
        }

        return status;
    }

    public void followJoyme(TokenInfo tokenInfo) throws ServiceException {
        Weibo weibo = new Weibo();
        weibo.setToken(tokenInfo.getAccessToken());
        Friendships friendships = new Friendships();
        try {
            friendships.createFriendshipsById("2253167477");
        } catch (WeiboException e) {
            GAlerter.lan(this.getClass() + "occured ServiceException.e", e);
        }
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

    @Override
    public String getFriends(FriendUrlParam friendUrlParam) throws ServiceException {
        String str = "";
        try {
            HttpParameter[] params = new HttpParameter[]{
                    new HttpParameter("access_token", friendUrlParam.getAccess_token()),
                    new HttpParameter("source", SinaWeiboConfig.get().getClientID()),
                    new HttpParameter("uid", friendUrlParam.getUid()),
                    new HttpParameter("count", StringUtil.isEmpty(friendUrlParam.getCount()) ? "20" : friendUrlParam.getCount()),
                    new HttpParameter("cursor", StringUtil.isEmpty(friendUrlParam.getCursor()) ? "0" : friendUrlParam.getCursor())
            };
            HttpResult httpResult = new HttpClientManager().get(SinaWeiboConfig.get().getFriends(), params);
            str = httpResult.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获得新浪微博互粉列表
     *
     * @param friendUrlParam
     * @return
     * @throws ServiceException
     */
    @Override
    public String getBilateral(FriendUrlParam friendUrlParam) throws ServiceException {
        String str = "";
        try {
            HttpParameter[] params = new HttpParameter[]{
                    new HttpParameter("access_token", friendUrlParam.getAccess_token()),
                    new HttpParameter("source", SinaWeiboConfig.get().getClientID()),
                    new HttpParameter("uid", friendUrlParam.getUid()),
                    new HttpParameter("count", StringUtil.isEmpty(friendUrlParam.getCount()) ? "20" : friendUrlParam.getCount()),
            };
            HttpResult httpResult = new HttpClientManager().get(SinaWeiboConfig.get().getFriendsBilateral(), params);
            str = httpResult.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * 获得新浪用户信息
     *
     * @param friendUrlParam
     * @return
     * @throws ServiceException
     */
    @Override
    public String getShowUser(FriendUrlParam friendUrlParam) throws ServiceException {
        String str = "";
        try {
            HttpParameter[] params = new HttpParameter[]{
                    new HttpParameter("access_token", friendUrlParam.getAccess_token()),
                    new HttpParameter("source", SinaWeiboConfig.get().getClientID()),
                    new HttpParameter("uid", friendUrlParam.getUid()),
            };
            HttpResult httpResult = new HttpClientManager().get(SinaWeiboConfig.get().getShowUser(), params);
            str = httpResult.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String atUsers(FriendUrlParam friendUrlParam) throws ServiceException {
        String str = "";
        try {
            HttpParameter[] params = new HttpParameter[]{
                    new HttpParameter("access_token", friendUrlParam.getAccess_token()),
                    new HttpParameter("source", SinaWeiboConfig.get().getClientID()),
                    new HttpParameter("q", friendUrlParam.getQ()),
                    new HttpParameter("type", "0"),//联想类型，0：关注、1：粉丝。
                    new HttpParameter("range", "0"),//联想类型，0：关注、1：粉丝。
                    new HttpParameter("count", StringUtil.isEmpty(friendUrlParam.getCursor()) ? "50" : friendUrlParam.getCursor())
            };
            HttpResult httpResult = new HttpClientManager().get(SinaWeiboConfig.get().getAtusers(), params);
            str = httpResult.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
