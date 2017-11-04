/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.joyme.weblogic.oauth.tokenprovider;

import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.webapps.common.oauth.OAuthException;
import com.enjoyf.webapps.joyme.weblogic.oauth.OAuthTokenProvider;
import com.enjoyf.webapps.joyme.weblogic.oauth.TokenAccessRequest;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-8 下午6:00
 * Description:
 */
public class TokenProviderAuthCode implements OAuthTokenProvider {
    @Override
    public AuthToken accessToken(TokenAccessRequest request) throws OAuthException {
        return null;
    }
}
