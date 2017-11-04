package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.gameres.GameCategory;
import com.enjoyf.platform.service.gameres.GameChannel;
import com.enjoyf.platform.service.gameres.GameDevice;
import com.enjoyf.platform.service.gameres.GameStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-2-21
 * Time: 上午11:11
 * To change this template use File | Settings | File Templates.
 */
public class GameResourceHotdeployConfig extends HotdeployConfig {
    private static Logger logger = LoggerFactory.getLogger(ContextHotdeployConfig.class);

    private static final String KEY_CHANNEL_CODE_LIST = "channel.code.list";

    private ResourceCache resourceCache;

    public GameResourceHotdeployConfig() {
        super(EnvConfig.get().getGameResourceHotdeployConfig());
    }

    @Override
    public void init() {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceHotdeployConfig Props init start......");
        }

        reload();

        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceHotdeployConfig Props init end......");
        }

    }

    @Override
    public void reload() {
        super.reload();

        ResourceCache tempCache = new ResourceCache();

        List<String> channelCodeList = getList(KEY_CHANNEL_CODE_LIST);
        Map<String, GameChannel> channelMap = new HashMap<String, GameChannel>();
        for (String code : channelCodeList) {
            channelMap.put(code, new GameChannel(code));
        }
        tempCache.setChannelMap(channelMap);

        this.resourceCache = tempCache;
    }

    private class ResourceCache {
        private Map<String, GameChannel> channelMap = new HashMap<String, GameChannel>();

        public Map<String, GameChannel> getChannelMap() {
            return channelMap;
        }

        public void setChannelMap(Map<String, GameChannel> channelMap) {
            this.channelMap = channelMap;
        }
    }

    /**
     * don't put anything
     *
     * @return
     */
    public Map<String, GameChannel> getChannelMap() {
        return resourceCache.getChannelMap();
    }

}
