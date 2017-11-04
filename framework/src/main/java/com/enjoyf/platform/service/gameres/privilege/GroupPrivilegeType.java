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
public class GroupPrivilegeType implements Serializable {
    private static Map<Integer, GroupPrivilegeType> map = new HashMap<Integer, GroupPrivilegeType>();

    public static final GroupPrivilegeType CONTENT = new GroupPrivilegeType(1);  //文章管理
    public static final GroupPrivilegeType PROFILE = new GroupPrivilegeType(2);  //成员管理
    public static final GroupPrivilegeType GROUP = new GroupPrivilegeType(3);  //小组管理
    public static final GroupPrivilegeType LEADER = new GroupPrivilegeType(4);  //组长管理
    public static final GroupPrivilegeType POST = new GroupPrivilegeType(5);  //发帖管理

    private int code;

    public GroupPrivilegeType() {
    }

    public GroupPrivilegeType(int code) {
        this.code = code;
        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    public static GroupPrivilegeType getByCode(int code) {
        return new GroupPrivilegeType(code);
    }

    public static Collection<GroupPrivilegeType> getAll() {
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
        if (code != ((GroupPrivilegeType) o).code)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PrivilegeType:code" + code;
    }
}
