package com.enjoyf.platform.serv.usercenter;

import com.enjoyf.platform.service.timeline.TimeLineConstants;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 下午2:17
 * Desc:
 */
public class UserCenterConfig {

    //the props.
    private FiveProps props;

    //

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";
    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";


    private int eventQueueThreadNum = 8;
    private String queueDiskStorePath;

    private MemCachedConfig memCachedConfig;

    private RedisManager redisManager;

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public UserCenterConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("ToolsConfig props is null.");

            return;
        }

        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        //others
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);
        queueDiskStorePath = props.get(TimeLineConstants.SERVICE_PREFIX + ".NAME");

        memCachedConfig=new MemCachedConfig(props);

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

    public int getEventQueueThreadNum() {
        return eventQueueThreadNum;
    }

    public MemCachedConfig getMemCachedConfig() {
        return memCachedConfig;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }


    public String getQueueDiskStorePath() {
        return queueDiskStorePath;
    }

    /////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
