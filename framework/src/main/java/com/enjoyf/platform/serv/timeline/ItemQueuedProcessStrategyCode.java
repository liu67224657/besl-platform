/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.timeline;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>  ,zx
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class ItemQueuedProcessStrategyCode implements Serializable {
    private static Map<String, ItemQueuedProcessStrategyCode> map = new HashMap<String, ItemQueuedProcessStrategyCode>();

    //the domain is fav item
    public static final ItemQueuedProcessStrategyCode PROCESS_ITEM_FAV = new ItemQueuedProcessStrategyCode("fav");
    public static final ItemQueuedProcessStrategyCode PROCESS_ITEM_DEF = new ItemQueuedProcessStrategyCode("def");

    private String code;

    public ItemQueuedProcessStrategyCode(String c) {
        code = c.toLowerCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "TimeLineDomain: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ItemQueuedProcessStrategyCode)) {
            return false;
        }

        return code.equalsIgnoreCase(((ItemQueuedProcessStrategyCode) obj).getCode());
    }

    public static ItemQueuedProcessStrategyCode getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ItemQueuedProcessStrategyCode> getAll() {
        return map.values();
    }

}
