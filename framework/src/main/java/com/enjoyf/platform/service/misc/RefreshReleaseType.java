package com.enjoyf.platform.service.misc;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Desc: RefreshReleaseType
 */
public class RefreshReleaseType implements Serializable {

    private static Map<Integer, RefreshReleaseType> map = new LinkedHashMap<Integer, RefreshReleaseType>();
    //the initialize status
    public static RefreshReleaseType ONE_TIME = new RefreshReleaseType(new Integer(1)); //单次
    public static RefreshReleaseType EVERY_DAYE = new RefreshReleaseType(new Integer(2)); //每天

    private Integer code;

    private RefreshReleaseType(Integer code) {
        this.code = code;
        map.put(this.code, this);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "RefreshReleaseType code:" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof RefreshReleaseType) {
            return code.equals(((RefreshReleaseType) obj).getCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static RefreshReleaseType getByCode(Integer i) {
        if (i == null || i == 0) {
            return null;
        }

        return map.get(i);
    }

    public static Collection<RefreshReleaseType> getAll() {
        return map.values();
    }
}
