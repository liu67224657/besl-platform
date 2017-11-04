package com.enjoyf.platform.service.ask;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2016/9/18 0018.
 */
public class WanbaMenuDomain implements Serializable {

    private static Map<Integer, WanbaMenuDomain> map = new HashMap<Integer, WanbaMenuDomain>();

    public static final WanbaMenuDomain HOT = new WanbaMenuDomain(1);    //热门

    private int code;

    public WanbaMenuDomain(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "WanbaMenuDomain: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((WanbaMenuDomain) o).code) return false;

        return true;
    }

    public static WanbaMenuDomain getByCode(int c) {
        return map.get(c);
    }

    public static Collection<WanbaMenuDomain> getAll() {
        return map.values();
    }
}
