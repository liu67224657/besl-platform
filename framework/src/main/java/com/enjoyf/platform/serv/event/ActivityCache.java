package com.enjoyf.platform.serv.event;

import com.enjoyf.platform.service.content.ContentConstants;
import com.enjoyf.platform.service.content.social.*;
import com.enjoyf.platform.service.event.Activity;
import com.enjoyf.platform.service.event.EventConstants;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.List;
import java.util.Set;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-15 下午4:19
 * Description:
 */
public class ActivityCache {
    private static final long TIME_OUT_SEC = 60l * 5l;
    private static final String PREFIX_ACTIVITY = "_activity_";

    private MemCachedConfig config;

    private MemCachedManager manager;

    ActivityCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(this.config);
    }

    public void putActivity(Activity activity) {
        manager.put(EventConstants.SERVICE_SECTION + PREFIX_ACTIVITY + activity.getActivityId(), activity, TIME_OUT_SEC);
    }

    public Activity getActivity(long activityId) {
        Object object = manager.get(EventConstants.SERVICE_SECTION + PREFIX_ACTIVITY + activityId);
        if (object == null) {
            return null;
        }
        return (Activity) object;
    }

    public boolean deleteActivity(long activityId) {
        return manager.remove(EventConstants.SERVICE_SECTION + PREFIX_ACTIVITY + activityId);
    }
}
