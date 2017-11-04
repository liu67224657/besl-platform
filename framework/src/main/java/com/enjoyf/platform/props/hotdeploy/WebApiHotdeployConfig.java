package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.joymeapp.AppCertificateInfo;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppPushChannel;
import com.enjoyf.platform.service.joymeapp.AppVersionInfo;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-5-30
 * Time: 下午3:00
 * To change this template use File | Settings | File Templates.
 */
public class WebApiHotdeployConfig extends HotdeployConfig {
    //
    private static final Logger logger = LoggerFactory.getLogger(WebApiHotdeployConfig.class);

    private static final String KEY_VERSION_UPDATE_TYPE_LIST = "version.update.type.list";
    private static final String KEY_MOBILE_CLIENT_TYPE_LIST = "mobile.client.type.list";

    private static final String KEY_VERSION_HISTORY_LIST = "version.history.list";
    private static final String KEY_LATEST_VERSION_NUM = "latest.version.num";
    private static final String KEY_LATEST_VERSION_DESC = "latest.version.desc";
    private static final String KEY_LATEST_VERSION_VER = "latest.version.ver";
    private static final String SUFFIX_TO_LATEST_VERSION_UPDATE_URL = "to.latest.version.update.url";
    private static final String SUFFIX_TO_LATEST_VERSION_UPDATE_TYPE = "to.latest.version.update.type";

    private static final String KEY_CERTIFICATE_PATH = "certificate.path";
    private static final String KEY_CERTIFICATE_PASSWORD = "certificate.password";
    private static final String KEY_CERTIFICATE_PRODUCTION = "certificate.production";

    private static final String KEY_IOS_TEST_CLIENT = "ios.test.client";
    private static final String KEY_ANDROID_TEST_CLIENT = "android.test.client";

    /////////////////////////////////////////////////////
    private static final String CLIENT_APPKEY_LIST = "client.appkey.list";
    private static final String KEY_VERSION_NUBMER = ".version.number";
    private static final String KEY_VERSION_URL = ".version.url";
    private static final String KEY_VERSION_INFO = ".verison.info";
    private static final String KEY_VERSION_TYPE = ".version.type";


    private static final String KEY_CERTIFICATE_LIST = "client.certificate.path";
    private static final String KEY_APP_CERTIFICATE_PATH = ".certificate.path";
    private static final String KEY_APP_CERTIFICATE_PASSWORD = ".certificate.password";
    private static final String KEY_APP_ISPRODUCTION = ".certificate.isproduction";

    private static final String KEY_APP_MENU_LIST = "client.menu.list";
    private static final String KEY_APP_MENU = ".menu.code";

    private static final String DEFAULT_SEPARATOR = ".";

    /////////////////////
    private static final String KEY_UMENG_PRODUCTION_MODE = "push.umeng.production.mode";
    private static final String KEY_PUSH_VERSION_LIST = ".push.version.list";
    private static final String KEY_UMENG_APP_KEY = ".umeng.appkey";
    private static final String KEY_UMENG_MASTER_SECRET = ".umeng.master.secret";

    private static final String KEY_SOLR_SERVER_URI = "solr.server.uri";
    private static final String KEY_SOLR_ANALYSIS_URI = "solr.analysis.uri";

    private static final String KEY_REDIS_SERVER_HOST = "redis.server.host";
    private static final String KEY_REDIS_SERVER_PORT = "redis.server.port";

    private static final String KEY_SEARCH_SERVER_API = "search.server.api";

    private Cached cached;
    private String searchServerApi;

    public WebApiHotdeployConfig() {
        super(EnvConfig.get().getWebApiHotdeployConfigureFile());
    }

    @Override
    public void init() {
        reload();
    }

