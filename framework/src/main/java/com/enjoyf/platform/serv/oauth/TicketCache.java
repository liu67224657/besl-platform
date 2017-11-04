/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.oauth;

import com.enjoyf.platform.service.oauth.OAuthConstants;
import com.enjoyf.platform.service.oauth.OAuthInfo;
import com.enjoyf.platform.service.oauth.Sticket;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
class TicketCache {

    private static final long TIME_OUT_SEC = 60l * 12l;

    private static final long TIME_OUT_SEC_OAUTH2 = 60L * 60L * 1000L;

    private static final long TIME_OUT_SEC_SOCIALAPIINTERCEPTOR = 30L;


    private String serviceSection;

    private static final String PREFIX_OAUTH_ST = "_oauth_st_";


    private MemCachedConfig config;

    private MemCachedManager manager;

    TicketCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
        this.serviceSection = OAuthConstants.SERVICE_SECTION;
    }

    //////////////////////////////////////////////////////////
    public void putSTicket(Sticket st) {
        manager.put(serviceSection + PREFIX_OAUTH_ST + st.getUno(), st, TIME_OUT_SEC);
    }

    public Sticket getSTicket(String uno) {
        Object object = manager.get(serviceSection + PREFIX_OAUTH_ST + uno);
        if (object == null) {
            return null;
        }
        return (Sticket) object;
    }

    public boolean deleteSTicket(String uno) {
        return manager.remove(serviceSection + PREFIX_OAUTH_ST + uno);
    }


    //////////////////////////////////////////////////////////
    public void putOAuthInfo(OAuthInfo oAuthInfo) {
        manager.put(oAuthInfo.getAccess_token() + oAuthInfo.getApp_key(), oAuthInfo, TIME_OUT_SEC_OAUTH2);
    }

    public OAuthInfo getOauthInfo(String accessToken, String appKey) {
        Object object = manager.get(accessToken + appKey);
        if (object == null) {
            return null;
        }
        return (OAuthInfo) object;
    }

    public boolean deleteOAuthInfo(String accessToken, String appKey) {
        return manager.remove(accessToken + appKey);
    }


    ////////////////
    public void putSocialAPI(String uno, String time) {
        manager.put(uno + time, time, TIME_OUT_SEC_SOCIALAPIINTERCEPTOR);
    }

    public String getSocialAPI(String uno, String time) {
        Object object = manager.get(uno + time);
        if (object == null) {
            return "";
        }
        return (String) object;
    }

    public boolean removeSocialAPI(String uno, String time) {
        return manager.remove(uno + time);
    }
}
