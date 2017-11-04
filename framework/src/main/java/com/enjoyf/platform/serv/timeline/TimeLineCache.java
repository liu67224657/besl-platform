package com.enjoyf.platform.serv.timeline;

import com.enjoyf.platform.service.timeline.SocialTimeLineItem;
import com.enjoyf.platform.service.timeline.TimeLineConstants;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;
/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-10-17
 * Time: 上午11:35
 * To change this template use File | Settings | File Templates.
 */
public class TimeLineCache {

    private static final String SOCIAL_TIME_LINE_CACHE = TimeLineConstants.SERVICE_SECTION + "_socialtimeline_";

    private MemCachedManager manager;

    public TimeLineCache(MemCachedConfig config) {
        manager = new MemCachedManager(config);
    }

    public SocialTimeLineItem getSocialTimeLineItem(String id) {
        Object obj = manager.get(SOCIAL_TIME_LINE_CACHE + id);
        if(obj == null){
            return null;
        }
        try {
            return (SocialTimeLineItem) obj;
        } catch (Exception e) {
            manager.remove(SOCIAL_TIME_LINE_CACHE + id);
        }
        return null;
    }

    public void putSocialTimeLineItem(SocialTimeLineItem socialTimeLineItem) {
        manager.put(SOCIAL_TIME_LINE_CACHE + socialTimeLineItem.getSid(), socialTimeLineItem, 60l*60l);
    }

    public boolean removeSocialTimeLineItem(String id){
        return manager.remove(SOCIAL_TIME_LINE_CACHE + id);
    }
}
