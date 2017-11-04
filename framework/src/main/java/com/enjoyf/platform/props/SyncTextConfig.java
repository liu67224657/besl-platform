package com.enjoyf.platform.props;

import com.enjoyf.platform.util.FiveProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SyncTextConfig {
    private static final Logger logger = LoggerFactory.getLogger(SyncTextConfig.class);

    //attributes
    private static SyncTextConfig instance;
    private FiveProps props;

    private String syncForwardTitleKey = "转发自@";
    private String syncContentUrlKey = "传送门→";

    private String KEY_SYNCTEXT_FORWARDTITLE = "synctext.key.forwardtitle";
    private String KEY_SYNCTEXT_CONTENTURL = "synctext.key.contenturl";

    public static synchronized SyncTextConfig get() {
        if (instance == null) {
            instance = new SyncTextConfig();
        }

        return instance;
    }

    private SyncTextConfig() {
        init();
    }

    private void init() {
        props = new FiveProps(EnvConfig.get().getSyncTextConfigFile());

        if (props == null) {
            logger.info("init props props is empty!");
            return;
        }
        syncForwardTitleKey = props.get(KEY_SYNCTEXT_FORWARDTITLE, syncForwardTitleKey);

        syncContentUrlKey = props.get(KEY_SYNCTEXT_CONTENTURL, syncContentUrlKey);

    }


    public String getSyncContentUrlKey() {
        return syncContentUrlKey;
    }

    public String getSyncForwardTitleKey() {
        return syncForwardTitleKey;
    }
}
