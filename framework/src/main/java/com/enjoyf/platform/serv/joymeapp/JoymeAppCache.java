/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.joymeapp;

import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTV;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.ShakeItem;
import com.enjoyf.platform.service.joymeapp.gameclient.*;
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

    private static final long TIME_OUT_SEC_30 = 30l;

    private static final String CACHE_KEY_CONTENT_VERSION_PREFIX = "app_conent_version_";

    private static final String CACHE_KEY_CLIENT_TOP_MENU = "joyme_app_client_top_menu_";

    private static final String CACHE_KEY_JOYME_SOCIAL_SHARE = "_joyme_social_share_";

    private static final String CACHE_ANIME_TAG = "_joyme_app_anime_tag_";

    private static final String CACHE_ANIME_TV = "_joyme_app_anime_tv_";

    private static final String CACHE_ANIME_TAG_DEDEARCHIVES = "_joyme_app_anime_tag_dedearchives";

    private static final String CACHE_ANIME_TAG_DEDEARCHIVE_CHEAT = "_joyme_app_anime_tag_dedearchive_cheat";

    private static final String CACHE_KEY_VALIDATE = "_validate";

    private static final String CACHE_KEY_PIC_INFO_LIST = "_pic_info_list";

    private static final String TAG_DEDEARCHIVES_CACHE = "_tag_dedearchives_";


    private MemCachedManager manager;

    private String section;

    JoymeAppCache(MemCachedConfig config) {
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
    private String generatorClientTopMenuKey(int platform) {
        return CACHE_KEY_CLIENT_TOP_MENU + platform;
    }

    public void putClientTopMenu(int platform, List<ActivityTopMenu> clientTopMenuList) {
        manager.put(generatorClientTopMenuKey(platform), clientTopMenuList, TIME_OUT_SEC);
    }

    public List<ActivityTopMenu> getClientTopMenu(int platform) {
        Object cachedObj = manager.get(generatorClientTopMenuKey(platform));
        if (cachedObj == null) {
            return null;
        }
        return (List<ActivityTopMenu>) cachedObj;
    }

    public boolean deleteClientTopMenu(int platform) {
        return manager.remove(generatorClientTopMenuKey(platform));
    }

    public List<SocialShare> getSocialShare(String appkey, int platform, int code, long activityid) {
        Object cachedObj = manager.get(CACHE_KEY_JOYME_SOCIAL_SHARE + appkey + platform + code + activityid);
        if (cachedObj == null) {
            return null;
        }
        return (List<SocialShare>) cachedObj;
    }

    public void putSocialShare(String appkey, int platform, int code, long activityid, List<SocialShare> socialShareList) {
        manager.put(CACHE_KEY_JOYME_SOCIAL_SHARE + appkey + platform + code + activityid, socialShareList, TIME_OUT_SEC);
    }

    public void deleteSocialShare(String appkey, int platform, int code, long activityid) {
        manager.remove(CACHE_KEY_JOYME_SOCIAL_SHARE + appkey + platform + code + activityid);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public AnimeTag getAnimeTagtByTagid(Long tagid) {
        Object menu = manager.get(section + CACHE_ANIME_TAG + tagid);
        if (menu == null) {
            return null;
        }
        return (AnimeTag) menu;
    }

    public void putAnimeTag(Long tagid, AnimeTag animeTag) {
        manager.put(section + CACHE_ANIME_TAG + tagid, animeTag, TIME_OUT_SEC);
    }

    public void deleteAnimeTagByTagid(Long tagid) {
        manager.remove(section + CACHE_ANIME_TAG + tagid);
    }

    /////////////
    public List<AnimeTV> getAnimeTVListByTagid(Long tagid) {
        Object menu = manager.get(section + CACHE_ANIME_TV + tagid);
        if (menu == null) {
            return null;
        }
        return (List<AnimeTV>) menu;
    }

    public void putAnimeTVList(Long tagid, List<AnimeTV> animeTVList) {
        manager.put(section + CACHE_ANIME_TV + tagid, animeTVList, TIME_OUT_SEC_30);
    }

    public void deleteAnimeTVListByTagid(Long tagid) {
        manager.remove(section + CACHE_ANIME_TV + tagid);
    }


    ////////////////
    public List<TagDedearchives> getTagDedearchivesListByPage(Long tagid, Integer platform, int curpage) {
        Object menu = manager.get(section + CACHE_ANIME_TAG_DEDEARCHIVES + tagid + platform + curpage);
        if (menu == null) {
            return null;
        }
        return (List<TagDedearchives>) menu;
    }

    public void putTagDedearchivesList(Long tagid, Integer platform, int curpage, List<TagDedearchives> tagDedearchivesList) {
        manager.put(section + CACHE_ANIME_TAG_DEDEARCHIVES + tagid + platform + curpage, tagDedearchivesList, TIME_OUT_SEC_30);
    }

    public void deleteTagDedearchivesListByPage(Long tagid, Integer platform, int curpage) {
        manager.remove(section + CACHE_ANIME_TAG_DEDEARCHIVES + tagid + platform + curpage);
    }
    ////////////////////


    ////////////////
    public TagDedearchiveCheat getTagDedearchiveCheat(Long dede_archives_id) {
        Object menu = manager.get(section + CACHE_ANIME_TAG_DEDEARCHIVE_CHEAT + dede_archives_id);
        if (menu == null) {
            return null;
        }
        return (TagDedearchiveCheat) menu;
    }

    public void putTagDedearchiveCheat(Long dede_archives_id, TagDedearchiveCheat cheat) {
        manager.put(section + CACHE_ANIME_TAG_DEDEARCHIVE_CHEAT + dede_archives_id, cheat, TIME_OUT_SEC_30);
    }

    public void deleteTagDedearchiveCheat(Long dede_archives_id) {
        manager.remove(section + CACHE_ANIME_TAG_DEDEARCHIVE_CHEAT + dede_archives_id);
    }

    ////////////////////////////////
    public void putValidateInfo(String key, int value) {
        manager.put(CACHE_KEY_VALIDATE + key, value, 60l * 60l * 120l);
    }

    public int getValidateInfo(String key) {
        Object pool = manager.get(CACHE_KEY_VALIDATE + key);
        if (pool == null) {
            return 0;
        }
        return (Integer) pool;
    }

    ////////////////////////////////////////////////
    public void putPicInfoList(String key, List<ImgDTO> pics) {
        manager.put(CACHE_KEY_PIC_INFO_LIST + key, pics, -1);
    }

    public List<ImgDTO> getPicInfoList(String key) {
        Object pool = manager.get(CACHE_KEY_PIC_INFO_LIST + key);
        if (pool == null) {
            return null;
        }
        return (List<ImgDTO>) pool;
    }

    public TagDedearchives getTagDedearchivesCache(long gameId, ArchiveRelationType gameRelation, String archiveId) {
        Object obj = manager.get(JoymeAppConstants.SERVICE_SECTION + TAG_DEDEARCHIVES_CACHE + gameId + "_" + gameRelation.getCode() + "_" + archiveId);
        if (obj == null) {
            return null;
        }
        return (TagDedearchives) obj;
    }

    public void putTagDedearchivesCache(long gameId, ArchiveRelationType gameRelation, String archiveId, TagDedearchives tagDedearchives) {
        manager.put(JoymeAppConstants.SERVICE_SECTION + TAG_DEDEARCHIVES_CACHE + gameId + "_" + gameRelation.getCode() + "_" + archiveId, tagDedearchives, 60l * 60l);
    }

    public boolean removeTagDedearchivesCache(long gameId, ArchiveRelationType gameRelation, String archiveId) {
        return manager.remove(JoymeAppConstants.SERVICE_SECTION + TAG_DEDEARCHIVES_CACHE + gameId + "_" + gameRelation.getCode() + "_" + archiveId);
    }
}
