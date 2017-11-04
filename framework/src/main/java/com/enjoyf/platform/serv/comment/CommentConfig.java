/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.comment;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.collection.MemDiskCacheConfig;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class CommentConfig {
    //the props.
    private FiveProps props;

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    private String mongoDbWriteAbleDateSourceName = "writeable";
    private Set<String> mongoDbReadonlyDataSourceNames = new HashSet<String>();

    //
    private String queueDiskStorePath;
    private int eventQueueThreadNum = 8;

    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";
    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";

    private static final String KEY_MONGODB_WRITEABLE_DSN = "mongodb.writeable.datasource.name";
    private static final String KEY_MONGODB_READONLY_DSNS = "mongodb.readonly.datasource.name";

    //cache
    private MemDiskCacheConfig memDiskCacheConfig;

    private MemCachedConfig memCachedConfig;

    //redis
    private String redisServerHost;
    private int redisServerPort;
    private static final String KEY_REDIS_SERVER_HOST = "redis.server.host";
    private static final String KEY_REDIS_SERVER_PORT = "redis.server.port";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public CommentConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("CommentConfig props is null.");

            return;
        }

        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        //queue
        queueDiskStorePath = props.get(CommentConstants.SERVICE_PREFIX + ".NAME");
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        memDiskCacheConfig = new MemDiskCacheConfig(props.get(CommentConstants.SERVICE_PREFIX + ".NAME"), props);

        memCachedConfig = new MemCachedConfig(props);

        //
        mongoDbWriteAbleDateSourceName = props.get(KEY_MONGODB_WRITEABLE_DSN, mongoDbWriteAbleDateSourceName);
        List<String> mongodbDsns = props.getList(KEY_MONGODB_READONLY_DSNS);
        for (String mongoDbDsn : mongodbDsns) {
            mongoDbReadonlyDataSourceNames.add(mongoDbDsn);
        }

        redisServerHost = props.get(KEY_REDIS_SERVER_HOST);
        redisServerPort = props.getInt(KEY_REDIS_SERVER_PORT);
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

    public MemDiskCacheConfig getMemDiskCacheConfig() {
        return memDiskCacheConfig;
    }

    public String getMongoDbWriteAbleDateSourceName() {
        return mongoDbWriteAbleDateSourceName;
    }

    public Set<String> getMongoDbReadonlyDataSourceNames() {
        return mongoDbReadonlyDataSourceNames;
    }

    public String getRedisServerHost() {
        return redisServerHost;
    }

    public int getRedisServerPort() {
        return redisServerPort;
    }

    /////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
