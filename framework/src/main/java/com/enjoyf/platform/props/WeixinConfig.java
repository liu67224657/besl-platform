package com.enjoyf.platform.props;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ThirdApiHotdeployConfig;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.FiveProps;

/**
 * weibo app 属性文件 todo
 */
@Deprecated
public class WeixinConfig {
    private static WeixinConfig instance;

    private FiveProps props;


    private String apiKey = "801180308";
    private String apiSecret = "06a8305487a53c275ac7c4cabb02257e";
    private String redirectUrl = "http://www.joyme.test/profile/sync/qweibo/access";

    private String accessTokenUri = "https://open.t.qq.com/cgi-bin/oauth2/access_token";

    private static final String QQ_CLIENT_ID = "wx.client.id";
    private static final String QQ_CLIENT_SERCRET = "wx.client.sercret";
    private static final String QQ_REDIRECT_URI = "wx.redirect.uri";


    private WeixinConfig() {
        init();
    }

    public static synchronized WeixinConfig get() {
        if (instance == null) {
            instance = new WeixinConfig();
        }

        return instance;
    }

    private void init() {
        props = new FiveProps(HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getProviderPropsMap().get(LoginDomain.WXLOGIN));


        apiKey = props.get(QQ_CLIENT_ID);
        apiSecret = props.get(QQ_CLIENT_SERCRET);
        redirectUrl = props.get(QQ_REDIRECT_URI);
    }


    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getApiKey() {
        return apiKey;
    }
}
