/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.joymeappconfig;

import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeappconfig.AppChannel;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
class JoymeAppCache {

    private static final long TIME_OUT_SEC = 60l * 5l;

    private static final String CACHE_KEY_CONTENT_VERSION_PREFIX = "app_conent_version_";

    private static final String CACHE_KEY_JOYME_APP_BUTTOM_MENU = "joyme_app_buttom_menu_";

    private static final String CACHE_KEY_JOYME_APP_TOP_MENU = "joyme_app_top_menu_";

    private static final String CACHE_KEY_ACTIVITY_TOP_MENU = "activity_top_menu_";

    private static final String CACHE_KEY_CLIENT_TOP_MENU = "joyme_app_client_top_menu_";

    private static final String CACHE_KEY_MENUID_2_TAG = "_menuid2tag_";

    private static final String CACHE_KEY_TAGID_2_MENU = "_tid2menu_";

    private static final String CACHE_KEY_JOYME_APP_TOP_NEWS = "joyme_app_top_news_";

    private static final String CACHE_KEY_JOYME_APP_TIPS = "_joyme_app_tips_";

    private static final String CACHE_KEY_JOYME_APP_MENU_MODULE = "_joyme_app_menu_module_";

    private static final String CACHE_KEY_APP_CONFIG = "_app_config_";

    private static final String CACHE_KEY_JOYME_APP_CHANNEL = "joyme_app_channel_";


    private static final String CACHE_KEY_ACTIVITY_TOP_MENU_ACTIVITYTOPMENULINEKEY = "activity_top_menu_activitytopmenulinekey_";

    private MemCachedConfig config;

    private MemCachedManager manager;

    private String section;

    JoymeAppCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
        section = JoymeAppConstants.SERVICE_SECTION;
    }


    public void putAppContentVersionList(String appKey, List<AppContentVersionInfo> infolist) {
        manager.put(CACHE_KEY_CONTENT_VERSION_PREFIX + appKey, infolist, TIME_OUT_SEC);
    }

    public List<AppContentVersionInfo> getAppContentVersionList(String appKey) {
        Object infoList = manager.get(CACHE_KEY_CONTENT_VERSION_PREFIX + appKey);
        if (infoList == null) {
            return null;
        }
        return (List<AppContentVersionInfo>) infoList;
    }

    public boolean deleteAppContentVersion(String appKey) {
        return manager.remove(CACHE_KEY_CONTENT_VERSION_PREFIX + appKey);
    }

    ////////////////////////////////////////////////////////////
    private String generatorAppButtomMenuKey(String appkey, long pid) {
        return CACHE_KEY_JOYME_APP_BUTTOM_MENU + appkey + "_" + pid;
    }

    public boolean deleteJoymeAppMenu(String appKey, long pid) {
        return manager.remove(generatorAppButtomMenuKey(appKey, pid));
    }

    /////////////////////////////////////////////
    private String generatorAppTopMenuKey(String appkey) {
        return CACHE_KEY_JOYME_APP_TOP_MENU + appkey;
    }

    public void putJoymeAppTopMenu(String appKey, List<JoymeAppTopMenu> joymeAppMenu) {
        manager.put(generatorAppTopMenuKey(appKey), joymeAppMenu, TIME_OUT_SEC);
    }

    public List<JoymeAppTopMenu> getJoymeAppTopMenu(String appKey) {
        Object cachedObj = manager.get(generatorAppTopMenuKey(appKey));
        if (cachedObj == null) {
            return null;
        }

        return (List<JoymeAppTopMenu>) cachedObj;
    }

    public boolean deleteJoymeAppTopMenu(String appKey) {
        return manager.remove(generatorAppTopMenuKey(appKey));
    }

    /////////////////////////////////////////////////////
    private String generatorActivityTopMenuKey(int platform) {
        return CACHE_KEY_ACTIVITY_TOP_MENU + platform;
    }

    public void putActivityTopMenu(int platform, List<ActivityTopMenu> activityTopMenuList) {
        manager.put(generatorActivityTopMenuKey(platform), activityTopMenuList, TIME_OUT_SEC);
    }

    public List<ActivityTopMenu> getActivityTopMenu(int platform) {
        Object cachedObj = manager.get(generatorActivityTopMenuKey(platform));
        if (cachedObj == null) {
            return null;
        }
        return (List<ActivityTopMenu>) cachedObj;
    }

    public boolean deleteActivityTopMenu(int platform) {
        return manager.remove(generatorActivityTopMenuKey(platform));
    }

    /////////////////////////////////////////////////////////////////////////////
    private String generatorClientTopMenuKey(int platform) {
        return CACHE_KEY_CLIENT_TOP_MENU + platform;
    }

    public boolean deleteClientTopMenu(int platform) {
        return manager.remove(generatorClientTopMenuKey(platform));
    }

    ///////////////////////////////menu tags cache//////////////////////////////////////////////
    public void putMenuTag(JoymeAppMenuTag tag) {
        List<JoymeAppMenuTag> tagList = getMenuTags(tag.getTopMenuId());

        if (tagList == null) {
            tagList = new ArrayList<JoymeAppMenuTag>();
        }
        tagList.add(tag);

        manager.put(getMenu2TagCacheKey(tag.getTopMenuId()), tagList, TIME_OUT_SEC);
    }

    public void putMenuTags(long menuId, List<JoymeAppMenuTag> tags) {

        manager.put(getMenu2TagCacheKey(menuId), tags, TIME_OUT_SEC);
    }

    public List<JoymeAppMenuTag> getMenuTags(long topMenuId) {
        Object cachedObj = manager.get(getMenu2TagCacheKey(topMenuId));
        if (cachedObj == null) {
            return null;
        }
        return (List<JoymeAppMenuTag>) cachedObj;
    }

    public boolean removeMenuTags(long topMenuId) {
        return manager.remove(getMenu2TagCacheKey(topMenuId));
    }

    private String getMenu2TagCacheKey(long menuId) {
        return section + CACHE_KEY_MENUID_2_TAG + menuId;
    }

    public void putMenusByTagId(long tagId, List<JoymeAppMenu> menu) {


        manager.put(getTagId2MenuCacheKey(tagId), menu, TIME_OUT_SEC);
    }

    public List<JoymeAppMenu> getMenusByTagId(long tagId) {
        Object cachedObj = manager.get(getTagId2MenuCacheKey(tagId));
        if (cachedObj == null) {
            return null;
        }
        return (List<JoymeAppMenu>) cachedObj;
    }

    public boolean removeMenusByTagId(long tagId) {
        return manager.remove(getTagId2MenuCacheKey(tagId));
    }

    private String getTagId2MenuCacheKey(long tagId) {
        return section + CACHE_KEY_TAGID_2_MENU + tagId;
    }

    ////////////////////////////////////////////////////
    public void putJoymeAppTopNews(String appKey, List<JoymeAppTopNews> joymeAppTopNewses) {
        manager.put(CACHE_KEY_JOYME_APP_TOP_NEWS + appKey, joymeAppTopNewses, TIME_OUT_SEC);
    }

    public List<JoymeAppTopNews> getJoymeAppTopNews(String appKey) {
        Object cachedObj = manager.get(CACHE_KEY_JOYME_APP_TOP_NEWS + appKey);
        if (cachedObj == null) {
            return null;
        }
        return (List<JoymeAppTopNews>) cachedObj;
    }

    public boolean deleteJoymeAppTopNews(String appKey) {
        return manager.remove(CACHE_KEY_JOYME_APP_TOP_NEWS + appKey);
    }

    public void putAppTips(String appKey, int platform, AppTips lastAppTips) {
        manager.put(section + CACHE_KEY_JOYME_APP_TIPS + appKey + platform, lastAppTips, TIME_OUT_SEC);
    }

    public AppTips getAppTips(String appKey, int platform) {
        Object appTips = manager.get(section + CACHE_KEY_JOYME_APP_TIPS + appKey + platform);
        if (appTips == null) {
            return null;
        }
        return (AppTips) appTips;
    }

    public boolean removeAppTips(String appKey, int platform) {
        return manager.remove(section + CACHE_KEY_JOYME_APP_TIPS + appKey + platform);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<JoymeAppMenu> getJoymeAppMenuListByCache(String appKey, long pid, Integer module) {
        Object menu = manager.get(section + CACHE_KEY_JOYME_APP_MENU_MODULE + appKey + "_" + pid + "_" + module);
        if (menu == null) {
            return null;
        }
        return (List<JoymeAppMenu>) menu;
    }

    public void putJoymeAppMenuListByCache(String appKey, long pid, Integer module, List<JoymeAppMenu> menu) {
        manager.put(section + CACHE_KEY_JOYME_APP_MENU_MODULE + appKey + "_" + pid + "_" + module, menu, 24l * 60l * 60l);
    }

    public void deleteJoymeAppMenuListByCahe(String appKey, long pid, Integer module) {
        manager.remove(section + CACHE_KEY_JOYME_APP_MENU_MODULE + appKey + "_" + pid + "_" + module);
    }

    ////////////////////////
    public void putAppConfig(String configId, AppConfig appConfig) {
        manager.put(section + CACHE_KEY_APP_CONFIG + configId, appConfig, TIME_OUT_SEC);
    }

    public AppConfig getAppConfig(String configId) {
        Object config = manager.get(section + CACHE_KEY_APP_CONFIG + configId);
        if (config == null) {
            return null;
        }
        return (AppConfig) config;
    }


    public void removeAppConfig(String configId) {
        manager.remove(section + CACHE_KEY_APP_CONFIG + configId);
    }
    ////////////////////////////////

    /////////////////////////////////////////////////////////////
    public void putAppChannel(long channelId, AppChannel appChannel) {
        manager.put(CACHE_KEY_JOYME_APP_CHANNEL + channelId, appChannel, TIME_OUT_SEC);
    }

    public AppChannel getAppChannel(long channelId) {
        Object returnObj = manager.get(CACHE_KEY_JOYME_APP_CHANNEL + channelId);
        if (returnObj == null) {
            return null;
        }
        return (AppChannel) returnObj;
    }

    public boolean deleteAppChannel(long channelId) {
        return manager.remove(CACHE_KEY_JOYME_APP_CHANNEL + channelId);
    }

    ////////////////////////////////////////////////////////////


    public void putActivityTopMenuList(String activitytopmenulinekey, List<ActivityTopMenu> list) {
        manager.put(CACHE_KEY_ACTIVITY_TOP_MENU_ACTIVITYTOPMENULINEKEY + activitytopmenulinekey, list, TIME_OUT_SEC);
    }

    public List<ActivityTopMenu> getActivityTopMenuList(String activitytopmenulinekey) {
        Object infoList = manager.get(CACHE_KEY_ACTIVITY_TOP_MENU_ACTIVITYTOPMENULINEKEY + activitytopmenulinekey);
        if (infoList == null) {
            return null;
        }
        return (List<ActivityTopMenu>) infoList;
    }

    public boolean deleteActivityTopMenuList(String activitytopmenulinekey) {
        return manager.remove(CACHE_KEY_ACTIVITY_TOP_MENU_ACTIVITYTOPMENULINEKEY + activitytopmenulinekey);
    }
}
