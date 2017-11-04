/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.social;


import com.enjoyf.platform.service.social.ObjectRelation;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.SocialConstants;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author <a href=mailto:ericliu@staff.joyme.com>Eirc Liu</a>
 */

public class SocialRedis {
    //
    private static final Logger logger = LoggerFactory.getLogger(SocialRedis.class);

    private static final String KEY_PREFIX = SocialConstants.SERVICE_SECTION;

    private static final String KEY_OBJECT_RELATION_OBJIDS = "_obj_relation_objids_";
    private static final String KEY_OBJECT_RELATION = "_obj_relation_";

    private static final String PROFILE_RELATION_FOCUS = "_profile_relation_focus_";
    private static final String PROFILE_RELATION_FANS = "_profile_relation_fans_";

    private static final String USER_RELATION_FOCUS = "_ur_follow_";
    private static final String USER_RELATION_FANS = "_ur_fans_";
    private static final String USER_RELATION_OBJECT = "_ur_obj_";

    private RedisManager manager;
    private String KEY_CHECK_FOLLOW_STATUS_TEMP = KEY_PREFIX + "_cfst_";
    private String KEY_CHECK_FOLLOW_STATUS_TEMPRESULT = KEY_PREFIX + "_cfstr_";

    private String KEY_CHECK_FANS_STATUS_TEMP = KEY_PREFIX + "_cfansst_";
    private String KEY_CHECK_FANS_STATUS_TEMPRESULT = KEY_PREFIX + "_cfansstr_";

    public SocialRedis(FiveProps p) {
        manager = new RedisManager(p);
    }

    public Set<String> queryObjectIdList(int objType, String profileId, Pagination pagination) {
        return manager.zrange(KEY_PREFIX + KEY_OBJECT_RELATION_OBJIDS + objType + "_" + profileId, pagination.getStartRowIdx(), pagination.getEndRowIdx(), manager.RANGE_ORDERBY_DESC);
    }

    public void putObjectId(int objType, String profileId, double orderScore, String objectId, int timeOutSec) {
        manager.zadd(KEY_PREFIX + KEY_OBJECT_RELATION_OBJIDS + objType + "_" + profileId, orderScore, objectId, timeOutSec);
    }

    public PageRows<String> queryFocusProfileId(String profileId, ObjectRelationType relationType, Pagination page) {
        List<String> list = manager.lrange(KEY_PREFIX + PROFILE_RELATION_FOCUS + profileId + "_" + relationType.getCode(), page == null ? 0 : page.getStartRowIdx(), page == null ? -1 : page.getEndRowIdx());
        if (page != null) {
            page.setTotalRows((int) manager.length(KEY_PREFIX + PROFILE_RELATION_FOCUS + profileId + "_" + relationType.getCode()));
        }
        PageRows<String> pageRows = new PageRows<String>();
        pageRows.setRows(list);
        pageRows.setPage(page);
        return pageRows;
    }

    public void putFocusProfileId(String profileId, ObjectRelationType relationType, String destProfileId) {
        manager.lpush(KEY_PREFIX + PROFILE_RELATION_FOCUS + profileId + "_" + relationType.getCode(), destProfileId);
    }

    public boolean removeFocusProfileId(String profileId, ObjectRelationType relationType, String destProfileId) {
        return manager.lrem(KEY_PREFIX + PROFILE_RELATION_FOCUS + profileId + "_" + relationType.getCode(), 0, destProfileId) > 0;
    }

    public PageRows<String> queryFansProfileId(String profileId, ObjectRelationType relationType, Pagination page) {
        List<String> list = manager.lrange(KEY_PREFIX + PROFILE_RELATION_FANS + profileId + "_" + relationType.getCode(), page == null ? 0 : page.getStartRowIdx(), page == null ? -1 : page.getEndRowIdx());
        if (page != null) {
            page.setTotalRows((int) manager.length(KEY_PREFIX + PROFILE_RELATION_FANS + profileId + "_" + relationType.getCode()));
        }
        PageRows<String> pageRows = new PageRows<String>();
        pageRows.setRows(list);
        pageRows.setPage(page);
        return pageRows;
    }

