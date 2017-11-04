package com.enjoyf.webapps.joyme.weblogic.giftmarket;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-26
 * Time: 下午5:33
 * To change this template use File | Settings | File Templates.
 */
public class AllowExchangeStatus {
    private static Map<Integer, AllowExchangeStatus> map = new HashMap<Integer, AllowExchangeStatus>();
    //不允许
    public static final AllowExchangeStatus NO_ALLOW = new AllowExchangeStatus(0);
    //可以领取
    public static final AllowExchangeStatus ALLOW = new AllowExchangeStatus(1);
    //永久一次
    public static final AllowExchangeStatus HAS_EXCHANGED = new AllowExchangeStatus(-1);
    //一天一次
    public static final AllowExchangeStatus HAS_EXCHANGED_BY_DAY = new AllowExchangeStatus(-2);
    //时间间隔
    public static final AllowExchangeStatus HAS_EXCHANGED_BY_INTRVAL = new AllowExchangeStatus(-3);

    private int code;

    public AllowExchangeStatus(int code) {
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


        if (code != ((AllowExchangeStatus) o).code) return false;

        return true;
    }

    public static AllowExchangeStatus getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AllowExchangeStatus> getAll() {
        return map.values();
    }
}
