package com.enjoyf.platform.service.tools;

import com.google.common.base.Strings;

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
public class PrivilegeRoleType implements Serializable {

    private static Map<String, PrivilegeRoleType> map = new LinkedHashMap<String, PrivilegeRoleType>();
    //the initialize status
    public static PrivilegeRoleType ROLE1 = new PrivilegeRoleType("1");
    public static PrivilegeRoleType ROLE2 = new PrivilegeRoleType("2");

    private String code;

    private PrivilegeRoleType(String code) {
        this.code = code.toLowerCase();
        map.put(this.code, this);
    }

    public String getCode() {
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

        if (obj instanceof PrivilegeRoleType) {
            return code == (((PrivilegeRoleType) obj).getCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static PrivilegeRoleType getByCode(String c) {
       if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<PrivilegeRoleType> getAll() {
        return map.values();
    }
}
