package com.enjoyf.platform.props;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ThirdApiHotdeployConfig;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.FiveProps;

/**
 * weibo app 属性文件 todo
 */
public class WeixinServConfig {
    private static WeixinServConfig instance;

    private FiveProps props;


    private String apiKey = "";
    private String apiSecret = "";
    private String redirectUrl = "";

    private static final String QQ_CLIENT_ID = "wx.client.id";
    private static final String QQ_CLIENT_SERCRET = "wx.client.sercret";
    private static final String QQ_REDIRECT_URI = "wx.redirect.uri";


    private WeixinServConfig() {
        init();
    }

    public static synchronized WeixinServConfig get() {
        if (instance == null) {
            instance = new WeixinServConfig();
        }

        return instance;
    }

    private void init() {
        props = new FiveProps(HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getProviderPropsMap().get(LoginDomain.WXSERVICE));


        apiKey = props.get(QQ_CLIENT_ID);
        apiSecret = props.get(QQ_CLIENT_SERCRET);
        redirectUrl = props.get(QQ_REDIRECT_URI);
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
