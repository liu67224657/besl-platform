package com.enjoyf.platform.thirdapi.oauth;

import com.enjoyf.platform.props.RenRenConfig;
import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.account.AuthVersion;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.AuthParam;
import com.enjoyf.platform.thirdapi.AuthUrlParam;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.RenrenApiClient;
import com.enjoyf.platform.util.oauth.renren.api.client.param.Auth;
import com.enjoyf.platform.util.oauth.renren.api.client.param.impl.AccessToken;
import com.enjoyf.platform.util.oauth.renren.api.client.services.FeedService;
import com.enjoyf.platform.util.oauth.renren.api.client.services.PhotoService;
import com.enjoyf.platform.util.oauth.renren.api.client.services.RenrenApiException;
import com.enjoyf.platform.util.oauth.renren.api.client.services.UserService;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.HttpURLUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class RenRenOauthApiImpl extends AbstractOauthApi {
    private static final String RENREN_CONTENT_SUBJECT = "来自着迷网";
    private static final String JSON_KEY_PICUREID = "pid";
    private static final String JSON_KEY_POSTID = "post_id";
    private static final int ACCESSTOKEN_EXPIRED = 2002;

    private FeedService feedService = RenrenApiClient.getInstance().getFeedService();

    @Override
    public OAuthStatus syncContent(TokenInfo tokenInfo, SyncContent syncContent) throws ServiceException {
        OAuthStatus status = OAuthStatus.OAUTH_SUCCESS;

        Auth auth = new AccessToken(tokenInfo.getAccessToken());
        long syncReturn = 0;
        try {
            String picUrl = "";
            if (StringUtil.isEmpty(syncContent.getSyncImg())) {
                picUrl = syncContent.getSyncContentImageUrl();
            } else {
                //上传图片
                PhotoService photoService = RenrenApiClient.getInstance().getPhotoService();
                JSONObject jsonObject = photoService.uploadLocalImg(0, syncContent.getSyncImg(), "", auth);
                Long pid = Long.parseLong(String.valueOf(jsonObject.get(JSON_KEY_PICUREID)));

                //得到uid
                UserService userService = RenrenApiClient.getInstance().getUserService();
                int uid = userService.getLoggedInUser(auth);

                //得到uid和pid
                if (uid > 0 && pid > 0) {
                    JSONArray jsonArray = photoService.getPhotos(Long.parseLong(String.valueOf(uid)), String.valueOf(pid), "", auth);
                    if (jsonArray.size() > 0) {
                        JSONObject jsonObj = (JSONObject) jsonArray.get(0);
                        picUrl = (String) jsonObj.get("url_large");
                    }
                }
            }
            JSONObject jsonObject = feedService.publicFeed(RENREN_CONTENT_SUBJECT + DateUtil.formatDateToString(new Date(), "yyyy-MM-dd"), syncContent.getSyncText(), syncContent.getSyncContentUrl(), picUrl, "", "", "", "", auth);
            syncReturn = Long.parseLong(String.valueOf(jsonObject.get(JSON_KEY_POSTID)));
        } catch (Exception e) {
            GAlerter.lab("oauth renren occured Exception:", e);
            if (e instanceof RenrenApiException) {
                RenrenApiException renrenApiException = (RenrenApiException) e;
                if (renrenApiException.getErrorCode() == ACCESSTOKEN_EXPIRED && !StringUtil.isEmpty(tokenInfo.getRefreshToken())) {
                    status = OAuthStatus.OAUTH_TOKEN_EXPIRE;
                    return status;
                }
            } else {
                status = OAuthStatus.OAUTH_TOKEN_FAILED;
                return status;
            }
        }

        if (syncReturn <= 0) {
            status = OAuthStatus.OAUTH_TOKEN_FAILED;
        }

        return status;
    }

    @Override
    public TokenInfo refreshToken(TokenInfo tokenInfo) throws ServiceException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("grant_type", "refresh_token");
        map.put("client_id", RenRenConfig.get().getRenrenApiKey());
        map.put("client_secret", RenRenConfig.get().getRenrenApiSecret());
        map.put("refresh_token", tokenInfo.getRefreshToken());
        String response = HttpURLUtils.doPost(RenRenConfig.get().getAccessTokenUri(), map);
        JSONObject jsonObject = (JSONObject) JSONValue.parse(response);

        tokenInfo.setAccessToken((String) jsonObject.get("access_token"));
        tokenInfo.setExpire(String.valueOf(jsonObject.get("expires_in")));
        tokenInfo.setRefreshToken((String) jsonObject.get("refresh_token"));
        return tokenInfo;
    }

    @Override
    public OAuthInfo getAccessToken(AuthParam authParam) throws ServiceException {

        String redirectUri = RenRenConfig.get().getRedirectUrl().trim() + new AuthUrlParam(authParam.getRurl(),authParam.isRl(),authParam.getRedirectType(),authParam.getAppKey()).generatorUrlParam();

        Map<String, String> map = new HashMap<String, String>();
        map.put("grant_type", "authorization_code");
        map.put("client_id", RenRenConfig.get().getRenrenApiKey());
        map.put("redirect_uri", redirectUri);
        map.put("client_secret", RenRenConfig.get().getRenrenApiSecret());
        map.put("code", authParam.getAuthCode());
        String response = HttpURLUtils.doPost(RenRenConfig.get().getAccessTokenUri(), map);
        JSONObject jsonObject = (JSONObject) JSONValue.parse(response);
        OAuthInfo OAuthInfo = getSyncByAccessTokenObj(jsonObject);

        return OAuthInfo;
    }

    @Override
    public String getAuthorizeUrl(AuthUrlParam authUrlParam) throws ServiceException {
        String redirectUri = RenRenConfig.get().getRedirectUrl().trim();
//        + authUrlParam.generatorUrlParam();

        try {
            redirectUri=URLEncoder.encode(redirectUri,"UTF-8");
        } catch (UnsupportedEncodingException e) {

        }

        return RenRenConfig.get().getAuthorizeUri().trim() + "?client_id="
                + RenRenConfig.get().getRenrenApiKey().trim() + "&redirect_uri="
                + redirectUri
                + "&response_type=code&scope=publish_feed+publish_blog+photo_upload+read_user_photo";
    }

    private OAuthInfo getSyncByAccessTokenObj(JSONObject jsonObject) {
        OAuthInfo oauthInfo = new OAuthInfo();

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setAccessToken((String) jsonObject.get("access_token"));
        tokenInfo.setExpire(String.valueOf(jsonObject.get("expires_in")));
        tokenInfo.setRefreshToken((String) jsonObject.get("refresh_token"));

        oauthInfo.setAuthVersion(AuthVersion.SYNC_AUTH_VERSION_AUTH20);
        oauthInfo.setAccessToken(tokenInfo);
        oauthInfo.setSyncProvider(AccountDomain.SYNC_RENREN);

        if (jsonObject.get("user") != null) {
            JSONObject userInfoMap = (JSONObject) jsonObject.get("user");

            oauthInfo.setForignId(String.valueOf(userInfoMap.get("id")));
            oauthInfo.setForignName(String.valueOf(userInfoMap.get("name")));
        }
        return oauthInfo;

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