    public void putFansProfileId(String profileId, ObjectRelationType relationType, String srcProfileId) {
        manager.lpush(KEY_PREFIX + PROFILE_RELATION_FANS + profileId + "_" + relationType.getCode(), srcProfileId);
    }

    public boolean removeFansProfileId(String profileId, ObjectRelationType relationType, String srcProfileId) {
        return manager.lrem(KEY_PREFIX + PROFILE_RELATION_FANS + profileId + "_" + relationType.getCode(), 0, srcProfileId) > 0;
    }

    public void lpushObjectRelationListCache(ObjectRelation relation) {
        manager.lpush(KEY_PREFIX + KEY_OBJECT_RELATION + relation.getType().getCode() + "_" + relation.getProfileId(), relation.toJson());
    }

    public void rpushObjectRelationListCache(ObjectRelation relation) {
        manager.rpush(KEY_PREFIX + KEY_OBJECT_RELATION + relation.getType().getCode() + "_" + relation.getProfileId(), relation.toJson());
    }

    public void removeRelationListCache(String profileId, ObjectRelationType relationType) {
        manager.remove(KEY_PREFIX + KEY_OBJECT_RELATION + relationType.getCode() + "_" + profileId);
    }

    public PageRows<ObjectRelation> queryObjectRelationListCache(ObjectRelationType type, String profileId, Pagination pagination) {
        PageRows<ObjectRelation> pageRows = new PageRows<ObjectRelation>();
        List<ObjectRelation> list = new ArrayList<ObjectRelation>();
        List<String> cacheList = manager.lrange(KEY_PREFIX + KEY_OBJECT_RELATION + type.getCode() + "_" + profileId, pagination == null ? 0 : pagination.getStartRowIdx(), pagination == null ? 20 : pagination.getEndRowIdx());
        if (cacheList != null && !CollectionUtil.isEmpty(cacheList)) {
            for (String objStr : cacheList) {
                ObjectRelation objectRelation = ObjectRelation.parse(objStr);
                if (objectRelation != null) {
                    list.add(objectRelation);
                }
            }
            pageRows.setRows(list);
            if (pagination != null) {
                pagination.setTotalRows((int) manager.length(KEY_PREFIX + KEY_OBJECT_RELATION + type.getCode() + "_" + profileId));
            }
            pageRows.setPage(pagination);
        }
        return pageRows;
    }

    ///////////////////////////////
    //todo zset!!!!
    public Set<String> queryUserRelationFocusProfileId(String profileId, ObjectRelationType relationType, Pagination page) {
        page.setTotalRows((int) manager.zcard(getUserRelationFocusKey(profileId, relationType)));
        return manager.zrange(getUserRelationFocusKey(profileId, relationType), page.getStartRowIdx(), page.getEndRowIdx(), RedisManager.RANGE_ORDERBY_DESC);
    }

    public void putUserRelationFocusProfileId(String profileId, ObjectRelationType relationType, long createTimestamp, String destProfileId) {
        double score = createTimestamp + Double.parseDouble("0." + String.valueOf(Math.abs(UUID.randomUUID().hashCode())));
        manager.zadd(getUserRelationFocusKey(profileId, relationType), score, destProfileId, -1);
    }

    public boolean removeUserRelationFocusProfileId(String profileId, ObjectRelationType relationType, String destProfileId) {
        return manager.zrem(getUserRelationFocusKey(profileId, relationType), destProfileId) > 0;
    }

    private String getUserRelationFocusKey(String profileId, ObjectRelationType relationType) {
        return KEY_PREFIX + USER_RELATION_FOCUS + profileId + "_" + relationType.getCode();
    }

