/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.message;

import com.enjoyf.platform.service.joymeapp.PushListType;
import com.enjoyf.platform.service.joymeapp.PushMessage;
import com.enjoyf.platform.service.message.Notice;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
class NoticeCache {

    private static final long TIME_OUT_SEC = 60l * 60l * 6l;

    private static final String SERVICE_SECTION = "message";

    private static final String PREFIX_MESSAGE_NOTICE = "message_notice_";

    private static final String CACHE_KEY_PUSHMSGLIST = "_pushmsg_list_";

    private static final String CACHE_KEY_LASTMESSAGE_PREFIX = "_last_pushmessage_";


    private MemCachedConfig config;

    private MemCachedManager manager;

    NoticeCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
    }

    //////////////////////////////////////////////////////////
    public void putNoticeCache(String uno, String type, Notice notice) {
        manager.put(PREFIX_MESSAGE_NOTICE + uno + type, notice, TIME_OUT_SEC);
    }

    public Notice getNoticeCache(String uno, String type) {
        Object archiveObj = manager.get(PREFIX_MESSAGE_NOTICE + uno + type);
        if (archiveObj == null) {
            return null;
        }
        return (Notice) archiveObj;
    }

    public boolean deleteNoticeCache(String uno, String type) {
        return manager.remove(PREFIX_MESSAGE_NOTICE + uno + type);
    }

    //////////////////////////////////////////////////////////
    public void putPushMessageList(String appKey, int platform, String uno, long timestamp, List<PushMessage> pushMessageList, PushListType pushListType) {
        manager.put(SERVICE_SECTION + CACHE_KEY_PUSHMSGLIST + appKey + platform + uno + timestamp + pushListType.getCode(), pushMessageList, 60l * 5l);
    }

    public List<PushMessage> getPushMessageList(String appKey, int platform, String uno, long timestamp, PushListType pushListType) {
        Object pushMessageList = manager.get(SERVICE_SECTION + CACHE_KEY_PUSHMSGLIST + appKey + platform + uno + timestamp + pushListType.getCode());
        if (pushMessageList == null) {
            return null;
        }
        return (List<PushMessage>) pushMessageList;
    }

    public boolean removePushMessageList(String appKey, int platform, String uno, long timestamp, PushListType pushListType) {
        return manager.remove(SERVICE_SECTION + CACHE_KEY_PUSHMSGLIST + appKey + platform + uno + timestamp);
    }

    ////////////////////////////////////////////////////////
    public void putLastPushMessage(String appKey, String version, PushMessage pushMessage) {
        manager.put(SERVICE_SECTION + CACHE_KEY_LASTMESSAGE_PREFIX + appKey + version, pushMessage, TIME_OUT_SEC);
    }

    public PushMessage getLastPushMessage(String appKey, String version) {
        Object message = manager.get(SERVICE_SECTION + CACHE_KEY_LASTMESSAGE_PREFIX + appKey + version);
        if (message == null) {
            return null;
        }
        return (PushMessage) message;
    }

    public boolean deleteLastPushMessage(String appKey, String version) {
        return manager.remove(SERVICE_SECTION + CACHE_KEY_LASTMESSAGE_PREFIX + appKey + version);
    }


}
