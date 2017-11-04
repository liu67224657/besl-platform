package com.enjoyf.platform.service.point;

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
public class GoodsType implements Serializable {
    private static Map<Integer, GoodsType> map = new HashMap<Integer, GoodsType>();
    //实物商品
    public static final GoodsType GOODS = new GoodsType(0);
    //虚拟商品
    public static final GoodsType VIRTUAL = new GoodsType(1);

    private int code;

    public GoodsType(int code) {
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


        if (code != ((GoodsType) o).code) return false;

        return true;
    }

    public static GoodsType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<GoodsType> getAll() {
        return map.values();
    }
}
