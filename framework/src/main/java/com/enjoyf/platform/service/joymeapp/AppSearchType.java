package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-1
 * Time: 下午3:06
 * To change this template use File | Settings | File Templates.
 */
public class AppSearchType implements Serializable {
    private static Map<Integer, AppSearchType> map = new HashMap<Integer, AppSearchType>();

    //游戏名称
    public static final AppSearchType GAME_NAME = new AppSearchType(1);
    //标签名称
    public static final AppSearchType TAG_NAME = new AppSearchType(2);
//    //游戏评价
//    public static final AppSearchType GAME_APPRAISE = new AppSearchType(3);

    private int code;

    public AppSearchType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AppSearchType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AppSearchType) o).code) return false;

        return true;
    }

    public static AppSearchType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AppSearchType> getAll() {
        return map.values();
    }
}
