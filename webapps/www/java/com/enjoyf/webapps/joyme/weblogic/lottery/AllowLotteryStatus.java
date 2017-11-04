package com.enjoyf.webapps.joyme.weblogic.lottery;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-7-5
 * Time: 下午12:09
 * To change this template use File | Settings | File Templates.
 */
public class AllowLotteryStatus {
       private static Map<Integer, AllowLotteryStatus> map = new HashMap<Integer, AllowLotteryStatus>();

    public static final AllowLotteryStatus NO_ALLOW = new AllowLotteryStatus(0);
    public static final AllowLotteryStatus ALLOW = new AllowLotteryStatus(1);
    public static final AllowLotteryStatus HAS_LOTTERY = new AllowLotteryStatus(-1);
    public static final AllowLotteryStatus HAS_LOTTERY_TODAY = new AllowLotteryStatus(-2);
    public static final AllowLotteryStatus NOT_CHANCE = new AllowLotteryStatus(-3);

    private int code;

    public AllowLotteryStatus(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AllowLotteryStatus) o).code) return false;

        return true;
    }

    public static AllowLotteryStatus getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AllowLotteryStatus> getAll() {
        return map.values();
    }
}
