package com.enjoyf.platform.serv.social;

import com.enjoyf.platform.service.social.SocialBlack;
import com.enjoyf.platform.service.social.SocialConstants;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-4
 * Time: 下午7:00
 * To change this template use File | Settings | File Templates.
 */
public class SocialBlackCache {
    private static final long TIME_OUT_SEC = 60l * 5l;
    private static final String PREFIX_CONTENT = "_black_";

    private MemCachedConfig config;

    private MemCachedManager manager;

    SocialBlackCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(this.config);
    }

    public void putSocialBlack(String ownuno, SocialBlack socialBlack) {
        Set<String> set = (Set<String>) manager.get(SocialConstants.SERVICE_SECTION + PREFIX_CONTENT + ownuno);
        if (set == null) {
            set = new HashSet<String>();
        }
        set.add(socialBlack.getDesUno());
        manager.put(ownuno, set, TIME_OUT_SEC);
    }

    public Set<String> getSocialBlack(String ownuno) {
        Set<String> set = (Set<String>) manager.get(SocialConstants.SERVICE_SECTION + PREFIX_CONTENT + ownuno);
        if (set == null) {
            return null;
        }
        return set;
    }

    public boolean removerSocialBlack(String ownuno) {
        return manager.remove(SocialConstants.SERVICE_SECTION + PREFIX_CONTENT + ownuno);
    }

}
