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
public class SinaSyncProcessor implements SyncProcessor {
    private Logger logger = LoggerFactory.getLogger(SinaSyncProcessor.class);

    @Override
    public boolean processShare(TokenInfo tokenInfo, SyncContent syncContent) throws ServiceException {
        IOauthApi oauthApi = OAuthInterfaceSngl.get().getApiByProvider(AccountDomain.SYNC_SINA_WEIBO, AuthVersion.SYNC_AUTH_VERSION_AUTH20);

        OAuthStatus oAuthStatus = oauthApi.syncContent(tokenInfo, syncContent);

        if (oAuthStatus.equals(oAuthStatus.OAUTH_TOKEN_EXPIRE)) {
            return false;
        }
        return oAuthStatus.equals(oAuthStatus.OAUTH_SUCCESS);
    }


}
