package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.AuthAppTypeConfig;
import com.enjoyf.platform.service.oauth.AuthGrantType;
import com.enjoyf.platform.service.oauth.AuthResponseType;
import com.enjoyf.platform.service.oauth.AuthTokenConfig;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="mailto:yinpengyi@enjoyf.com">Yin Pengyi</a>
 */
public class OAuthHotdeployConfig extends HotdeployConfig {
    //
    private static final Logger logger = LoggerFactory.getLogger(OAuthHotdeployConfig.class);

    /////////////////////////////////////////////////////////////////////////////
    //the keys
    private static final String KEY_OAUTH_APPTYPE_LIST = "oauth.app.type.list";

    //
    private static final String SUFFIX_KEY_APP_GRANT_TYPES = ".grant.types";
    private static final String SUFFIX_KEY_APP_RESPONSE_TYPES = ".response.types";
    private static final String SUFFIX_KEY_APP_REFRESH_SUPPORT = ".refresh.support";

    //
    private static final String SUFFIX_KEY_ACCESSTOKEN_LIFE_SECS = ".accesstoken.life.secs";
    private static final String SUFFIX_KEY_ACCESSTOKEN_MAX_ACCESS_TIMES = ".accesstoken.max.access.times";

    private static final String SUFFIX_KEY_REFRESHTOKEN_LIFE_SECS = ".refreshtoken.life.secs";
    private static final String SUFFIX_KEY_REFRESHTOKEN_MAX_ACCESS_TIMES = ".refreshtoken.max.access.times";

    //
    private Cached cached;

    public OAuthHotdeployConfig() {
        super(EnvConfig.get().getOauthHotdeployConfigureFile());
    }

    @Override
    public void init() {
        reload();
    }

    public synchronized void reload() {
        super.reload();

        Map<AuthAppType, AuthAppTypeConfig> appTypeConfigMap = new HashMap<AuthAppType, AuthAppTypeConfig>();

        //
        List<String> appTypeCodes = getList(KEY_OAUTH_APPTYPE_LIST);
        for (String appTypeCode : appTypeCodes) {
            AuthAppType appType = AuthAppType.getByCode(appTypeCode);

            if (appType != null) {
                AuthAppTypeConfig appTypeConfig = new AuthAppTypeConfig(appType);

                //get the grant types.
                List<String> grantTypeCodes = getList(appTypeCode + SUFFIX_KEY_APP_GRANT_TYPES);
                for (String grantTypeCode : grantTypeCodes) {
                    AuthGrantType grantType = AuthGrantType.getByCode(grantTypeCode);

                    if (grantType != null) {
                        appTypeConfig.getSupportGrantTypes().add(grantType);
                    } else {
                        GAlerter.lab("There is a configure error in the OAuthHotdeployConfig's grantType, the grantType is " + grantTypeCode);
                    }
                }

                //get the response types.
                List<String> responseTypeCodes = getList(appTypeCode + SUFFIX_KEY_APP_RESPONSE_TYPES);
                for (String responseTypeCode : responseTypeCodes) {
                    AuthResponseType responseType = AuthResponseType.getByCode(responseTypeCode);

                    if (responseType != null) {
                        appTypeConfig.getSupportResponseTypes().add(responseType);
                    } else {
                        GAlerter.lab("There is a configure error in the OAuthHotdeployConfig's responseType, the responseType is " + responseTypeCode);
                    }
                }

                //get the access token setting.
                AuthTokenConfig accessTokenConfig = new AuthTokenConfig();

                accessTokenConfig.setExpireTimeSecs(getLong(appTypeCode + SUFFIX_KEY_ACCESSTOKEN_LIFE_SECS, accessTokenConfig.getExpireTimeSecs()));
                accessTokenConfig.setMaxAccessTimes(getInt(appTypeCode + SUFFIX_KEY_ACCESSTOKEN_MAX_ACCESS_TIMES, accessTokenConfig.getMaxAccessTimes()));

                appTypeConfig.setAccessTokenConfig(accessTokenConfig);

                //get the refresh token setting.
                appTypeConfig.setSupportRefreshToken(getBoolean(appTypeCode + SUFFIX_KEY_APP_REFRESH_SUPPORT, false));
                if (appTypeConfig.isSupportRefreshToken()) {
                    AuthTokenConfig refreshTokenConfig = new AuthTokenConfig();

                    refreshTokenConfig.setExpireTimeSecs(getLong(appTypeCode + SUFFIX_KEY_REFRESHTOKEN_LIFE_SECS, accessTokenConfig.getExpireTimeSecs()));
                    refreshTokenConfig.setMaxAccessTimes(getInt(appTypeCode + SUFFIX_KEY_REFRESHTOKEN_MAX_ACCESS_TIMES, accessTokenConfig.getMaxAccessTimes()));

                    appTypeConfig.setRefreshTokenConfig(refreshTokenConfig);
                }

                //put it into the map
                appTypeConfigMap.put(appType, appTypeConfig);
            } else {
                GAlerter.lab("There is a configure error in the OAuthHotdeployConfig'appType, the appType is " + appTypeCode);
            }
        }

        //
        Cached tmpCache = new Cached();

        tmpCache.setAppTypeConfigMap(appTypeConfigMap);

        //
        this.cached = tmpCache;

        logger.info("OAuth Props init finished, the props is " + getProps());
    }

    public AuthAppTypeConfig getAppTypeConfig(AuthAppType appType) {
        return cached.getAppTypeConfigMap().get(appType);
    }

    //
    private class Cached {
        private Map<AuthAppType, AuthAppTypeConfig> appTypeConfigMap = new HashMap<AuthAppType, AuthAppTypeConfig>();

        public Map<AuthAppType, AuthAppTypeConfig> getAppTypeConfigMap() {
            return appTypeConfigMap;
        }

        public void setAppTypeConfigMap(Map<AuthAppType, AuthAppTypeConfig> appTypeConfigMap) {
            this.appTypeConfigMap = appTypeConfigMap;
        }
    }

}
