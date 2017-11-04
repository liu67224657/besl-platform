/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.collection;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-12-6 下午6:50
 * Description:
 */
public abstract class MemDiskCacheManager {
    //
    private MemDiskCacheConfig config;
    private CacheManager cacheManager;

    public MemDiskCacheManager(MemDiskCacheConfig conf) {
        this.config = conf;

        //
        cacheManager = new CacheManager(config.getManagerConfig());

        for (CacheConfiguration cacheConfig : config.getCacheConfigMap().values()) {
            cacheManager.addCache(new Cache(cacheConfig));
        }
    }

    public Cache getCache(String name) {
        return cacheManager.getCache(name);
    }
}
