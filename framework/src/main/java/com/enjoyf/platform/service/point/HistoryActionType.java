package com.enjoyf.platform.service.point;

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
public class HistoryActionType implements Serializable {
    private static Map<Integer, HistoryActionType> map = new HashMap<Integer, HistoryActionType>();
    //积分操作
    public static final HistoryActionType POINT = new HistoryActionType(0);
    //声望操作
    public static final HistoryActionType PRESTIGE = new HistoryActionType(1);




    private int code;

    public HistoryActionType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "HistoryActionType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((HistoryActionType) o).code) return false;

        return true;
    }

    public static HistoryActionType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<HistoryActionType> getAll() {
        return map.values();
    }
}
