package com.enjoyf.platform.service.ask;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class WanbaProfileClassifyType implements Serializable {
    private static Map<Integer, WanbaProfileClassifyType> map = new HashMap<Integer, WanbaProfileClassifyType>();

    public static final WanbaProfileClassifyType WANBA_ASK_VIRTUAL = new WanbaProfileClassifyType(1);    //虚拟用户


    private int code;

    public WanbaProfileClassifyType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AskVirtualType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((WanbaProfileClassifyType) o).code) return false;

        return true;
    }

    public static WanbaProfileClassifyType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<WanbaProfileClassifyType> getAll() {
        return map.values();
    }
}
