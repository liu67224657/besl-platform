/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.social;


import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author <a href=mailto:wengangsai@staff.joyme.com>saiwengang</a>
 */

public class UserRelationRedis {
    //
    private static final Logger logger = LoggerFactory.getLogger(UserRelationRedis.class);

    private static final String KEY_PREFIX = "user_relation";

    private static final String USER_RELATION_FOCUS = "_focus_";
    private static final String USER_RELATION_FANS = "_fans_";

    public static final String USER_RELATION_PROFILE_KEY = KEY_PREFIX + "_profileid_focus";

    private RedisManager manager;

    public UserRelationRedis(FiveProps p) {
        manager = new RedisManager(p);
    }


    public PageRows<String> queryFocusByProfileId(String profileId, ObjectRelationType relationType, Pagination page) {
        List<String> list = manager.lrange(KEY_PREFIX + USER_RELATION_FOCUS + profileId + "_" + relationType.getCode(), page == null ? 0 : page.getStartRowIdx(), page == null ? -1 : page.getEndRowIdx());
        if (page != null) {
            page.setTotalRows((int) manager.length(KEY_PREFIX + USER_RELATION_FOCUS + profileId + "_" + relationType.getCode()));
        }
        PageRows<String> pageRows = new PageRows<String>();
        pageRows.setRows(list);
        pageRows.setPage(page);
        return pageRows;
    }

    public void putFocusByProfileId(String profileId, ObjectRelationType relationType, String destProfileId) {
        manager.lpush(KEY_PREFIX + USER_RELATION_FOCUS + profileId + "_" + relationType.getCode(), destProfileId);
    }

    public boolean removeFocusByProfileId(String profileId, ObjectRelationType relationType, String destProfileId) {
        return manager.lrem(KEY_PREFIX + USER_RELATION_FOCUS + profileId + "_" + relationType.getCode(), 0, destProfileId) > 0;
    }

    public PageRows<String> queryFansByProfileId(String profileId, ObjectRelationType relationType, Pagination page) {
        List<String> list = manager.lrange(KEY_PREFIX + USER_RELATION_FANS + profileId + "_" + relationType.getCode(), page == null ? 0 : page.getStartRowIdx(), page == null ? -1 : page.getEndRowIdx());
        if (page != null) {
            page.setTotalRows((int) manager.length(KEY_PREFIX + USER_RELATION_FANS + profileId + "_" + relationType.getCode()));
        }
        PageRows<String> pageRows = new PageRows<String>();
        pageRows.setRows(list);
        pageRows.setPage(page);
        return pageRows;
    }

    public void putFansByProfileId(String profileId, ObjectRelationType relationType, String srcProfileId) {
        manager.lpush(KEY_PREFIX + USER_RELATION_FANS + profileId + "_" + relationType.getCode(), srcProfileId);
    }

    public boolean removeFansByProfileId(String profileId, ObjectRelationType relationType, String srcProfileId) {
        return manager.lrem(KEY_PREFIX + USER_RELATION_FANS + profileId + "_" + relationType.getCode(), 0, srcProfileId) > 0;
    }
    public List<String> getAllFansByProfileId(String profileId, ObjectRelationType relationType) {
        String key = KEY_PREFIX + USER_RELATION_FANS + profileId + "_" + relationType.getCode();
        return manager.lrange(key,0,Integer.parseInt(String.valueOf(manager.length(key)-1)));
    }
}

