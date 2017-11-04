package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli
 * Date: 2014/12/12
 * Time: 14:36
 */
public class AnimeSpecialDisplayType implements Serializable {
    private static Map<Integer, AnimeSpecialDisplayType> map = new HashMap<Integer, AnimeSpecialDisplayType>();

    //小图
    public static final AnimeSpecialDisplayType SMALL_PIC = new AnimeSpecialDisplayType(1);
    //大图
    public static final AnimeSpecialDisplayType BIG_PIC = new AnimeSpecialDisplayType(2);

    //
    private int code;

    private AnimeSpecialDisplayType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AnimeSpecialDisplayType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AnimeSpecialDisplayType) o).code) return false;

        return true;
    }

    public static AnimeSpecialDisplayType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AnimeSpecialDisplayType> getAll() {
        return map.values();
    }
}