    @Override
    public synchronized void reload() {
        super.reload();

        Cached tmpCache = new Cached();

        // def update type
        List<String> verUpdateTypeList = this.getList(KEY_VERSION_UPDATE_TYPE_LIST);

        Map<String, String> verUpdateTypeMap = new HashMap<String, String>();
        for (String upType : verUpdateTypeList) {
            verUpdateTypeMap.put(upType, upType);
        }
        tmpCache.setDefUpdateTypeMap(verUpdateTypeMap);

        // mobile client type
        List<String> mobileClientTypeList = this.getList(KEY_MOBILE_CLIENT_TYPE_LIST);

        Map<String, Map<String, String>> historyVersionMap = new HashMap<String, Map<String, String>>();
        Map<String, LastVersion> lastVersionMap = new HashMap<String, LastVersion>();
        Map<String, UpdateToLastVersion> updateTOLastVersionMap = new HashMap<String, UpdateToLastVersion>();

        for (String mobileClientType : mobileClientTypeList) {
            // last version
            String latestVersionNum = this.getString(mobileClientType + DEFAULT_SEPARATOR + KEY_LATEST_VERSION_NUM);
            String latestVersionVer = this.getString(mobileClientType + DEFAULT_SEPARATOR + KEY_LATEST_VERSION_VER);
            String latestVersionDesc = this.getString(mobileClientType + DEFAULT_SEPARATOR + KEY_LATEST_VERSION_DESC);

            LastVersion lastVersion = new LastVersion();
            lastVersion.setLatestVersionNum(latestVersionNum);
            lastVersion.setLatestVersionVer(latestVersionVer);
            lastVersion.setLatestVersionDesc(latestVersionDesc);

            lastVersionMap.put(mobileClientType, lastVersion);


            // history version and to last version update type
            List<String> historyVerList = this.getList(mobileClientType + DEFAULT_SEPARATOR + KEY_VERSION_HISTORY_LIST);

            Map<String, String> historyVerMap = new HashMap<String, String>();
            for (String ver : historyVerList) {
                historyVerMap.put(ver, ver);

                // to last version update type
                if (!ver.equals(latestVersionNum)) {
                    UpdateToLastVersion updateToLastVersion = new UpdateToLastVersion();

                    String toLatestUpType = this.getString(ver + DEFAULT_SEPARATOR + mobileClientType + DEFAULT_SEPARATOR + SUFFIX_TO_LATEST_VERSION_UPDATE_TYPE);
                    String toLatestUpUrl = this.getString(ver + DEFAULT_SEPARATOR + mobileClientType + DEFAULT_SEPARATOR + SUFFIX_TO_LATEST_VERSION_UPDATE_URL);

                    updateToLastVersion.setToLatestVerUpdateType(toLatestUpType);
                    updateToLastVersion.setToLatestVerUpdateUrl(toLatestUpUrl);

                    updateTOLastVersionMap.put(ver + DEFAULT_SEPARATOR + mobileClientType, updateToLastVersion);
                }

            }

            historyVersionMap.put(mobileClientType, historyVerMap);
        }

        //push
        int umengProductionMode = getInt(KEY_UMENG_PRODUCTION_MODE);
        tmpCache.setUmengPushProductionMode(umengProductionMode);

        Map<String, AppVersionInfo> versionInfoMap = new HashMap<String, AppVersionInfo>();
        List<String> clientAppKeyList = getList(CLIENT_APPKEY_LIST);
        Map<String, List<String>> pushVersionMap = new HashMap<String, List<String>>();
        Map<String, String> umengKeyMap = new HashMap<String, String>();
        Map<String, String> umengMasterSecretMap = new HashMap<String, String>();
        for (String appKey : clientAppKeyList) {
            String verisonNum = getString(appKey + KEY_VERSION_NUBMER);
            String versionUrl = getString(appKey + KEY_VERSION_URL);
            String versionInfo = getString(appKey + KEY_VERSION_INFO);
            String versionType = getString(appKey + KEY_VERSION_TYPE);

            if (!StringUtil.isEmpty(verisonNum) && !StringUtil.isEmpty(versionUrl)) {
                AppVersionInfo info = new AppVersionInfo(appKey, verisonNum, versionUrl, versionInfo);
                if (!StringUtil.isEmpty(versionType)) {
                    info.setUpdate_type(Integer.valueOf(versionType));
                }
                versionInfoMap.put(appKey, info);
            }

            Collection<AppPushChannel> pushChannelColl = AppPushChannel.getAll();
            for (AppPushChannel pushChannel : pushChannelColl) {
                List<String> pushVersionList = getList(appKey + "." + pushChannel.getCode() + KEY_PUSH_VERSION_LIST);
                if (!CollectionUtil.isEmpty(pushVersionList)) {
                    pushVersionMap.put(appKey + "." + pushChannel.getCode(), pushVersionList);
                }
            }

            String umengKey = getString(appKey + KEY_UMENG_APP_KEY);
            String umengMasterSecret = getString(appKey + KEY_UMENG_MASTER_SECRET);
            if (!StringUtil.isEmpty(umengKey) && !StringUtil.isEmpty(umengMasterSecret)) {
                umengKeyMap.put(appKey, umengKey);
                umengMasterSecretMap.put(appKey, umengMasterSecret);
            }
        }
        tmpCache.setAppVersionInfoMap(versionInfoMap);
        tmpCache.setPushVersionMap(pushVersionMap);
        tmpCache.setUmengKeyMap(umengKeyMap);
        tmpCache.setUmengMasterSecretMap(umengMasterSecretMap);


        Map<String, AppCertificateInfo> certInfoMap = new HashMap<String, AppCertificateInfo>();
        List<String> appCertList = getList(KEY_CERTIFICATE_LIST);
        for (String appKey : appCertList) {
            String path = getString(appKey + KEY_APP_CERTIFICATE_PATH);
            String password = getString(appKey + KEY_APP_CERTIFICATE_PASSWORD);
            boolean isproduction = getBoolean(appKey + KEY_APP_ISPRODUCTION, false);


            if (!StringUtil.isEmpty(path) && !StringUtil.isEmpty(password)) {
                AppCertificateInfo info = new AppCertificateInfo(appKey, path, password, isproduction);
                certInfoMap.put(appKey, info);
            }
        }
        tmpCache.setCertificatieInfoMap(certInfoMap);


        tmpCache.setHistoryVersionMap(historyVersionMap);
        tmpCache.setLastVersionMap(lastVersionMap);
        tmpCache.setUpdateTOLastVersionMap(updateTOLastVersionMap);

        // test client
        List<String> iosTestClient = this.getList(KEY_IOS_TEST_CLIENT);
        List<String> androidClient = this.getList(KEY_ANDROID_TEST_CLIENT);

        tmpCache.setIosTestClientList(iosTestClient);
        tmpCache.setAndroidTestClientList(androidClient);

        String certificatePath = this.getString(KEY_CERTIFICATE_PATH);
        String certificatePassword = this.getString(KEY_CERTIFICATE_PASSWORD);
        boolean certificateProduction = this.getBoolean(KEY_CERTIFICATE_PRODUCTION, false);

        tmpCache.setCertificatePath(certificatePath);
        tmpCache.setCertificatePassword(certificatePassword);
        tmpCache.setCertificateProduction(certificateProduction);

        //solr
        tmpCache.setSolrServerUri(this.getString(KEY_SOLR_SERVER_URI));
        tmpCache.setSolrAnalysisUri(this.getString(KEY_SOLR_ANALYSIS_URI));

        //redis
        tmpCache.setRedisServerHost(this.getString(KEY_REDIS_SERVER_HOST));
        tmpCache.setRedisServerPort(this.getInt(KEY_REDIS_SERVER_PORT));

        tmpCache.setSearchServerApi(this.getString(KEY_SEARCH_SERVER_API));

        cached = tmpCache;

        if (logger.isDebugEnabled()) {
            logger.debug("WebApiHotdeploy  Props reload ......");
        }

    }