    public Set<String> checkFollowStatus(String srcProfileId, ObjectRelationType relationType, Collection<String> profileIds) {
        String keySuffix = MD5Util.Md5(srcProfileId + UUID.randomUUID().toString());
        String tempKey = KEY_CHECK_FOLLOW_STATUS_TEMP + keySuffix;
        String tempResulKey = KEY_CHECK_FOLLOW_STATUS_TEMPRESULT + keySuffix;

        Map<String, Double> questionMap = new HashMap<String, Double>();
        for (String profileId : profileIds) {
            questionMap.put(profileId, 0.0);
        }

        manager.zadd(tempKey, questionMap, 3600);
        manager.zinterstore(tempResulKey, null, 3600, tempKey, getUserRelationFocusKey(srcProfileId, relationType));
        Set<String> result = manager.zrange(tempResulKey, 0, -1, RedisManager.RANGE_ORDERBY_ASC);
        manager.remove(tempResulKey);
        manager.remove(tempKey);
        return result;
    }

    //////////////////////////////

    public Set<String> queryUserRelationFansProfileId(String profileId, ObjectRelationType relationType, Pagination page) {
        page.setTotalRows((int) manager.zcard(getUserRelationFansKey(profileId, relationType)));
        return manager.zrange(getUserRelationFansKey(profileId, relationType), page.getStartRowIdx(), page.getEndRowIdx(), RedisManager.RANGE_ORDERBY_DESC);
    }

    public void putUserRelationFansProfileId(String profileId, ObjectRelationType relationType, long createTimestamp, String srcProfileId) {
        double score = createTimestamp + Double.parseDouble("0." + String.valueOf(Math.abs(UUID.randomUUID().hashCode())));
        manager.zadd(getUserRelationFansKey(profileId, relationType), score, srcProfileId, -1);
    }

    public boolean removeUserRelationFansProfileId(String profileId, ObjectRelationType relationType, String srcProfileId) {
        return manager.zrem(getUserRelationFansKey(profileId, relationType), srcProfileId) > 0;
    }

    private String getUserRelationFansKey(String profileId, ObjectRelationType relationType) {
        return KEY_PREFIX + USER_RELATION_FANS + profileId + "_" + relationType.getCode();
    }

    public Set<String> checkFansStatus(String srcProfileId, ObjectRelationType relationType, Collection<String> profileIds) {
        String keySuffix = MD5Util.Md5(srcProfileId + UUID.randomUUID().toString());
        String tempKey = KEY_CHECK_FANS_STATUS_TEMP + keySuffix;
        String tempResulKey = KEY_CHECK_FANS_STATUS_TEMP + keySuffix;

        Map<String, Double> questionMap = new HashMap<String, Double>();
        for (String profileId : profileIds) {
            questionMap.put(profileId, 0.0);
        }

        manager.zadd(tempKey, questionMap, 3600);
        manager.zinterstore(tempResulKey, null, 3600, tempKey, getUserRelationFansKey(srcProfileId, relationType));
        Set<String> result = manager.zrange(tempResulKey, 0, -1, RedisManager.RANGE_ORDERBY_ASC);
        manager.remove(tempResulKey);
        manager.remove(tempKey);
        return result;
    }
    /////////////////////////////////

    public Set<String> getAllUserRelationFansByProfileId(String profileId, ObjectRelationType relationType) {
        String key = KEY_PREFIX + USER_RELATION_FANS + profileId + "_" + relationType.getCode();
        return manager.zrange(key, 0, -1,RedisManager.RANGE_ORDERBY_DESC);
    }

    public String getUserRelationByProfileId(String relationId) {
        return manager.get(KEY_PREFIX + USER_RELATION_OBJECT + relationId);
    }

    public void putUserRelationByProfileId(String relationId, String userRelation) {
        manager.set(KEY_PREFIX + USER_RELATION_OBJECT + relationId, userRelation);
    }

    public void removeUserRelationByProfileId(String relationId) {
        manager.remove(KEY_PREFIX + USER_RELATION_OBJECT + relationId);
    }

    public static void main(String agres[]) {
//        FiveProps props = Props.instance().getServProps();
//        SocialRedis socialRedis = new SocialRedis(props);
//        Set<String> a = socialRedis.getAllUserRelationFansByProfileId("62af6b2e76d7f76b75ea1e4aeefa8a0e", ObjectRelationType.WIKI_PROFILE);
//        System.out.println(a);
    }
}

