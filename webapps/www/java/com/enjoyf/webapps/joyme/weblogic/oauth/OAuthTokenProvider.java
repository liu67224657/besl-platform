/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.joyme.weblogic.oauth;

import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.webapps.common.oauth.OAuthException;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-8 下午5:58
 * Description:
 */
public interface OAuthTokenProvider {
    public AuthToken accessToken(TokenAccessRequest request) throws OAuthException;
}
