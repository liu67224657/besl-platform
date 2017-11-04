/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.joymeappconfig;

import com.enjoyf.platform.service.joymeapp.JoymeAppConstants;
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
public class JoymeAppConfigConfig {
    //the props.
    private FiveProps props;

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    private String wikiUrlWriteAbleDataSourceName = "wikiurlwriteable";
    private Set<String> wikiUrlReadonlyDataSourceNames = new HashSet<String>();

    private String appFavPublishTriggerExp = "";
    private boolean appFavPublishTriggerOpen = false;

    //
    private String queueDiskStorePath;
    private int eventQueueThreadNum = 8;

    private String cmsWriteableDataSourceName = "cmswriteable";
    private Set<String> cmsReadonlyDataSourceNames = new HashSet<String>();

    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";

    private static final String KEY_WIKIURL_WRITEABLE_DSN = "wikiurl.writeable.datasource.name";
    private static final String KEY_WIKIURL_READONLY_DSNS = "wikiurl.readonly.datasource.names";

    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";

    private static final String KEY_APPFAV_PUBLISH_TIGGER_OPEN = "appfav.publish.trigger.open";
    private static final String KEY_APPFAV_PUBLISH_TIGGER_EXP = "appfav.publish.trigger.exp";

    private static final String KEY_MONGODB_WRITEABLE_DSN = "mongodb.writeable.datasource.name";
    private static final String KEY_MONGODB_READONLY_DSNS = "mongodb.readonly.datasource.name";

    private static final String KEY_CMSWRITEABLE_DSN = "cmswriteable.datasource.name";
    private static final String KEY_CMSREADONLY_DSNS = "cmsreadonly.datasource.names";


    //cache
    private MemDiskCacheConfig memDiskCacheConfig;

    private MemCachedConfig memCachedConfig;


    private String mongoDbWriteAbleDateSourceName = "writeable";
    private Set<String> mongoDbReadonlyDataSourceNames = new HashSet<String>();

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public JoymeAppConfigConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("ClientConfig props is null.");

            return;
        }

        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);
        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        wikiUrlWriteAbleDataSourceName = props.get(KEY_WIKIURL_WRITEABLE_DSN, wikiUrlWriteAbleDataSourceName);
        List<String> wikiUrlDsns = props.getList(KEY_WIKIURL_READONLY_DSNS);
        for (String dsn : wikiUrlDsns) {
            wikiUrlReadonlyDataSourceNames.add(dsn);
        }

        //queue
        queueDiskStorePath = props.get(JoymeAppConstants.SERVICE_PREFIX + ".NAME");
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        memDiskCacheConfig = new MemDiskCacheConfig(props.get(JoymeAppConstants.SERVICE_PREFIX + ".NAME"), props);

        memCachedConfig = new MemCachedConfig(props);

        appFavPublishTriggerExp = props.get(KEY_APPFAV_PUBLISH_TIGGER_EXP, appFavPublishTriggerExp);
        appFavPublishTriggerOpen = props.getBoolean(KEY_APPFAV_PUBLISH_TIGGER_OPEN, appFavPublishTriggerOpen);

        //
        mongoDbWriteAbleDateSourceName = props.get(KEY_MONGODB_WRITEABLE_DSN, mongoDbWriteAbleDateSourceName);
        List<String> mongodbDsns = props.getList(KEY_MONGODB_READONLY_DSNS);
        for (String mongoDbDsn : mongodbDsns) {
            mongoDbReadonlyDataSourceNames.add(mongoDbDsn);
        }

        //read the datasource setting.
        cmsWriteableDataSourceName = props.get(KEY_CMSWRITEABLE_DSN, cmsWriteableDataSourceName);

        List<String> cmsDsns = props.getList(KEY_CMSREADONLY_DSNS);
        for (String dsn : cmsDsns) {
            cmsReadonlyDataSourceNames.add(dsn);
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

    public String getWikiUrlWriteableDataSourceName() {
        return wikiUrlWriteAbleDataSourceName;
    }

    public Set<String> getWikiUrlReadonlyDataSourceNames() {
        return wikiUrlReadonlyDataSourceNames;
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

    public String getAppFavPublishTriggerExp() {
        return appFavPublishTriggerExp;
    }

    public boolean isAppFavPublishTriggerOpen() {
        return appFavPublishTriggerOpen;
    }

    public String getMongoDbWriteAbleDateSourceName() {
        return mongoDbWriteAbleDateSourceName;
    }

    public Set<String> getMongoDbReadonlyDataSourceNames() {
        return mongoDbReadonlyDataSourceNames;
    }

    public String getCmsWriteableDataSourceName() {
        return cmsWriteableDataSourceName;
    }

    public Set<String> getCmsDbReadonlyDataSourceNames() {
        return cmsReadonlyDataSourceNames;
    }

    /////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public MemDiskCacheConfig getMemDiskCacheConfig() {
        return memDiskCacheConfig;
    }

}
