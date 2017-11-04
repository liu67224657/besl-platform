package com.enjoyf.platform.props;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.PcThirdApiHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.ThirdApiHotdeployConfig;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.FiveProps;


/**
 * webapp 属性文件
 */
public class PcSinaWeiboConfig {
    private static PcSinaWeiboConfig instance;

    private FiveProps props;

    private String clientID = "3904005873";
    private String clientSercret = "ea601f11451c4970403a29df9fc0ed8b";
    private String redirectUri = "http://www.joyme.com/sync/sina/access";
    private String baseUrl = "https://api.weibo.com/2/";
    private String accessTokenUri = "https://api.weibo.com/2/oauth2/access_token";
    private String authorizeUri = "https://api.weibo.com/2/oauth2/authorize";

    //关注的好友
    private static final String SINA_FRIENDSHIPS_FRIENDS = "https://api.weibo.com/2/friendships/friends.json";
    //获取互粉列表
    private static final String SINA_FRIENDS_BILATERAL = "https://api.weibo.com/2/friendships/friends/bilateral.json";
    //获得用户信息
    private static final String SINA_SHOW_USER = "https://api.weibo.com/2/users/show.json";
    //搜索建议
    private static final String SINA_SEARCH_AT_USERS = "https://api.weibo.com/2/search/suggestions/at_users.json";

    private static final String SINA_CLIENT_ID = "sina.client.id";
    private static final String SINA_CLIENT_SERCRET = "sina.client.sercret";
    private static final String SINA_REDIRECT_URI = "sina.redirect.uri";
    private static final String SINA_BASE_URL = "sina.base.uri";
    private static final String SINA_ACCESSTOKEN_URL = "sina.access.token.uri";
    private static final String SINA_AUTHORIZE_URL = "sina.authorize.url";

    private PcSinaWeiboConfig() {
        init();
    }

    public static synchronized PcSinaWeiboConfig get() {
        if (instance == null) {
            instance = new PcSinaWeiboConfig();
        }

        return instance;
    }

    private void init() {
        props = new FiveProps(HotdeployConfigFactory.get().getConfig(PcThirdApiHotdeployConfig.class).getProviderPropsMap().get(LoginDomain.SINAWEIBO));

        if (props != null) {
            clientID = props.get(SINA_CLIENT_ID, clientID);
            clientSercret = props.get(SINA_CLIENT_SERCRET, clientSercret);
            redirectUri = props.get(SINA_REDIRECT_URI, redirectUri);
            baseUrl = props.get(SINA_BASE_URL, baseUrl);
            accessTokenUri = props.get(SINA_ACCESSTOKEN_URL, accessTokenUri);
            authorizeUri = props.get(SINA_AUTHORIZE_URL, authorizeUri);
        }
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientSercret() {
        return clientSercret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public String getAuthorizeUri() {
        return authorizeUri;
    }

    public String getFriends() {
        return SINA_FRIENDSHIPS_FRIENDS;
    }

    public String getAtusers() {
        return SINA_SEARCH_AT_USERS;
    }

    public String getFriendsBilateral() {
        return SINA_FRIENDS_BILATERAL;
    }

    public String getShowUser() {
        return SINA_SHOW_USER;
    }


}
