package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 上午10:01
 * Description:
 */
public class AppTipsType implements Serializable {

    private static Map<Integer, AppTipsType> map = new HashMap<Integer, AppTipsType>();

    //
    public static final AppTipsType DEFAULT = new AppTipsType(0);

    private int code;

    private AppTipsType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AppDisplayType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AppTipsType) o).code) return false;

        return true;
    }

    public static AppTipsType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AppTipsType> getAll() {
        return map.values();
    }
}
