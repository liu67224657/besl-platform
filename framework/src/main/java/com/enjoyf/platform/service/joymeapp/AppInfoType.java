package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-27
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class AppInfoType implements Serializable {
    private static Map<Integer, AppInfoType> map = new HashMap<Integer, AppInfoType>();

    public static final AppInfoType GAME = new AppInfoType(0);
    public static final AppInfoType APP = new AppInfoType(1);
    public static final AppInfoType UNKONWN = new AppInfoType(-1);

    private int code;

    public AppInfoType(int code) {
        this.code = code;

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


        if (code != ((AppInfoType) o).code) return false;

        return true;
    }

    public static AppInfoType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AppInfoType> getAll() {
        return map.values();
    }
}
