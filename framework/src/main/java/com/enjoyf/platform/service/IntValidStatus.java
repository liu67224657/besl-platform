package com.enjoyf.platform.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/10
 * Description:
 */
public class IntValidStatus implements Serializable {
    private static Map<Integer, IntValidStatus> map = new HashMap<Integer, IntValidStatus>();

    public static final IntValidStatus UNVALID = new IntValidStatus(0);    //未关注
    public static final IntValidStatus VALID = new IntValidStatus(1);      //已关注
    public static final IntValidStatus VALIDING = new IntValidStatus(2);

    private int code;

    public IntValidStatus(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "IntValidStatus: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((IntValidStatus) o).code) return false;

        return true;
    }

    public static IntValidStatus getByCode(int c) {
        return map.get(c);
    }

    public static Collection<IntValidStatus> getAll() {
        return map.values();
    }
}