    // get latest version ver
    public String getLatestVersionVer(String mobileClientType) {
        return this.cached.getLastVersionMap().get(mobileClientType).getLatestVersionVer();
    }

    // get latest version num
    public String getLatestVersionNum(String mobileClientType) {
        return this.cached.getLastVersionMap().get(mobileClientType).getLatestVersionNum();
    }

    // get to latest version update type
    public String getToLatestVersionUpdateType(String verNum, String mobileClientType) {
        return this.cached.getUpdateTOLastVersionMap().get(verNum + DEFAULT_SEPARATOR + mobileClientType).getToLatestVerUpdateType();
    }

    // get to latest version update url
    public String getToLatestVersionUpdateUrl(String verNum, String mobileClientType) {
        return this.cached.getUpdateTOLastVersionMap().get(verNum + DEFAULT_SEPARATOR + mobileClientType).getToLatestVerUpdateUrl();
    }

    // ios test client
    public List<String> getIosTestClient() {
        return this.cached.getIosTestClientList();
    }

    // android test client
    public List<String> getAndroidTestClient() {
        return this.cached.getAndroidTestClientList();
    }

    // certificate path
    public String getCertificatePath() {
        return this.cached.getCertificatePath();
    }

    // certificate password
    public String getCertificatePassword() {
        return this.cached.getCertificatePassword();
    }

