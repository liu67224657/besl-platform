/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.viewline;

import com.enjoyf.platform.service.viewline.ViewLine;
import com.enjoyf.platform.util.collection.MemDiskCacheConfig;
import com.enjoyf.platform.util.collection.MemDiskCacheManager;
import net.sf.ehcache.Element;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
public class ViewLineCache extends MemDiskCacheManager {
    //
    private static final String KEY_CACHE_VIEWLINE_NAME = "viewline";

    //
    public ViewLineCache(MemDiskCacheConfig config) {
        super(config);
    }

    public ViewLine getContent(String key) {
        Element element = getCache(KEY_CACHE_VIEWLINE_NAME).get(key);

        if (element == null) {
            return null;
        } else {
            return (ViewLine) element.getObjectValue();
        }
    }

    public void putViewLine(String key, ViewLine value) {
        getCache(KEY_CACHE_VIEWLINE_NAME).put(new Element(key, value));
    }

    public boolean removeViewLine(String key) {
        return getCache(KEY_CACHE_VIEWLINE_NAME).remove(key);
    }

    public int getViewLineCacheSize() {
        return getCache(KEY_CACHE_VIEWLINE_NAME).getSize();
    }
}
