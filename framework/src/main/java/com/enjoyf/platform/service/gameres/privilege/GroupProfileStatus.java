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
public class GroupProfileStatus implements Serializable {
    private static Map<Integer, GroupProfileStatus> map = new HashMap<Integer, GroupProfileStatus>();

    public static final GroupProfileStatus DEFAULT = new GroupProfileStatus(1); //正常
    public static final GroupProfileStatus NO_SPEAK = new GroupProfileStatus(2); //禁言
    public static final GroupProfileStatus KICKED_OUT = new GroupProfileStatus(3); //踢出
    public static final GroupProfileStatus EXIT = new GroupProfileStatus(4); //主动退出

    private int code;

    public GroupProfileStatus() {
    }

    public GroupProfileStatus(int code) {
        this.code = code;
        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    public static GroupProfileStatus getByCode(int code) {
        return new GroupProfileStatus(code);
    }

    public static Collection<GroupProfileStatus> getAll() {
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
        if (code != ((GroupProfileStatus) o).code)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PrivilegeType:code" + code;
    }
}
