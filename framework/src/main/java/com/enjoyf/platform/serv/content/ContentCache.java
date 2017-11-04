/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.content;

import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.util.collection.MemDiskCacheConfig;
import com.enjoyf.platform.util.collection.MemDiskCacheManager;
import net.sf.ehcache.Element;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
public class ContentCache extends MemDiskCacheManager {

    private static final long TIME_OUT_SEC = 60 * 5;
    //
    private static final String KEY_CACHE_CONTENT_NAME = "content";
    //
    public ContentCache(MemDiskCacheConfig config) {
        super(config);
    }

    public Content getContent(String key) {
        Element element = getCache(KEY_CACHE_CONTENT_NAME).get(key);

        if (element == null) {
            return null;
        } else {
            return (Content) element.getObjectValue();
        }
    }

    public void putContent(String key, Content value) {
        getCache(KEY_CACHE_CONTENT_NAME).put(new Element(key, value));
    }

    public boolean removeContent(String key) {
        return getCache(KEY_CACHE_CONTENT_NAME).remove(key);
    }

    public int getContentSize() {
        return getCache(KEY_CACHE_CONTENT_NAME).getSize();
    }
    ////////////////////////////////////////////////////////////////
}
