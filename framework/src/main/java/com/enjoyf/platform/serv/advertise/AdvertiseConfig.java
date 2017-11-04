/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.advertise;

import com.enjoyf.platform.service.advertise.AdvertiseConstants;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class AdvertiseConfig {
    //the props.
    private FiveProps props;

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    //
    private String mongoDbWriteAbleDateSourceName = "writeable";
    private Set<String> mongoDbReadonlyDataSourceNames = new HashSet<String>();

    //
    private String queueDiskStorePath;
    private int eventProcessQueueThreadNum = 4;

    //
    private boolean openStatTrigger;
    private String eventStatCronExp = "0 */1 * * * ?";

    private MemCachedConfig memCachedConfig;

    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";

    //
    private static final String KEY_PROCESS_QUEUE_THREAD_NUM = "event.queue.thread.num";

    //
    private final static String KEY_STAT_TRIGGER_OPEN = "advertise.stat.trigger.open";
    private final static String KEY_STAT_CRON_EXP = "advertise.stat.cron.exp";

    //
    private static final String KEY_MONGODB_WRITEABLE_DSN = "mongodb.writeable.datasource.name";
    private static final String KEY_MONGODB_READONLY_DSNS = "mongodb.readonly.datasource.name";


    //////////////////////////////////////////////////////////////////////////////////////////////////
    public AdvertiseConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("AdvertiseConfig props is null.");

            return;
        }

        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        //
        mongoDbWriteAbleDateSourceName = props.get(KEY_MONGODB_WRITEABLE_DSN, mongoDbWriteAbleDateSourceName);
        List<String> mongodbDsns = props.getList(KEY_MONGODB_READONLY_DSNS);
        for (String mongoDbDsn : mongodbDsns) {
            mongoDbReadonlyDataSourceNames.add(mongoDbDsn);
        }

        //others
        //
        queueDiskStorePath = props.get(AdvertiseConstants.SERVICE_PREFIX + ".NAME");
        eventProcessQueueThreadNum = props.getInt(KEY_PROCESS_QUEUE_THREAD_NUM, eventProcessQueueThreadNum);

        //
        openStatTrigger = props.getBoolean(KEY_STAT_TRIGGER_OPEN, openStatTrigger);
        eventStatCronExp = props.get(KEY_STAT_CRON_EXP, eventStatCronExp);

        memCachedConfig = new MemCachedConfig(props);
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

    public int getEventProcessQueueThreadNum() {
        return eventProcessQueueThreadNum;
    }

    public boolean isOpenStatTrigger() {
        return openStatTrigger;
    }

    public String getEventStatCronExp() {
        return eventStatCronExp;
    }

    public MemCachedConfig getMemCachedConfig() {
        return memCachedConfig;
    }

    public void setMemCachedConfig(MemCachedConfig memCachedConfig) {
        this.memCachedConfig = memCachedConfig;
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
