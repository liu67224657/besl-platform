package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.advertise.AdvertiseEventType;
import com.enjoyf.platform.service.event.user.UserEventType;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="mailto:yinpengyi@enjoyf.com">Yin Pengyi</a>
 */
public class AdvertiseHotdeployConfig extends HotdeployConfig {
    //
    private static final Logger logger = LoggerFactory.getLogger(AdvertiseHotdeployConfig.class);

    /////////////////////////////////////////////////////////////////////////////
    //the keys
    private static final String KEY_USER_EVENT_TYPE_LIST = "advertise.event.type.list";
    private static final String SUFFEX_KEY_TO_ADVERTISE_TYPE = ".to.adv.type";


    private Cached cached;

    public AdvertiseHotdeployConfig() {
        super(EnvConfig.get().getAdvertiseHotdeployConfigureFile());
    }

    @Override
    public void init() {
        reload();
    }

    public synchronized void reload() {
        super.reload();

        //
        Cached tmpCache = new Cached();

        List<String> userEventTypeCodes = getList(KEY_USER_EVENT_TYPE_LIST);
        for (String userEventTypeCode : userEventTypeCodes) {
            UserEventType userEventType = UserEventType.getByCode(userEventTypeCode);

            if (userEventType != null) {
                String toAdvEventTypeCode = getString(userEventTypeCode + SUFFEX_KEY_TO_ADVERTISE_TYPE);
                AdvertiseEventType advertiseEventType = AdvertiseEventType.getByCode(toAdvEventTypeCode);

                if (advertiseEventType != null) {
                    tmpCache.getUserAdvertiseEventTypeMap().put(userEventType, advertiseEventType);
                } else {
                    GAlerter.lab("There is a unkown toAdvEventTypeCode in AdvertiseHotdeployConfig, toAdvEventTypeCode:" + toAdvEventTypeCode);
                }
            } else {
                GAlerter.lab("There is a unkown userEventTypeCode in AdvertiseHotdeployConfig, userEventTypeCode:" + userEventTypeCode);
            }
        }

        //
        this.cached = tmpCache;

        logger.info("Advertise Props init finished.");
    }

    public boolean supportUserEvent(UserEventType eventType) {
        return cached.getUserAdvertiseEventTypeMap().containsKey(eventType);
    }

    public AdvertiseEventType getAdvertiseEventType(UserEventType eventType) {
        return cached.getUserAdvertiseEventTypeMap().get(eventType);
    }

    //
    private class Cached {
        private Map<UserEventType, AdvertiseEventType> userAdvertiseEventTypeMap = new HashMap<UserEventType, AdvertiseEventType>();

        public Map<UserEventType, AdvertiseEventType> getUserAdvertiseEventTypeMap() {
            return userAdvertiseEventTypeMap;
        }
    }

}
