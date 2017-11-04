/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.example;

import com.enjoyf.platform.service.example.Example;
import com.enjoyf.platform.util.collection.MemDiskCacheConfig;
import com.enjoyf.platform.util.collection.MemDiskCacheManager;
import net.sf.ehcache.Element;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
public class ExampleCache extends MemDiskCacheManager {
    //
    private static final String KEY_CACHE_EXAMPLE_NAME = "example";

    //
    public ExampleCache(MemDiskCacheConfig config) {
        super(config);
    }

    public Example getContent(String key) {
        Element element = getCache(KEY_CACHE_EXAMPLE_NAME).get(key);

        if (element == null) {
            return null;
        } else {
            return (Example) element.getObjectValue();
        }
    }

    public void putExampleEntry(String key, Example value) {
        getCache(KEY_CACHE_EXAMPLE_NAME).put(new Element(key, value));
    }

    public boolean removeExampleEntry(String key) {
        return getCache(KEY_CACHE_EXAMPLE_NAME).remove(key);
    }

    public int getExampleCacheSize() {
        return getCache(KEY_CACHE_EXAMPLE_NAME).getSize();
    }
}
