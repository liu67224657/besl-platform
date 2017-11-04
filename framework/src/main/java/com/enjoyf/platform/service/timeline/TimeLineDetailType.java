/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.timeline;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class TimeLineDetailType implements Serializable{
    private static Map<String, TimeLineDetailType> map = new HashMap<String, TimeLineDetailType>();

     //the domain is fav item
    public static final TimeLineDetailType ITEM_DETAILTYPE_FAV = new TimeLineDetailType("fav");


    private String code;

    public TimeLineDetailType(String c) {
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
        if ((obj == null) || !(obj instanceof TimeLineDetailType)) {
            return false;
        }

        return code.equalsIgnoreCase(((TimeLineDetailType) obj).getCode());
    }

    public static TimeLineDetailType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<TimeLineDetailType> getAll() {
        return map.values();
    }
}
