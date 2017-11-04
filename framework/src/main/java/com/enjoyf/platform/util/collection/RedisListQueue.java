package com.enjoyf.platform.util.collection;


import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.redis.RedisManager;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/8/12
 */
public class RedisListQueue implements Queue {

    private String queueName;
    private FiveProps props;
    private int objectLifecycle = -1;


    private RedisManager redisManager;

    public RedisListQueue(String queueName, FiveProps props) {
        this(queueName, props, -1);
    }

    public RedisListQueue(String queueName, FiveProps props, int objectLifecycle) {
        this.queueName = queueName;
        this.props = props;
        this.objectLifecycle = objectLifecycle;

        redisManager = new RedisManager(this.props);
    }


    @Override
    public int size() {
        return (int) redisManager.length(queueName);
    }

    /**
     * must string
     *
     * @param obj
     */
    @Override
    public void add(Object obj) {
        if (!(obj instanceof String)) {
            return;
        }

        redisManager.lpush(queueName, (String)obj,objectLifecycle);
    }

    /**
     * get String plz to Object
     *
     * @return
     */
    @Override
    public Object get() {
        Object obj = redisManager.rpop(queueName);
        return obj;
    }

    @Override
    public void clear() {
        redisManager.remove(queueName);
    }
}
