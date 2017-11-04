package com.enjoyf.platform.service.tools;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-11-23
 * Time: 上午10:36
 * Desc:
 */
public class PrivilegeResourceLevel implements Serializable {
    private static Map<Integer, PrivilegeResourceLevel> map = new LinkedHashMap<Integer, PrivilegeResourceLevel>();
    //the initialize status
    public static PrivilegeResourceLevel LEVEL1 = new PrivilegeResourceLevel(new Integer(1)); //1
    public static PrivilegeResourceLevel LEVEL2 = new PrivilegeResourceLevel(new Integer(2)); //2
    public static PrivilegeResourceLevel LEVEL3 = new PrivilegeResourceLevel(new Integer(3)); //3
    public static PrivilegeResourceLevel LEVEL4 = new PrivilegeResourceLevel(new Integer(4)); //4

    private Integer code;

    private PrivilegeResourceLevel(Integer code) {
        this.code = code;
        map.put(this.code, this);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "PrivilegeResourceLevel code:" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof PrivilegeResourceLevel) {
            return code.equals(((PrivilegeResourceLevel) obj).getCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static PrivilegeResourceLevel getByCode(Integer i) {
        if (i == null || i == 0) {
            return null;
        }

        return map.get(i);
    }

    public static Collection<PrivilegeResourceLevel> getAll() {
        return map.values();
    }
}
