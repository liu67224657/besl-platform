package com.enjoyf.platform.serv.gameres;

import com.enjoyf.platform.service.gameres.GameResourceConstants;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.collection.MemDiskCacheConfig;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-8-25
 * Time: 下午4:32
 * Desc:
 */
class GameResourceConfig {
    //the props.
    private FiveProps props;

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";
    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";

    private int eventQueueThreadNum = 8;
    private String queueDiskStorePath = "gameresservice01";

    private String wikiStatsCronExp = "0 0 3 * * ?";
    private boolean openStatTrigger = false;

    private MemCachedConfig memCachedConfig;

    private static final String KEY_MONGODB_WRITEABLE_DSN = "mongodb.writeable.datasource.name";
    private static final String KEY_MONGODB_READONLY_DSNS = "mongodb.readonly.datasource.name";


    private String mongoDbWriteAbleDateSourceName = "writeable";
    private Set<String> mongoDbReadonlyDataSourceNames = new HashSet<String>();

    public GameResourceConfig(FiveProps servProps) {
        props = servProps;
        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("GameResourceConfig props is null.");
            return;
        }

        //read the datasource setting.
        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        //others such as the cache settings
        queueDiskStorePath = props.get(GameResourceConstants.SERVICE_PREFIX + ".NAME");
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        openStatTrigger = props.getBoolean("wiki.stat.trigger.open", openStatTrigger);
        wikiStatsCronExp = props.get("wiki.stat.cron.exp", wikiStatsCronExp);

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

    public Set<String> getReadonlyDataSourceNames() {
        return readonlyDataSourceNames;
    }

    public String getWriteableDataSourceName() {
        return writeableDataSourceName;
    }

    public int getEventQueueThreadNum() {
        return eventQueueThreadNum;
    }

    public String getQueueDiskStorePath() {
        return queueDiskStorePath;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public String getWikiStatsCronExp() {
        return wikiStatsCronExp;
    }

    public boolean isOpenStatTrigger() {
        return openStatTrigger;
    }

    public MemCachedConfig getCachedConfig() {
        return memCachedConfig;
    }

    public String getMongoDbWriteAbleDateSourceName() {
        return mongoDbWriteAbleDateSourceName;
    }

    public Set<String> getMongoDbReadonlyDataSourceNames() {
        return mongoDbReadonlyDataSourceNames;
    }

}
