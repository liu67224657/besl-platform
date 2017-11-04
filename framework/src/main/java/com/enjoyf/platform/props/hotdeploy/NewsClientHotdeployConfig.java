package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.joymeapp.JoymeAppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-2-21
 * Time: 上午11:11
 * To change this template use File | Settings | File Templates.
 */
public class NewsClientHotdeployConfig extends HotdeployConfig {
    private static Logger logger = LoggerFactory.getLogger(ContextHotdeployConfig.class);

    private static final String KEY_JOYMEAPP_NEWSCLIENT_ISOPEN = "joymeapp.newsclient.isopen";

    private JoymeAppCache cache;

    public NewsClientHotdeployConfig() {
        super(EnvConfig.get().getJoymeAppHotdeployConfig());
    }

    @Override
    public void init() {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppHotdeployConfig Props init start......");
        }

        reload();

        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppHotdeployConfig Props init end......");
        }

    }

    @Override
    public void reload() {
        super.reload();

        JoymeAppCache tempCache = new JoymeAppCache();

        String isOpen = getString(KEY_JOYMEAPP_NEWSCLIENT_ISOPEN);
        Map<String, JoymeAppConfig> joymeAppConfigMap = new HashMap<String, JoymeAppConfig>();

        JoymeAppConfig joymeAppConfig = new JoymeAppConfig();
        joymeAppConfig.setShakeIsOpen(isOpen);

        tempCache.setJoymeAppConfig(joymeAppConfig);

        this.cache = tempCache;
    }

    private class JoymeAppCache {
        private JoymeAppConfig joymeAppConfig = new JoymeAppConfig();

        public JoymeAppConfig getJoymeAppConfig() {
            return joymeAppConfig;
        }

        public void setJoymeAppConfig(JoymeAppConfig joymeAppConfig) {
            this.joymeAppConfig = joymeAppConfig;
        }
    }

    /**
     * don't put anything
     *
     * @return
     */
    public JoymeAppConfig getJoymeAppConfig() {
        return cache.getJoymeAppConfig();
    }

}
