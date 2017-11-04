package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.joymeapp.JoymeAppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-2-21
 * Time: 上午11:11
 * To change this template use File | Settings | File Templates.
 */
public class ProfileHotdeployConfig extends HotdeployConfig {
    private static Logger logger = LoggerFactory.getLogger(ProfileHotdeployConfig.class);

    private static final String KEY_FOCUS_UNO_LIST = "social.profile.default.focus.uno.list";

    private ProfileCache cache;

    public ProfileHotdeployConfig() {
        super(EnvConfig.get().getProfileHotdeployConfig());
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
    public synchronized void reload() {
        super.reload();

        ProfileCache tempCache = new ProfileCache();

        List<String> focusUnoList = getList(KEY_FOCUS_UNO_LIST);

        tempCache.setFocusUnoList(focusUnoList);

        this.cache = tempCache;
    }

    private class ProfileCache {
        private List<String> focusUnoList = new ArrayList<String>();

        private List<String> getFocusUnoList() {
            return focusUnoList;
        }

        private void setFocusUnoList(List<String> focusUnoList) {
            this.focusUnoList = focusUnoList;
        }
    }

    /**
     * don't put anything
     *
     * @return
     */
    public List<String> getFocusUnoList() {
        return cache.getFocusUnoList();
    }

}
