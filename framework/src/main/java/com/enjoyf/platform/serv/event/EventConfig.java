/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.serv.event;

import com.enjoyf.platform.service.event.EventConstants;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.redis.TaskRedisServer;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class EventConfig {
    //
    private FiveProps servProps;

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    //
    private String queueDiskStorePath;
    private int eventProcessQueueThreadNum = 4;

    //
    private boolean openStatTrigger;
    private String eventStatCronExp = "0 */1 * * * ?";

    private int locationReloadIntervalSecs = 300;

    private int onlineCacheCheckIntervalMsecs = 1000 * 60 * 5;
    private int onlineCacheExpireMsecs = 1000 * 60 * 5;

    // the keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";

    //
    private static final String KEY_PROCESS_QUEUE_THREAD_NUM = "event.queue.thread.num";
    private static final String KEY_ONLINE_CACHE_CHECK_INTERVAL = "event.online.cache.check.interval.msecs";
    private static final String KEY_ONLINE_CACHE_EXPIRE = "event.online.cache.expire.msecs";

    //
    private final static String KEY_STAT_TRIGGER_OPEN = "event.stat.trigger.open";
    private final static String KEY_STAT_CRON_EXP = "event.stat.cron.exp";

    private final static String KEY_LOCATION_RELOAD_INTERVAL_SECS = "event.location.reload.interval.secs";

    private static final String KEY_MONGODB_WRITEABLE_DSN = "mongodb.writeable.datasource.name";
    private static final String KEY_MONGODB_READONLY_DSNS = "mongodb.readonly.datasource.name";

    private String mongoDbWriteAbleDateSourceName = "writeable";
    private Set<String> mongoDbReadonlyDataSourceNames = new HashSet<String>();

    private MemCachedConfig memCachedConfig;

    private TaskRedisServer taskRedisServer;

    public EventConfig(FiveProps props) {
        servProps = props;

        // db configure
        if (servProps == null) {
            GAlerter.lab("Server configure is null.");
            return;
        }

        //
        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        //
        queueDiskStorePath = props.get(EventConstants.SERVICE_PREFIX + ".NAME");
        eventProcessQueueThreadNum = servProps.getInt(KEY_PROCESS_QUEUE_THREAD_NUM, eventProcessQueueThreadNum);

        //
        onlineCacheCheckIntervalMsecs = servProps.getInt(KEY_ONLINE_CACHE_CHECK_INTERVAL, onlineCacheCheckIntervalMsecs);
        onlineCacheExpireMsecs = servProps.getInt(KEY_ONLINE_CACHE_EXPIRE, onlineCacheExpireMsecs);

        //
        openStatTrigger = servProps.getBoolean(KEY_STAT_TRIGGER_OPEN, openStatTrigger);
        eventStatCronExp = servProps.get(KEY_STAT_CRON_EXP, eventStatCronExp);

        //
        locationReloadIntervalSecs = servProps.getInt(KEY_LOCATION_RELOAD_INTERVAL_SECS, locationReloadIntervalSecs);

        mongoDbWriteAbleDateSourceName = props.get(KEY_MONGODB_WRITEABLE_DSN, mongoDbWriteAbleDateSourceName);
        List<String> mongodbDsns = props.getList(KEY_MONGODB_READONLY_DSNS);
        for (String mongoDbDsn : mongodbDsns) {
            mongoDbReadonlyDataSourceNames.add(mongoDbDsn);
        }

        memCachedConfig = new MemCachedConfig(props);

        taskRedisServer = new TaskRedisServer(props);
    }

    public String getWriteableDataSourceName() {
        return writeableDataSourceName;
    }

    public void setWriteableDataSourceName(String writeableDataSourceName) {
        this.writeableDataSourceName = writeableDataSourceName;
    }

    public Set<String> getReadonlyDataSourceNames() {
        return readonlyDataSourceNames;
    }

    public void setReadonlyDataSourceNames(Set<String> readonlyDataSourceNames) {
        this.readonlyDataSourceNames = readonlyDataSourceNames;
    }

    public String getQueueDiskStorePath() {
        return queueDiskStorePath;
    }

    public int getEventProcessQueueThreadNum() {
        return eventProcessQueueThreadNum;
    }

    public FiveProps getProps() {
        return servProps;
    }

    public boolean isOpenStatTrigger() {
        return openStatTrigger;
    }

    public String getEventStatCronExp() {
        return eventStatCronExp;
    }

    public int getOnlineCacheCheckIntervalMsecs() {
        return onlineCacheCheckIntervalMsecs;
    }

    public int getOnlineCacheExpireMsecs() {
        return onlineCacheExpireMsecs;
    }

    public int getLocationReloadIntervalSecs() {
        return locationReloadIntervalSecs;
    }


    public String getMongoDbWriteAbleDateSourceName() {
        return mongoDbWriteAbleDateSourceName;
    }

    public Set<String> getMongoDbReadonlyDataSourceNames() {
        return mongoDbReadonlyDataSourceNames;
    }

    MemCachedConfig getMemCachedConfig() {
        return memCachedConfig;
    }

    public TaskRedisServer getTaskRedisServer() {
        return taskRedisServer;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
