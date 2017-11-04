package com.enjoyf.platform.serv.notice;

import com.enjoyf.platform.service.notice.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.util.MD5Util;
import com.google.gson.Gson;

import java.util.*;

/**
 * Created by ericliu on 16/3/17.
 */
public class NoticeRedis {

    private String KEY_NOTICESUM = NoticeConstants.SERVICE_SECTION + "_nsum_";
    private String KEY_SYS_NOTICESUM = NoticeConstants.SERVICE_SECTION + "_syssum_";
    private String KEY_ZSET_SYSTNOTICE = NoticeConstants.SERVICE_SECTION + "_zsysnotice";
    private String KEY_SYSTNOTICEOBJ = NoticeConstants.SERVICE_SECTION + "_sysnoticeobj";
    private String KEY_ZSET_USERNOTICE = NoticeConstants.SERVICE_SECTION + "_zunotice";
    private String KEY_USERNOTICEOBJ = NoticeConstants.SERVICE_SECTION + "_usernoticeobj";


    private RedisManager manager;

    public NoticeRedis(FiveProps p) {
        manager = new RedisManager(p);
    }

    public boolean increaseNoticeSum(String prifileId, String appKey, NoticeType type, int value) {
        return manager.hinccrby(getNoticeSumKey(prifileId, appKey), type.getCode(), value) > 0;
    }

    public boolean removeNoticeSum(String prifileId, String appKey, NoticeType type) {
        return manager.delHash(getNoticeSumKey(prifileId, appKey), type.getCode()) > 0;
    }

    private String getNoticeSumKey(String profileId, String appkey) {
        return KEY_NOTICESUM + MD5Util.Md5(profileId + appkey);
    }

    public Map<String, AppNoticeSum> getNoticeSums(String prifileId, String appKey, Collection<NoticeType> types) {
        Map<String, AppNoticeSum> returnMap = new HashMap<String, AppNoticeSum>();
        for (NoticeType type : types) {
            String value = manager.hget(getNoticeSumKey(prifileId, appKey), String.valueOf(type.getCode()));
            if (!StringUtil.isEmpty(value)) {
                AppNoticeSum noticeSum = new AppNoticeSum();
                noticeSum.setAppkey(appKey);
                noticeSum.setProfileId(prifileId);
                noticeSum.setValue(value);
                noticeSum.setType(type.getCode());
                noticeSum.setDtype(type.getDtype());
                returnMap.put(type.getCode(), noticeSum);
            }
        }
        return returnMap;
    }


    ///////////////////////////////////////
    public String getSysNoticeSum(String prifileId, String appKey) {
        return manager.get(getSysNoticeSumKey(prifileId, appKey));
    }


    public void setSysNoticeSum(String prifileId, String appKey, String value) {
        manager.set(getSysNoticeSumKey(prifileId, appKey), value);
    }


    private String getSysNoticeSumKey(String profileId, String appkey) {
        return KEY_SYS_NOTICESUM + MD5Util.Md5(profileId + appkey);
    }

    public static void main(String[] args) {
        System.out.println(NoticeConstants.SERVICE_SECTION + "_syssum_" + MD5Util.Md5("bbcc40bae994dd7740aa0f9ea94159e8" + "17yfn24TFexGybOF0PqjdY"));

        System.out.println(NoticeConstants.SERVICE_SECTION + "_zsysnotice" + "17yfn24TFexGybOF0PqjdY" + "3.0.0" + "0");

    }

    public void addNoticeSystem(SystemNotice noticeSystem) {
        manager.zadd(getSystemNoticeKey(noticeSystem.getAppkey(), noticeSystem.getVersion(), String.valueOf(noticeSystem.getPlatform())),
                noticeSystem.getSystemNoticeId(), String.valueOf(noticeSystem.getSystemNoticeId()), -1);
    }

    public boolean deleteNoticeSystem(String appkey, String version, String platform, long noticeId) {
        return manager.zrem(getSystemNoticeKey(appkey, version, platform), String.valueOf(noticeId)) > 0;
    }

    public int systemNoticeSize(String appkey, String version, String platform, String flag) {
        long flagId = -1l;
        if (!StringUtil.isEmpty(flag)) {
            flagId = Long.parseLong(flag);
        }
        if (flagId < 0l) {
            return (int) manager.zcard(getSystemNoticeKey(appkey, version, platform));
        }

        String maxScore = maxFlag(appkey, version, platform);
        if (StringUtil.isEmpty(maxScore) || maxScore.equals(flag)) {
            return 0;
        }


        return manager.zrangeByScore(getSystemNoticeKey(appkey, version, platform), maxScore, "(" + flag, RedisManager.RANGE_ORDERBY_DESC).size();
    }

