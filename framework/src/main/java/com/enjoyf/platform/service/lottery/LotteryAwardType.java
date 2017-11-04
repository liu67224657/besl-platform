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
public class LotteryAwardType implements Serializable {
    private static Map<Integer, LotteryAwardType> map = new HashMap<Integer, LotteryAwardType>();
    //实物商品
    public static final LotteryAwardType GOODS = new LotteryAwardType(1);
    //虚拟商品
    public static final LotteryAwardType VIRTUAL = new LotteryAwardType(0);

    //抽奖机会
    public static final LotteryAwardType POINT = new LotteryAwardType(2);

    private int code;

    public LotteryAwardType(int code) {
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


        if (code != ((LotteryAwardType) o).code) return false;

        return true;
    }

    public static LotteryAwardType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<LotteryAwardType> getAll() {
        return map.values();
    }
}
