package com.enjoyf.platform.service.lottery;

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
public class LotteryType implements Serializable {
    private static Map<Integer, LotteryType> map = new HashMap<Integer, LotteryType>();
    //普通
    public static final LotteryType LOTTERY_TYPE_COMMON = new LotteryType(0);
    //必中
    public static final LotteryType LOTTERY_TYPE_MUST_REAWARD = new LotteryType(1);

    private int code;

    public LotteryType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "LotteryAwardType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((LotteryType) o).code) return false;

        return true;
    }

    public static LotteryType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<LotteryType> getAll() {
        return map.values();
    }
}
