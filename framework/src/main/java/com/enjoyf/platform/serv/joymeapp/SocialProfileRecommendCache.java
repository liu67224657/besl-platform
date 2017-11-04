/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.joymeapp;

import com.enjoyf.platform.service.joymeapp.SocialProfileRecommend;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
class SocialProfileRecommendCache {

    private static final long TIME_OUT_SEC = 60l * 60l * 6l;

    private static final String PREFIX_SOCIAL_PROFILE_RECOMMEND = "joyme_social_profile_recommend_";


    private MemCachedConfig config;

    private MemCachedManager manager;

    SocialProfileRecommendCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
    }

    //////////////////////////////////////////////////////////
    public void putSocialProfileReommend(int recommendType, PageRows<SocialProfileRecommend> recommendPageRows) {
        manager.put(PREFIX_SOCIAL_PROFILE_RECOMMEND + recommendType, recommendPageRows, TIME_OUT_SEC);
    }

    public PageRows<SocialProfileRecommend> getSocialProfileReommend(int recommendType) {
        Object archiveObj = manager.get(PREFIX_SOCIAL_PROFILE_RECOMMEND + recommendType);
        if (archiveObj == null) {
            return null;
        }
        return (PageRows<SocialProfileRecommend>) archiveObj;
    }

    public boolean removeSocialProfileReommend(int recommendType) {
        return manager.remove(PREFIX_SOCIAL_PROFILE_RECOMMEND + recommendType);
    }


}
