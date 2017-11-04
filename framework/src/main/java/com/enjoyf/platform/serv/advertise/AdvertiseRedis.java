/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.advertise;


import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.advertise.AdvertiseConstants;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.UUID;

/**
 * @author <a href=mailto:ericliu@staff.joyme.com>Eirc Liu</a>
 */

public class AdvertiseRedis {
    //
    private static final Logger logger = LoggerFactory.getLogger(AdvertiseRedis.class);


    private static final String KEY_CLICK_KEY = AdvertiseConstants.SERVICE_SECTION + "_clkdevice_";


    private static final String KEY_SET_ACTIVITYDEVICE_KEY = AdvertiseConstants.SERVICE_SECTION + "_set_actdevie_";

    private static final int activityBaseNum = 100;

    private RedisManager manager;

    public AdvertiseRedis(FiveProps p) {
        manager = new RedisManager(p);
    }

    public void putClickDevice(String deviceId, String agentId) {
        manager.set(KEY_CLICK_KEY + deviceId, agentId);

    }

    public String getClickDevice(String deviceId) {
        return manager.get(KEY_CLICK_KEY + deviceId);
    }

    public void removeClickDevice(String deviceId) {
        manager.remove(KEY_CLICK_KEY + deviceId);

    }
    ////////////////

    public void pushActivityDevice(String appkey, String deviceId) {
        manager.sadd(getActivityDeviceKey(appkey, deviceId), deviceId);
    }

    public boolean isExistsActivityDevice(String appkey, String deviceId) {
        return manager.sismember(getActivityDeviceKey(appkey, deviceId), deviceId);
    }

    public boolean removeActivityDevice(String appkey, String deviceId) {
        return manager.srem(appkey, deviceId) > 0;
    }

    public String getActivityDeviceKey(String appkey, String deviceId) {
        return KEY_SET_ACTIVITYDEVICE_KEY + appkey + TableUtil.getTableNumSuffix(deviceId.hashCode(), activityBaseNum);
    }

    ////////////////
    public Set<String> checkActivityDevice(String deviceKey, String[] deviceIds) {
        String tempKey = UUID.randomUUID().toString();
        try {
            manager.sadd(tempKey, deviceIds, 60);
            return manager.sinter(new String[]{tempKey, deviceKey});
        } finally {
            manager.remove(tempKey);
        }
    }
}


