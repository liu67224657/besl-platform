package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.PointProps;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class PointHotdeployConfig extends HotdeployConfig {
    private static Logger logger = LoggerFactory.getLogger(PointHotdeployConfig.class);

    private static final String KEY_POINT_REWARD_TYPE_LIST = "point.reward.type.list";

    private static final String KEY_POINT_ACTIONTYPE = ".point.actiontype";
    private static final String KEY_POINT_REWARD_POINT_VALUE = ".reward.point.value";
    private static final String KEY_POINT_REWARD_POINT_LIMIT = ".reward.point.limit";
    private static final String KEY_POINT_REWARD_POINT_TIMES = ".reward.point.times";

    private PointPropsCache pointPropsCache;

    public PointHotdeployConfig() {
        super(EnvConfig.get().getPointHotdeployConfig());
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

        PointPropsCache tempCache = new PointPropsCache();

        List<String> typeCodeList = getList(KEY_POINT_REWARD_TYPE_LIST);

        Map<PointActionType, PointProps> propsMap = new HashMap<PointActionType, PointProps>();
        for (String typeCode : typeCodeList) {
            int actionTypeCode = getInt(typeCode + KEY_POINT_ACTIONTYPE);

            PointActionType pointActionType = PointActionType.getByCode(actionTypeCode);
            if (pointActionType == null) {
                GAlerter.lan(this.getClass().getName() + " get ponit actionType is null. typeCode:" + typeCode);
            }

            int pointValue = getInt(typeCode + KEY_POINT_REWARD_POINT_VALUE);
            int pointLimit = getInt(typeCode + KEY_POINT_REWARD_POINT_LIMIT);
            int pointTimes = getInt(typeCode + KEY_POINT_REWARD_POINT_TIMES);

            PointProps props = new PointProps();
            props.setCode(typeCode);
            props.setValue(pointValue);
            props.setLimit(pointLimit);
            props.setTimes(pointTimes);

            propsMap.put(pointActionType, props);
        }
        tempCache.setPropsMap(propsMap);

        this.pointPropsCache = tempCache;
    }

    private class PointPropsCache {
        private Map<PointActionType, PointProps> propsMap = new HashMap<PointActionType, PointProps>();

        public Map<PointActionType, PointProps> getPropsMap() {
            return propsMap;
        }

        public void setPropsMap(Map<PointActionType, PointProps> propsMap) {
            this.propsMap = propsMap;
        }
    }

    /**
     * don't put anything
     *
     * @return
     */
    public Map<PointActionType, PointProps> getPropsMap() {
        return pointPropsCache.getPropsMap();
    }

}
