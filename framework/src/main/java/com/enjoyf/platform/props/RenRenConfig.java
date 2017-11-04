package com.enjoyf.platform.props;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ThirdApiHotdeployConfig;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.FiveProps;

/**
 * webapp 属性文件
 */
public class RenRenConfig {
    private static RenRenConfig instance;

    private FiveProps props;

    private String renrenApiUrl = "http://api.renren.com/restserver.do";
    private String renrenApiVersion = "2.0";
    private String renrenApiKey = "fbd6eba730b1483a983dab8876b8bf4b";
    private String renrenApiSecret = "9267ff122a2d438185eea050434b38d3";
    private String renrenApiId = "173001";
    private String authorizeUri = "https://graph.renren.com/oauth/authorize";
    private String redirectUrl = "http://www.joyme.com/sync/renren/access";
    private String accessTokenUri = "https://graph.renren.com/oauth/token";

    private static final String RENREN_API_URL = "renren.api.url";
    private static final String RENREN_API_VERSION = "renren.api.version";
    private static final String RENREN_API_KEY = "renren.api.key";
    private static final String RENREN_API_SECRET = "renren.api.secret";
    private static final String RENREN_APP_ID = "renren.app.id";
    private static final String AUTHORIZE_URI = "renren.authorize.url";
    private static final String REDIRECT_URL = "renren.redirect.url";
    private static final String ACCESSTOKEN_URI = "renren.access.info.uri";

    private RenRenConfig() {
        init();
    }

    public static synchronized RenRenConfig get() {
        if (instance == null) {
            instance = new RenRenConfig();
        }

        return instance;
    }

    private void init() {
        props = new FiveProps(HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getProviderPropsMap().get(LoginDomain.RENREN));

        if (props != null) {
            renrenApiUrl = props.get(RENREN_API_URL, renrenApiUrl);
            renrenApiVersion = props.get(RENREN_API_VERSION, renrenApiVersion);
            renrenApiKey = props.get(RENREN_API_KEY, renrenApiKey);
            renrenApiSecret = props.get(RENREN_API_SECRET, renrenApiSecret);
            renrenApiId = props.get(RENREN_APP_ID, renrenApiId);
            authorizeUri = props.get(AUTHORIZE_URI, authorizeUri);
            redirectUrl = props.get(REDIRECT_URL, redirectUrl);
            accessTokenUri = props.get(ACCESSTOKEN_URI, accessTokenUri);
        }
    }

    public String getRenrenApiUrl() {
        return renrenApiUrl;
    }

    public String getRenrenApiVersion() {
        return renrenApiVersion;
    }

    public String getRenrenApiKey() {
        return renrenApiKey;
    }

    public String getRenrenApiSecret() {
        return renrenApiSecret;
    }

    public String getRenrenApiId() {
        return renrenApiId;
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
