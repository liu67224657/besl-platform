/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.sync;

import com.enjoyf.platform.service.sync.SyncConstants;
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
class SyncConfig {
    //the props.
    private FiveProps props;

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    //
    private String queueDiskStorePath;
    private int eventQueueThreadNum = 8;

    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";
    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";

    private static final String KEY_SEARCH_SERVER_NAME = "syncserver.NAME";
    private static final String KEY_SYNC_TEMP_DIR = "sync.temp.dir";
    private static final String KEY_SYNC_MSYH_TTF_DIR = "sync.msyh.ttf.dir";
    private static final String KEY_SYNC_SIMSUN_TTF_DIR = "sync.simsun.ttf.dir";
    private static final String KEY_SYNC_RETRY_TIMES = "sync.retry.times";

    private String tempImageDir = "/opt/servicedata/temp";
    private String syncMsyhTTFDir = "/usr/share/fonts/chinese/TrueType/msyh.ttf";
    private String syncSimSunTTFDir = "/usr/share/fonts/chinese/TrueType/simsun.ttf";
    private String syncServerName = "syncservice01";
    private int retryTimes = 3;


    private MemCachedConfig memCachedConfig;


    //////////////////////////////////////////////////////////////////////////////////////////////////
    public SyncConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("SyncConfig props is null.");

            return;
        }

        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        //queue
        queueDiskStorePath = props.get(SyncConstants.SERVICE_PREFIX + ".NAME");
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        syncServerName = props.get(KEY_SEARCH_SERVER_NAME, syncServerName);
        tempImageDir = props.get(KEY_SYNC_TEMP_DIR, tempImageDir);
        syncMsyhTTFDir = props.get(KEY_SYNC_MSYH_TTF_DIR, syncMsyhTTFDir);
        syncSimSunTTFDir = props.get(KEY_SYNC_SIMSUN_TTF_DIR, syncSimSunTTFDir);
        retryTimes = props.getInt(KEY_SYNC_RETRY_TIMES, retryTimes);

        memCachedConfig=new MemCachedConfig(props);
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

    public String getTempImagePath() {
        return tempImageDir + "/" + syncServerName + "/";
    }

    public String getSyncMsyhTTFDir() {
        return syncMsyhTTFDir;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public String getSyncSimSunTTFDir() {
        return syncSimSunTTFDir;
    }

    public MemCachedConfig getMemCachedConfig() {
        return memCachedConfig;
    }

    /////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
