package com.enjoyf.platform.serv.social;

import com.enjoyf.platform.service.social.RecommendType;
import com.enjoyf.platform.service.social.SocialRecommend;
import com.enjoyf.platform.service.social.SocialRelationAtFocus;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.collection.MemDiskCacheConfig;
import com.enjoyf.platform.util.collection.MemDiskCacheManager;
import net.sf.ehcache.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-10-17
 * Time: 上午11:35
 * To change this template use File | Settings | File Templates.
 */
public class SocialCache extends MemDiskCacheManager {
    //map max size
    private static final int AT_FOUCS_MAX_SIZE = 1000;
    //cache at focus (search by nick)
    private static final String KEY_CACHE_FOCUS_NAME = "focus";
    private static final String KEY_CACHE_RECOMMEND_NAME = "recommend";

    public SocialCache(MemDiskCacheConfig config) {
        super(config);
    }

    //the at foucs apis:
    //get focus map
    public Map<String, SocialRelationAtFocus> getSocialRelationFocus(String mapKey) {
        Element element = getCache(KEY_CACHE_FOCUS_NAME).get(mapKey);
        if (element != null) {
            return (Map<String, SocialRelationAtFocus>) element.getObjectValue();
        } else {
            return null;
        }
    }

    //add focus map item
    public void putSocialRelationFocusMapItem(String mapKey, SocialRelationAtFocus value) {
        Map<String, SocialRelationAtFocus> map = (Map) getCache(KEY_CACHE_FOCUS_NAME).get(mapKey);
        if (map.size() < AT_FOUCS_MAX_SIZE) {
            map.put(value.getUno(), value);
        }
        getCache(KEY_CACHE_FOCUS_NAME).put(new Element(mapKey, map));
    }

    //add focus map
    public void putSocialRelationFocusMap(String mapKey, Map<String, SocialRelationAtFocus> map) {
        getCache(KEY_CACHE_FOCUS_NAME).put(new Element(mapKey, map));
    }

    //remove focus map item
    public void removeSocialRelationFoucsMapItem(String mapKey, String mapItemKey) {
        Map<String, SocialRelationAtFocus> map = (Map) getCache(KEY_CACHE_FOCUS_NAME).get(mapKey);
        map.remove(mapItemKey);
        getCache(KEY_CACHE_FOCUS_NAME).put(new Element(mapKey, map));
    }

    //remove focus map
    public boolean removeSocialRelationFocusMap(String key) {
        return getCache(KEY_CACHE_FOCUS_NAME).remove(key);
    }

    public int getSocialCacheMapItemSize() {
        return AT_FOUCS_MAX_SIZE;
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    public Map<RecommendType, SocialRecommend> getSocialRecommend(String mapKey) {
        Element element = getCache(KEY_CACHE_RECOMMEND_NAME).get(mapKey);
        if (element != null) {
            return (Map<RecommendType, SocialRecommend>) element.getObjectValue();
        } else {
            return null;
        }
    }

    public SocialRecommend getSocialRecommend(String mapKey, RecommendType recommendType) {
        Element element = getCache(KEY_CACHE_RECOMMEND_NAME).get(mapKey);
        if (element == null) {
            return null;

        }

        Map<RecommendType, SocialRecommend> recommendMap = (Map<RecommendType, SocialRecommend>) element.getObjectValue();
        return recommendMap.get(recommendType);
    }

    public void putSocialRecommendItem(String mapKey, SocialRecommend value) {

        Map<RecommendType, SocialRecommend> map = null;

        Element element = getCache(KEY_CACHE_RECOMMEND_NAME).get(mapKey);
        if (element == null) {
            map = new HashMap<RecommendType, SocialRecommend>();
        } else {
            map = (Map<RecommendType, SocialRecommend>)element.getObjectValue();
        }

        map.put(value.getRecommendType(), value);
        getCache(KEY_CACHE_RECOMMEND_NAME).put(new Element(mapKey, map));
    }

    //add focus map
    public void putSocialRecommendMap(String mapKey, Map<RecommendType, SocialRecommend> map) {
        getCache(KEY_CACHE_RECOMMEND_NAME).put(new Element(mapKey, map));
    }

    //remove focus map item
    public void removeSocialRecommendMapItem(String mapKey, RecommendType itemKey) {
        Map<RecommendType, SocialRecommend> map = (Map<RecommendType, SocialRecommend>) getCache(KEY_CACHE_RECOMMEND_NAME).get(mapKey);
        if (!CollectionUtil.isEmpty(map)) {
            map.remove(itemKey);
        }
        getCache(KEY_CACHE_RECOMMEND_NAME).put(new Element(mapKey, map));
    }

    //remove focus map
    public boolean removeSocialRecommendMap(String key) {
        return getCache(KEY_CACHE_RECOMMEND_NAME).remove(key);
    }

}