    public Set<String> querySystemNotice(String appkey, String version, String platform, Pagination page) {
        page.setTotalRows((int) manager.zcard(getSystemNoticeKey(appkey, version, platform)));
        return manager.zrange(getSystemNoticeKey(appkey, version, platform), page.getStartRowIdx(), page.getEndRowIdx(), RedisManager.RANGE_ORDERBY_DESC);
    }


    public String maxFlag(String appkey, String version, String platform) {
        String maxScore = null;//todo 因为score和value是一样的
        Iterator<String> maxScoreIter = manager.zrange(getSystemNoticeKey(appkey, version, platform), 0, 0l, RedisManager.RANGE_ORDERBY_DESC).iterator();
        if (!maxScoreIter.hasNext()) {
            return null;
        }
        maxScore = maxScoreIter.next();
        return maxScore;
    }


    private String getSystemNoticeKey(String appkey, String version, String platform) {
        return KEY_ZSET_SYSTNOTICE + appkey + version + platform;
    }

    /////////
    public void setSystemNotice(SystemNotice systemNotice) {
        manager.set(KEY_SYSTNOTICEOBJ + systemNotice.getSystemNoticeId(), new Gson().toJson(systemNotice, systemNotice.getClass()));
    }

    public boolean delSystemNotice(String noticeId) {
        return manager.remove(KEY_SYSTNOTICEOBJ + noticeId) > 0;
    }

    public SystemNotice getSystemNotice(String noticeId) {
        String noticeStr = manager.get(KEY_SYSTNOTICEOBJ + noticeId);

        if (StringUtil.isEmpty(noticeStr)) {
            return null;
        }

        return new Gson().fromJson(noticeStr, SystemNotice.class);
    }


    ////////////////////////////////
    public void setUserNotice(UserNotice userNotice) {
        manager.set(KEY_USERNOTICEOBJ + userNotice.getUserNoticeId(), new Gson().toJson(userNotice, userNotice.getClass()));
    }

    public UserNotice getUserNotice(String userNoticeId) {
        String s = manager.get(KEY_USERNOTICEOBJ + userNoticeId);

        if (StringUtil.isEmpty(s)) {
            return null;
        }

        return new Gson().fromJson(s, UserNotice.class);
    }

    public boolean delUserNotice(long userNoticeId) {
        return manager.remove(KEY_USERNOTICEOBJ + userNoticeId) > 0;
    }

    public void addUserNoticeList(String profileId, String appkey, String noticeType, long noticeId) {
        manager.zadd(getUserNoticeListKey(profileId, appkey, noticeType), noticeId, String.valueOf(noticeId), -1);
    }

    public boolean removeUserNoticeList(String profileId, String appkey, String noticeType, long noticeId) {
        return manager.zrem(getUserNoticeListKey(profileId, appkey, noticeType), String.valueOf(noticeId)) > 0;
    }


    public boolean removeUserNoticeAll(String profileId, String appkey, String noticeType) {
        Set<String> noticeIds = manager.zrange(getUserNoticeListKey(profileId, appkey, noticeType), 0, -1, RedisManager.RANGE_ORDERBY_DESC);
        if (CollectionUtil.isEmpty(noticeIds)) {
            return false;
        }

        for (String noticeId : noticeIds) {
            delUserNotice(Long.parseLong(noticeId));
        }
        manager.remove(getUserNoticeListKey(profileId, appkey, noticeType));
        return true;
    }


    public Set<String> queryUserNoticeList(String profileId, String appkey, String noticeType, Pagination page) {
        Set<String> noticeIds = manager.zrange(getUserNoticeListKey(profileId, appkey, noticeType), page.getStartRowIdx(), page.getEndRowIdx(), RedisManager.RANGE_ORDERBY_DESC);
        page.setTotalRows((int) manager.zcard(getUserNoticeListKey(profileId, appkey, noticeType)));
        return noticeIds;
    }

    private String getUserNoticeListKey(String profile, String appkey, String noticeType) {
        return KEY_ZSET_USERNOTICE + MD5Util.Md5(profile + appkey + noticeType);
    }
}
