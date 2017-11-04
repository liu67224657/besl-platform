package com.enjoyf.platform.serv.notice;

import com.enjoyf.platform.service.notice.NoticeConstants;
import com.enjoyf.platform.service.notice.wiki.WikiNotice;
import com.enjoyf.platform.service.notice.wiki.WikiNoticeType;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by ericliu on 16/3/17.
 */
public class WikiNoticeRedis {

    //
    private static final Logger logger = LoggerFactory.getLogger(WikiNoticeRedis.class);

    private static final String KEY_PREFIX = NoticeConstants.SERVICE_SECTION;

    private static final String KEY_WIKI_NOTICE_LIST = KEY_PREFIX + "_wikinoticelist_";

    private static final String KEY_WIKI_NOTICE_SUM = KEY_PREFIX + "_wikinoticesum_";

    private static final String KEY_WIKI_MESSAGE_LIST = KEY_PREFIX + "_wikimessage_";

    private static final String KEY_SRC_WIKI_MESSAGE_LIST = KEY_PREFIX + "_src_wikimessage_";

    private RedisManager manager;

    public WikiNoticeRedis(FiveProps p) {
        manager = new RedisManager(p);
    }

    public long length(String ownProfileId, WikiNoticeType wikiNoticeType) {
        return manager.length(KEY_WIKI_NOTICE_LIST + ownProfileId +"_"+ wikiNoticeType.getCode());
    }

    public void pushNotice(String ownProfileId, WikiNoticeType wikiNoticeType, String... wikiNoticeJson) {
        manager.lpush(KEY_WIKI_NOTICE_LIST + ownProfileId +"_"+ wikiNoticeType.getCode(), wikiNoticeJson);
    }

    public List<String> queryNotices(String ownProfileId, WikiNoticeType wikiNoticeType, Pagination pagination) {
        return manager.lrange(KEY_WIKI_NOTICE_LIST + ownProfileId +"_"+ wikiNoticeType.getCode(), pagination.getStartRowIdx(), pagination.getEndRowIdx());
    }

    public long removeNotices(String ownProfileId, WikiNoticeType wikiNoticeType) {
        return manager.remove(KEY_WIKI_NOTICE_LIST + ownProfileId +"_"+ wikiNoticeType.getCode());
    }


    public long removeNoticeFromList(String ownProfileId, WikiNoticeType wikiNoticeType,WikiNotice wikiNotice) {
        return manager.lrem(KEY_WIKI_NOTICE_LIST + ownProfileId +"_"+ wikiNoticeType.getCode(),1,wikiNotice.toJson());
    }

    public void incrseNoticeSum(String ownProfileId, WikiNoticeType wikiNoticeType, int value) {
        manager.incr(KEY_WIKI_NOTICE_LIST + ownProfileId +"_"+ wikiNoticeType.getCode(), value, -1);
    }

    public int getNoticeSum(String ownProfileId, WikiNoticeType wikiNoticeType) {
        return Integer.parseInt(manager.get(KEY_WIKI_NOTICE_LIST + ownProfileId +"_"+ wikiNoticeType.getCode()));
    }

    //置为0
    public void removeNoticeSum(String ownProfileId, WikiNoticeType wikiNoticeType) {
        manager.set(KEY_WIKI_NOTICE_LIST + ownProfileId + wikiNoticeType.getCode(), String.valueOf(0));
    }

    ////////////////////////////////
    public long lengthWikiMesssage(String sessionId) {
        return manager.length(KEY_WIKI_MESSAGE_LIST + sessionId);
    }

    public void lpushWikiMessage(String sessionId, String... wikiMessage) {
        manager.lpush(KEY_WIKI_MESSAGE_LIST + sessionId, wikiMessage);
    }

    public void rpushWikiMessage(String sessionId, String... wikiMessage) {
        manager.rpush(KEY_WIKI_MESSAGE_LIST + sessionId, wikiMessage);
    }

    public List<String> queryWikiMessage(String sessionId, Pagination pagination) {
        return manager.lrange(KEY_WIKI_MESSAGE_LIST + sessionId, pagination.getStartRowIdx(), pagination.getEndRowIdx());
    }

    public long removeWikiMessage(String sessionId) {
        return manager.remove(KEY_WIKI_MESSAGE_LIST + sessionId);
    }


}
