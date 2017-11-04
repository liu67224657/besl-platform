package com.enjoyf.platform.service.content.social;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-15 下午1:29
 * Description:
 */
public class SubscriptType implements Serializable {

    private static Map<Integer, SubscriptType> map = new HashMap<Integer, SubscriptType>();

    //无
    public static final SubscriptType NULL = new SubscriptType(0);
    //热门
    public static final SubscriptType HOT = new SubscriptType(1);
    //最新
    public static final SubscriptType NEW = new SubscriptType(2);

    private int code;

    public SubscriptType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "SubscriptType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((SubscriptType) o).code) return false;

        return true;
    }

    public static SubscriptType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<SubscriptType> getAll() {
        return map.values();
    }
}
