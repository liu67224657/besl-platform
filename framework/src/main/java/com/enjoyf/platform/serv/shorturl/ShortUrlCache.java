/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.shorturl;

import com.enjoyf.platform.service.shorturl.ShortUrl;
import com.enjoyf.platform.util.collection.MemDiskCacheConfig;
import com.enjoyf.platform.util.collection.MemDiskCacheManager;
import net.sf.ehcache.Element;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
public class ShortUrlCache extends MemDiskCacheManager {
    //
    private static final String KEY_CACHE_COTENT_NAME = "shorturl";

    public ShortUrlCache(MemDiskCacheConfig config) {
        super(config);
    }

    public ShortUrl getShortUrl(String key) {
        Element element = getCache(KEY_CACHE_COTENT_NAME).get(key);

        if (element != null) {
            return (ShortUrl) element.getObjectValue();
        } else {
            return null;
        }
    }

    public void putShortUrl(String key, ShortUrl value) {
        getCache(KEY_CACHE_COTENT_NAME).put(new Element(key, value));
    }

    public boolean removeShortUrl(String key) {
        return getCache(KEY_CACHE_COTENT_NAME).remove(key);
    }

    public int getShortUrlSize() {
        return getCache(KEY_CACHE_COTENT_NAME).getSize();
    }
}
