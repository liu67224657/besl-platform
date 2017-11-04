/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.advertise;

import com.enjoyf.platform.service.advertise.AdvertiseConstants;
import com.enjoyf.platform.service.advertise.app.AppAdvertise;
import com.enjoyf.platform.service.advertise.app.AppAdvertiseInfo;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublish;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublishType;
import com.enjoyf.platform.service.content.ContentConstants;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
class AppAdvertiseCache {


    private static final long TIME_OUT_SEC = 60l * 30l;

    private static final String PREFIX_APP_ADVERTISE = "_appad_";

    private static final String PREFIX_APP_ADVERTISE_ID = "_appadid_";

    private static final String PREFIX_APP_PUBLISH = "_appid_type_";


    private MemCachedConfig config;

    private MemCachedManager manager;

    AppAdvertiseCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(this.config);
    }

    //////////////////////////////////////////////////////////
    public void putAppAdvertises(String appKey, AppAdvertisePublishType type, List<AppAdvertiseInfo> advertise) {
        manager.put(getKey(appKey, type), advertise, TIME_OUT_SEC);
    }

    public List<AppAdvertiseInfo> getAppAdvertises(String appKey, AppAdvertisePublishType type) {
        Object obj = manager.get(getKey(appKey, type));
        if (obj == null) {
            return null;
        }
        return (List<AppAdvertiseInfo>) obj;
    }

    public boolean deleteAppAdvertises(String appKey, AppAdvertisePublishType type) {
        return manager.remove(getKey(appKey, type));
    }

    private String getKey(String appKey, AppAdvertisePublishType type) {
        return ContentConstants.SERVICE_SECTION + PREFIX_APP_ADVERTISE + appKey + "_" + type.getCode();
    }

      //////////////////////////////////////////////////////////
    public void putAppAdvertiseById(AppAdvertise advertise) {
        manager.put(getKeyId(advertise.getAdvertiseId()), advertise, TIME_OUT_SEC);
    }

    public AppAdvertise getAppAdvertisById(long advertiseId) {
        Object obj = manager.get(getKeyId(advertiseId));
        if (obj == null) {
            return null;
        }
        return (AppAdvertise) obj;
    }

    public boolean deleteAppAdvertiseById(long advertiseId) {
        return manager.remove(getKeyId(advertiseId));
    }

    private String getKeyId(long advertiseId){
        return AdvertiseConstants.SERVICE_SECTION + PREFIX_APP_ADVERTISE_ID+advertiseId;
    }

    public void putAppAdvertisePublish(String appId, int code, String channel, List<AppAdvertisePublish> list) {
        manager.put(AdvertiseConstants.SERVICE_SECTION + PREFIX_APP_PUBLISH + appId + code + channel, list, TIME_OUT_SEC);
    }

    public List<AppAdvertisePublish> getAppAdvertisePublish(String appId, int code, String channel) {
        Object obj = manager.get(AdvertiseConstants.SERVICE_SECTION + PREFIX_APP_PUBLISH + appId + code + channel);
        if (obj == null) {
            return null;
        }
        return (List<AppAdvertisePublish>) obj;
    }

    public boolean deleteAppAdvertisePublish(String appId, int code, String channel) {
        return manager.remove(AdvertiseConstants.SERVICE_SECTION + PREFIX_APP_PUBLISH + appId + code + channel);
    }
}
