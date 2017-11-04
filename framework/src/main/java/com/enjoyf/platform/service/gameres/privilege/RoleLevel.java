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
public class RoleLevel implements Serializable {
    private static Map<Integer, RoleLevel> map = new HashMap<Integer, RoleLevel>();
    public static final RoleLevel CREATOR = new RoleLevel(1);  //创建者
    public static final RoleLevel LEADER = new RoleLevel(2);  //组长
    public static final RoleLevel SECOND_LEADER = new RoleLevel(3);  //小组长
    public static final RoleLevel MEMBER = new RoleLevel(4);  //组员

    //todo add more PrivilegeType

    private int code;

    public RoleLevel() {
    }

    public RoleLevel(int code) {
        this.code = code;
        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    public static RoleLevel getByCode(int code) {
        return new RoleLevel(code);
    }

    public static Collection<RoleLevel> getAll() {
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
        if (code != ((RoleLevel) o).code)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PrivilegeType:code" + code;
    }
}
