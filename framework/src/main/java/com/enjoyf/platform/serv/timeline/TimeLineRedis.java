package com.enjoyf.platform.serv.timeline;

import com.enjoyf.platform.service.timeline.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.redis.RedisManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by zhitaoshi on 2016/3/23.
 */
public class TimeLineRedis {

    private static final String SOCIAL_TIME_LINE_ITEM_LIST_DOMAIN = TimeLineConstants.SERVICE_SECTION + "_socialtimelineitem_domain_";
    private static final String SOCIAL_TIME_LINE_ITEM_LIST_DOMAIN_TYPE = TimeLineConstants.SERVICE_SECTION + "_socialtimelineitem_domain_type_";

    private static final String KEY_PREFIX = "user_timeline";
    //我的动态
    private static final String USER_TIMELINE_ALL = "_all_";

    //我的分类动态
    private static final String USER_TIMELINE_TYPE = "_type_";
    //我的好友动态
    private static final String USER_TIMELINE_FRIEND = "_friend_";

    private static final String USER_TIMELINE_OBJ = "_obj_";

    private RedisManager manager;

    public TimeLineRedis(FiveProps props) {
        this.manager = new RedisManager(props);
    }

    public PageRows<SocialTimeLineItem> querySocialTimeLineItemList(String ownUno, SocialTimeLineDomain socialTimeLineDomain, Pagination page) {
        List<SocialTimeLineItem> list = new ArrayList<SocialTimeLineItem>();
        List<String> cacheList = manager.lrange(SOCIAL_TIME_LINE_ITEM_LIST_DOMAIN + ownUno + "_" + socialTimeLineDomain.getCode(), page == null ? 0 : page.getStartRowIdx(), page == null ? 100 : page.getEndRowIdx());
        if (!CollectionUtil.isEmpty(cacheList)) {
            for (String objStr : cacheList) {
                SocialTimeLineItem socialTimeLineItem = SocialTimeLineItem.parse(objStr);
                if (socialTimeLineItem != null) {
                    list.add(socialTimeLineItem);
                }
            }
            if (page != null) {
                page.setTotalRows((int) manager.length(SOCIAL_TIME_LINE_ITEM_LIST_DOMAIN + ownUno + "_" + socialTimeLineDomain.getCode()));
            }
        }
        PageRows<SocialTimeLineItem> pageRows = new PageRows<SocialTimeLineItem>();
        pageRows.setRows(list);
        pageRows.setPage(page);
        return pageRows;
    }

    public void putSocialTimeLineItemList(String ownUno, SocialTimeLineDomain socialTimeLineDomain, String value) {
        manager.lpush(SOCIAL_TIME_LINE_ITEM_LIST_DOMAIN + ownUno + "_" + socialTimeLineDomain.getCode(), value);
    }

    public void removeSocialTimeLineItemList(String ownUno, SocialTimeLineDomain socialTimeLineDomain) {
        manager.remove(SOCIAL_TIME_LINE_ITEM_LIST_DOMAIN + ownUno + "_" + socialTimeLineDomain.getCode());
    }

    public PageRows<String> queryUGCWikiUserDynamicCache(SocialTimeLineDomain socialTimeLineDomain, SocialTimeLineItemType socialTimeLineItemType, String profileId, Pagination page) {
        PageRows<String> pageRows = null;
        List<String> list = manager.lrange(SOCIAL_TIME_LINE_ITEM_LIST_DOMAIN_TYPE + socialTimeLineDomain.getCode() + "_" + socialTimeLineItemType.getCode() + "_" + profileId, page == null ? 0 : page.getStartRowIdx(), page == null ? 10 : page.getEndRowIdx());
        if (!CollectionUtil.isEmpty(list)) {
            page.setTotalRows((int) manager.length(SOCIAL_TIME_LINE_ITEM_LIST_DOMAIN_TYPE + socialTimeLineDomain.getCode() + "_" + socialTimeLineItemType.getCode() + "_" + profileId));
            pageRows = new PageRows<String>();
            pageRows.setRows(list);
            pageRows.setPage(page);
        }
        return pageRows;
    }

    public void putUGCWikiUserDynamicCache(SocialTimeLineDomain socialTimeLineDomain, SocialTimeLineItemType socialTimeLineItemType, String profileId, String value) {
        manager.lpush(SOCIAL_TIME_LINE_ITEM_LIST_DOMAIN_TYPE + socialTimeLineDomain.getCode() + "_" + socialTimeLineItemType.getCode() + "_" + profileId, value);
    }

    public boolean removeAllUGCWikiUserDynamicCache(SocialTimeLineDomain socialTimeLineDomain, SocialTimeLineItemType socialTimeLineItemType, String profileId) {
        return manager.remove(SOCIAL_TIME_LINE_ITEM_LIST_DOMAIN_TYPE + socialTimeLineDomain.getCode() + "_" + socialTimeLineItemType.getCode() + "_" + profileId) > 0;
    }


