/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.joymeapp;

import com.enjoyf.platform.service.joymeapp.Archive;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
class CmsArchiveCache {

    private static final long TIME_OUT_SEC = 60l * 60l * 6l;

    private static final String PREFIX_CMS_ARCHIVE = "joyme_app_cms_archive_";


    private MemCachedConfig config;

    private MemCachedManager manager;

    CmsArchiveCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
    }

    //////////////////////////////////////////////////////////
    public void putArchive(Archive archive) {
        manager.put(PREFIX_CMS_ARCHIVE + archive.getArchiveId(), archive, TIME_OUT_SEC);
    }

    public Archive getArchive(String archiveId) {
        Object archiveObj = manager.get(PREFIX_CMS_ARCHIVE);
        if (archiveObj == null) {
            return null;
        }
        return (Archive) archiveObj;
    }

    public boolean removeArchive(String archiveId) {
        return manager.remove(PREFIX_CMS_ARCHIVE + archiveId);
    }


}
