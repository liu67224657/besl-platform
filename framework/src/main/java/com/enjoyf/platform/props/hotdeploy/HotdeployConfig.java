package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.HotdeployFile;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A generic hotdeployable config file
 *
 * @author Daniel
 */
public abstract class HotdeployConfig {
    private static final Logger logger = LoggerFactory.getLogger(HotdeployConfig.class);

    //
    private FiveProps props;
    private HotdeployFile hotdeployFile;

    //
    public HotdeployConfig(String configFile) {
        hotdeployFile = new HotdeployFile(configFile);

        init();
    }

    public abstract void init();

    public synchronized void reload() {
        if (logger.isDebugEnabled()) {
            logger.debug("Load hotdeployable file " + hotdeployFile.getFileName());
        }
        props = new FiveProps(hotdeployFile.getFileName());
    }

    public boolean isModified() {
        return hotdeployFile.isModified();
    }

    public String getString(String key) {
        return props.get(key);
    }

    public String getString(String key, String defaultVal) {
        String s = props.get(key);
        if (Strings.isNullOrEmpty(s)) {
            s = defaultVal;
        }
        return s;
    }

    public List<String> getList(String key) {
        return props.getList(key);
    }

    public List<String> getList(String key, String delimiter) {
        return props.getList(key, delimiter);
    }

    public Integer getInt(String key, int defaultVal) {
        return props.getInt(key, defaultVal);
    }

    public Integer getInt(String key) {
        return props.getInt(key);
    }

    public Long getLong(String key, long defaultVal) {
        return props.getLong(key, defaultVal);
    }

    public Long getLong(String key) {
        return props.getLong(key, 0L);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return props.getBoolean(key, defaultValue);
    }

    protected FiveProps getProps() {
        return props;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
