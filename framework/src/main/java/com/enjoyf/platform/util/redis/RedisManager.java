package com.enjoyf.platform.util.redis;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.StringUtil;
import io.codis.jodis.RoundRobinJedisPool;
import redis.clients.jedis.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/18
 * Description:
 */
public class RedisManager {
    private FiveProps props;

    private static final String KEY_REDIS_HOST = "redis.host";
    private static final String KEY_REDIS_MAXACTIVE = "redis.maxactvie";
    private static final String KEY_REDIS_MAXWAIT = "redis.maxwait";
    private static final String KEY_REDIS_MAXIDEL = "redis.idel";
    private static final String KEY_REDIS_PASSWORD = "redis.password";

    public static final String RANGE_ORDERBY_DESC = "desc";

    public static final String RANGE_ORDERBY_ASC = "asc";

    protected JedisPool pool;

    protected RoundRobinJedisPool pool2;

    public RedisManager(FiveProps p) {
        this.props = p;
        init();
    }


    private void init() {
        String host = props.get(KEY_REDIS_HOST);
        int maxActive = props.getInt(KEY_REDIS_MAXACTIVE, 100);
        int maxWait = props.getInt(KEY_REDIS_MAXWAIT, 20);
        int maxIdel = props.getInt(KEY_REDIS_MAXIDEL, 1000);
        String password = props.get(KEY_REDIS_PASSWORD);

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdel);
        poolConfig.setMaxWaitMillis(maxWait);

