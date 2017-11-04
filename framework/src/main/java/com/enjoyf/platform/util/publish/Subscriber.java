package com.enjoyf.platform.util.publish;/**
 * Created by ericliu on 16/8/13.
 */

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.redis.RedisManager;
import redis.clients.jedis.JedisPubSub;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/8/13
 */
public class Subscriber {
    private RedisManager redisManager;

    public Subscriber(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    public Subscriber(FiveProps fiveProps) {
        this(new RedisManager(fiveProps));
    }

    public void subscribe(final String key, final SubscriberListener listener) {
        new Thread() {
            @Override
            public void run() {
                redisManager.subscribe(listener, key);
            }
        }.start();
    }

    public static abstract class SubscriberListener extends JedisPubSub {
        // 取得订阅的消息后的处理
        public abstract void onMessage(String channel, String message);

        // 初始化订阅时候的处理
        public void onSubscribe(String channel, int subscribedChannels) {
        }

        // 取消订阅时候的处理
        public void onUnsubscribe(String channel, int subscribedChannels) {
        }

        // 初始化按表达式的方式订阅时候的处理
        public void onPSubscribe(String pattern, int subscribedChannels) {
        }

        // 取消按表达式的方式订阅时候的处理
        public void onPUnsubscribe(String pattern, int subscribedChannels) {
        }

        // 取得按表达式的方式订阅的消息后的处理
        public void onPMessage(String pattern, String channel, String message) {
            System.out.println(pattern + "=" + channel + "=" + message);
        }

    }
}
