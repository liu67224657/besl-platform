package com.enjoyf.platform.serv.sync.processor;

import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.account.AuthVersion;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.thirdapi.oauth.IOauthApi;
import com.enjoyf.platform.thirdapi.oauth.OAuthInterfaceSngl;
import com.enjoyf.platform.thirdapi.oauth.OAuthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class QqZoneSyncProcessor implements SyncProcessor {
    Logger logger = LoggerFactory.getLogger(SinaSyncProcessor.class);


    @Override
    public boolean processShare(TokenInfo tokenInfo, SyncContent syncContent) throws ServiceException {

        IOauthApi oauthApi = OAuthInterfaceSngl.get().getApiByProvider(AccountDomain.SYNC_QQ, AuthVersion.SYNC_AUTH_VERSION_AUTH20);
        OAuthStatus oAuthStatus = oauthApi.syncContent(tokenInfo, syncContent);

        boolean returnBoolen = oAuthStatus.equals(oAuthStatus.OAUTH_SUCCESS);
        if (!returnBoolen) {
            logger.error("oauth qq content failed.status:" + oAuthStatus);
        }
        return returnBoolen;
    }
}
