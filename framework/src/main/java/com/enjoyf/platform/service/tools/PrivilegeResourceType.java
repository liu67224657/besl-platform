package com.enjoyf.platform.service.tools;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-11-23
 * Time: 上午10:20
 * Desc: 资源类型
 */
public class PrivilegeResourceType implements Serializable {

    private static Map<Integer, PrivilegeResourceType> map = new LinkedHashMap<Integer, PrivilegeResourceType>();
    //the initialize status
    public static PrivilegeResourceType MENU = new PrivilegeResourceType(new Integer(1)); //菜单
    public static PrivilegeResourceType URL = new PrivilegeResourceType(new Integer(2)); //URL

    private Integer code;

    private PrivilegeResourceType(Integer code) {
        this.code = code;
        map.put(this.code, this);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "PrivilegeResourceType code:" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof PrivilegeResourceType) {
            return code.equals(((PrivilegeResourceType) obj).getCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static PrivilegeResourceType getByCode(Integer i) {
        if (i==null || i == 0) {
            return null;
        }

        return map.get(i);
    }

    public static Collection<PrivilegeResourceType> getAll() {
        return map.values();
    }
}
