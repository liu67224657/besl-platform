package com.enjoyf.platform.service.lottery;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 */
public class LotteryItemResultType implements Serializable {

    private static Map<Integer, LotteryItemResultType> map = new LinkedHashMap<Integer, LotteryItemResultType>();
    //the initialize status


    public static LotteryItemResultType NO_CHANCE_LOTTERY = new LotteryItemResultType(new Integer(-4));//无抽奖机会
    public static LotteryItemResultType NOT_LOTTERY = new LotteryItemResultType(new Integer(-3));//无活动
    public static LotteryItemResultType HAS_END = new LotteryItemResultType(new Integer(-2));//已结束
    public static LotteryItemResultType NOT_START = new LotteryItemResultType(new Integer(-1));//未开始
    public static LotteryItemResultType FAILED = new LotteryItemResultType(new Integer(0));//失败
    public static LotteryItemResultType SUCCESS = new LotteryItemResultType(new Integer(1));//成功

    private Integer code;

    private LotteryItemResultType(Integer code) {
        this.code = code;
        map.put(this.code, this);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "GoodItemType code:" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof LotteryItemResultType) {
            return code.equals(((LotteryItemResultType) obj).getCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static LotteryItemResultType getByCode(Integer i) {
        if (i == null || i == 0) {
            return null;
        }

        return map.get(i);
    }

    public static Collection<LotteryItemResultType> getAll() {
        return map.values();
    }
}
