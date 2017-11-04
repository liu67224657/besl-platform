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
public class LotteryRuleChanceType implements Serializable {

    private static Map<Integer, LotteryRuleChanceType> map = new HashMap<Integer, LotteryRuleChanceType>();

    //按天
    public static final LotteryRuleChanceType BYDAY = new LotteryRuleChanceType(1);

    //永久
    public static final LotteryRuleChanceType PERMANENT = new LotteryRuleChanceType(2);

    //无限制
    public static final LotteryRuleChanceType INFINITE = new LotteryRuleChanceType(3);//无限制


    private int code;

    public LotteryRuleChanceType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "LotteryChanceType : code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((LotteryRuleChanceType) o).code) return false;

        return true;
    }

    public static LotteryRuleChanceType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<LotteryRuleChanceType> getAll() {
        return map.values();
    }
}
