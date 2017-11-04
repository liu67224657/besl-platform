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
public class AppVersionUpdateType implements Serializable {

    private static Map<Integer, AppVersionUpdateType> map = new HashMap<Integer, AppVersionUpdateType>();

    //非强制更新
    public static final AppVersionUpdateType DEFAULT = new AppVersionUpdateType(0);
    //强制更新
    public static final AppVersionUpdateType FORCE = new AppVersionUpdateType(1);
    //
    private int code;

    private AppVersionUpdateType(int c) {
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


        if (code != ((AppVersionUpdateType) o).code) return false;

        return true;
    }

    public static AppVersionUpdateType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AppVersionUpdateType> getAll() {
        return map.values();
    }
}
