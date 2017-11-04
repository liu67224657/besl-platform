package com.enjoyf.platform.service.content;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午1:09
 * To change this template use File | Settings | File Templates.
 */
public class ActivityType implements Serializable {
    private static Map<Integer, ActivityType> map = new HashMap<Integer, ActivityType>();

    public static final ActivityType GOODS = new ActivityType(0);
    public static final ActivityType EXCHANGE_GOODS = new ActivityType(1);
//        public static final ActivityType ADVERTISE = new ActivityType(0);
//    public static final ActivityType LOTTERY = new ActivityType(1);
//    public static final ActivityType TICKET = new ActivityType(4);
//    public static final ActivityType GAME_MODULE = new ActivityType(5);
//    public static final ActivityType GAME = new ActivityType(6);
//    public static final ActivityType WIKI = new ActivityType(7);

    private int code;

    public ActivityType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "GoodsType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ActivityType) o).code) return false;

        return true;
    }

    public static ActivityType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ActivityType> getAll() {
        return map.values();
    }
}
