/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.misc;

import com.enjoyf.platform.service.misc.MiscConstants;
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
class MiscConfig {
    //the props.
    private FiveProps props;

    private boolean openStatTrigger = false;
    private String refreshcmstimingStatCronExp = "0 */10 * * * ?";

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";

    private static final String KEY_MONGODB_WRITEABLE_DSN = "mongodb.writeable.datasource.name";
    private static final String KEY_MONGODB_READONLY_DSNS = "mongodb.readonly.datasource.name";
    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";

    private final static String KEY_STAT_TRIGGER_OPEN = "misc.stat.trigger.open";
    private final static String KEY_REFRESHCMSTIMING_STAT_CRON_EXP = "misc.refreshcmstiming.stat.cron.exp";

    private String mongoDbWriteAbleDateSourceName = "writeable";
    private Set<String> mongoDbReadonlyDataSourceNames = new HashSet<String>();
    private String queueDiskStorePath;
    private int eventQueueThreadNum = 8;

    private MemCachedConfig memCachedConfig;

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public MiscConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("MiscConfig props is null.");

            return;
        }

        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }
        memCachedConfig = new MemCachedConfig(props);
        //others
        mongoDbWriteAbleDateSourceName = props.get(KEY_MONGODB_WRITEABLE_DSN, mongoDbWriteAbleDateSourceName);
        List<String> mongodbDsns = props.getList(KEY_MONGODB_READONLY_DSNS);
        for (String mongoDbDsn : mongodbDsns) {
            mongoDbReadonlyDataSourceNames.add(mongoDbDsn);
        }

        queueDiskStorePath = props.get(MiscConstants.SERVICE_PREFIX + ".NAME");
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        openStatTrigger = props.getBoolean(KEY_STAT_TRIGGER_OPEN, openStatTrigger);
        refreshcmstimingStatCronExp = props.get(KEY_REFRESHCMSTIMING_STAT_CRON_EXP, refreshcmstimingStatCronExp);
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

    public String getMongoDbWriteAbleDateSourceName() {
        return mongoDbWriteAbleDateSourceName;
    }

    public Set<String> getMongoDbReadonlyDataSourceNames() {
        return mongoDbReadonlyDataSourceNames;
    }

    public String getQueueDiskStorePath() {
        return queueDiskStorePath;
    }

    public int getEventQueueThreadNum() {
        return eventQueueThreadNum;
    }

    public static String getKeyEventQueueThreadNum() {
        return KEY_EVENT_QUEUE_THREAD_NUM;
    }

    public MemCachedConfig getMemCachedConfig() {
        return memCachedConfig;
    }

    public void setMemCachedConfig(MemCachedConfig memCachedConfig) {
        this.memCachedConfig = memCachedConfig;
    }

    public boolean isOpenStatTrigger() {
        return openStatTrigger;
    }

    public String getRefreshcmstimingStatCronExp() {
        return refreshcmstimingStatCronExp;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
