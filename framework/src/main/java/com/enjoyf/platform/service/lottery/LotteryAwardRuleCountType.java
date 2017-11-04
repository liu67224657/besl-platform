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
public class LotteryAwardRuleCountType implements Serializable {

    private static Map<Integer, LotteryAwardRuleCountType> map = new HashMap<Integer, LotteryAwardRuleCountType>();

    //按天
    public static final LotteryAwardRuleCountType DATE = new LotteryAwardRuleCountType(1);

    //按小时
    public static final LotteryAwardRuleCountType HOUR = new LotteryAwardRuleCountType(2);

    //自定义
    public static final LotteryAwardRuleCountType CUSTOM = new LotteryAwardRuleCountType(3);

    //无限制
    public static final LotteryAwardRuleCountType INFINITE = new LotteryAwardRuleCountType(4);


    private int code;

    public LotteryAwardRuleCountType(int code) {
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


        if (code != ((LotteryAwardRuleCountType) o).code) return false;

        return true;
    }

    public static LotteryAwardRuleCountType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<LotteryAwardRuleCountType> getAll() {
        return map.values();
    }
}
