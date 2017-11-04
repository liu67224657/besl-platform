/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.joymeapp;


import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author <a href=mailto:ericliu@staff.joyme.com>Eirc Liu</a>
 */

public class JoymeAppRedis {
    //
    private static final Logger logger = LoggerFactory.getLogger(JoymeAppRedis.class);

    private static final int TIME_OUT_SEC = 24 * 60 * 60;

    private static final String KEY_PREFIX = JoymeAppConstants.SERVICE_SECTION;

    private static final String KEY_JOYME_WIKI = "_joymewiki_";
    private static final String KEY_CLIENTLINE = "_clientline_";
    private static final String KEY_CLIENTLINE_LIST = "_clientline_list_";
    private static final String KEY_CLIENTLINEITEM_IDS = "_clientlineitem_ids_";
    private static final String KEY_CLIENTLINEITEM = "_clientlineitem_";
    private static final String KEY_WANBA_SHAKE_GAME = "_wanba_shake_game_";

    private static final String KEY_TAG_DEDEARCHIVES_TIMER_KEYS = "_tagdedeachives_timertask_keys_";
    private static final String KEY_TAG_DEDEARCHIVES_TIMER = "_tagdedeachives_timertask_";

    private RedisManager manager;

    public JoymeAppRedis(FiveProps p) {
        manager = new RedisManager(p);
    }

    public JoymeWiki getJoymeWiki(JoymeWikiContextPath contextPath, String wikiKey) {
        String wikiJson = manager.get(KEY_PREFIX + KEY_JOYME_WIKI + contextPath.getCode() + "_" + wikiKey);
        return JoymeWiki.parse(wikiJson);
    }

    public void putJoymeWiki(JoymeWikiContextPath contextPath, String wikiKey, JoymeWiki wiki) {
        manager.setSec(KEY_PREFIX + KEY_JOYME_WIKI + contextPath.getCode() + "_" + wikiKey, wiki.toJsonStr(), TIME_OUT_SEC);
    }

    public Set<String> getClientLineCodeList(ClientLineType lineType, Integer platform) {
        String key = KEY_PREFIX + KEY_CLIENTLINE_LIST + lineType.getCode();
        if (platform != null) {
            key = key + "_" + platform;
        }
        return manager.zrange(key, 0l, -1l, manager.RANGE_ORDERBY_ASC);
    }

    public void putClientLineCodeList(int lineType, Integer platform, ClientLine clientLine) {
        String key = KEY_PREFIX + KEY_CLIENTLINE_LIST + lineType;
        if (platform != null) {
            key = key + "_" + platform;
        }
        manager.zadd(key, clientLine.getDisplay_order(), clientLine.getCode(), 0);
    }


    public void removeClientLineCodeList(int lineType, Integer platform, String lineCode) {
        String key = KEY_PREFIX + KEY_CLIENTLINE_LIST + lineType;
        if (platform != null) {
            key = key + "_" + platform;
        }
        manager.zrem(key, lineCode);
    }

    public void sortClientLineCodeList(int lineType, int platform, double incOrder, String lineCode) {
        manager.zincrby(KEY_PREFIX + KEY_CLIENTLINE_LIST + lineType + "_" + platform, incOrder, lineCode);
    }

    public Set<String> getClientItemIdList(String lineCode, Pagination pagination) {
        return manager.zrange(KEY_PREFIX + KEY_CLIENTLINEITEM_IDS + lineCode, pagination.getStartRowIdx(), pagination.getEndRowIdx(), manager.RANGE_ORDERBY_ASC);

    }

    public void putClientItemIdList(String lineCode, ClientLineItem item) {
        manager.zadd(KEY_PREFIX + KEY_CLIENTLINEITEM_IDS + lineCode, item.getDisplayOrder(), String.valueOf(item.getItemId()), 0);
    }

    public Long removeClientItemIdList(String lineCode, ClientLineItem item) {
        return manager.zrem(KEY_PREFIX + KEY_CLIENTLINEITEM_IDS + lineCode, String.valueOf(item.getItemId()));
    }

    public ClientLineItem getClientItem(String itemId) {
        return ClientLineItem.parse(manager.get(KEY_PREFIX + KEY_CLIENTLINEITEM + itemId));
    }

    public void putClientItem(String itemId, ClientLineItem item) {
        manager.setSec(KEY_PREFIX + KEY_CLIENTLINEITEM + itemId, item.toJson(), 0);
    }

    public String getClientLine(String lineCode) {
        return manager.get(KEY_PREFIX + KEY_CLIENTLINE + lineCode);
    }

