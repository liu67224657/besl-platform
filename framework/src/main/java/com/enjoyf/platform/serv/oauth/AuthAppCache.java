/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.oauth;

import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.GameChannelConfig;
import com.enjoyf.platform.service.oauth.GameChannelInfo;
import com.enjoyf.platform.service.oauth.OAuthConstants;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
public class AuthAppCache {

    private static final long TIME_OUT_SEC = 60l * 60l * 12l;

    private static final long TIME_OUT_SEC_OAUTH2 = 60L * 5L;

    private static final long APPCHANNELINFO_TIME_OUT_SEC = 60l * 60l * 12l;

    private static final long APPCHANNELCONFIG_TIME_OUT_SEC = 60l * 60l * 12l;

    private String serviceSection;

    private static final String PREFIX_OAUTH_ST = "_oauth_app_";
    private static final String PREFIX_OAUTH_GAMEKEY = "_oauth_app_gk_";

    private static final String PREFIX_OAUTH_CHANNELINFO = "_oauth_app_ci_";

    private static final String PREFIX_OAUTH_CHANNELCONFIG = "_oauth_app_cc_";

    private MemCachedConfig config;

    private MemCachedManager manager;

    public AuthAppCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
        this.serviceSection = OAuthConstants.SERVICE_SECTION;
    }

    /////////////////////////////////////////////////////////
    public AuthApp get(String appId) {
        Object object = manager.get(serviceSection + PREFIX_OAUTH_ST + appId);
        if (object == null) {
            return null;
        }
        return (AuthApp) object;
    }

    public void put(String appId, AuthApp returnValue) {
        manager.put(serviceSection + PREFIX_OAUTH_ST + appId, returnValue, TIME_OUT_SEC);
        if (!StringUtil.isEmpty(returnValue.getGameKey())) {
            manager.put(serviceSection + PREFIX_OAUTH_GAMEKEY + returnValue.getGameKey(), returnValue, TIME_OUT_SEC);
        }
    }

    public boolean remove(String appId) {
        AuthApp authApp = get(appId);
        if (authApp != null && !StringUtil.isEmpty(authApp.getGameKey())) {
            manager.remove(serviceSection + PREFIX_OAUTH_GAMEKEY + authApp.getGameKey());
        }
        return manager.remove(serviceSection + PREFIX_OAUTH_ST + appId);
    }

    public AuthApp getByGameKey(String gameKey) {
        Object object = manager.get(serviceSection + PREFIX_OAUTH_GAMEKEY + gameKey);
        if (object == null) {
            return null;
        }
        return (AuthApp) object;
    }

    //////////////////
    public void putChannelInfo(GameChannelInfo channelInfo) {
        manager.put(serviceSection + PREFIX_OAUTH_CHANNELINFO + channelInfo.getInfoId(), channelInfo, APPCHANNELINFO_TIME_OUT_SEC);
    }

    public GameChannelInfo getChannelInfo(String infoId) {
        Object object = manager.get(serviceSection + PREFIX_OAUTH_CHANNELINFO + infoId);
        if (object == null) {
            return null;
        }
        return (GameChannelInfo) object;
    }

    public boolean removeChannelInfo(String infoId) {
        return manager.remove(serviceSection + PREFIX_OAUTH_CHANNELINFO + infoId);
    }
    //////////////////
    public void putChannelConfig(GameChannelConfig config) {
        manager.put(serviceSection + PREFIX_OAUTH_CHANNELCONFIG + config.getConfigId(), config, APPCHANNELCONFIG_TIME_OUT_SEC);
    }

    public GameChannelConfig getChannelConfig(String configId) {
        Object object = manager.get(serviceSection + PREFIX_OAUTH_CHANNELCONFIG + configId);
        if (object == null) {
            return null;
        }
        return (GameChannelConfig) object;
    }

    public boolean removeChannelConfig(String configId) {
        return manager.remove(serviceSection + PREFIX_OAUTH_CHANNELCONFIG + configId);
    }
}
