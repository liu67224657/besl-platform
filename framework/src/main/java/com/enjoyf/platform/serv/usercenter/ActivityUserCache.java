package com.enjoyf.platform.serv.usercenter;

import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityUser;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/10/23
 * Description:
 */
public class ActivityUserCache {

    private static final long TIME_OUT_SEC_ONE_HOUR = 60l * 60l * 6l;
    private static final String KEY_ACTIVIT_USER = "_actuser";


    private MemCachedConfig config;

    private MemCachedManager manager;

    private String serverPrefix = UserCenterConstants.SERVICE_SECTION;

    ActivityUserCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
    }

    //////////////////////////////////////////////////////////
    public void putActivityUser(ActivityUser activityUser) {
        manager.put(serverPrefix + KEY_ACTIVIT_USER + activityUser.getActivityUserId(), activityUser, TIME_OUT_SEC_ONE_HOUR);
    }


    public ActivityUser getActivityUser(String activityUserId) {
        Object obj = manager.get(serverPrefix + KEY_ACTIVIT_USER + activityUserId);

        if (obj != null) {
            return (ActivityUser) obj;
        }

        return null;
    }

    public boolean deleteActivityUser(String activityUserId) {
        return manager.remove(serverPrefix + KEY_ACTIVIT_USER + activityUserId);
    }

}
