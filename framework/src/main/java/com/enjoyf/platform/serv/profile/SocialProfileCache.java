package com.enjoyf.platform.serv.profile;

import com.enjoyf.platform.service.profile.ProfileConstants;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-7-18
 * Time: 上午11:40
 * To change this template use File | Settings | File Templates.
 */
public class SocialProfileCache {


    private static final long TIME_OUT_SEC = 60l * 30l;

    private static final long TIME_SPNAME_OUT_SEC = 60l * 5l;

    private String serviceSection;

    private static final String PREFIX_SOCIAL_PROFILE_UNO_CODE = "_sprofileuno_";

    private static final String PREFIX_SOCIAL_PROFILE_SCREENNAME_CODE = "_sprofilescreenname_";

    private static final String PREFIX_NEWEST_SOCIAL_PROFILE_UNO_LIST = "_newestsocialprofileunolist_";


    private MemCachedConfig config;

    private MemCachedManager manager;

    public SocialProfileCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(this.config);
        this.serviceSection = ProfileConstants.SERVICE_SECTION;
    }

    //////////////////////////////////////////////////////////
    public SocialProfile getSocialProfileByUno(String uno) {
        Object code = manager.get(serviceSection + PREFIX_SOCIAL_PROFILE_UNO_CODE + uno);

        if (code == null) {
            return null;
        }
        return (SocialProfile) code;
    }

    public void putSocialProfile(SocialProfile socialProfile) {
        manager.put(serviceSection + PREFIX_SOCIAL_PROFILE_UNO_CODE + socialProfile.getBlog().getUno(), socialProfile, TIME_OUT_SEC);
    }

    public boolean removeSocialProfile(String uno) {
        return manager.remove(serviceSection + PREFIX_SOCIAL_PROFILE_UNO_CODE + uno);
    }

    ////////////////////////////////////////////////////////////////
    public String getUnoByScreenName(String name) {
        Object uno = manager.get(serviceSection + PREFIX_SOCIAL_PROFILE_SCREENNAME_CODE + name);

        if (uno == null) {
            return null;
        }
        return (String) uno;
    }

    public void putUnoByScreenName(String name,String uno) {
        manager.put(serviceSection + PREFIX_SOCIAL_PROFILE_SCREENNAME_CODE + name, uno, TIME_SPNAME_OUT_SEC);
    }

    public boolean removeUnoByScreenName(String name) {
        return manager.remove(serviceSection + PREFIX_SOCIAL_PROFILE_SCREENNAME_CODE + name);
    }

    public void putNewestSocialProfileUnoList(int curPage, List<String> unoList) {
        manager.put(serviceSection + PREFIX_NEWEST_SOCIAL_PROFILE_UNO_LIST + curPage, unoList, 5l*60l);
    }

    public List<String> getNewestSocialProfileUnoList(int curPage) {
        Object unoList = manager.get(serviceSection + PREFIX_NEWEST_SOCIAL_PROFILE_UNO_LIST + curPage);
        if(unoList == null){
            return null;
        }
        return (List<String>) unoList;
    }

    public boolean removeNewestSocialProfileUnoList(int curPage){
        return manager.remove(serviceSection + PREFIX_NEWEST_SOCIAL_PROFILE_UNO_LIST + curPage);
    }
}
