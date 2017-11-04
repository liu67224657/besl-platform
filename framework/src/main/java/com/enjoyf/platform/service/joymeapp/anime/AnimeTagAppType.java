package com.enjoyf.platform.service.joymeapp.anime;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli
 * Date: 2014/12/12
 * Time: 17:39
 */
public class AnimeTagAppType implements Serializable {
    //content, profile, gameres
    private static Map<Integer, AnimeTagAppType> map = new HashMap<Integer, AnimeTagAppType>();

    //the content
    public static final AnimeTagAppType ANIME = new AnimeTagAppType(1);//大动漫
    public static final AnimeTagAppType SYHB = new AnimeTagAppType(2);//手游画报
    public static final AnimeTagAppType SYHB_MIYOU = new AnimeTagAppType(3);//手游画报--迷友
    public static final AnimeTagAppType SYHB_QUANZI = new AnimeTagAppType(4);//玩霸2.2圈子

    public static final AnimeTagAppType WANBA_ASK = new AnimeTagAppType(5);//玩霸问答标签
    public static final AnimeTagAppType WANBA_VERIFYPROFILE = new AnimeTagAppType(6);//玩霸达人标签
    public static final AnimeTagAppType WANBA_ACTIVITY = new AnimeTagAppType(7);//玩霸活动标签

    private int code;

    private AnimeTagAppType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AppPlatform: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AnimeTagAppType) o).code) return false;

        return true;
    }

    public static AnimeTagAppType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AnimeTagAppType> getAll() {
        return map.values();
    }


}