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
public class TimeLineDomain implements Serializable{
    private static Map<String, TimeLineDomain> map = new HashMap<String, TimeLineDomain>();

    //the home page timeline
    public static final TimeLineDomain HOME = new TimeLineDomain("home");

    //the blog timeline
    public static final TimeLineDomain BLOG = new TimeLineDomain("blog");

    //the replyme timeline
    public static final TimeLineDomain REPLYME = new TimeLineDomain("replyme");

    //the my reply timeline
    public static final TimeLineDomain MYREPLY = new TimeLineDomain("myreply");

    //the my reply timeline
    public static final TimeLineDomain ATME = new TimeLineDomain("atme");

    private String code;

    public TimeLineDomain(String c) {
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
        if ((obj == null) || !(obj instanceof TimeLineDomain)) {
            return false;
        }

        return code.equalsIgnoreCase(((TimeLineDomain) obj).getCode());
    }

    public static TimeLineDomain getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<TimeLineDomain> getAll() {
        return map.values();
    }
}
