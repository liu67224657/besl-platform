package com.enjoyf.platform.util.publish;/**
 * Created by ericliu on 16/8/13.
 */

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.redis.RedisManager;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/8/13
 */
public class Publisher {
    private RedisManager redisManager;

    public Publisher(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    public Publisher(FiveProps fiveProps) {
        this(new RedisManager(fiveProps));
    }

    public Long publish(String key, String message) {
        return redisManager.publish(key, message);
    }

}
