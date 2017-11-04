/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.ask;

import com.enjoyf.platform.service.ask.AskConstants;
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
public class AskConfig {
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

    //cache
    private MemDiskCacheConfig memDiskCacheConfig;

    private MemCachedConfig memCachedConfig;

    //redis
    private String redisServerHost;
    private int redisServerPort;
    private static final String KEY_REDIS_SERVER_HOST = "redis.server.host";
    private static final String KEY_REDIS_SERVER_PORT = "redis.server.port";

    private String checkExpireQuestionCronExp = "";
    private boolean checkExpireQuesitonTrigger = false;
    private static final String KEY_CHECKEXPIREQUESTIONCRONEXP = "check.expiredquestion.cronexp";
    private static final String KEY_CHECKEXPIREQUESITONTRIGGER = "check.expiredquestion.trigger";


    //热度排行定时器
    private String viewsumHotrankCronExp = "";
    private boolean viewsumHotrankTrigger = false;
    private static final String KEY_VIEWSUMHOTRANK_CRONEXP = "viewsum.hotrank.cronexp";
    private static final String KEY_VIEWSUMHOTRANK_TRIGGER = "viewsum.hotrank.trigger";


    //向对应游戏下的达人推送
    private String verifyNoticeCronExp = "";
    private boolean verifyNoticeTrigger = false;
    private static final String KEY_VERIFYNOTICE_CRONEXP = "verify.notice.cronexp";
    private static final String KEY_VERIFYNOTICE_TRIGGER = "verify.notice.trigger";


    //定时任务：活动、问题
    private String timedReleaseCronExp = "";
    private boolean timedReleaseTrigger = false;
    private static final String KEY_TIMEDRELEASE_CRONEXP = "timed.release.cronexp";
    private static final String KEY_TIMEDRELEASE_TRIGGER = "timed.release.trigger";

    //广告 轮播图定时
    private boolean adTrigger = false;
    private static final String KEY_AD_TRIGGER = "ad.trigger";


    //////////////////////////////////////////////////////////////////////////////////////////////////
    public AskConfig(FiveProps servProps) {
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
        queueDiskStorePath = props.get(AskConstants.SERVICE_PREFIX + ".NAME");
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        memDiskCacheConfig = new MemDiskCacheConfig(props.get(AskConstants.SERVICE_PREFIX + ".NAME"), props);
        memCachedConfig = new MemCachedConfig(props);

        redisServerHost = props.get(KEY_REDIS_SERVER_HOST);
        redisServerPort = props.getInt(KEY_REDIS_SERVER_PORT);

        checkExpireQuestionCronExp = props.get(KEY_CHECKEXPIREQUESTIONCRONEXP, checkExpireQuestionCronExp);
        checkExpireQuesitonTrigger = props.getBoolean(KEY_CHECKEXPIREQUESITONTRIGGER, false);


        viewsumHotrankCronExp = props.get(KEY_VIEWSUMHOTRANK_CRONEXP, viewsumHotrankCronExp);
        viewsumHotrankTrigger = props.getBoolean(KEY_VIEWSUMHOTRANK_TRIGGER, false);


        verifyNoticeCronExp = props.get(KEY_VERIFYNOTICE_CRONEXP, verifyNoticeCronExp);
        verifyNoticeTrigger = props.getBoolean(KEY_VERIFYNOTICE_TRIGGER, false);


        timedReleaseCronExp = props.get(KEY_TIMEDRELEASE_CRONEXP, timedReleaseCronExp);
        timedReleaseTrigger = props.getBoolean(KEY_TIMEDRELEASE_TRIGGER, false);
        adTrigger = props.getBoolean(KEY_AD_TRIGGER, false);
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

    public boolean isCheckExpireQuesitonTrigger() {
        return checkExpireQuesitonTrigger;
    }

    public String getCheckExpireQuestionCronExp() {
        return checkExpireQuestionCronExp;
    }

    public String getViewsumHotrankCronExp() {
        return viewsumHotrankCronExp;
    }

    public boolean isViewsumHotrankTrigger() {
        return viewsumHotrankTrigger;
    }


    public String getKeyVerifynoticeCronexp() {
        return verifyNoticeCronExp;
    }

    public boolean isVerifyNoticeTrigger() {
        return verifyNoticeTrigger;
    }


    public String getKeyTimedreleaseCronexp() {
        return timedReleaseCronExp;
    }

    public boolean isTimedReleaseTrigger() {
        return timedReleaseTrigger;
    }

    public boolean isAdTrigger() {
        return adTrigger;
    }

    /////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
