/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.search;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class SearchConstants {
    public static final String SERVICE_SECTION = "searchservice";
    public static final String SERVICE_PREFIX = "searchserver";
    public static final String SERVICE_TYPE = "searchserver";
    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public static final byte RECIEVE_EVENT = 1;

    public static final byte SEARCH_CONTENTS_BY_TEXT = 2;//搜索博文列表
    public static final byte SEARCH_BLOGS_BY_TEXT = 3;//搜索博客列表
    public static final byte SEARCH_ALL_BY_TEXT = 4;//搜索博客列表
    public static final byte SEARCH_GAME = 5;//搜索博客列表
    public static final byte SEARCH_GROUP = 6;//搜索博客列表
    public static final byte SEARCH_GAMEDB = 7;//搜索游戏资料库
    public static final byte SEARCH_GIFT = 8;//搜索游戏礼包
    public static final byte SEARCH_CONTENT_DEL_BY_UNO_CONTENTID = 10;//删除博文索引
    public static final byte SEARCH_PROFILE_DEL_BY_UNO = 11;//删除profile索引

    public static final byte SOLR_SEARCH_PROFILE = 12;
    public static final byte SOLR_CREATE_PROFILE = 13;

    public static final byte REDIS_LPUSH = 14;

    static {
        transProfileContainer.put(new TransProfile(RECIEVE_EVENT, "RECIEVE_EVENT"));
        transProfileContainer.put(new TransProfile(SEARCH_CONTENTS_BY_TEXT, "SEARCH_CONTENTS_BY_TEXT"));
        transProfileContainer.put(new TransProfile(SEARCH_BLOGS_BY_TEXT, "SEARCH_BLOGS_BY_TEXT"));
        transProfileContainer.put(new TransProfile(SEARCH_GAME, "SEARCH_GAME"));
        transProfileContainer.put(new TransProfile(SEARCH_GROUP, "SEARCH_GROUP"));
        transProfileContainer.put(new TransProfile(SEARCH_PROFILE_DEL_BY_UNO, "SEARCH_PROFILE_DEL_BY_UNO"));
        transProfileContainer.put(new TransProfile(SEARCH_CONTENT_DEL_BY_UNO_CONTENTID, "SEARCH_CONTENT_DEL_BY_UNO_CONTENTID"));
        transProfileContainer.put(new TransProfile(SEARCH_PROFILE_DEL_BY_UNO, "SEARCH_PROFILE_DEL_BY_UNO"));
        transProfileContainer.put(new TransProfile(SEARCH_GAMEDB, "SEARCH_GAMEDB"));
        transProfileContainer.put(new TransProfile(SEARCH_GIFT, "SEARCH_GIFT"));

        transProfileContainer.put(new TransProfile(SOLR_SEARCH_PROFILE, "SOLR_SEARCH_PROFILE"));
        transProfileContainer.put(new TransProfile(SOLR_CREATE_PROFILE, "SOLR_CREATE_PROFILE"));
        transProfileContainer.put(new TransProfile(REDIS_LPUSH, "REDIS_LPUSH"));
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
