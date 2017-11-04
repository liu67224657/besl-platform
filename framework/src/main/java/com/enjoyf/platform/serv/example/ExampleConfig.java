/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.serv.example;

import com.enjoyf.platform.service.example.ExampleConstants;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.collection.MemDiskCacheConfig;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class ExampleConfig {
    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";
    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";

    private static final String KEY_STATS_TRIGGER_OPEN = "stats.trigger.open";
    private static final String KEY_STATS_TRIGGER_CRON_EXP = "stats.trigger.cron.exp";

    private static final String KEY_TIMER_RUN_INTERVAL_SECS = "timer.run.interval.secs";

    //the props.
    private FiveProps props;

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    //
    private int eventQueueThreadNum = 8;
    private String queueDiskStorePath = "/r001/file";

    //cache
    private MemDiskCacheConfig memDiskCacheConfig;

    //quartz setting.
    private boolean openStatTrigger;
    private String statCronExp = "*/10 * * * * ?";

    //the timer task running interval
    private int exampleTimerRunIntervalSecs = 10;


    public ExampleConfig(FiveProps servProps) {
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
        queueDiskStorePath = props.get(ExampleConstants.SERVICE_PREFIX + ".NAME");
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        //cache
        memDiskCacheConfig = new MemDiskCacheConfig(props.get(ExampleConstants.SERVICE_PREFIX + ".NAME"), props);

        //trigger
        openStatTrigger = props.getBoolean(KEY_STATS_TRIGGER_OPEN, openStatTrigger);
        statCronExp = props.get(KEY_STATS_TRIGGER_CRON_EXP, statCronExp);

        //the timer task
        exampleTimerRunIntervalSecs = props.getInt(KEY_TIMER_RUN_INTERVAL_SECS, exampleTimerRunIntervalSecs);

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

    public MemDiskCacheConfig getMemDiskCacheConfig() {
        return memDiskCacheConfig;
    }

    public String getStatCronExp() {
        return statCronExp;
    }

    public boolean isOpenStatTrigger() {
        return openStatTrigger;
    }

    public int getExampleTimerRunIntervalSecs() {
        return exampleTimerRunIntervalSecs;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
