/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.point;

import com.enjoyf.platform.service.point.PointConstants;
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
public class PointConfig {
    //the props.
    private FiveProps props;

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    //
    private String queueDiskStorePath;
    private int eventQueueThreadNum = 8;

    private boolean calTaoCodeTimerOpen = false;
    private boolean calPrestigeTimerOpen = false;
    private long calTaoCodeTimerinterval = 1200000;

    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";
    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";

    private static final String CALTAOCODE_TIMER_OPEN = "caltaocode.timer.open";
    private static final String CALPRESTIGE_TIMER_OPEN = "calprestige.timer.open";
    private static final String CALTAOCODE_TIMER_INTERVAL = "caltaocode.timer.interval";

    private MemCachedConfig memCachedConfig;


    //////////////////////////////////////////////////////////////////////////////////////////////////
    public PointConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("PointConfig props is null.");

            return;
        }

        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        //queue
        queueDiskStorePath = props.get(PointConstants.SERVICE_PREFIX + ".NAME");
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        calTaoCodeTimerOpen = props.getBoolean(CALTAOCODE_TIMER_OPEN, calTaoCodeTimerOpen);
        calTaoCodeTimerinterval = props.getLong(CALTAOCODE_TIMER_INTERVAL, calTaoCodeTimerinterval);
        calPrestigeTimerOpen=props.getBoolean(CALPRESTIGE_TIMER_OPEN,calPrestigeTimerOpen);


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

    public int getEventQueueThreadNum() {
        return eventQueueThreadNum;
    }

    public MemCachedConfig getMemCachedConfig() {
        return memCachedConfig;
    }

    public boolean isCalTaoCodeTimerOpen() {
        return calTaoCodeTimerOpen;
    }

    public boolean isCalPrestigeTimerOpen() {
        return calPrestigeTimerOpen;
    }

    public long getCalTaoCodeTimerinterval() {
        return calTaoCodeTimerinterval;
    }

    /////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
