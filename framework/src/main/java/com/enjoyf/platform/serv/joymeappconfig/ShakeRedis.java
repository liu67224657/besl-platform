/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.joymeappconfig;


import com.enjoyf.platform.service.joymeapp.JoymeAppConstants;
import com.enjoyf.platform.service.joymeapp.config.AppConfigUtil;
import com.enjoyf.platform.service.joymeapp.config.ShakeConfig;
import com.enjoyf.platform.service.joymeapp.config.ShakeType;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.RandomUtil;
import com.enjoyf.platform.util.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author <a href=mailto:ericliu@staff.joyme.com>Eirc Liu</a>
 */

public class ShakeRedis {
    //
    private static final Logger logger = LoggerFactory.getLogger(ShakeRedis.class);

    private static final String KEY_PREFIX = JoymeAppConstants.SERVICE_SECTION;

    private static final String KEY_SHAKE_KEY = "_sk_";

    private static final String KEY_SHAKE_PROFILEFLAG = "_pf_";

    private RedisManager manager;

    public ShakeRedis(FiveProps p) {
        manager = new RedisManager(p);
    }

    /**
     * 存放item
     *
     * @param appkey
     * @param platform
     * @param channel
     * @param shakeType
     * @param items
     */
    public void pushItems(String appkey, int platform, String channel, ShakeType shakeType, ShakeConfig config, String[] items) {
        manager.lpush(KEY_PREFIX + KEY_SHAKE_KEY + AppConfigUtil.getShakePoolKey(appkey, platform, channel, shakeType, config), items);
    }


    public void removeItems(String appkey, int platform, String channel, ShakeType shakeType, ShakeConfig config, int count, String shakeItemJsonStr) {
        manager.lrem(KEY_PREFIX + KEY_SHAKE_KEY + AppConfigUtil.getShakePoolKey(appkey, platform, channel, shakeType, config), count, shakeItemJsonStr);
    }

    public void remove(String appkey, int platform, String channel, ShakeType shakeType, ShakeConfig config) {
        manager.remove(KEY_PREFIX + KEY_SHAKE_KEY + AppConfigUtil.getShakePoolKey(appkey, platform, channel, shakeType, config));
    }

    /**
     * 随机获取
     *
     * @param appkey
     * @param platform
     * @param channel
     * @param shakeType
     */
    public String randomGetItems(String appkey, int platform, String channel, ShakeType shakeType, ShakeConfig config) {
        String key = KEY_PREFIX + KEY_SHAKE_KEY + AppConfigUtil.getShakePoolKey(appkey, platform, channel, shakeType, config);

        int i = RandomUtil.getRandomInt((int) manager.length(key));
        return manager.lindex(key, i);
    }

    /**
     * 随机pop
     *
     * @param appkey
     * @param platform
     * @param channel
     * @param shakeType
     * @return
     */
    public String randomPop(String appkey, int platform, String channel, ShakeType shakeType, ShakeConfig config) {
        String key = KEY_PREFIX + KEY_SHAKE_KEY + AppConfigUtil.getShakePoolKey(appkey, platform, channel, shakeType, config);

        if (manager.length(key) == 0) {
            return null;
        }
        int i = RandomUtil.getRandomInt((int) manager.length(key));
        String s = manager.lindex(key, i);

        manager.lrem(key, 1, s);

        return s;

//        Transaction transaction = null;
//        JedisPool pool = null;
//        Jedis redis = null;
//        try {
//            pool=manager.getPool();
//
//            int i=10;
//
//            redis = pool.getResource();
//            redis.watch(key);
//            transaction = redis.multi();
//            //            int i = RandomUtil.getRandomInt((int) manager.length(key));
//            Response<String> response = transaction.lindex(key, i);
//
//            transaction.exec();
//            transaction.lrem(key, 1, response.get());
//
//            if (response==null || StringUtil.isEmpty(response.get())) {
//                return null;
//            }
//
//            return response.get();
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.discard();
//            }
//            return null;
//        } finally {
//
//            RedisUtil.releaseJedis(pool, redis);
//        }
    }

    public List<String> queryShakeItems(String appkey, int platform, String channel, ShakeType shakeType, ShakeConfig config, Pagination page) {
        String key = KEY_PREFIX + KEY_SHAKE_KEY + AppConfigUtil.getShakePoolKey(appkey, platform, channel, shakeType, config);
        page.setTotalRows((int) manager.length(key));

        return manager.lrange(key, page.getStartRowIdx(), page.getEndRowIdx());
    }

    public boolean keyExists(String appkey, int platform, String channel, ShakeType shakeType, ShakeConfig config) {
        return manager.keyExists(KEY_PREFIX + KEY_SHAKE_KEY + AppConfigUtil.getShakePoolKey(appkey, platform, channel, shakeType, config));
    }

    /**
     * 用户摇一摇的限制
     *
     * @param appkey
     * @param platform
     * @param channel
     * @param shakeType
     * @param profileId
     */
    public void incrProfile(String appkey, int platform, String channel, ShakeType shakeType, ShakeConfig config, String profileId, int timeOutDate) {
        manager.incr(KEY_PREFIX + KEY_SHAKE_PROFILEFLAG + AppConfigUtil.getShakePoolKey(appkey, platform, channel, shakeType, config) + profileId, 1, timeOutDate);
    }

    public String getProfile(String appkey, int platform, String channel, ShakeType shakeType, ShakeConfig config, String profileId) {
        return manager.get(KEY_PREFIX + KEY_SHAKE_PROFILEFLAG + AppConfigUtil.getShakePoolKey(appkey, platform, channel, shakeType, config) + profileId);
    }

    public boolean removeProfile(String appkey, int platform, String channel, ShakeType shakeType, ShakeConfig config, String profileId) {
        return manager.remove(KEY_PREFIX + KEY_SHAKE_PROFILEFLAG + AppConfigUtil.getShakePoolKey(appkey, platform, channel, shakeType, config) + profileId) > 0l;
    }


}

