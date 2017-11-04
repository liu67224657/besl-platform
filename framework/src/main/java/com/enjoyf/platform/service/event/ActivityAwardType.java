package com.enjoyf.platform.service.event;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-1
 * Time: 上午11:21
 */
public class ActivityAwardType implements Serializable{
    private static Map<Integer, ActivityAwardType> map = new HashMap<Integer, ActivityAwardType>();

    public static final ActivityAwardType AWARD_LOTTERY = new ActivityAwardType(0);
    public static final ActivityAwardType AWARD_GIFT = new ActivityAwardType(1);


    private Integer code;

    public ActivityAwardType(Integer c) {
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
        return "ActivityAwardType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ActivityAwardType)) {
            return false;
        }

        return code.equals(((ActivityAwardType) obj).getCode());
    }

    public static ActivityAwardType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ActivityAwardType> getAll() {
        return map.values();
    }
}
