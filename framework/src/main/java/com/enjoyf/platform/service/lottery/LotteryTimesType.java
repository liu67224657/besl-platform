package com.enjoyf.platform.service.lottery;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午2:07
 * To change this template use File | Settings | File Templates.
 */
public class LotteryTimesType implements Serializable{

    private static Map<Integer, LotteryTimesType> map = new HashMap<Integer, LotteryTimesType>();

    //永久一次
    public static final LotteryTimesType ONLY_ONE = new LotteryTimesType(1);
    //一天一次 todo by day
    public static final LotteryTimesType BYDAY = new LotteryTimesType(2);
    //不受限制
    public static final LotteryTimesType MANY = new LotteryTimesType(3);


    public static final LotteryTimesType NEED_CHANCE = new LotteryTimesType(4);

    private int code;

    public LotteryTimesType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "LotteryTimesType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((LotteryTimesType) o).code) return false;

        return true;
    }

    public static LotteryTimesType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<LotteryTimesType> getAll() {
        return map.values();
    }
}
