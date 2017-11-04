package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.ServiceType;
import com.enjoyf.platform.service.event.system.SystemEventType;
import com.enjoyf.platform.service.event.system.SystemEventTypeConfig;
import com.enjoyf.platform.service.event.user.UserEventType;
import com.enjoyf.platform.service.event.user.UserEventTypeConfig;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <a href="mailto:yinpengyi@enjoyf.com">Yin Pengyi</a>
 */
public class EventHotdeployConfig extends HotdeployConfig {
    //
    private static final Logger logger = LoggerFactory.getLogger(EventHotdeployConfig.class);

    /////////////////////////////////////////////////////////////////////////////
    //the keys
    private static final String KEY_USER_EVENT_TYPE_LIST = "user.event.type.list";
    private static final String KEY_SYSTEM_EVENT_TYPE_LIST = "system.event.type.list";
    private static final String KEY_CLIENT_EVENT_TYPE_LIST = "client.event.type.list";

    private static final String KEY_DISPATCH_SERVICE_LIST = ".dispatch.services";
    private static final String KEY_STORE_TO_DB = ".store.to.db";

    private static final String KEY_USER_EVENT_TYPE_DEFAULT = "user.event.default";

    //the pageview event configure.
    private static final String KEY_PAGEVIEW_SUPPORT_DOMAINS = "pageview.support.domains";
    private static final String KEY_PAGEVIEW_SPIDER_USER_AGENTS = "pageview.spider.user.agents";
    private static final String KEY_PAGEVIEW_IGNORE_REMOTE_IPS = "pageview.ignore.remote.ips";

    private Cached cached;

    public EventHotdeployConfig() {
        super(EnvConfig.get().getEventHotdeployConfigureFile());
    }

    @Override
    public void init() {
        reload();
    }

    public synchronized void reload() {
        super.reload();

        ////////////////////////////////////
        Cached tmpCache = new Cached();

        Map<UserEventType, UserEventTypeConfig> userEventTypeConfigHashMap = new HashMap<UserEventType, UserEventTypeConfig>();

        // get the player event list
        List<String> userEventTypeList = this.getList(KEY_USER_EVENT_TYPE_LIST);


        //get the default user event.
        List<String> userEventDefaultserviceTypes = this.getList(KEY_USER_EVENT_TYPE_DEFAULT + KEY_DISPATCH_SERVICE_LIST);
        boolean eventEventDefaultWriteToDB = this.getBoolean(KEY_USER_EVENT_TYPE_DEFAULT + KEY_STORE_TO_DB, false);

        // for each event type.
        for (String userEventTypeCode : userEventTypeList) {
            //check the player event type.
            if (UserEventType.getByCode(userEventTypeCode) == null) {
                GAlerter.lab("EventHotdeployConfig, there is a type error in the user event type:" + userEventTypeCode);

                continue;
            }

            UserEventTypeConfig config = new UserEventTypeConfig(UserEventType.getByCode(userEventTypeCode));

            //
            if (this.getString(userEventTypeCode + KEY_DISPATCH_SERVICE_LIST) == null) {
                for (String st : userEventDefaultserviceTypes) {
                    ServiceType sType = ServiceType.getByCode(st);

                    if (sType != null) {
                        config.getReceivers().put(sType, sType.getEventReceiver());
                    }
                }
            } else {
                //get the event type service.
                List<String> serviceTypes = this.getList(userEventTypeCode + KEY_DISPATCH_SERVICE_LIST);

                if (!CollectionUtil.isEmpty(serviceTypes)) {
                    for (String st : serviceTypes) {
                        ServiceType sType = ServiceType.getByCode(st);

                        if (sType != null) {
                            config.getReceivers().put(sType, sType.getEventReceiver());
                        }
                    }
                }
            }

            if (this.getString(userEventTypeCode + KEY_STORE_TO_DB) == null) {
                config.setStoreToDB(eventEventDefaultWriteToDB);
            } else {
                config.setStoreToDB(this.getBoolean(userEventTypeCode + KEY_STORE_TO_DB, false));
            }

            userEventTypeConfigHashMap.put(config.getEventType(), config);
        }

        ////////////////////////////////////
        Map<SystemEventType, SystemEventTypeConfig> systemEventTypeConfigHashMap = new HashMap<SystemEventType, SystemEventTypeConfig>();

        // get the player event list
        List<String> systemEventTypeList = this.getList(KEY_SYSTEM_EVENT_TYPE_LIST);

        // for each event type.
        for (String systemEventTypeCode : systemEventTypeList) {
            //check the player event type.
            if (SystemEventType.getByCode(systemEventTypeCode) == null) {
                GAlerter.lab("EventHotdeployConfig, there is a type error in the system event type:" + systemEventTypeCode);

                continue;
            }

            SystemEventTypeConfig config = new SystemEventTypeConfig(SystemEventType.getByCode(systemEventTypeCode));

            //get the event type service.
            List<String> serviceTypes = this.getList(systemEventTypeCode + KEY_DISPATCH_SERVICE_LIST);

            if (!CollectionUtil.isEmpty(serviceTypes)) {
                for (String st : serviceTypes) {
                    ServiceType sType = ServiceType.getByCode(st);

                    if (sType != null) {
                        config.getReceivers().put(sType, sType.getEventReceiver());
                    }
                }
            }

            systemEventTypeConfigHashMap.put(config.getEventType(), config);
        }

        /////////////////////////////client event type/////////////////////////////////////

        //the pageview setting configure.
        List<String> supportDomains = getList(KEY_PAGEVIEW_SUPPORT_DOMAINS);
        for (String domain : supportDomains) {
            tmpCache.getPageViewSupportDomainRegexs().add(Pattern.compile(domain));
        }

        //
        List<String> ignoreRemoteIps = getList(KEY_PAGEVIEW_IGNORE_REMOTE_IPS);
        for (String remoteIps : ignoreRemoteIps) {
            tmpCache.getPageViewIgnoreRemoteIpRegexs().add(Pattern.compile(remoteIps));
        }

        //
        List<String> spiderUserAgents = getList(KEY_PAGEVIEW_SPIDER_USER_AGENTS);
        for (String spiderUserAgent : spiderUserAgents) {
            tmpCache.getPageViewSpiderUserAgentRegexs().add(Pattern.compile(spiderUserAgent));
        }

        //
        tmpCache.setUserEventTypeConfigMap(userEventTypeConfigHashMap);
        tmpCache.setSystemEventTypeConfigMap(systemEventTypeConfigHashMap);

        //
        this.cached = tmpCache;

        logger.info("Event Props init finished.");
    }

