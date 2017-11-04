/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.userprops;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class UserPropDomain implements Serializable {
    //
    private static Map<String, UserPropDomain> map = new HashMap<String, UserPropDomain>();

    //
    private String code;

    //the default user props domain..
    public static UserPropDomain DEFAULT = new UserPropDomain("default");

    ///////////////////////////////////////////////////////////////
    private UserPropDomain(String c) {
        code = c.toLowerCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public static UserPropDomain getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return DEFAULT;
        }

        if (map.containsKey(c.toLowerCase())) {
            return map.get(c.toLowerCase());
        } else {
            return DEFAULT;
        }
    }

    public static Collection<UserPropDomain> getAll() {
        return map.values();
    }

    ///////////////////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public int hashCode() {
        return code.hashCode();
    }

    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof UserPropDomain)) {
            return false;
        }

        return code.equalsIgnoreCase(((UserPropDomain) obj).getCode());
    }
}
