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
public class UserTimelineDomain implements Serializable {
    private static Map<String, UserTimelineDomain> map = new HashMap<String, UserTimelineDomain>();

    //我的
    public static final UserTimelineDomain MY = new UserTimelineDomain("my");

    //好友
    public static final UserTimelineDomain FRIEND = new UserTimelineDomain("friend");

    //
    private String code;

    public UserTimelineDomain(String c) {
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
        if ((obj == null) || !(obj instanceof UserTimelineDomain)) {
            return false;
        }

        return code.equalsIgnoreCase(((UserTimelineDomain) obj).getCode());
    }

    public static UserTimelineDomain getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<UserTimelineDomain> getAll() {
        return map.values();
    }
}
