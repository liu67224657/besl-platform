/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.content;

import com.enjoyf.platform.service.content.ContentConstants;
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
public class ContentConfig {
    //the props.
    private FiveProps props;

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    //the attributes
    private String cmsWriteableDataSourceName = "cmswriteable";
    private Set<String> cmsReadonlyDataSourceNames = new HashSet<String>();

    private String mongoDbWriteAbleDateSourceName = "writeable";
    private Set<String> mongoDbReadonlyDataSourceNames = new HashSet<String>();
    private static final String KEY_MONGODB_WRITEABLE_DSN = "mongodb.writeable.datasource.name";
    private static final String KEY_MONGODB_READONLY_DSNS = "mongodb.readonly.datasource.name";
    //
    private String queueDiskStorePath;
    private int eventQueueThreadNum = 8;

    //cache
    private MemDiskCacheConfig memDiskCacheConfig;

    private MemCachedConfig memCachedConfig;

    //
    private int wallContentSize = 1000;
    private long wallReloadIntervalMsecs = 5 * 60 * 1000;
    private String serviceName = "contentservice01";

    private int hotContentSize = 1000;
    private int hotContentScore = 10;

    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";

    private static final String KEY_WALL_CONTENT_SIZE = "wall.content.size";
    private static final String KEY_WALL_RELOAD_INTERVAL_MSECS = "wall.reload.interval.msecs";

    private static final String KEY_HOT_CONTENT_SIZE = "hot.content.size";
    private static final String KEY_HOT_CONTENT_SCORE = "hot.content.score";
    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";

    //
    private final static String KEY_TRIGGER_OPEN = "content.trigger.open";
    private final static String KEY_STAT_CRON_EXP = "content.stat.cron.exp";

    private static final String KEY_CMSWRITEABLE_DSN = "cmswriteable.datasource.name";
    private static final String KEY_CMSREADONLY_DSNS = "cmsreadonly.datasource.names";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public ContentConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("ContentConfig props is null.");

            return;
        }

        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        //read the datasource setting.
        cmsWriteableDataSourceName = props.get(KEY_CMSWRITEABLE_DSN, cmsWriteableDataSourceName);

        List<String> cmsDsns = props.getList(KEY_CMSREADONLY_DSNS);
        for (String dsn : cmsDsns) {
            cmsReadonlyDataSourceNames.add(dsn);
        }

        //others such as the cache settings
        wallContentSize = props.getInt(KEY_WALL_CONTENT_SIZE, wallContentSize);
        wallReloadIntervalMsecs = props.getLong(KEY_WALL_RELOAD_INTERVAL_MSECS, wallReloadIntervalMsecs);
        serviceName = props.get(ContentConstants.SERVICE_PREFIX + ".NAME");

        hotContentSize = props.getInt(KEY_HOT_CONTENT_SIZE, hotContentSize);
        hotContentScore = props.getInt(KEY_HOT_CONTENT_SCORE, hotContentScore);

        //
        queueDiskStorePath = props.get(ContentConstants.SERVICE_PREFIX + ".NAME");
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        //cache
        memDiskCacheConfig = new MemDiskCacheConfig(props.get(ContentConstants.SERVICE_PREFIX + ".NAME"), props);

        memCachedConfig = new MemCachedConfig(props);

        mongoDbWriteAbleDateSourceName = props.get(KEY_MONGODB_WRITEABLE_DSN, mongoDbWriteAbleDateSourceName);
        List<String> mongodbDsns = props.getList(KEY_MONGODB_READONLY_DSNS);
        for (String mongoDbDsn : mongodbDsns) {
            mongoDbReadonlyDataSourceNames.add(mongoDbDsn);
        }
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

    public int getWallContentSize() {
        return wallContentSize;
    }

    public long getWallReloadIntervalMsecs() {
        return wallReloadIntervalMsecs;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getQueueDiskStorePath() {
        return queueDiskStorePath;
    }

    public int getEventQueueThreadNum() {
        return eventQueueThreadNum;
    }

    public MemDiskCacheConfig getMemDiskCacheConfig() {
        return memDiskCacheConfig;
    }

    public MemCachedConfig getMemCachedConfig() {
        return memCachedConfig;
    }

    public int getHotContentSize() {
        return hotContentSize;
    }

    public int getHotContentScore() {
        return hotContentScore;
    }

    public String getCmsWriteableDataSourceName() {
        return cmsWriteableDataSourceName;
    }

    public Set<String> getCmsReadonlyDataSourceNames() {
        return cmsReadonlyDataSourceNames;
    }

    public String getMongoDbWriteAbleDateSourceName() {
        return mongoDbWriteAbleDateSourceName;
    }

    public Set<String> getMongoDbReadonlyDataSourceNames() {
        return mongoDbReadonlyDataSourceNames;
    }

    /////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
