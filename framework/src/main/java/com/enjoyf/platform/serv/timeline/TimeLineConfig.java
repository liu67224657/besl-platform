/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.timeline;

import com.enjoyf.platform.service.timeline.TimeLineConstants;
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
class TimeLineConfig {
    //the props.
    private FiveProps props;

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    //queue
    private String queueDiskStorePath;
    private int timeLineQueueThreadNum = 8;
    private int eventQueueThreadNum = 8;

    private long timelineInitTimeMsecLimit = 1000l * 60 * 60 * 24 * 30;
    private int timelineInitContentSize = 10;

    private MemCachedConfig memCachedConfig;

    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";

    private static final String KEY_TIMELINE_QUEUE_THREAD_NUM = "timeline.queue.thread.num";
    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";

    private static final String KEY_TIMELINE_INIT_TIME_MSEC_LIMIT = "timeline.init.time.msec.limit";
    private static final String KEY_TIMELINE_INIT_CONTENT_SIZE = "timeline.init.content.size";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public TimeLineConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("MessageConfig props is null.");

            return;
        }

        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        //queue
        queueDiskStorePath = props.get(TimeLineConstants.SERVICE_PREFIX + ".NAME");
        timeLineQueueThreadNum = props.getInt(KEY_TIMELINE_QUEUE_THREAD_NUM, timeLineQueueThreadNum);
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        timelineInitTimeMsecLimit = props.getLong(KEY_TIMELINE_INIT_TIME_MSEC_LIMIT, timelineInitTimeMsecLimit);
        timelineInitContentSize = props.getInt(KEY_TIMELINE_INIT_CONTENT_SIZE, timelineInitContentSize);

        memCachedConfig =  new MemCachedConfig(props);
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

    public int getTimeLineQueueThreadNum() {
        return timeLineQueueThreadNum;
    }

    public int getEventQueueThreadNum() {
        return eventQueueThreadNum;
    }

    public long getTimelineInitTimeMsecLimit() {
        return timelineInitTimeMsecLimit;
    }

    public int getTimelineInitContentSize() {
        return timelineInitContentSize;
    }

    public MemCachedConfig getMemCachedConfig() {
        return memCachedConfig;
    }

    public void setMemCachedConfig(MemCachedConfig memCachedConfig) {
        this.memCachedConfig = memCachedConfig;
    }

    /////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
