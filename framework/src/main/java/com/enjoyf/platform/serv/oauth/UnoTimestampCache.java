/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.oauth;

import com.enjoyf.platform.service.oauth.OAuthConstants;
import com.enjoyf.platform.service.oauth.TimestampVerification;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
class UnoTimestampCache {

    private static final long TIME_OUT_SEC = 60L*20L;//默认是60L*20L

    private String serviceSection;

    private static final String PREFIX_UNO_TIMESTAMP = "_ut_";


    private MemCachedConfig config;

    private MemCachedManager manager;

    UnoTimestampCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
        this.serviceSection = OAuthConstants.SERVICE_SECTION;
    }

    //////////////////////////////////////////////////////////


    public void putTimestamp(TimestampVerification shareInfo) {
        manager.put(serviceSection + PREFIX_UNO_TIMESTAMP + shareInfo.getUno() + shareInfo.getTimestamp(), shareInfo, TIME_OUT_SEC);
    }

    public TimestampVerification getTimestamp(String uno, String timestamp) {
        Object object = manager.get(serviceSection + PREFIX_UNO_TIMESTAMP + uno + timestamp);
        if (object == null) {
            return null;
        }
        return (TimestampVerification) object;
    }

    public boolean deleteTimestamp(String uno) {
        return manager.remove(serviceSection + PREFIX_UNO_TIMESTAMP + uno);
    }


}
