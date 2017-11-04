/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.timeline;

import com.enjoyf.platform.util.reflect.ReflectUtil;
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
public class UserTimelineType implements Serializable {
    private static Map<String, UserTimelineType> map = new HashMap<String, UserTimelineType>();

    //玩霸
    public static final UserTimelineType ASK = new UserTimelineType("ask");

    //wiki
    public static final UserTimelineType WIKI = new UserTimelineType("wiki");

    //
    private String code;

    public UserTimelineType(String c) {
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
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof UserTimelineType)) {
            return false;
        }

        return code.equalsIgnoreCase(((UserTimelineType) obj).getCode());
    }

    public static UserTimelineType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<UserTimelineType> getAll() {
        return map.values();
    }
}
