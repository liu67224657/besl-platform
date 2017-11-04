/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.collection;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-12-6 下午7:12
 * Description:
 */
public class MemDiskCacheConfig {
    //
    private static final String KEY_MANAGER_UPDATECHECK = "mdcache.manager.updateCheck";
    private static final String KEY_MANAGER_MAXBYTESONHEAP = "mdcache.manager.maxBytesOnHeap";
    private static final String KEY_MANAGER_MONITORING = "mdcache.manager.Monitoring";
    private static final String KEY_MANAGER_DYNAMICCONFIG = "mdcache.manager.dynamicConfig";

    private static final String KEY_MANAGER_CACHE_LIST = "mdcache.cache.list";

    private static final String SUFFIX_KEY_CACHE_MAXELEMENTSINMEMORY = ".cache.maxElementsInMemory";
    private static final String SUFFIX_KEY_CACHE_MAXELEMENTSONDISK = ".cache.maxElementsOnDisk";
    private static final String SUFFIX_KEY_CACHE_ETERNAL = ".cache.eternal";

    private static final String SUFFIX_KEY_CACHE_OVERFLOWTODISK = ".cache.overflowToDisk";
    private static final String SUFFIX_KEY_CACHE_TOVERFLOWTOOFFHEAP = ".cache.tOverflowToOffHeap";

    private static final String SUFFIX_KEY_CACHE_DISKSPOOLBUFFERSIZEMB = ".cache.diskSpoolBufferSizeMB";
    private static final String SUFFIX_KEY_CACHE_TIMETOIDLESECONDS = ".cache.timeToIdleSeconds";
    private static final String SUFFIX_KEY_CACHE_TIMETOLIVESECONDS = ".cache.timeToLiveSeconds";
    private static final String SUFFIX_KEY_CACHE_MEMORYSTOREEVICTIONPOLICY = ".cache.memoryStoreEvictionPolicy";
    private static final String SUFFIX_KEY_CACHE_TRANSACTIONALMODE = ".cache.transactionalMode";

    //
    private FiveProps props;

    //
    private static final String PATH_ROOT = "/opt/servicedata/cache";
    private Configuration managerConfig;
    private Map<String, CacheConfiguration> cacheConfigMap = new HashMap<String, CacheConfiguration>();

    //
    public MemDiskCacheConfig(String managerName, FiveProps p) {
        //
        props = p;

        //fullfill the props to object.
        //
        managerConfig = new Configuration();
        managerConfig.setName(managerName);
        managerConfig.setDynamicConfig(props.getBoolean(KEY_MANAGER_DYNAMICCONFIG, false));
        managerConfig.setMonitoring(props.get(KEY_MANAGER_MONITORING, "autodetect"));
        managerConfig.setUpdateCheck(props.getBoolean(KEY_MANAGER_UPDATECHECK, true));

        String storePath = PATH_ROOT + "/" + managerName;

        //
        List<String> cacheNames = props.getList(KEY_MANAGER_CACHE_LIST);
        for (String cacheName : cacheNames) {
            CacheConfiguration cacheConfig = new CacheConfiguration();

            cacheConfig.setName(cacheName);

            cacheConfig.setMaxElementsInMemory(props.getInt(cacheName + SUFFIX_KEY_CACHE_MAXELEMENTSINMEMORY, 1024));
            cacheConfig.setMaxElementsOnDisk(props.getInt(cacheName + SUFFIX_KEY_CACHE_MAXELEMENTSONDISK, 10240));

            cacheConfig.setEternal(props.getBoolean(cacheName + SUFFIX_KEY_CACHE_ETERNAL, false));

            cacheConfig.setOverflowToDisk(props.getBoolean(cacheName + SUFFIX_KEY_CACHE_OVERFLOWTODISK, false));
            cacheConfig.setOverflowToOffHeap(props.getBoolean(cacheName + SUFFIX_KEY_CACHE_TOVERFLOWTOOFFHEAP, false));

            cacheConfig.setDiskStorePath(storePath);

            cacheConfig.setDiskSpoolBufferSizeMB(props.getInt(cacheName + SUFFIX_KEY_CACHE_DISKSPOOLBUFFERSIZEMB, 128));
            cacheConfig.setTimeToIdleSeconds(props.getInt(cacheName + SUFFIX_KEY_CACHE_TIMETOIDLESECONDS, 3600));
            cacheConfig.setTimeToLiveSeconds(props.getInt(cacheName + SUFFIX_KEY_CACHE_TIMETOLIVESECONDS, 600));
            cacheConfig.setMemoryStoreEvictionPolicy(props.get(cacheName + SUFFIX_KEY_CACHE_MEMORYSTOREEVICTIONPOLICY, "LFU"));
            cacheConfig.setTransactionalMode(props.get(cacheName + SUFFIX_KEY_CACHE_TRANSACTIONALMODE, "off"));

            //
            cacheConfigMap.put(cacheName, cacheConfig);
        }
    }

    //
    public Configuration getManagerConfig() {
        return managerConfig;
    }

    public Map<String, CacheConfiguration> getCacheConfigMap() {
        return cacheConfigMap;
    }

    //
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