    public void putUserTimeLineItem(UserTimeline userTimeline) {
        manager.set(KEY_PREFIX + USER_TIMELINE_OBJ + userTimeline.getItemId(), userTimeline.toJson());
    }

    public UserTimeline getUserTimeLineItem(String itemId) {
        String objString = manager.get(KEY_PREFIX + USER_TIMELINE_OBJ + itemId);
        return UserTimeline.toObject(objString);
    }


    //todo 用zset
//    public void putUserTimeLineByProfileId(UserTimeline userTimeline) {
//        //保存我的动态
//        manager.lpush(KEY_PREFIX + USER_TIMELINE_ALL + userTimeline.getProfileId(), userTimeline.getExtendBody());
//        //保存我的分类动态 例如：wiki或者ask(玩霸)
//        manager.lpush(KEY_PREFIX + USER_TIMELINE_TYPE + userTimeline.getProfileId() + "_" + userTimeline.getType(), userTimeline.getExtendBody());
//    }

    public void putUserTimeLineByProfileId(UserTimeline userTimeline) {
        //to Json
        manager.zadd(getUserTimeLineKey(userTimeline.getProfileId(), "all", userTimeline.getDomain()), userTimeline.getItemId(), String.valueOf(userTimeline.getItemId()), -1);
        manager.zadd(getUserTimeLineKey(userTimeline.getProfileId(), userTimeline.getType(), userTimeline.getDomain()), userTimeline.getItemId(), String.valueOf(userTimeline.getItemId()), -1);
        putUserTimeLineItem(userTimeline);
    }

    public PageRows<UserTimeline> queryUserTimeLineByProfileId(String profileId, String type, String domain, Pagination page) {
        String key = getUserTimeLineKey(profileId, type, domain);
        int total = (int) manager.zcard(key);
        page.setTotalRows(total);
        Set<String> idSet = manager.zrange(key, page.getStartRowIdx(), page.getEndRowIdx(), RedisManager.RANGE_ORDERBY_DESC);
        PageRows<UserTimeline> rows = new PageRows<UserTimeline>();
        rows.setPage(page);
        if (CollectionUtil.isEmpty(idSet)) {
            return rows;
        }

        for (String id : idSet) {
            UserTimeline userTimeline = getUserTimeLineItem(id);
            if (userTimeline != null) {
                rows.getRows().add(userTimeline);
            }
        }
        return rows;
    }

    public boolean removeUserTimelineByProfileId(String itemId) {
        UserTimeline userTimeline = getUserTimeLineItem(itemId);
        String pid = userTimeline.getProfileId();
        boolean result = manager.zrem(getUserTimeLineKey(pid, "all", userTimeline.getDomain()), String.valueOf(userTimeline.getItemId())) > 0;
        result = manager.zrem(getUserTimeLineKey(pid, userTimeline.getType(), userTimeline.getDomain()), String.valueOf(userTimeline.getItemId())) > 0;
        return result;
    }


    public String getUserTimeLineKey(String profileId, String type, String userTimeLineDomain) {
        return KEY_PREFIX + USER_TIMELINE_ALL + type + userTimeLineDomain+profileId;
    }


//    public boolean removeUserTimelineByProfileId(UserTimeline userTimeline) {
//        return manager.lrem(KEY_PREFIX + USER_TIMELINE_ALL + userTimeline.getProfileId(), 0, userTimeline.getExtendBody()) > 0 &&
//                manager.lrem(KEY_PREFIX + USER_TIMELINE_TYPE + userTimeline.getProfileId() + "_" + userTimeline.getType(), 0, userTimeline.getExtendBody()) > 0;
//    }

//    public String getUserTimelineLineKey(String profileid) {
//        //保存我的好友动态
//        return KEY_PREFIX + USER_TIMELINE_ALL + profileid + "-" + KEY_PREFIX + USER_TIMELINE_TYPE + profileid;
//    }

//
//    public String getFriendUserTimelineLineKey(String profileid) {
//        //保存我的好友动态
//        return KEY_PREFIX + USER_TIMELINE_FRIEND + profileid;
//    }

//    public void putFriendUserTimelineByProfileId(String profileid, Long itemId, String extendBody) {
//        //保存我的好友动态
//        manager.lpush(KEY_PREFIX + USER_TIMELINE_FRIEND + profileid, itemId + "-" + extendBody);
//    }

//    public void removeFriendUserTimelineByProfileId(String profileid, Long itemId, String extendBody) {
//        //删除好友动态
//        manager.lrem(KEY_PREFIX + USER_TIMELINE_FRIEND + profileid, 0, itemId + "-" + extendBody);
//    }
}
