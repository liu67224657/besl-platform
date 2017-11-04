package com.enjoyf.platform.service.ask;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/10
 * Description:
 */
public class ItemType implements Serializable {
    private static Map<Integer, ItemType> map = new HashMap<Integer, ItemType>();

    public static final ItemType QUESTION = new ItemType(1);    //提问
    public static final ItemType ANSWER = new ItemType(2);      //回答
    public static final ItemType ADVERTISE = new ItemType(3);      //广告

    private int code;

    public ItemType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "SumType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ItemType) o).code) return false;

        return true;
    }

    public static ItemType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ItemType> getAll() {
        return map.values();
    }
}
