package com.enjoyf.platform.props;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.google.common.base.Strings;
import org.apache.log4j.PropertyConfigurator;

import java.util.Locale;
import java.util.StringTokenizer;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class EnvConfig {
    private static EnvConfig instance;

    private FiveProps props;

    /////////////////////////////////////////////////////////////////
    //the attributes of the env.
    /////////////////////////////////////////////////////////////////
    //naming setting
    private String namingHostName = "127.0.0.1";
    private int namingPort = 7500;

    //the local setting.
    private Locale defaultLocale = Locale.getDefault();

    //sequence setting.
    private int sequenceFetchStep = 10;
    private int sequenceFetchTryTimes = 3;
    private int requestTimeoutMsecs = 1000 * 30;

    //jvm setting
    private String javaMinVersion = "1.5";
    private boolean javaJitRequired = false;

    //hotdeploy reload setting.
    private long hotdeployCheckIntervalMsec = 5 * 60 * 1000;

    //////////////////////////////////////////////////////////////////////////////
    //log4j configure files.
    private String log4jConfigFile = "/props/log4j/log4j.properties";

    //static configure files
    private String webConfigFile = "/props/web/def.properties";
    private String adminConfigFile = "/props/admin/def.properties";
    private String syncTextConfigFile = "/props/synctext/def.properties";
    private String imageConfigFile = "/props/image/def.properties";

    //hotdeploy configure files.
    private String advertiseHotdeployConfigureFile = "/hotdeploy/props/advertise/def.properties";
    private String eventHotdeployConfigureFile = "/hotdeploy/props/event/def.properties";
    private String templateContentConfigFile = "/hotdeploy/props/template/content/def.properties";
    private String moodHotdeployConfigureFile = "/hotdeploy/props/mood/def.properties";
    private String contextHotdeployConfigureFile = "/hotdeploy/props/context/def.properties";
    private String voteHotDeployConfigureFile = "/hotdeploy/props/vote/def.properties";
    private String webHotdeployConfigureFile = "/hotdeploy/props/web/def.properties";
    private String batchManageHotdeployConfig = "/hotdeploy/props/batchmanage/def.properties";
    private String toolsHotdeployConfigureFile = "/hotdeploy/props/tools/def.properties";
    private String oauthHotdeployConfigureFile = "/hotdeploy/props/oauth/def.properties";
    private String resdomainHotdeployConfigureFile = "/hotdeploy/props/resdomain/def.properties";
    private String billingHotDeployConfigureFile = "/hotdeploy/props/billing/def.properties";
    private String syncHotdeployConfigureFile = "/hotdeploy/props/sync/def.properties";

    private String viewLineHotdeployConfigureFile = "/hotdeploy/props/viewline/def.properties";
    private String wallHotdeployConfigureFile = "/hotdeploy/props/wall/def.properties";
    private String webApiHotdeployConfigureFile = "/hotdeploy/props/webapi/def.properties";
    private String displayHotdeployConfigureFile = "/hotdeploy/props/display/def.properties";

    private String templateHotdeployConfigureFile = "/hotdeploy/props/template/def.properties";

    private String gameResourceHotdeployConfig = "/hotdeploy/props/game/def.properties";

    private String pointHotdeployConfig = "/hotdeploy/props/point/def.properties";

    private String activityHotdeployConfig = "/hotdeploy/props/activity/def.properties";

    private String joymeAppHotdeployConfig = "/hotdeploy/props/joymeapp/def.properties";

    private String profileHotdeployConfig = "/hotdeploy/props/profile/def.properties";

    private String pcsyncHotdeployConfigureFile = "/hotdeploy/props/sync/pc_def.properties";

    //the oauth app setting
    private String oauthAppId = null;
    private String oauthAppKey = null;


    /////////////////////////////////////////////////////////////////
    public static final String SUFFIX_KEY_PARTITION_NUM = ".partitions";
    public static final String SUFFIX_KEY_PARTITION_FAILOVER_NUM = ".partitions.failover";

    public static final String KEY_NAMING_HOSTNAME = "namingservice.hostname";
    public static final String KEY_NAMING_PORT = "namingservice.port";

    public static final String KEY_DEFAULT_LOCALE = "locale.default";

    public static final String KEY_JAVA_MIN_VERSION = "jvm.minVersion";
    public static final String KEY_JAVA_JIT_REQUIRED = "jvm.jitRequired";

    public static final String KEY_SEQUENCE_FETCH_STEP = "sequence.cache.step";
    public static final String KEY_SEQUENCE_FETCH_TRY_TIMES = "sequence.retry.times";
    public static final String KEY_REQUEST_TIMEOUT_MSECS = "request.timeout.msecs";

    public static final String KEY_HOTDEPLOY_CHECK_INTERVAL_MSEC = "hotdeploy.reload.interval.msec";


    //configure dirs.
    public static final String KEY_MAIL_TEMPLATE_CONF_DIR = "mailtemplate.config.dir";

    /////////////////////////////////////////////////////////////////
    public static final String KEY_LOG4J_CONF_FILE = "log4j.config.file";

    //static configure files.
    public static final String KEY_WEBAPPS_CONF_FILE = "webapps.config.file";
    public static final String KEY_ADMIN_CONF_FILE = "admin.config.file";
    public static final String KEY_SYNC_TEXT_CONF_FILE = "synctext.config.file";
    public static final String KEY_IMAGE_CONF_FILE = "image.config.file";

    //hotdeploy configure files
    public static final String KEY_ADVERTISE_HOTDEPLOY_CONF_FILE = "advertise.hotdeploy.config.file";
    public static final String KEY_EVENT_HOTDEPLOY_CONF_FILE = "event.hotdeploy.config.file";
    public static final String KEY_CONTENT_TEMPLATE_HOTDEPLOY_CONF_FILE = "template.hotdeploy.content.file";
    public static final String KEY_MOOD_HOTDEPLOY_CONF_FILE = "mood.hotdeploy.config.file";
    public static final String KEY_CONTEXT_HOTDEPLOY_CONF_FILE = "context.hotdeploy.config.file";
    public static final String KEY_VOTE_HOTDEPLOY_CONF_FILE = "vote.hotdeploy.config.file";
    public static final String KEY_WEB_HOTDEPLOY_CONF_FILE = "web.hotdeploy.config.file";
    public static final String KEY_BATCHMANAGE_HOTDEPLOY_CONF_FILE = "batchmanage.hotdeploy.config.file";
    public static final String KEY_OAUTH_HOTDEPLOY_CONF_FILE = "oauth.hotdeploy.config.file";
    public static final String KEY_RESDOMAIN_HOTDEPLOY_CONF_FILE = "resdomain.hotdeploy.config.file";
    public static final String KEY_TOOLS_HOTDEPLOY_CONF_FILE = "tools.hotdeploy.config.file";
    public static final String KEY_BILLING_HOTDEPLOY_CONF_FILE = "billing.hotdeploy.config.file";
    public static final String KEY_SYNC_HOTDEPLOY_CONF_FILE = "sync.hotdeploy.config.file";
    public static final String KEY_VIEWLINE_HOTDEPLOY_CONF_FILE = "viewline.hotdeploy.config.file";
    public static final String KEY_WALL_HOTDEPLOY_CONF_FILE = "wall.hotdeploy.config.file";
    public static final String KEY_WEBAPI_HOTDEPLOY_CONF_FILE = "webapi.hotdeploy.config.file";
    public static final String KEY_DISPLAY_HOTDEPLOY_CONF_FILE = "display.hotdeploy.config.file";

    public static final String KEY_TEMPLATE_HOTDEPLOY_CONF_FILE = "template.hotdeploy.config.file";

    public static final String KEY_GAMERES_HOTDEPLOY_CONF_FILE = "gameres.hotdeploy.config.file";

    public static final String KEY_POINT_HOTDEPLOY_CONF_FILE = "point.hotdeploy.config.file";

    public static final String KEY_ACTIVITY_HOTDEPLOY_CONF_FILE = "activity.hotdeploy.config.file";

    public static final String KEY_JOYMEAPP_HOTDEPLOY_CONF_FILE = "joymeapp.hotdeploy.config.file";

    public static final String KEY_PROFILE_HOTDEPLOY_CONF_FILE = "profile.hotdeploy.config.file";

    public static final String KEY_PC_SYNC_HOTDEPLOY_CONF_FILE = "pc.sync.hotdeploy.config.file";
    //the oauth app setting keys
    public static final String KEY_OAUTH_APP_ID = "oauth.app.id";
    public static final String KEY_OAUTH_APP_KEY = "oauth.app.key";


    /////////////////////////////////////////////////////////////////
    private EnvConfig() {
        init();

        //setting the log level.
        PropertyConfigurator.configure(new FiveProps(log4jConfigFile).getProps());
    }

    public static synchronized EnvConfig get() {
        if (instance == null) {
            instance = new EnvConfig();
        }

        return instance;
    }

    private void init() {
        props = Props.instance().getEnvProps();

        //
        namingHostName = props.get(KEY_NAMING_HOSTNAME, namingHostName);
        namingPort = props.getInt(KEY_NAMING_PORT, namingPort);

        //locale loading.
        String localeStr = props.get(KEY_DEFAULT_LOCALE);
        if (!Strings.isNullOrEmpty(localeStr)) {
            StringTokenizer tokenizer = new StringTokenizer(localeStr, "_");

            if (tokenizer.countTokens() >= 2) {
                String language = tokenizer.nextToken();
                String country = tokenizer.nextToken();

                defaultLocale = new Locale(language, country);
            }
        }

        //
        javaMinVersion = props.get(KEY_JAVA_MIN_VERSION, javaMinVersion);
        javaJitRequired = props.getBoolean(KEY_JAVA_JIT_REQUIRED, javaJitRequired);

        hotdeployCheckIntervalMsec = props.getLong(KEY_HOTDEPLOY_CHECK_INTERVAL_MSEC, hotdeployCheckIntervalMsec);
        sequenceFetchStep = props.getInt(KEY_SEQUENCE_FETCH_STEP, sequenceFetchStep);
        sequenceFetchTryTimes = props.getInt(KEY_SEQUENCE_FETCH_TRY_TIMES, sequenceFetchTryTimes);

        requestTimeoutMsecs = props.getInt(KEY_REQUEST_TIMEOUT_MSECS, requestTimeoutMsecs);

        //
        log4jConfigFile = props.get(KEY_LOG4J_CONF_FILE, log4jConfigFile);

        //configure file setting load.
        webConfigFile = props.get(KEY_WEBAPPS_CONF_FILE, webConfigFile);
        adminConfigFile = props.get(KEY_ADMIN_CONF_FILE, adminConfigFile);
        syncTextConfigFile = props.get(KEY_SYNC_TEXT_CONF_FILE, syncTextConfigFile);
        imageConfigFile = props.get(KEY_IMAGE_CONF_FILE, imageConfigFile);

        //hotdeploy configure file setting load.
        advertiseHotdeployConfigureFile = props.get(KEY_ADVERTISE_HOTDEPLOY_CONF_FILE, advertiseHotdeployConfigureFile);

        eventHotdeployConfigureFile = props.get(KEY_EVENT_HOTDEPLOY_CONF_FILE, eventHotdeployConfigureFile);

        templateContentConfigFile = props.get(KEY_CONTENT_TEMPLATE_HOTDEPLOY_CONF_FILE, templateContentConfigFile);

        moodHotdeployConfigureFile = props.get(KEY_MOOD_HOTDEPLOY_CONF_FILE, moodHotdeployConfigureFile);

        contextHotdeployConfigureFile = props.get(KEY_CONTEXT_HOTDEPLOY_CONF_FILE, contextHotdeployConfigureFile);

        voteHotDeployConfigureFile = props.get(KEY_VOTE_HOTDEPLOY_CONF_FILE, voteHotDeployConfigureFile);

        webHotdeployConfigureFile = props.get(KEY_WEB_HOTDEPLOY_CONF_FILE, webHotdeployConfigureFile);

        batchManageHotdeployConfig = props.get(KEY_BATCHMANAGE_HOTDEPLOY_CONF_FILE, batchManageHotdeployConfig);

        oauthHotdeployConfigureFile = props.get(KEY_OAUTH_HOTDEPLOY_CONF_FILE, oauthHotdeployConfigureFile);

        resdomainHotdeployConfigureFile = props.get(KEY_RESDOMAIN_HOTDEPLOY_CONF_FILE, resdomainHotdeployConfigureFile);

        toolsHotdeployConfigureFile = props.get(KEY_TOOLS_HOTDEPLOY_CONF_FILE, toolsHotdeployConfigureFile);

        billingHotDeployConfigureFile = props.get(KEY_BILLING_HOTDEPLOY_CONF_FILE, billingHotDeployConfigureFile);

        syncHotdeployConfigureFile = props.get(KEY_SYNC_HOTDEPLOY_CONF_FILE, syncHotdeployConfigureFile);

        viewLineHotdeployConfigureFile = props.get(KEY_VIEWLINE_HOTDEPLOY_CONF_FILE, viewLineHotdeployConfigureFile);

        wallHotdeployConfigureFile = props.get(KEY_WALL_HOTDEPLOY_CONF_FILE, wallHotdeployConfigureFile);

        webApiHotdeployConfigureFile = props.get(KEY_WEBAPI_HOTDEPLOY_CONF_FILE, webApiHotdeployConfigureFile);

        displayHotdeployConfigureFile = props.get(KEY_DISPLAY_HOTDEPLOY_CONF_FILE, displayHotdeployConfigureFile);

        templateHotdeployConfigureFile = props.get(KEY_TEMPLATE_HOTDEPLOY_CONF_FILE, templateHotdeployConfigureFile);

        gameResourceHotdeployConfig = props.get(KEY_GAMERES_HOTDEPLOY_CONF_FILE, gameResourceHotdeployConfig);


        pointHotdeployConfig = props.get(KEY_POINT_HOTDEPLOY_CONF_FILE, pointHotdeployConfig);

        activityHotdeployConfig = props.get(KEY_ACTIVITY_HOTDEPLOY_CONF_FILE, activityHotdeployConfig);

        joymeAppHotdeployConfig = props.get(KEY_JOYMEAPP_HOTDEPLOY_CONF_FILE, joymeAppHotdeployConfig);

        profileHotdeployConfig = props.get(KEY_PROFILE_HOTDEPLOY_CONF_FILE, profileHotdeployConfig);

        pcsyncHotdeployConfigureFile = props.get(KEY_PC_SYNC_HOTDEPLOY_CONF_FILE, pcsyncHotdeployConfigureFile);
        //the oauth app setting read.
        oauthAppId = props.get(KEY_OAUTH_APP_ID);
        oauthAppKey = props.get(KEY_OAUTH_APP_KEY);
    }

    /////////////////////////////////////////////////////////////////
    public FiveProps getProps() {
        return props;
    }

    public String getNamingHostName() {
        return namingHostName;
    }

    public int getNamingPort() {
        return namingPort;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public int getSequenceFetchStep() {
        return sequenceFetchStep;
    }

    public int getSequenceFetchTryTimes() {
        return sequenceFetchTryTimes;
    }

    public int getRequestTimeoutMsecs() {
        return requestTimeoutMsecs;
    }

    public String getJavaMinVersion() {
        return javaMinVersion;
    }

    public boolean isJavaJitRequired() {
        return javaJitRequired;
    }

    public long getHotdeployCheckIntervalMsec() {
        return hotdeployCheckIntervalMsec;
    }

    public String getWebappConfigFile() {
        return webConfigFile;
    }

    public String getImageConfigFile() {
        return imageConfigFile;
    }

    public String getAdminConfigFile() {
        return adminConfigFile;
    }

    public int getServicePartitionNum(String serviceSection) {
        return props.getInt(serviceSection + SUFFIX_KEY_PARTITION_NUM, 1);
    }

    public int getServicePartitionFailoverNum(String serviceSection) {
        return props.getInt(serviceSection + SUFFIX_KEY_PARTITION_FAILOVER_NUM, 2);
    }

    public String getMailTemplateConfigHotdeployDir() {
        //todo mail
        return null;
    }

    public String getLog4jConfigFile() {
        return log4jConfigFile;
    }

    public String getAdvertiseHotdeployConfigureFile() {
        return advertiseHotdeployConfigureFile;
    }

    public String getEventHotdeployConfigureFile() {
        return eventHotdeployConfigureFile;
    }

    public String getTemplateContentConfigFile() {
        return templateContentConfigFile;
    }

    public String getMoodHotdeployConfigureFile() {
        return moodHotdeployConfigureFile;
    }

    public String getContextHotdeployConfigureFile() {
        return contextHotdeployConfigureFile;
    }

    public String getVoteHotDeployConfigureFile() {
        return voteHotDeployConfigureFile;
    }

    public String getSyncHotdeployConfigureFile() {
        return syncHotdeployConfigureFile;
    }

    public String getWebHotdeployConfigureFile() {
        return webHotdeployConfigureFile;
    }

    public String getBatchManageHotdeployConfig() {
        return batchManageHotdeployConfig;
    }

    public String getOauthHotdeployConfigureFile() {
        return oauthHotdeployConfigureFile;
    }

    public String getResdomainHotdeployConfigureFile() {
        return resdomainHotdeployConfigureFile;
    }

    public String getBillingHotDeployConfigureFile() {
        return billingHotDeployConfigureFile;
    }

    public String getViewLineHotdeployConfigureFile() {
        return viewLineHotdeployConfigureFile;
    }

    public String getWallHotdeployConfigureFile() {
        return wallHotdeployConfigureFile;
    }

    public String getWebApiHotdeployConfigureFile() {
        return webApiHotdeployConfigureFile;
    }

    public String getDisplayHotdeployConfigureFile() {
        return displayHotdeployConfigureFile;
    }

    public String getOauthAppId() {
        return oauthAppId;
    }

    public String getOauthAppKey() {
        return oauthAppKey;
    }

    public String getToolsHotdeployConfigureFile() {
        return toolsHotdeployConfigureFile;
    }

    public String getSyncTextConfigFile() {
        return syncTextConfigFile;
    }

    public String getTemplateHotdeployConfigureFile() {
        return templateHotdeployConfigureFile;
    }

    public String getGameResourceHotdeployConfig() {
        return gameResourceHotdeployConfig;
    }

    public String getPointHotdeployConfig() {
        return pointHotdeployConfig;
    }

    public String getActivityHotdeployConfig() {
        return activityHotdeployConfig;
    }

    public String getJoymeAppHotdeployConfig() {
        return joymeAppHotdeployConfig;
    }

    public String getProfileHotdeployConfig() {
        return profileHotdeployConfig;
    }

    public String getPcSyncHotdeployConfFile() {
        return pcsyncHotdeployConfigureFile;
    }
}
