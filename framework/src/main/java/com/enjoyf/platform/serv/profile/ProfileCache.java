/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.profile;

import com.enjoyf.platform.service.profile.*;
import com.enjoyf.platform.util.collection.MemDiskCacheConfig;
import com.enjoyf.platform.util.collection.MemDiskCacheManager;
import net.sf.ehcache.Element;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
public class ProfileCache extends MemDiskCacheManager {
    //
    private static final String KEY_CACHE_PROFILE_NAME = "profile";
    private static final String KEY_CACHE_PROFILE_BLOG = "_blog_";
    private static final String KEY_CACHE_PROFILE_DETAIL = "_detail_";
    private static final String KEY_CACHE_PROFILE_SUM = "_sum_";
    private static final String KEY_CACHE_PROFILE_SETTING = "_setting_";
    private static final String KEY_CACHE_ONLINE_NAME = "online";

    //
    public ProfileCache(MemDiskCacheConfig config) {
        super(config);
    }

    //the profile apis
    public Profile getProfile(String key) {
        Element element = getCache(KEY_CACHE_PROFILE_NAME).get(key);

        if (element != null) {
            return (Profile) element.getObjectValue();
        } else {
            return null;
        }
    }

    public void putProfile(String key, Profile value) {
        getCache(KEY_CACHE_PROFILE_NAME).put(new Element(key, value));
    }

    public boolean removeProfile(String key) {
        return getCache(KEY_CACHE_PROFILE_NAME).remove(key);
    }

    public int getProfileSize() {
        return getCache(KEY_CACHE_PROFILE_NAME).getSize();
    }

    //the online apis
    public ProfileOnlineStatus getProfileOnlineStatus(String key) {
        Element element = getCache(KEY_CACHE_ONLINE_NAME).get(key);

        if (element != null) {
            return ProfileOnlineStatus.ONLINE;
        } else {
            return ProfileOnlineStatus.OFFLINE;
        }
    }

    public void putProfileOnlineStatus(String key) {
        getCache(KEY_CACHE_ONLINE_NAME).put(new Element(key, 1));
    }

    public boolean removeProfileOnlineStatus(String key) {
        return getCache(KEY_CACHE_ONLINE_NAME).remove(key);
    }

    public int getProfileOnlineStatusSize() {
        return getCache(KEY_CACHE_ONLINE_NAME).getSize();
    }


    public ProfileDetail getProfileDetail(String uno) {
        Element element = getCache(KEY_CACHE_PROFILE_NAME + KEY_CACHE_PROFILE_DETAIL).get(uno);
        if (element != null) {
            return (ProfileDetail) element.getObjectValue();
        } else {
            return null;
        }
    }

    public void putProfileDetail(String uno, ProfileDetail detail) {
        getCache(KEY_CACHE_PROFILE_NAME + KEY_CACHE_PROFILE_DETAIL).put(new Element(uno, detail));
    }

    public boolean removeProfileDetail(String uno) {
        return getCache(KEY_CACHE_PROFILE_NAME + KEY_CACHE_PROFILE_DETAIL).remove(uno);
    }

    public ProfileSum getProfileSum(String uno) {
        Element element = getCache(KEY_CACHE_PROFILE_NAME + KEY_CACHE_PROFILE_SUM).get(uno);
        if (element != null) {
            return (ProfileSum) element.getObjectValue();
        } else {
            return null;
        }
    }

    public void putProfileSum(String uno, ProfileSum sum) {
        getCache(KEY_CACHE_PROFILE_NAME + KEY_CACHE_PROFILE_SUM).put(new Element(uno, sum));
    }

    public boolean removeProfileSum(String uno) {
        return getCache(KEY_CACHE_PROFILE_NAME + KEY_CACHE_PROFILE_SUM).remove(uno);
    }

    public ProfileSetting getProfileSetting(String uno) {
        Element element = getCache(KEY_CACHE_PROFILE_NAME + KEY_CACHE_PROFILE_SETTING).get(uno);
        if (element != null) {
            return (ProfileSetting) element.getObjectValue();
        } else {
            return null;
        }
    }

    public void putProfileSetting(String uno, ProfileSetting setting) {
        getCache(KEY_CACHE_PROFILE_NAME + KEY_CACHE_PROFILE_SETTING).put(new Element(uno, setting));
    }

    public boolean removeProfileSetting(String uno) {
        return getCache(KEY_CACHE_PROFILE_NAME + KEY_CACHE_PROFILE_SETTING).remove(uno);
    }
}
