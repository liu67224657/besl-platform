package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.FiveProps;

/**
 * weibo app 属性文件
 */
public class PcQqV2Config {
    private static PcQqV2Config instance;

    private FiveProps props;

    private String clientId = "68ace50c15654ff38a0c850625c4c1f7";
    private String clientSecret = "58ad3f70c3e731dd858d4ef7c57c4dce";

    private String redirectUrl = "http://www.joyme.com/profile/sync/qq/access";

    private String authorizeUri = "https://graph.qq.com/oauth2.0/authorize";
    private String accessTokenUri = "https://graph.qq.com/oauth2.0/token";

    private String apiURL = "https://graph.qq.com/";

    private static final String APP_CLIENT_ID = "app.client.id";
    private static final String APP_CLIENT_SERCRET = "app.client.sercret";
    private static final String QQ_REDIRECT_URI = "qq.redirect.uri";
    private static final String QQ_AUTHORIZE_URI = "qq.authorize.uri";

    private static final String QQ_API_URI = "qq.api.uri";
    private static final String QQ_ACCESSTOKEN_URI = "qq.accesstoken.uri";

    public PcQqV2Config() {
        init();
    }

    public static synchronized PcQqV2Config get() {
        if (instance == null) {
            instance = new PcQqV2Config();
        }

        return instance;
    }

    private void init() {
        props = new FiveProps(HotdeployConfigFactory.get().getConfig(PcThirdApiHotdeployConfig.class).getProviderPropsMap().get(LoginDomain.QQ));

        clientId = props.get(APP_CLIENT_ID, clientId);
        clientSecret = props.get(APP_CLIENT_SERCRET, clientSecret);

        redirectUrl = props.get(QQ_REDIRECT_URI, redirectUrl);

        authorizeUri = props.get(QQ_AUTHORIZE_URI, authorizeUri);
        accessTokenUri = props.get(QQ_ACCESSTOKEN_URI, accessTokenUri);

        apiURL = props.get(QQ_API_URI, apiURL);
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getApiURL() {
        return apiURL;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAuthorizeUri() {
        return authorizeUri;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getAccessTokenUri() {
        return accessTokenUri;
    }
}
