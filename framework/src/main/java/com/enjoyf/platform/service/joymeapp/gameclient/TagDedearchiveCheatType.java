package com.enjoyf.platform.service.joymeapp.gameclient;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2015/6/10.
 */
public class TagDedearchiveCheatType implements Serializable {

    private static Map<Integer, TagDedearchiveCheatType> map = new HashMap<Integer, TagDedearchiveCheatType>();

    //the content
    public static final TagDedearchiveCheatType WANBA = new TagDedearchiveCheatType(0);//玩霸
    public static final TagDedearchiveCheatType YOUKU = new TagDedearchiveCheatType(1);//优酷

    private int code;

    private TagDedearchiveCheatType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "code: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((TagDedearchiveCheatType) o).code) return false;

        return true;
    }

    public static TagDedearchiveCheatType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<TagDedearchiveCheatType> getAll() {
        return map.values();
    }
}