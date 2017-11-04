/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.oauth;

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
public class OAuthConfig {
    //the props.
    private FiveProps props;

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    //
    private long expireTokenClearIntervalMsecs = 1000l * 60 * 60 * 24;

    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";

    private static final String KEY_EXPIRE_TOKEN_CLEAR_INTERVAL_MSECS = "expire.token.clear.interval.msecs";

    private MemCachedConfig memCachedConfig;


    //////////////////////////////////////////////////////////////////////////////////////////////////
    public OAuthConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("OAuthConfig props is null.");

            return;
        }

        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        //others
        expireTokenClearIntervalMsecs = props.getLong(KEY_EXPIRE_TOKEN_CLEAR_INTERVAL_MSECS, expireTokenClearIntervalMsecs);

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

    public long getExpireTokenClearIntervalMsecs() {
        return expireTokenClearIntervalMsecs;
    }

    public MemCachedConfig getMemCachedConfig() {
        return memCachedConfig;
    }

    /////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
