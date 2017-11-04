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
public class ChooseType implements Serializable {
    private static Map<Integer, ChooseType> map = new HashMap<Integer, ChooseType>();
    //兑换商品
    public static final ChooseType NO = new ChooseType(0);
    //秒杀商品
    public static final ChooseType YES = new ChooseType(1);

    //特殊 可用于短期活动
    public static final ChooseType OTHER = new ChooseType(2);

    private int code;

    public ChooseType(int code) {
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


        if (code != ((ChooseType) o).code) return false;

        return true;
    }

    public static ChooseType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ChooseType> getAll() {
        return map.values();
    }
}
