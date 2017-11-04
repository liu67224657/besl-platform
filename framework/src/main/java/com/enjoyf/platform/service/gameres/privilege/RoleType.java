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
public class RoleType implements Serializable {
    private static Map<Integer, RoleType> map = new HashMap<Integer, RoleType>();

    public static final RoleType DEFAULT = new RoleType(1);//默认
    public static final RoleType EXPAND = new RoleType(2); //拓展

    private int code;

    public RoleType() {
    }

    public RoleType(int code) {
        this.code = code;
        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    public static RoleType getByCode(int code) {
        return new RoleType(code);
    }

    public static Collection<RoleType> getAll() {
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
        if (code != ((RoleType) o).code)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PrivilegeType:code" + code;
    }
}
