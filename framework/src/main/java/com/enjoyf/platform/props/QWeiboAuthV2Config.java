package com.enjoyf.platform.props;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ThirdApiHotdeployConfig;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.FiveProps;

/**
 * weibo app 属性文件 todo
 */
public class QWeiboAuthV2Config {
    private static QWeiboAuthV2Config instance;

    private FiveProps props;



    private String qweiboApiKey = "801180308";
    private String qweiboApiSecret = "06a8305487a53c275ac7c4cabb02257e";
     private String redirectUrl = "http://www.joyme.test/profile/sync/qweibo/access";

    private String authorizeUri = "https://open.t.qq.com/cgi-bin/oauth2/authorize";
    private String accessTokenUri = "https://open.t.qq.com/cgi-bin/oauth2/access_token";
    private String qweiboApiUrl = "https://open.t.qq.com/api/";
    private String qweiboApiVersion = "2.0";

    private static final String QQ_CLIENT_ID = "qq.client.id";
    private static final String QQ_CLIENT_SERCRET = "qq.client.sercret";
    private static final String QQ_REDIRECT_URI = "qq.redirect.uri";


    private QWeiboAuthV2Config() {
        init();
    }

    public static synchronized QWeiboAuthV2Config get() {
        if (instance == null) {
            instance = new QWeiboAuthV2Config();
        }

        return instance;
    }

    private void init() {
        props = new FiveProps(HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getProviderPropsMap().get(LoginDomain.QWEIBO));


        qweiboApiKey = props.get(QQ_CLIENT_ID);
        qweiboApiSecret = props.get(QQ_CLIENT_SERCRET);
        redirectUrl= props.get(QQ_REDIRECT_URI);
    }

    public String getQweiboApiSecret() {
        return qweiboApiSecret;
    }

    public String getQweiboApiUrl() {
        return qweiboApiUrl;
    }

    public String getQweiboApiVersion() {
        return qweiboApiVersion;
    }

    public String getQweiboApiKey() {
        return qweiboApiKey;
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
