package com.enjoyf.platform.props;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ThirdApiHotdeployConfig;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.FiveProps;

/**
 * youku 属性文件
 */
public class YoukuConfig {
    private static YoukuConfig instance;

    private FiveProps props;


    private String apiKey = "";
    private String apiSecret = "";
    private String redirectUrl = "";

    private static final String CLIENT_ID = "youku.client.id";
    private static final String CLIENT_SERCRET = "youku.client.sercret";
    private static final String REDIRECT_URI = "youku.redirect.uri";


    private YoukuConfig() {
        init();
    }

    public static synchronized YoukuConfig get() {
        if (instance == null) {
            instance = new YoukuConfig();
        }

        return instance;
    }

    private void init() {
        props = new FiveProps(HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getProviderPropsMap().get(LoginDomain.YOUKU));

        apiKey = props.get(CLIENT_ID);
        apiSecret = props.get(CLIENT_SERCRET);
        redirectUrl = props.get(REDIRECT_URI);
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
