package com.enjoyf.platform.serv.sync.processor;

import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.account.AuthVersion;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.SyncServiceException;
import com.enjoyf.platform.service.usercenter.LoginDomain;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SyncProcessorFactory {
    private static Map<String, SyncProcessor> syncCodeMap = new HashMap<String, SyncProcessor>();

    private static SyncProcessorFactory instance;

    public synchronized static SyncProcessorFactory get() {
        if (instance == null) {
            instance = new SyncProcessorFactory();
        }
        return instance;
    }

    private SyncProcessorFactory() {
        syncCodeMap.put(AccountDomain.SYNC_SINA_WEIBO.getCode() + AuthVersion.SYNC_AUTH_VERSION_AUTH20.getCode(), new SinaSyncProcessor());
        syncCodeMap.put(AccountDomain.SYNC_QQ_WEIBO.getCode() + AuthVersion.SYNC_AUTH_VERSION_AUTH10.getCode(), new QqWeiboSyncProcessor());
        syncCodeMap.put(AccountDomain.SYNC_QQ_WEIBO.getCode() + AuthVersion.SYNC_AUTH_VERSION_AUTH20.getCode(), new QweiboV2SyncProcessor());
        syncCodeMap.put(AccountDomain.SYNC_RENREN.getCode() + AuthVersion.SYNC_AUTH_VERSION_AUTH20.getCode(), new RenRenSyncProcessor());
        syncCodeMap.put(AccountDomain.SYNC_QQ.getCode() + AuthVersion.SYNC_AUTH_VERSION_AUTH20.getCode(), new QqZoneSyncProcessor());

    }

    public SyncProcessor getByProviderAndVsersion(LoginDomain loginDomain, AuthVersion version) throws ServiceException {
        String key = loginDomain.getCode() + version.getCode();

        if (!syncCodeMap.containsKey(key)) {
            throw new ServiceException(SyncServiceException.SYNC_UNSUPPORT_APP_SERVICE_EXCEPTION, "provider is:" + loginDomain);
        }

        return syncCodeMap.get(key);
    }
}
