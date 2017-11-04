/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.content;

import com.enjoyf.platform.service.content.Activity;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
class ActivityCache {

    private static final long TIME_OUT_SEC = 60 * 60 * 6l;
    private static final long HOT_TIME_OUT_SEC = 60 * 60;
    //
    private static final String KEY_CACHE_LASTED_ACTIVITY = "lastedactivity";

    private static final String KEY_MEMCACHE_ACTIVITY_BY_REALTIONID = "activity_by_relaiton_";

    private static final String KEY_MEMCACHE_ACTIVITYID_TO_REALTIONID = "activityid_to_relaitonid_";


    private static final String KEY_MEMCACHE_ACTIVITY = "activity_";

    //
    private MemCachedConfig config;

    private MemCachedManager manager;

    ActivityCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
    }

    /////////////////////////////////////////////
    public List<Activity> getLastedActivity() {
        Object infoList = manager.get(KEY_CACHE_LASTED_ACTIVITY);
        if (infoList == null) {
            return null;
        }
        return (List<Activity>) infoList;
    }

    public void putLastedActivity(List<Activity> list) {
        manager.put(KEY_CACHE_LASTED_ACTIVITY, list, TIME_OUT_SEC);
    }

    public boolean removeLastedActivity() {
        return manager.remove(KEY_CACHE_LASTED_ACTIVITY);
    }

    /////////////////////////////
    public Activity getActivityByRelationId(long relaitonId, ActivityType activityType) {
        Object activity = manager.get(KEY_MEMCACHE_ACTIVITY_BY_REALTIONID + relaitonId + activityType.getCode());
        if (activity == null) {
            return null;
        }
        return (Activity) activity;
    }

    public void putActivityByRelationId(long relaitonId, ActivityType activityType, Activity activity) {
        manager.put(KEY_MEMCACHE_ACTIVITY_BY_REALTIONID + relaitonId + activityType.getCode(), activity, TIME_OUT_SEC);
    }

    public boolean removeActivityByRelationId(long relaitonId, ActivityType activityType) {
        return manager.remove(KEY_MEMCACHE_ACTIVITY_BY_REALTIONID + relaitonId + activityType.getCode());
    }


    public Long getRelationidByActivityId(long activityId) {
        Object relationId = manager.get(KEY_MEMCACHE_ACTIVITYID_TO_REALTIONID + activityId);
        if (relationId == null) {
            return null;
        }
        return (Long) relationId;
    }

    public void putRelationidByActivityId(long activityId, long relationId) {
        manager.put(KEY_MEMCACHE_ACTIVITYID_TO_REALTIONID + activityId, relationId, TIME_OUT_SEC);
    }

    public boolean removeRelationidByActivityId(long activityId) {
        return manager.remove(KEY_MEMCACHE_ACTIVITYID_TO_REALTIONID + activityId);
    }



    /////////////////////////////
    public Activity getActivityById(long activityId) {
        Object activity = manager.get(KEY_MEMCACHE_ACTIVITY + activityId);
        if (activity == null) {
            return null;
        }
        return (Activity) activity;
    }

    public void putActivity(Activity activity) {
        manager.put(KEY_MEMCACHE_ACTIVITY + activity.getActivityId(), activity, HOT_TIME_OUT_SEC);
    }

    public boolean removeActivityById(long activityId) {
        return manager.remove(KEY_MEMCACHE_ACTIVITY + activityId);
    }

}
