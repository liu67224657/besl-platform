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
public class AppAnimeIndexLinkType implements Serializable {

    private static Map<Integer, AppAnimeIndexLinkType> map = new HashMap<Integer, AppAnimeIndexLinkType>();

    //native
    public static final AppAnimeIndexLinkType NATIVE = new AppAnimeIndexLinkType(1);

    //wap
    public static final AppAnimeIndexLinkType WAP = new AppAnimeIndexLinkType(2);

    private int code;

    private AppAnimeIndexLinkType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AppAnimeIndexLinkType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AppAnimeIndexLinkType) o).code) return false;

        return true;
    }

    public static AppAnimeIndexLinkType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AppAnimeIndexLinkType> getAll() {
        return map.values();
    }
}
