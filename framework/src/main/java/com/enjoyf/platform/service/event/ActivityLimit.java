package com.enjoyf.platform.service.event;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-5
 * Time: 下午4:48
 * To change this template use File | Settings | File Templates.
 */
public class ActivityLimit implements Serializable{
    private static Map<Integer, ActivityLimit> map = new HashMap<Integer, ActivityLimit>();

    public static final ActivityLimit AWARD_LIMIT_DAY = new ActivityLimit(1);
    public static final ActivityLimit AWARD_LIMIT_NULL = new ActivityLimit(2);


    private Integer code;

    public ActivityLimit(Integer c) {
        this.code = c;
        map.put(code, this);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "ActivityLimit: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ActivityLimit)) {
            return false;
        }

         return code.equals(((ActivityLimit) obj).getCode());
    }

    public static ActivityLimit getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ActivityLimit> getAll() {
        return map.values();
    }
}
