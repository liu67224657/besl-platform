package com.enjoyf.platform.service.joymeapp.anime;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 下午3:26
 * To change this template use File | Settings | File Templates.
 */
public class AnimeTagType implements Serializable {
    //content, profile, gameres
    private static Map<Integer, AnimeTagType> map = new HashMap<Integer, AnimeTagType>();

    //the content
    //手游画报2 里面 1是非默认 2是默认
    public static final AnimeTagType PALY_ING = new AnimeTagType(1);//播放中
    public static final AnimeTagType PLAY_OVER = new AnimeTagType(2);//完结


    private int code;

    private AnimeTagType(int c) {
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


        if (code != ((AnimeTagType) o).code) return false;

        return true;
    }

    public static AnimeTagType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AnimeTagType> getAll() {
        return map.values();
    }


}
