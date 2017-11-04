/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.joyme.weblogic.oauth.tokenprovider;

import com.enjoyf.platform.service.oauth.AuthGrantType;
import com.enjoyf.webapps.joyme.weblogic.oauth.OAuthTokenProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-10 下午3:10
 * Description:
 */
public class TokenProviderFactory {
    //
    private static Map<AuthGrantType, OAuthTokenProvider> tokenProviderMap = new HashMap<AuthGrantType, OAuthTokenProvider>();

    static {
        tokenProviderMap.put(AuthGrantType.REFRESH_TOKEN, new TokenProviderRefreshToken());
    }

    //
    public static OAuthTokenProvider factory(AuthGrantType grantType) {
        return tokenProviderMap.get(grantType);
    }
}