    public boolean isSupport(UserEventType type) {
        return this.cached.getUserEventTypeConfigMap().containsKey(type);
    }

    public boolean isSupport(SystemEventType type) {
        return this.cached.getSystemEventTypeConfigMap().containsKey(type);
    }

    public UserEventTypeConfig getUserEventTypeConfig(UserEventType type) {
        return this.cached.getUserEventTypeConfigMap().get(type);
    }

    public SystemEventTypeConfig getSystemEventTypeConfig(SystemEventType type) {
        return this.cached.getSystemEventTypeConfigMap().get(type);
    }


    public boolean isSupportDomain(String domain) {
        boolean returnValue = false;

        for (Pattern p : cached.getPageViewSupportDomainRegexs()) {
            returnValue = returnValue || p.matcher(domain).find();
        }

        return returnValue;
    }

    public boolean isSupportUrlDomain(String url) {
        boolean returnValue = false;

        if (!Strings.isNullOrEmpty(url)) {
            try {
                URL urlObj = new URL(url);

                returnValue = isSupportDomain(urlObj.getHost());
            } catch (Exception e) {
                //
            }
        }

        return returnValue;
    }

    public boolean isIgnoreRemoteIp(String ip) {
        boolean returnValue = false;

        for (Pattern p : cached.getPageViewIgnoreRemoteIpRegexs()) {
            returnValue = returnValue || p.matcher(ip).find();
        }

        return returnValue;
    }

    public boolean isSpiderUserAgent(String userAgent) {
        boolean returnValue = false;

        for (Pattern p : cached.getPageViewSpiderUserAgentRegexs()) {
            returnValue = returnValue || p.matcher(userAgent).find();
        }

        return returnValue;
    }

    //
    private class Cached {
        private Map<UserEventType, UserEventTypeConfig> userEventTypeConfigMap;
        private Map<SystemEventType, SystemEventTypeConfig> systemEventTypeConfigMap;

        //
        private List<Pattern> pageViewSupportDomainRegexs = new ArrayList<Pattern>();
        private List<Pattern> pageViewIgnoreRemoteIpRegexs = new ArrayList<Pattern>();
        private List<Pattern> pageViewSpiderUserAgentRegexs = new ArrayList<Pattern>();


        public Map<UserEventType, UserEventTypeConfig> getUserEventTypeConfigMap() {
            return userEventTypeConfigMap;
        }

        public void setUserEventTypeConfigMap(Map<UserEventType, UserEventTypeConfig> userEventTypeConfigMap) {
            this.userEventTypeConfigMap = userEventTypeConfigMap;
        }

        public Map<SystemEventType, SystemEventTypeConfig> getSystemEventTypeConfigMap() {
            return systemEventTypeConfigMap;
        }

        public void setSystemEventTypeConfigMap(Map<SystemEventType, SystemEventTypeConfig> systemEventTypeConfigMap) {
            this.systemEventTypeConfigMap = systemEventTypeConfigMap;
        }

        public List<Pattern> getPageViewSupportDomainRegexs() {
            return pageViewSupportDomainRegexs;
        }

        public void setPageViewSupportDomainRegexs(List<Pattern> pageViewSupportDomainRegexs) {
            this.pageViewSupportDomainRegexs = pageViewSupportDomainRegexs;
        }

        public List<Pattern> getPageViewIgnoreRemoteIpRegexs() {
            return pageViewIgnoreRemoteIpRegexs;
        }

        public void setPageViewIgnoreRemoteIpRegexs(List<Pattern> pageViewIgnoreRemoteIpRegexs) {
            this.pageViewIgnoreRemoteIpRegexs = pageViewIgnoreRemoteIpRegexs;
        }

        public List<Pattern> getPageViewSpiderUserAgentRegexs() {
            return pageViewSpiderUserAgentRegexs;
        }

        public void setPageViewSpiderUserAgentRegexs(List<Pattern> pageViewSpiderUserAgentRegexs) {
            this.pageViewSpiderUserAgentRegexs = pageViewSpiderUserAgentRegexs;
        }
    }

}
