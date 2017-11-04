/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.stats;

import com.enjoyf.platform.service.stats.StatConstants;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class StatConfig {
    //
    private FiveProps servProps;

    //
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();


    private String mongoDbWriteAbleDateSourceName = "writeable";
    private Set<String> mongoDbReadonlyDataSourceNames = new HashSet<String>();
    private String serviceName;

    //
    private String queueDiskStorePath;
    private int statItemProcessQueueThreadNum = 8;

    private String pcAccessLogNameKey = "pcaccess.log";
    private String pcAccessLogDir = "/opt/servicelogs/pcaccess";

    // /////////////////////////////////////////////////////////
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";


    private static final String KEY_STAT_PROCESS_QUEUE_THREAD_NUM = "stat.process.queue.thread.num";

    private final static String KEY_STAT_TRIGGER_OPEN = "advertise.stat.trigger.open";
    private final static String KEY_STAT_CRON_EXP = "advertise.stat.cron.exp";

    private static final String KEY_MONGODB_WRITEABLE_DSN = "mongodb.writeable.datasource.name";
    private static final String KEY_MONGODB_READONLY_DSNS = "mongodb.readonly.datasource.name";

    private static final String KEY_PCACCESSLOG_DIR = "pcaccesslog.dir";
    private static final String KEY_PCLOGNAME_KEY = "pcaccess.log.key";

    private static final String KEY_OPEN_PCSTATTRIGGER = "pclog.stat.trigger.open";
    private static final String KEY_PCLOG_STAT_CRON_EXP = "pclog.stat.cron.exp";

    private boolean openPCStatTrigger;
    private String pcStatCronExp = "0 0/3 * * * ?";

    private static final String KEY_PCSTATGAMEEVENTLOG = "stats.gameeventlog.stat.cron.exp";
    private static final String KEY_PCSTATGAMEEVENTLOGOPEN = "stats.stat.trigger.open";
    private String pcstatGameeventlog = "";
    private boolean pcstatGameeventlogOpen = false;
    // /////////////////////////////////////////////////////////

    private static final String KEY_STATITEMRESULTJOB = "stats.statitem.result.cron.exp";
    private static final String KEY_STATITEMRESULTJOBOPEN = "stats.statitem.result.open";

    private String statItemResultJob = "";
    private boolean statItemResultJobOpen = false;


    private MemCachedConfig memCachedConfig;

    public StatConfig(FiveProps props) {
        servProps = props;

        // db configure
        if (servProps == null) {
            GAlerter.lab("StatConfig is null.");

            return;
        }

        //
        writeableDataSourceName = servProps.get(KEY_WRITEABLE_DSN, writeableDataSourceName);
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

        //queue
        queueDiskStorePath = props.get(StatConstants.SERVICE_PREFIX + ".NAME");
        statItemProcessQueueThreadNum = servProps.getInt(KEY_STAT_PROCESS_QUEUE_THREAD_NUM, statItemProcessQueueThreadNum);

        openPCStatTrigger = props.getBoolean(KEY_OPEN_PCSTATTRIGGER, openPCStatTrigger);
        pcStatCronExp = props.get(KEY_PCLOG_STAT_CRON_EXP, pcStatCronExp);
        serviceName = props.get(StatConstants.SERVICE_PREFIX + ".NAME");

        pcAccessLogDir = props.get(KEY_PCACCESSLOG_DIR, pcAccessLogDir);
        pcAccessLogNameKey = props.get(KEY_PCLOGNAME_KEY, pcAccessLogNameKey);

        pcstatGameeventlog = props.get(KEY_PCSTATGAMEEVENTLOG, pcstatGameeventlog);
        pcstatGameeventlogOpen = props.getBoolean(KEY_PCSTATGAMEEVENTLOGOPEN, pcstatGameeventlogOpen);

        //
        statItemResultJob = props.get(KEY_STATITEMRESULTJOB, statItemResultJob);
        statItemResultJobOpen = props.getBoolean(KEY_STATITEMRESULTJOBOPEN, statItemResultJobOpen);

        memCachedConfig = new MemCachedConfig(props);
    }

    public String getWriteableDataSourceName() {
        return writeableDataSourceName;
    }

    public String getQueueDiskStorePath() {
        return queueDiskStorePath;
    }

    public int getStatItemProcessQueueThreadNum() {
        return statItemProcessQueueThreadNum;
    }

    public FiveProps getProps() {
        return servProps;
    }

    public String getMongoDbWriteAbleDateSourceName() {
        return mongoDbWriteAbleDateSourceName;
    }

    public Set<String> getMongoDbReadonlyDataSourceNames() {
        return mongoDbReadonlyDataSourceNames;
    }


    public String getPcAccessLogNameKey() {
        return pcAccessLogNameKey;
    }


    public String getPcAccessLogDir() {
        return pcAccessLogDir;
    }


    public boolean isOpenPCStatTrigger() {
        return openPCStatTrigger;
    }

    public String getPcStatCronExp() {
        return pcStatCronExp;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }


    public String getServiceName() {
        return serviceName;
    }

    public String getPcstatGameeventlog() {
        return pcstatGameeventlog;
    }

    public void setPcstatGameeventlog(String pcstatGameeventlog) {
        this.pcstatGameeventlog = pcstatGameeventlog;
    }

    public boolean isPcstatGameeventlogOpen() {
        return pcstatGameeventlogOpen;
    }

    public void setPcstatGameeventlogOpen(boolean pcstatGameeventlogOpen) {
        this.pcstatGameeventlogOpen = pcstatGameeventlogOpen;
    }

    public MemCachedConfig getMemCachedConfig() {
        return memCachedConfig;
    }

    public Set<String> getReadonlyDataSourceNames() {
        return readonlyDataSourceNames;
    }

    public void setReadonlyDataSourceNames(Set<String> readonlyDataSourceNames) {
        this.readonlyDataSourceNames = readonlyDataSourceNames;
    }

    public String getStatItemResultJob() {
        return statItemResultJob;
    }

    public boolean isStatItemResultJobOpen() {
        return statItemResultJobOpen;
    }
}
