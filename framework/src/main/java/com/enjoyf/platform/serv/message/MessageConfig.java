/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.message;

import com.enjoyf.platform.service.message.MessageConstants;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class MessageConfig {
    //the props.
    private FiveProps props;
    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();
    //
    private String queueDiskStorePath;
    private int eventQueueThreadNum = 8;
    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";
    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";
    //
    private MemCachedConfig memCachedConfig;
    //
    private String joymeappWriteableDataSourceName = "joymeappwriteable";
    private Set<String> joymeappReadonlyDataSourceNames = new HashSet<String>();
    private static final String KEY_JOYMEAPP_WRITEABLE_DSN = "joymeappwriteable.datasource.name";
    private static final String KEY_JOYMEAPP_READONLY_DSNS = "joymeappreadonly.datasource.names";

    private RedisManager redisManager;

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public MessageConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("MessageConfig props is null.");

            return;
        }

        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        joymeappWriteableDataSourceName = props.get(KEY_JOYMEAPP_WRITEABLE_DSN, joymeappWriteableDataSourceName);

        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        List<String> joymeappDsns = props.getList(KEY_JOYMEAPP_READONLY_DSNS);
        for (String dsn : joymeappDsns) {
            joymeappReadonlyDataSourceNames.add(dsn);
        }

        //queue
        queueDiskStorePath = props.get(MessageConstants.SERVICE_PREFIX + ".NAME");
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        memCachedConfig = new MemCachedConfig(props);

        redisManager=new RedisManager(props);
    }

    public FiveProps getProps() {
        return props;
    }

    public String getWriteableDataSourceName() {
        return writeableDataSourceName;
    }

    public Set<String> getReadonlyDataSourceNames() {
        return readonlyDataSourceNames;
    }

    public String getQueueDiskStorePath() {
        return queueDiskStorePath;
    }

    public int getEventQueueThreadNum() {
        return eventQueueThreadNum;
    }

    public MemCachedConfig getMemCachedConfig() {
        return memCachedConfig;
    }

    public String getJoymeAppWriteableDataSourceName() {
        return joymeappWriteableDataSourceName;
    }

    public Set<String> getJoymeAppReadonlyDataSourceNames() {
        return joymeappReadonlyDataSourceNames;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    /////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
