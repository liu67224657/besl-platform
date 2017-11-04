package com.enjoyf.platform.webapps.common.wordfilter;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.util.regex.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * <p/>
 * Description:验证app地址
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class AppURIFilters {
    private Logger logger = LoggerFactory.getLogger(AppURIFilters.class);
    private static AppURIFilters instance;

    private AppURIFilters() {

    }

    public synchronized static AppURIFilters get() {
        if (instance == null) {
            return new AppURIFilters();
        }

        return instance;
    }


    /**
     * 验证apple地址符合^http://itunes.apple.com才能获取地址
     *
     * @param url
     * @return true verifysuccess
     */
    public boolean verifyAppUrl(String url) {
        if (logger.isDebugEnabled()) {
            logger.debug("verify app url:" + url);
        }

        return RegexUtil.contain(url, HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getAppFormulaRegexs());
    }

}
