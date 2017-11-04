package com.enjoyf.platform.service.gameres.privilege;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-12
 * Time: 下午3:26
 * To change this template use File | Settings | File Templates.
 */
public class PrivilegeCategory implements Serializable {
    //todo rename
    private static Map<Integer, PrivilegeCategory> map = new HashMap<Integer, PrivilegeCategory>();

    public static final PrivilegeCategory PRIVILEGE = new PrivilegeCategory(1);//权限
    public static final PrivilegeCategory ROLE = new PrivilegeCategory(2);//角色

    private int code;

    public PrivilegeCategory() {
    }

    public PrivilegeCategory(int code) {
        this.code = code;
        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    public static PrivilegeCategory getByCode(int code) {
        return new PrivilegeCategory(code);
    }

    public static Collection<PrivilegeCategory> getAll() {
        return map.values();
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        if (code != ((PrivilegeCategory) o).code)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PrivilegeType:code" + code;
    }
}
