package com.enjoyf.platform.service.lottery;/**
 * Created by ericliu on 16/6/20.
 */

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/6/20
 */
public class LotteryRuleLimitType implements Serializable {

    private static Map<Integer, LotteryRuleLimitType> map = new HashMap<Integer, LotteryRuleLimitType>();

    //按天
    public static final LotteryRuleLimitType BYDAY = new LotteryRuleLimitType(1);

    //永久
    public static final LotteryRuleLimitType PERMANENT = new LotteryRuleLimitType(2);

    //无限制
    public static final LotteryRuleLimitType INFINITE = new LotteryRuleLimitType(3);//无限制



    private int code;

    public LotteryRuleLimitType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "LotteryLimitType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((LotteryRuleLimitType) o).code) return false;

        return true;
    }

    public static LotteryRuleLimitType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<LotteryRuleLimitType> getAll() {
        return map.values();
    }
}
