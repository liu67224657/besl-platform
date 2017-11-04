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
public class GroupPrivilegeCode implements Serializable {
    private static Map<String, GroupPrivilegeCode> map = new HashMap<String, GroupPrivilegeCode>();

    public static final GroupPrivilegeCode CLASSIC = new GroupPrivilegeCode("classic");  //加精
    public static final GroupPrivilegeCode TOP = new GroupPrivilegeCode("top"); //置顶
    public static final GroupPrivilegeCode LIGHT = new GroupPrivilegeCode("light"); //高亮
    public static final GroupPrivilegeCode SHIELD = new GroupPrivilegeCode("shield"); //屏蔽

    public static final GroupPrivilegeCode BAN = new GroupPrivilegeCode("ban"); //封禁
    public static final GroupPrivilegeCode KICKED = new GroupPrivilegeCode("kicked"); //踢出
    public static final GroupPrivilegeCode APPROVED = new GroupPrivilegeCode("approved"); //通过审核
    public static final GroupPrivilegeCode NO_APPROVED = new GroupPrivilegeCode("closure"); //拒绝审核
    public static final GroupPrivilegeCode NO_BAN = new GroupPrivilegeCode("noban"); //解禁

    public static final GroupPrivilegeCode GROUP_MANAGE = new GroupPrivilegeCode("group"); //添加/删除 小组长

    public static final GroupPrivilegeCode LEADER_MANAGE = new GroupPrivilegeCode("leader"); //添加/删除 组长
    public static final GroupPrivilegeCode SECOND_LEADER_MANAGE = new GroupPrivilegeCode("secondleader"); //添加/删除 小组长

    public static final GroupPrivilegeCode POST = new GroupPrivilegeCode("post"); //发帖
    public static final GroupPrivilegeCode REPLY = new GroupPrivilegeCode("reply"); //回复
    //add more PrivilegeCode

    private String code;

    public GroupPrivilegeCode() {
    }

    public GroupPrivilegeCode(String code) {
        this.code = code;
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public static GroupPrivilegeCode getByCode(String code) {
        return new GroupPrivilegeCode(code);
    }

    public static Collection<GroupPrivilegeCode> getAll() {
        return map.values();
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        if (code != ((GroupPrivilegeCode) o).code)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PrivilegeType:code" + code;
    }
}
