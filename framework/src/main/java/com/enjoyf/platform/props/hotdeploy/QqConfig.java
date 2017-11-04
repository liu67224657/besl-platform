package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.util.FiveProps;

/**
 * weibo app 属性文件
 *
 */
public class QqConfig {
    private static QqConfig instance;

    private FiveProps props;

    private String qqAppKey = "100292513";
    private String qqAppSecret = "d1cdb01b266ee668855029a593223a88";

    private String redirectUrl = "http://www.joyme.com/profile/sync/qzone/access";


    private static final String QQ_APP_ID = "qq.app.id";
    private static final String QQ_CLIENT_SERCRET = "qq.app.sercret";
    private static final String QQ_REDIRECT_URI = "qq.redirect.uri";


    public QqConfig() {
        init();
    }

    public static synchronized QqConfig get() {
        if (instance == null) {
            instance = new QqConfig();
        }

        return instance;
    }

    private void init() {
        props = new FiveProps(HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getProviderPropsMap().get(AccountDomain.SYNC_QQ));

        qqAppKey = props.get(QQ_APP_ID);
        qqAppSecret = props.get(QQ_CLIENT_SERCRET);
        redirectUrl = props.get(QQ_REDIRECT_URI);
    }

    public String getQqAppSecret() {
        return qqAppSecret;
    }

    public String getQqAppKey() {
        return qqAppKey;
    }


    public String getRedirectUrl() {
        return redirectUrl;
    }
}
