/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.profile;

import com.enjoyf.platform.service.profile.ProfileConstants;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
class ProfileMemcachedCache {
    private static final Logger logger = LoggerFactory.getLogger(ProfileMemcachedCache.class);

    private static final long TIME_OUT_SEC = 60l * 30l;

    private String serviceSection;

    private static final String PREFIX_MOBILE_CODE = "_mobile_code_";


    private MemCachedConfig config;

    private MemCachedManager manager;

    ProfileMemcachedCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
        this.serviceSection = ProfileConstants.SERVICE_SECTION;
    }

    //////////////////////////////////////////////////////////
    public String getMobileCode(String uno) {
        Object code = manager.get(serviceSection + PREFIX_MOBILE_CODE + uno);

        if (code == null) {
            return null;
        }
        return (String) code;
    }

    public void putMobileCode(String uno, String code) {
        logger.info("PROFILE MemcachedCache =======key=" + serviceSection + PREFIX_MOBILE_CODE + uno);
        manager.put(serviceSection + PREFIX_MOBILE_CODE + uno, code, TIME_OUT_SEC);
    }

    public boolean removeMobileCode(String uno) {
        return manager.remove(serviceSection + PREFIX_MOBILE_CODE + uno);
    }


}
