package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.account.AuthVersion;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.thirdapi.ThirdApiProps;
import com.enjoyf.platform.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * webapp 属性文件
 */
public class PcThirdApiHotdeployConfig extends HotdeployConfig {

    private static final Logger logger = LoggerFactory.getLogger(PcThirdApiHotdeployConfig.class);

    private static final String KEY_SUPPORT_DOMAIN_CODE = "support.provider.code";
    private static final String KEY_AUTH_PROPS = ".auth.props";
    private static final String KEY_AUTH_VERSION = ".auth.vsersion";
    private static final String KEY_AUTH_DISPLAY = ".login.display";
    private static final String KEY_BIND_DISPLAY = ".bind.display";
    private static final String KEY_SUPPORT_SYNC = ".support.sync";
    private static final String KEY_SUPPORT_SHARE = ".support.share";
    private static final String KEY_DEFAULT_RURL = ".default.rurl";


    private SyncCache cache;

    public PcThirdApiHotdeployConfig() {
        super(EnvConfig.get().getPcSyncHotdeployConfFile());
        init();
    }

    @Override
    public synchronized void reload() {
        super.reload();

        List<String> providerCodes = getList(KEY_SUPPORT_DOMAIN_CODE);
        Map<String, LoginDomain> providerMap = new LinkedHashMap<String, LoginDomain>();
        Map<LoginDomain, String> providerPropsMap = new HashMap<LoginDomain, String>();
        Map<LoginDomain, AuthVersion> authVersionMap = new HashMap<LoginDomain, AuthVersion>();
        Map<LoginDomain, ThirdApiProps> thirdApiPropsMap = new HashMap<LoginDomain, ThirdApiProps>();

        for (String providerCode : providerCodes) {
            LoginDomain syncProvider = LoginDomain.getByCode(providerCode);
            if (syncProvider == null) {
                continue;
            }

            String propsSrc = getString(providerCode + KEY_AUTH_PROPS);
            if (!StringUtil.isEmpty(propsSrc)) {
                providerPropsMap.put(syncProvider, propsSrc);
            }

            String authVersionCode = getString(providerCode + KEY_AUTH_VERSION);
            AuthVersion syncAuthVersion = AuthVersion.getByCode(authVersionCode);
            if (syncAuthVersion != null) {
                authVersionMap.put(syncProvider, syncAuthVersion);
            }

            boolean loginDisplay = getBoolean(providerCode + KEY_AUTH_DISPLAY, true);
            boolean bindDisplay = getBoolean(providerCode + KEY_BIND_DISPLAY, true);
            boolean supportSync = getBoolean(providerCode + KEY_SUPPORT_SYNC, true);
            boolean supportShare = getBoolean(providerCode + KEY_SUPPORT_SHARE, false);
            String defaultRurl = getString(providerCode + KEY_DEFAULT_RURL, "/");

            ThirdApiProps thirdApiProps = new ThirdApiProps();
            thirdApiProps.setLoginDomain(syncProvider);
            thirdApiProps.setBindDisplay(bindDisplay);
            thirdApiProps.setLoginDisplay(loginDisplay);
            thirdApiProps.setSupportSync(supportSync);
            thirdApiProps.setDefaultRurl(defaultRurl);
            thirdApiProps.setSupportShare(supportShare);
            thirdApiPropsMap.put(syncProvider, thirdApiProps);

            providerMap.put(providerCode, syncProvider);
        }
        SyncCache tmpCache = new SyncCache();
        tmpCache.setProviderMap(providerMap);
        tmpCache.setProviderPropsMap(providerPropsMap);
        tmpCache.setAuthVersionMap(authVersionMap);
        tmpCache.setThirdApiPropsMap(thirdApiPropsMap);

        this.cache = tmpCache;

        logger.info("Event Props init finished." + cache);
    }

    public void init() {
        reload();
    }

    public Map<String, LoginDomain> getProviderMap() {
        return cache.getProviderMap();
    }

    public Map<LoginDomain, String> getProviderPropsMap() {
        return cache.getProviderPropsMap();
    }

    public Map<LoginDomain, AuthVersion> getAuthVersionMap() {
        return cache.getAuthVersionMap();
    }

    public Map<LoginDomain, ThirdApiProps> getThirdApiPropsMap() {
        return cache.getThirdApiPropsMap();
    }

    public ThirdApiProps getThirdApiPropByAccountDomain(LoginDomain accountDomain) {
        return getThirdApiPropsMap().get(accountDomain);
    }


    private class SyncCache {
        private Map<String, LoginDomain> providerMap = new LinkedHashMap<String, LoginDomain>();
        private Map<LoginDomain, String> providerPropsMap = new HashMap<LoginDomain, String>();
        private Map<LoginDomain, AuthVersion> authVersionMap = new HashMap<LoginDomain, AuthVersion>();
        private Map<LoginDomain, ThirdApiProps> thirdApiPropsMap = new HashMap<LoginDomain, ThirdApiProps>();

        public Map<String, LoginDomain> getProviderMap() {
            return providerMap;
        }

        public void setProviderMap(Map<String, LoginDomain> providerMap) {
            this.providerMap = providerMap;
        }

        public Map<LoginDomain, String> getProviderPropsMap() {
            return providerPropsMap;
        }

        public void setProviderPropsMap(Map<LoginDomain, String> providerPropsMap) {
            this.providerPropsMap = providerPropsMap;
        }

        public Map<LoginDomain, AuthVersion> getAuthVersionMap() {
            return authVersionMap;
        }

        public void setAuthVersionMap(Map<LoginDomain, AuthVersion> authVersionMap) {
            this.authVersionMap = authVersionMap;
        }

        public Map<LoginDomain, ThirdApiProps> getThirdApiPropsMap() {
            return thirdApiPropsMap;
        }

        public void setThirdApiPropsMap(Map<LoginDomain, ThirdApiProps> thirdApiPropsMap) {
            this.thirdApiPropsMap = thirdApiPropsMap;
        }
    }

    public static void main(String[] args) {
        HotdeployConfigFactory.get().getConfig(PcThirdApiHotdeployConfig.class);
    }
}
