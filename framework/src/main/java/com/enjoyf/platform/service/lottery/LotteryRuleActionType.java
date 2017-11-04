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
public class LotteryRuleActionType implements Serializable {

    private static Map<Integer, LotteryRuleActionType> map = new HashMap<Integer, LotteryRuleActionType>();

    //随机数
    public static final LotteryRuleActionType RANDOM = new LotteryRuleActionType(1);

    //时间戳
    public static final LotteryRuleActionType TIMESTAMP = new LotteryRuleActionType(2);

    //次数
    public static final LotteryRuleActionType TIMES = new LotteryRuleActionType(3);

    private int code;

    public LotteryRuleActionType(int code) {
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


        if (code != ((LotteryRuleActionType) o).code) return false;

        return true;
    }

    public static LotteryRuleActionType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<LotteryRuleActionType> getAll() {
        return map.values();
    }
}