    public Map<String, AppVersionInfo> getAppVersionInfoMap() {
        return cached.getAppVersionInfoMap();
    }

    public Map<String, AppCertificateInfo> getCertificatieInfoMap() {
        return cached.getCertificatieInfoMap();
    }

    // certificate production
    public boolean isCertificateProduction() {
        return cached.isCertificateProduction();
    }

    public List<String> getPushVersionList(String appKeyPlatform, int pushChannel) {
        return cached.getPushVersionMap().get(appKeyPlatform + "." + pushChannel);
    }

    public String getUmengAppKey(String toolsAppId) {
        return cached.getUmengKeyMap().get(toolsAppId);
    }

    public String getUmengMasterSecret(String toolsAppId) {
        return cached.getUmengMasterSecretMap().get(toolsAppId);
    }

    public String getRedisServerHost() {
        return cached.getRedisServerHost();
    }

    public int getRedisServerPort() {
        return cached.getRedisServerPort();
    }

    public String getSolrSecrverUri() {
        return cached.getSolrServerUri();
    }

    public String getSolrAnalysisUri() {
        return cached.getSolrAnalysisUri();
    }

    public String getSearchServerApi() {
        return cached.getSearchServerApi();
    }

    public int getUmengProductionMode() {
        return cached.getUmengPushProductionMode();
    }

    public List<String> getUmengVersionList(String appKey, AppPlatform appPlatform, AppPushChannel byCode) {
        return cached.getUmengVersionList();
    }

    private class Cached {
        //
        Map<String, String> defUpdateTypeMap;
        Map<String, Map<String, String>> historyVersionMap;

        Map<String, LastVersion> lastVersionMap;

        // 1000.android
        Map<String, UpdateToLastVersion> updateTOLastVersionMap;

        List<String> iosTestClientList;
        List<String> androidTestClientList;

        String certificatePath = "";
        String certificatePassword = "";
        boolean certificateProduction = false;

        private Map<String, AppVersionInfo> appVersionInfoMap;
        private Map<String, AppCertificateInfo> certificatieInfoMap;
        private Map<String, List<String>> pushVersionMap;
        private Map<String, String> umengKeyMap;
        private Map<String, String> umengMasterSecretMap;

        //solr
        String solrServerUri;
        String solrAnalysisUri;

        //redis
        String redisServerHost;
        int redisServerPort;

        String searchServerApi;
        //push
        private int umengPushProductionMode;
        private List<String> umengVersionList;

        // the getter and setter

        public Map<String, String> getDefUpdateTypeMap() {
            return defUpdateTypeMap;
        }

        public void setDefUpdateTypeMap(Map<String, String> defUpdateTypeMap) {
            this.defUpdateTypeMap = defUpdateTypeMap;
        }

        public Map<String, Map<String, String>> getHistoryVersionMap() {
            return historyVersionMap;
        }

        public void setHistoryVersionMap(Map<String, Map<String, String>> historyVersionMap) {
            this.historyVersionMap = historyVersionMap;
        }

        public Map<String, LastVersion> getLastVersionMap() {
            return lastVersionMap;
        }

        public void setLastVersionMap(Map<String, LastVersion> lastVersionMap) {
            this.lastVersionMap = lastVersionMap;
        }

        public Map<String, UpdateToLastVersion> getUpdateTOLastVersionMap() {
            return updateTOLastVersionMap;
        }

        public void setUpdateTOLastVersionMap(Map<String, UpdateToLastVersion> updateTOLastVersionMap) {
            this.updateTOLastVersionMap = updateTOLastVersionMap;
        }

        public List<String> getIosTestClientList() {
            return iosTestClientList;
        }

        public void setIosTestClientList(List<String> iosTestClientList) {
            this.iosTestClientList = iosTestClientList;
        }

        public List<String> getAndroidTestClientList() {
            return androidTestClientList;
        }

        public void setAndroidTestClientList(List<String> androidTestClientList) {
            this.androidTestClientList = androidTestClientList;
        }

        public String getCertificatePath() {
            return certificatePath;
        }

        public void setCertificatePath(String certificatePath) {
            this.certificatePath = certificatePath;
        }

        public String getCertificatePassword() {
            return certificatePassword;
        }

