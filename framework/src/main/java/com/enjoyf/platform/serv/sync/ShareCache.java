package com.enjoyf.platform.serv.sync;

import com.enjoyf.platform.service.sync.ShareBody;
import com.enjoyf.platform.service.sync.ShareBaseInfo;
import com.enjoyf.platform.service.sync.ShareTopic;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-5
 * Time: 下午8:08
 * To change this template use File | Settings | File Templates.
 */
class ShareCache {
    private static final long TIME_OUT_SEC = 60l * 5l;

    private static final String CACHE_SHARETOPIC_PREFIX = "share_topic_";
    private static final String CACHE_SHAREBODY_PREFIX = "share_body_";
    private static final String CACHE_SHAREBASEINFO_PREFIX = "share_base_info_";
//    private static final String CACHE_INFOID2SOURCE_PREFIX = "share_base_infoid2source_";

    private MemCachedConfig config;

    private MemCachedManager manager;

    ShareCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
    }


    public void putTopicList(long shareId, List<ShareTopic> topicList) {
        manager.put(CACHE_SHARETOPIC_PREFIX + shareId, topicList, TIME_OUT_SEC);
    }

    public List<ShareTopic> getTopicList(long shareId) {
        Object topicList = manager.get(CACHE_SHARETOPIC_PREFIX + shareId);
        if (topicList == null) {
            return null;
        }
        return (List<ShareTopic>) topicList;
    }

    public boolean deleteTopicList(long shareId) {
        return manager.remove(CACHE_SHARETOPIC_PREFIX + shareId);
    }

    //
    public void putBodyList(long shareId, List<ShareBody> bodyList) {
        manager.put(CACHE_SHAREBODY_PREFIX + shareId, bodyList, TIME_OUT_SEC);
    }

    public List<ShareBody> getBodyList(long shareId) {
        Object bodyList = manager.get(CACHE_SHAREBODY_PREFIX + shareId);
        if (bodyList == null) {
            return null;
        }
        return (List<ShareBody>) bodyList;
    }

    public boolean deleteBodyList(long shareId) {
        return manager.remove(CACHE_SHAREBODY_PREFIX + shareId);
    }

    //
    public void putShareBaseInfo(long shareId, ShareBaseInfo shareBaseInfo) {
        manager.put(CACHE_SHAREBASEINFO_PREFIX + shareId, shareBaseInfo, TIME_OUT_SEC);
//        manager.put(CACHE_INFOID2SOURCE_PREFIX + shareBaseInfo.getShareId(), shareId, TIME_OUT_SEC);
    }

    public ShareBaseInfo getShareBaseInfoBySource(long shareId) {
        Object shareBaseInfo = manager.get(CACHE_SHAREBASEINFO_PREFIX + shareId);
        if (shareBaseInfo == null) {
            return null;
        }
        return (ShareBaseInfo) shareBaseInfo;
    }

    public boolean deleteBaseInfoSource(long infoId) {
//        Object sourUrl = manager.get(CACHE_INFOID2SOURCE_PREFIX + infoId);
//        if (sourUrl == null) {
//            return true;
//        }
        manager.remove(CACHE_SHAREBASEINFO_PREFIX + infoId);
//        manager.remove(CACHE_INFOID2SOURCE_PREFIX + infoId);
        return true;
    }

    public ShareBaseInfo getShareBaseInfoById(long shareId) {
//        Object sourUrl = manager.get(CACHE_INFOID2SOURCE_PREFIX + shareId);
//        if (sourUrl == null) {
//            return null;
//        }

        Object shareBaseInfo = manager.get(CACHE_SHAREBASEINFO_PREFIX + shareId);
        if (shareBaseInfo == null) {
            return null;
        }
        return (ShareBaseInfo) shareBaseInfo;
    }

}
