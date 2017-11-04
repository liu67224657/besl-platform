package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.advertise.AdvertiseEventType;
import com.enjoyf.platform.service.content.ActivityConfig;
import com.enjoyf.platform.service.event.user.UserEventType;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="mailto:yinpengyi@enjoyf.com">Yin Pengyi</a>
 */
public class ActivityHotdeployConfig extends HotdeployConfig {
    //
    private static final Logger logger = LoggerFactory.getLogger(ActivityHotdeployConfig.class);

    /////////////////////////////////////////////////////////////////////////////
    //the keys
    private static final String KEY_ACTIVITY_CODE_LIST = "activity.code.list";
    private static final String SUFFEX_ACTIVITY_STARTTIME = ".activity.starttime";
    private static final String SUFFEX_ACTIVITY_ENDTIME = ".activity.endtime";

    private Cached cached;

    public ActivityHotdeployConfig() {
        super(EnvConfig.get().getActivityHotdeployConfig());
    }

    @Override
    public void init() {
        reload();
    }

    public synchronized void reload() {
        super.reload();

        Cached tmpCache = new Cached();

        List<String> activityList = getList(KEY_ACTIVITY_CODE_LIST);

        Map<String, ActivityConfig> activityConfigMap=new HashMap<String,ActivityConfig>();
        for (String activityCode : activityList) {
            ActivityConfig config = new ActivityConfig();
            config.setCode(activityCode);

            String startTimeStr = getString(activityCode + SUFFEX_ACTIVITY_STARTTIME);
            if(!StringUtil.isEmpty(startTimeStr)){
                Date startDate=null;
                try {
                    startDate=DateUtil.formatStringToDate(startTimeStr,DateUtil.PATTERN_DATE_TIME);
                } catch (ParseException e) {
                }
                if(startDate!=null){
                    config.setStartTime(startDate);
                }
            }
            String endTimeStr = getString(activityCode + SUFFEX_ACTIVITY_ENDTIME);
            if(!StringUtil.isEmpty(endTimeStr)){
                Date endDate=null;
                try {
                    endDate=DateUtil.formatStringToDate(endTimeStr,DateUtil.PATTERN_DATE_TIME);
                } catch (ParseException e) {
                }
                if(endDate!=null){
                    config.setEndTime(endDate);
                }
            }

            activityConfigMap.put(activityCode,config);
        }
         tmpCache.setActivityConfigMap(activityConfigMap);

        this.cached = tmpCache;

        logger.info("Activity Props init finished.");

    }


    public ActivityConfig getActivityConfig(String activityConfig) {
        return cached.getActivityConfigMap().get(activityConfig);
    }

    //
    private class Cached {
        private Map<String, ActivityConfig> activityConfigMap;

        public Map<String, ActivityConfig> getActivityConfigMap() {
            return activityConfigMap;
        }

        public void setActivityConfigMap(Map<String, ActivityConfig> activityConfigMap) {
            this.activityConfigMap = activityConfigMap;
        }
    }



}