        public void setCertificatePassword(String certificatePassword) {
            this.certificatePassword = certificatePassword;
        }

        public boolean isCertificateProduction() {
            return certificateProduction;
        }

        public void setCertificateProduction(boolean certificateProduction) {
            this.certificateProduction = certificateProduction;
        }

        public Map<String, AppVersionInfo> getAppVersionInfoMap() {
            return appVersionInfoMap;
        }

        public void setAppVersionInfoMap(Map<String, AppVersionInfo> appVersionInfoMap) {
            this.appVersionInfoMap = appVersionInfoMap;
        }

        public Map<String, AppCertificateInfo> getCertificatieInfoMap() {
            return certificatieInfoMap;
        }

        public void setCertificatieInfoMap(Map<String, AppCertificateInfo> certificatieInfoMap) {
            this.certificatieInfoMap = certificatieInfoMap;
        }

        //push
        private int getUmengPushProductionMode() {
            return umengPushProductionMode;
        }

        private void setUmengPushProductionMode(int umengPushProductionMode) {
            this.umengPushProductionMode = umengPushProductionMode;
        }

        public void setPushVersionMap(Map<String, List<String>> pushVersionMap) {
            this.pushVersionMap = pushVersionMap;
        }

        public Map<String, List<String>> getPushVersionMap() {
            return pushVersionMap;
        }

        public Map<String, String> getUmengKeyMap() {
            return umengKeyMap;
        }

        public void setUmengKeyMap(Map<String, String> umengKeyMap) {
            this.umengKeyMap = umengKeyMap;
        }

        private Map<String, String> getUmengMasterSecretMap() {
            return umengMasterSecretMap;
        }

        private void setUmengMasterSecretMap(Map<String, String> umengMasterSecretMap) {
            this.umengMasterSecretMap = umengMasterSecretMap;
        }

        private String getSolrServerUri() {
            return solrServerUri;
        }

        private void setSolrServerUri(String solrServerUri) {
            this.solrServerUri = solrServerUri;
        }

        private String getSolrAnalysisUri() {
            return solrAnalysisUri;
        }

        private void setSolrAnalysisUri(String solrAnalysisUri) {
            this.solrAnalysisUri = solrAnalysisUri;
        }

        private String getRedisServerHost() {
            return redisServerHost;
        }

        private void setRedisServerHost(String redisServerHost) {
            this.redisServerHost = redisServerHost;
        }

        private int getRedisServerPort() {
            return redisServerPort;
        }

        private void setRedisServerPort(int redisServerPort) {
            this.redisServerPort = redisServerPort;
        }

        private String getSearchServerApi() {
            return searchServerApi;
        }

        private void setSearchServerApi(String searchServerApi) {
            this.searchServerApi = searchServerApi;
        }

        public List<String> getUmengVersionList() {
            return umengVersionList;
        }

        public void setUmengVersionList(List<String> umengVersionList) {
            this.umengVersionList = umengVersionList;
        }
    }

    private class LastVersion {
        String latestVersionNum;
        String latestVersionDesc;
        String latestVersionVer;

        // the getter and setter

        public String getLatestVersionNum() {
            return latestVersionNum;
        }

        public void setLatestVersionNum(String latestVersionNum) {
            this.latestVersionNum = latestVersionNum;
        }

        public String getLatestVersionDesc() {
            return latestVersionDesc;
        }

        public void setLatestVersionDesc(String latestVersionDesc) {
            this.latestVersionDesc = latestVersionDesc;
        }

        public String getLatestVersionVer() {
            return latestVersionVer;
        }

        public void setLatestVersionVer(String latestVersionVer) {
            this.latestVersionVer = latestVersionVer;
        }
    }

    private class UpdateToLastVersion {
        String toLatestVerUpdateType;
        String toLatestVerUpdateUrl;

        public String getToLatestVerUpdateType() {
            return toLatestVerUpdateType;
        }

        public void setToLatestVerUpdateType(String toLatestVerUpdateType) {
            this.toLatestVerUpdateType = toLatestVerUpdateType;
        }

        public String getToLatestVerUpdateUrl() {
            return toLatestVerUpdateUrl;
        }

        public void setToLatestVerUpdateUrl(String toLatestVerUpdateUrl) {
            this.toLatestVerUpdateUrl = toLatestVerUpdateUrl;
        }
    }

}