    public void putClientLine(String lineCode, ClientLine line) {
        manager.setSec(KEY_PREFIX + KEY_CLIENTLINE + lineCode, line.toJsonStr(), 0);
    }

    public void putGameIdByWeight(String code, String gameId) {
        manager.lpush(KEY_PREFIX + KEY_WANBA_SHAKE_GAME + code, gameId);
    }

    public boolean delGameIdByWeight(String code) {
        if (!manager.keyExists(KEY_PREFIX + KEY_WANBA_SHAKE_GAME + code)) {
            return true;
        }
        return manager.remove(KEY_PREFIX + KEY_WANBA_SHAKE_GAME + code) > 0l;
    }

    public Set<String> getGameIdByWeight(String code) {
        String key = KEY_PREFIX + KEY_WANBA_SHAKE_GAME + code;
        if (!manager.keyExists(key)) {
            return null;
        }
        Set<String> idSet = new HashSet<String>();
        int length = (int) manager.length(key);
        if (length <= 0) {
            return null;
        }

        int index = RandomUtil.getRandomInt(length);
        String iid = manager.lindex(key, index);
        if (StringUtil.isEmpty(iid)) {
            idSet.add(iid);
        }
        if (length > (index + 1)) {
            for (int i = (index + 1); i < length; i++) {
                String gid = manager.lindex(key, i);
                if (StringUtil.isEmpty(gid) || idSet.contains(gid)) {
                    continue;
                }
                idSet.add(gid);
                if (idSet.size() >= 5) {
                    break;
                }
            }
        }

        if (idSet.size() < 5) {
            if (index > 0) {
                for (int i = 0; i < index; i++) {
                    String gid = manager.lindex(key, i);
                    if (StringUtil.isEmpty(gid) || idSet.contains(gid)) {
                        continue;
                    }
                    idSet.add(gid);
                    if (idSet.size() >= 5) {
                        break;
                    }
                }
            }
        }
        return idSet;
    }

    public void putTagDedearchivesTimerTaskCache(Long tagid, Set<String> idSet, Date publishDate) {
        if(CollectionUtil.isEmpty(idSet) || tagid == null || publishDate == null){
            return;
        }
        String key = KEY_PREFIX + KEY_TAG_DEDEARCHIVES_TIMER_KEYS + tagid;
        for (String id : idSet) {
            manager.zadd(key, publishDate.getTime() / 1000, id, -1);
            putTagDedearchivesTimer(id, publishDate.getTime());
        }
    }

    public void putTagDedearchivesTimer(String id, long time) {
        String key = KEY_PREFIX + KEY_TAG_DEDEARCHIVES_TIMER + id;
        manager.set(key, String.valueOf(time));
    }

    public PageRows<String> getTagDedearchivesTimerTaskCache(Long tagid, Pagination page) {
        PageRows<String> pageRows = null;

        String key = KEY_PREFIX + KEY_TAG_DEDEARCHIVES_TIMER_KEYS + tagid;
        if(!manager.keyExists(key)){
            return null;
        }
        long start = 0l;
        if(page != null){
            start = page.getStartRowIdx();
        }
        long end = -1l;
        if(page != null){
            end = page.getEndRowIdx();
        }
        Set<String> idSet = manager.zrange(key, start, end, RedisManager.RANGE_ORDERBY_ASC);
        if(!CollectionUtil.isEmpty(idSet)){
            pageRows = new PageRows<String>();
            List<String> list = new ArrayList<String>();
            for(String id:idSet){
                list.add(id);
            }
            pageRows.setRows(list);
            if(page != null){
                page.setTotalRows((int) manager.zcard(key));
            }
            pageRows.setPage(page);
        }
        return pageRows;
    }

    public String getTagDedearchivesTimer(String id) {
        String key = KEY_PREFIX + KEY_TAG_DEDEARCHIVES_TIMER + id;
        return manager.get(key);
    }

    public boolean removeTagDedearchivesTimerTaskCache(Long tagid, Set<String> idSet, Date publishDate) {
        if(CollectionUtil.isEmpty(idSet) || tagid == null){
            return false;
        }
        String key = KEY_PREFIX + KEY_TAG_DEDEARCHIVES_TIMER_KEYS + tagid;
        for (String id : idSet) {
            manager.zrem(key, id);
            removeTagDedearchivesTimer(id);
        }
        return true;
    }

    public boolean removeTagDedearchivesTimer(String id) {
        String key = KEY_PREFIX + KEY_TAG_DEDEARCHIVES_TIMER + id;
        return manager.remove(key) > 0l;
    }
}

