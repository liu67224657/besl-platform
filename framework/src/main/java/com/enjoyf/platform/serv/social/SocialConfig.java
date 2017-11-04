/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.social;

import com.enjoyf.platform.service.social.SocialConstants;
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
class SocialConfig {
    //the props.
    private FiveProps props;

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    //
    private String queueDiskStorePath;
    private int eventQueueThreadNum = 8;

    private int calRecommendInterval = 3600;
    private int calRecommendBalckListInterval = 2592000;

    //cache
    private MemDiskCacheConfig memDiskCacheConfig;

    private MemCachedConfig memCachedConfig;


    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";
    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";

    private static final String KEY_CAL_RECOMMEND_INTERVAL_SECONDS = "cal.recommend.interval.seconds";
    private static final String KEY_CAL_RECOMMEND_BALCKVALID_INTERVAL_TIMESECONDS = "cal.recommend.balckvalid.interval.seconds";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public SocialConfig(FiveProps servProps) {
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
        queueDiskStorePath = props.get(SocialConstants.SERVICE_PREFIX + ".NAME");
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        //cache
        memDiskCacheConfig = new MemDiskCacheConfig(props.get(SocialConstants.SERVICE_PREFIX + ".NAME"), props);

        calRecommendInterval = props.getInt(KEY_CAL_RECOMMEND_INTERVAL_SECONDS, calRecommendInterval);
        calRecommendBalckListInterval = props.getInt(KEY_CAL_RECOMMEND_BALCKVALID_INTERVAL_TIMESECONDS, calRecommendBalckListInterval);

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

    public int getEventQueueThreadNum() {
        return eventQueueThreadNum;
    }

    public MemDiskCacheConfig getMemDiskCacheConfig() {
        return memDiskCacheConfig;
    }

    public int getCalRecommendInterval() {
        return calRecommendInterval;
    }

    public int getCalRecommendBalckListInterval() {
        return calRecommendBalckListInterval;
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
