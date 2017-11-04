package com.enjoyf.platform.service.ask;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class TimeRelseaseType implements Serializable {
    private static Map<Integer, TimeRelseaseType> map = new HashMap<Integer, TimeRelseaseType>();

    public static final TimeRelseaseType QUESTION = new TimeRelseaseType(1);    //提问
    public static final TimeRelseaseType ACTIVITY = new TimeRelseaseType(2);      //活动

    private int code;

    public TimeRelseaseType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "TimeRelseaseType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((TimeRelseaseType) o).code) return false;

        return true;
    }

    public static TimeRelseaseType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<TimeRelseaseType> getAll() {
        return map.values();
    }
}
