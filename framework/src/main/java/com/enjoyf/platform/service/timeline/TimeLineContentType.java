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
public class TimeLineContentType implements Serializable {
    private static Map<String, TimeLineContentType> map = new HashMap<String, TimeLineContentType>();

    //the content
    public static final TimeLineContentType CONTENT = new TimeLineContentType("content");

    //the content reply
    public static final TimeLineContentType REPLY = new TimeLineContentType("reply");

    private String code;

    public TimeLineContentType(String c) {
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
        if ((obj == null) || !(obj instanceof TimeLineContentType)) {
            return false;
        }

        return code.equalsIgnoreCase(((TimeLineContentType) obj).getCode());
    }

    public static TimeLineContentType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<TimeLineContentType> getAll() {
        return map.values();
    }
}
