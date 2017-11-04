package com.enjoyf.platform.thirdapi.oauth;

import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.account.AuthVersion;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.SyncServiceException;
import com.enjoyf.platform.service.usercenter.LoginDomain;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:第三方同步接口的单例类
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class OAuthInterfaceSngl {

    private static Map<String, IOauthApi> syncCodeMap = new HashMap<String, IOauthApi>();

    private static OAuthInterfaceSngl instance;

    public synchronized static OAuthInterfaceSngl get() {
        if (instance == null) {
            instance = new OAuthInterfaceSngl();
        }
        return instance;
    }

    private OAuthInterfaceSngl() {
        syncCodeMap.put(LoginDomain.SINAWEIBO.getCode() + AuthVersion.SYNC_AUTH_VERSION_AUTH20.getCode(), new SinaOauthApiImpl());
        syncCodeMap.put(LoginDomain.QWEIBO.getCode() + AuthVersion.SYNC_AUTH_VERSION_AUTH20.getCode(), new QWeiboV2OauthApiImpl());
        syncCodeMap.put(LoginDomain.RENREN.getCode() + AuthVersion.SYNC_AUTH_VERSION_AUTH20.getCode(), new RenRenOauthApiImpl());
        syncCodeMap.put(LoginDomain.QQ.getCode() + AuthVersion.SYNC_AUTH_VERSION_AUTH20.getCode(), new QqV2OauthApiImpl());
        syncCodeMap.put(LoginDomain.WXLOGIN.getCode() + AuthVersion.SYNC_AUTH_VERSION_AUTH20.getCode(), new WeixinOauthApiImpl());
        syncCodeMap.put(LoginDomain.WXSERVICE.getCode() + AuthVersion.SYNC_AUTH_VERSION_AUTH20.getCode(), new WeixinServOauthApiImpl());
    }

    @Deprecated
    public IOauthApi getApiByProvider(AccountDomain provider, AuthVersion version) throws ServiceException {
        String key = provider.getCode() + version.getCode();

        if (!syncCodeMap.containsKey(key)) {
            throw new ServiceException(SyncServiceException.SYNC_UNSUPPORT_APP_SERVICE_EXCEPTION, "appcode is:" + provider);
        }

        return syncCodeMap.get(key);
    }


    public IOauthApi getApiByProvider(LoginDomain loginDomain, AuthVersion version) throws ServiceException {
        String key = loginDomain.getCode() + version.getCode();

        if (!syncCodeMap.containsKey(key)) {
            throw new ServiceException(SyncServiceException.SYNC_UNSUPPORT_APP_SERVICE_EXCEPTION, "appcode is:" + loginDomain);
        }

        return syncCodeMap.get(key);
    }


}
