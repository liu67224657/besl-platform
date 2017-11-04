package com.enjoyf.platform.service.gameres;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-27
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class GroupValidStatus implements Serializable {
    private static Map<Integer, GroupValidStatus> map = new HashMap<Integer, GroupValidStatus>();

    public static final GroupValidStatus INIT = new GroupValidStatus(0);
    public static final GroupValidStatus VALID = new GroupValidStatus(1);
    public static final GroupValidStatus REMOVE = new GroupValidStatus(2);
    //todo
    public static final GroupValidStatus INVALID = new GroupValidStatus(3);

    private int code;

    public GroupValidStatus(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "GroupValidStatus: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((GroupValidStatus) o).code) return false;

        return true;
    }

    public static GroupValidStatus getByCode(int c) {
        return map.get(c);
    }

    public static Collection<GroupValidStatus> getAll() {
        return map.values();
    }
}
