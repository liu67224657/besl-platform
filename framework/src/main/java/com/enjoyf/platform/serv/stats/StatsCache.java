package com.enjoyf.platform.serv.stats;

import com.enjoyf.platform.service.stats.StatConstants;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

/**
 * Created by zhimingli on 2015/11/14 0014.
 */
public class StatsCache {

    private MemCachedConfig config;

    private MemCachedManager manager;

    private String section;

    private static final long TIME_OUT_SEC = 60l * 60l * 24l;


    StatsCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
        section = StatConstants.SERVICE_SECTION;
    }


}