        String[] hosts = host.split(":");
        if (!StringUtil.isEmpty(password)) {
            pool = new JedisPool(poolConfig, hosts[0], Integer.parseInt(hosts[1]), 100000, password);
        } else {
            pool = new JedisPool(poolConfig, hosts[0], Integer.parseInt(hosts[1]));
        }
//        pool = new JedisPool(poolConfig, host, Integer.parseInt(hosts[1]), 100000);
    }

    public JedisPool getPool() {
        return pool;
    }

    public boolean keyExists(String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.exists(key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public List<String> lrange(String key, int startIdx, int endIdx) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.lrange(key, startIdx, endIdx);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public String lindex(String key, int i) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.lindex(key, i);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Long lrem(String key, int count, String value) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.lrem(key, count, value);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    @Deprecated
    public void lpush(String key, List<String> list) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            for (String string : list) {
                redis.lpush(key, string);
            }
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public void lset(String key, long index, String value) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            redis.lset(key, index, value);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public void lpush(String key, String[] array) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            redis.lpush(key, array);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }


    public void lpush(String key, String value) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            redis.lpush(key, value);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public void lpush(String key, String value, int timeOutSec) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            redis.lpush(key, value);

            if (timeOutSec > 0) {
                redis.expire(key, timeOutSec);
            }

        } finally {
            RedisUtil.closeJedis(redis);
        }
    }


    public void rpush(String key, String[] array) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            redis.rpush(key, array);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    @Deprecated
    public void rpush(String key, List<String> list) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            for (String string : list) {
                redis.rpush(key, string);
            }
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public void rpush(String key, String value) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            redis.rpush(key, value);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public String lpop(String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.lpop(key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public List<String> blpop(int timeSec, String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.blpop(timeSec, key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public List<String> blpop(int timeSec, String[] keys) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.blpop(timeSec, keys);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public String rpop(String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.rpop(key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public List<String> brpop(int timeSec, String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.brpop(timeSec, key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public List<String> brpop(int timeSec, String[] keys) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.brpop(timeSec, keys);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }


    public String get(String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.get(key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public void set(String key, String value) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            redis.set(key, value);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public void setSec(String key, String value, int timeOutSec) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            redis.set(key, value);

            if (timeOutSec > 0) {
                redis.expire(key, timeOutSec);
            }
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Long remove(String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.del(key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public long length(String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.llen(key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public void sadd(String key, String[] list, int timeOutSec) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            redis.sadd(key, list);

            if (timeOutSec > 0) {
                redis.expire(key, timeOutSec);
            }
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public void sadd(String key, List<String> list, int timeOutSec) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            for (String val : list) {
                redis.sadd(key, val);
            }
            if (timeOutSec > 0) {
                redis.expire(key, timeOutSec);
            }
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public void sadd(String key, String value) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            redis.sadd(key, value);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public void sadd(String key, String value, int timeOutSec) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            redis.sadd(key, value);

            if (timeOutSec > 0) {
                redis.expire(key, timeOutSec);
            }

        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public String spop(String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.spop(key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public String srandmember(String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.srandmember(key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Set<String> smembers(String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.smembers(key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    /**
     * 判断value是否已经存在于与Key相关联的Set集合中
     *
     * @param key
     * @param value
     * @return
     */
    public boolean sismember(String key, String value) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.sismember(key, value);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Set<String> sinter(String... keys) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.sinter(keys);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Set<String> sdiff(String... keys) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.sdiff(keys);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    /**
     * @param key
     * @param value
     * @param timeOutSec 秒
     */
    public Long incr(String key, long value, int timeOutSec) {
        Jedis redis = null;
        long inc = 0;
        try {
            redis = pool.getResource();

            inc = redis.incrBy(key, value);

            if (timeOutSec > 0) {
                redis.expire(key, timeOutSec);
            }

        } finally {
            RedisUtil.closeJedis(redis);
        }
        return inc;
    }


    ////////////////////
    public void zadd(String key, double score, String value, int timeOutSec) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            redis.zadd(key, score, value);

            if (timeOutSec > 0) {
                redis.expire(key, timeOutSec);
            }

        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public void zadd(String key, Map<String, Double> value, int timeOutSec) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            redis.zadd(key, value);

            if (timeOutSec > 0) {
                redis.expire(key, timeOutSec);
            }

        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    /**
     * 取交集输出到destkey中
     *
     * @param destKey        输出参数
     * @param zParams        参数 weight Aggregate比较重要
     * @param destTimeOutSec 结果过期时间
     * @param sets           取交集的集合的key
     */
    public void zinterstore(String destKey, ZParams zParams, int destTimeOutSec, String... sets) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            if (zParams != null) {
                redis.zinterstore(destKey, zParams, sets);
            } else {
                redis.zinterstore(destKey, sets);
            }

            if (destTimeOutSec > 0) {
                redis.expire(destKey, destTimeOutSec);
            }

        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    /**
     * @param key
     * @param start
     * @param end
     * @param orderBy ASC or DESC default asc
     * @return
     */
    public Set<String> zrange(String key, long start, long end, String orderBy) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            if (orderBy.equalsIgnoreCase(RANGE_ORDERBY_DESC)) {
                return redis.zrevrange(key, start, end);
            } else {
                return redis.zrange(key, start, end);
            }
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Set<Tuple> zrangeWithScores(Jedis redis, String key, long start, long end, String orderBy) {
        boolean needgetResouce = false;
        try {
            if (redis == null) {
                redis = pool.getResource();
                needgetResouce = true;
            }

            if (orderBy.equalsIgnoreCase(RANGE_ORDERBY_DESC)) {
                return redis.zrangeWithScores(key, start, end);
            } else {
                return redis.zrangeWithScores(key, start, end);
            }
        } finally {
            if (needgetResouce) {
                RedisUtil.closeJedis(redis);
            }
        }
    }

    public long zcard(String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.zcard(key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Long zrem(String key, String value) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.zrem(key, value);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Double zincrby(String key, double incScore, String value) {
        Jedis redis = null;
        try {
            redis = pool.getResource();

            return redis.zincrby(key, incScore, value);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Set<String> zrangeByScoreOffset(String key, double min, double max, int offset, int count) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.zrangeByScore(key, min, max, offset, count);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Set<String> zrangeByScoreOffset(String key, String min, String max, int offset, int count) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.zrangeByScore(key, min, max, offset, count);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Set<String> zrangeByScore(String key, String min, String max, String sort) {
        if (sort.equalsIgnoreCase(RANGE_ORDERBY_DESC)) {
            return zrevrangeByScore(key, min, max);
        } else {
            return zrangeByScore(key, min, max);
        }

    }

    public Set<String> zrangeByScore(String key, double min, double max, String sort) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return sort.equalsIgnoreCase(RANGE_ORDERBY_DESC) ? redis.zrevrangeByScore(key, min, max) : redis.zrangeByScore(key, min, max);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Set<String> zrangeByScore(String key, ScoreRange scoreRange, String sort) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            if (scoreRange.getMin() < 0.0) {
                Set<Tuple> minElement = sort.equalsIgnoreCase(RANGE_ORDERBY_DESC) ?
                        redis.zrevrangeWithScores(key, -1, -1) :
                        redis.zrangeWithScores(key, 0, 0);
                if (!CollectionUtil.isEmpty(minElement)) {
                    scoreRange.setMin(minElement.iterator().next().getScore());
                }
            }

            if (scoreRange.getMax() < 0.0) {
                Set<Tuple> maxElement = sort.equalsIgnoreCase(RANGE_ORDERBY_DESC) ?
                        redis.zrevrangeWithScores(key, 0, 0) :
                        redis.zrangeWithScores(key, -1, -1);
                if (!CollectionUtil.isEmpty(maxElement)) {
                    scoreRange.setMax(maxElement.iterator().next().getScore());
                }
            }
//            String minExpress = String.valueOf(!scoreRange.isContainStart() ? "(" + scoreRange.getMin() : scoreRange.getMin());
//            String maxExpress = String.valueOf(!scoreRange.isContainEnd() ? "(" + scoreRange.getMax() : scoreRange.getMax());
            String minExpress = String.valueOf(scoreRange.getMin());
            String maxExpress = String.valueOf(scoreRange.getMax());
            if (sort.equalsIgnoreCase(RANGE_ORDERBY_DESC)) {
                maxExpress = String.valueOf("(" + (scoreRange.isFirstPage() ? scoreRange.getMax() + 1 : scoreRange.getMax()));
            } else {
                minExpress = String.valueOf("(" + (scoreRange.isFirstPage() ? scoreRange.getMin() - 1 : scoreRange.getMin()));
            }

            Set<Tuple> tuples = sort.equalsIgnoreCase(RANGE_ORDERBY_DESC) ?
                    redis.zrevrangeByScoreWithScores(key, maxExpress, minExpress, scoreRange.getLimit(), scoreRange.getSize()) :
                    redis.zrangeByScoreWithScores(key, minExpress, maxExpress, scoreRange.getLimit(), scoreRange.getSize());

            Set<String> result = new LinkedHashSet<String>();
            int i = 0;
            for (Tuple tuple : tuples) {
                if (i == tuples.size() - 1) {
                    double flag = tuple.getScore();
                    scoreRange.setScoreflag(flag);
                }
                result.add(tuple.getElement());
                i++;
            }

            boolean hasNext = sort.equalsIgnoreCase(RANGE_ORDERBY_DESC) ? scoreRange.getScoreflag() > scoreRange.getMin()
                    : scoreRange.getScoreflag() < scoreRange.getMax();
            scoreRange.setHasnext(hasNext);
            return result;
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.zrangeByScoreWithScores(key, min, max, offset, count);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.zrangeByScore(key, min, max, offset, count);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.zrevrangeByScore(key, max, min, offset, count);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    @Deprecated
    public Set<String> zrangeByScore(String key, String min, String max) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.zrangeByScore(key, min, max);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    @Deprecated
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.zrevrangeByScore(key, max, min);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public long zcount(String key, double min, double max) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.zcount(key, min, min);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Double zscore(String key, String value) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.zscore(key, value);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public long scard(String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.scard(key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }


    public long srem(String key, String value) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.srem(key, value);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Set<String> sinter(String key1, String key2) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.sinter(key1, key2);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public long zrank(String key, String value, String sort) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            Long rank;
            if (sort.equalsIgnoreCase(RANGE_ORDERBY_DESC)) {
                rank = redis.zrevrank(key, value);
            } else {
                rank = redis.zrank(key, value);
            }

            return rank == null ? -1l : rank;
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Set<String> keys(String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.keys(key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    //////////////////////////////////////////////
    public String mset(String... keysvalues) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.mset(keysvalues);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public List<String> mget(String... keys) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.mget(keys);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Long hset(String key, String field, String value) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.hset(key, field, value);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public String hmset(String key, Map<String, String> value) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.hmset(key, value);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public Map<String, String> hgetAll(String key) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.hgetAll(key);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public List<String> hmget(String key, String... fields) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.hmget(key, fields);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public String hget(String key, String field) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.hget(key, field);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }


    public long delHash(String key, String... fields) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.hdel(key, fields);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public long hinccrby(String key, String filed, long incr) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.hincrBy(key, filed, incr);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }


    //////////////////////////////////////
    public long publish(String channel, String message) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            return redis.publish(channel, message);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }

    public void subscribe(JedisPubSub jedisPubSub, String channel) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            redis.subscribe(jedisPubSub, channel);
        } finally {
            RedisUtil.closeJedis(redis);
        }
    }


}
