/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.usercenter;


import com.enjoyf.platform.service.joymeapp.JoymeAppConstants;
import com.enjoyf.platform.service.joymeapp.config.AppConfigUtil;
import com.enjoyf.platform.service.joymeapp.config.ShakeConfig;
import com.enjoyf.platform.service.joymeapp.config.ShakeType;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.RandomUtil;
import com.enjoyf.platform.util.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author <a href=mailto:ericliu@staff.joyme.com>Eirc Liu</a>
 */

public class ActivityUserRedis {
    //
    private static final Logger logger = LoggerFactory.getLogger(ActivityUserRedis.class);

    private static final String KEY_PREFIX = JoymeAppConstants.SERVICE_SECTION;

    private static final String KEY_ACTIVITY_USER_LIST_KEY = "_auserlist_";

    private static final String KEY_APP_ACTIVITY_USERSUM_KEY = "_ausersum_";

    private RedisManager manager;

    public ActivityUserRedis(FiveProps p) {
        manager = new RedisManager(p);
    }


    public void pushActivityUser(String appkey, String subKey, String id, long sort) {
        manager.zadd(KEY_PREFIX + KEY_ACTIVITY_USER_LIST_KEY + UserCenterUtil.getAppSumId(appkey, subKey), (double) (sort), id, -1);
    }

    public Set<String> queryActivityuser(String appkey, String subKey, Pagination pagination) {
        return manager.zrange(KEY_PREFIX + KEY_ACTIVITY_USER_LIST_KEY + UserCenterUtil.getAppSumId(appkey, subKey), pagination.getStartRowIdx(), pagination.getEndRowIdx(), manager.RANGE_ORDERBY_ASC);
    }

    public long getActivityUserSize(String appkey, String subKey) {
        return manager.zcard(KEY_PREFIX + KEY_ACTIVITY_USER_LIST_KEY + UserCenterUtil.getAppSumId(appkey, subKey));
    }

    public void increaseActivityUserSize(String appkey, String subKey, long sum) {
        manager.incr(KEY_PREFIX + KEY_APP_ACTIVITY_USERSUM_KEY + UserCenterUtil.getAppSumId(appkey, subKey), sum, -1);
    }

    public String queryActivityUserSize(String appkey, String subKey) {
        return manager.get(KEY_PREFIX + KEY_APP_ACTIVITY_USERSUM_KEY + UserCenterUtil.getAppSumId(appkey, subKey));
    }

    ////////////////


}


