package com.enjoyf.platform.util.redis;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.redis.RedisUtil;
import redis.clients.jedis.Jedis;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-12-17
 * Time: 上午9:55
 * To change this template use File | Settings | File Templates.
 */
public class TaskRedisServer extends RedisManager {

    public TaskRedisServer(FiveProps p) {
        super(p);
    }

    public void save(String key, String value) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            redis.set(key, value);
            redis.save();
        } finally {
            RedisUtil.releaseJedis(pool, redis);
        }
    }

    public String get(String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.get(key);
        } finally {
            RedisUtil.releaseJedis(pool, redis);
        }
    }

}
