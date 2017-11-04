package com.enjoyf.platform.service.point;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午2:07
 * To change this template use File | Settings | File Templates.
 */
public class ConsumeTimesType implements Serializable {

    private static Map<Integer, ConsumeTimesType> map = new HashMap<Integer, ConsumeTimesType>();
    //永久一次
    public static final ConsumeTimesType ONETIMESMANYDAY = new ConsumeTimesType(1);
    //一天一次
    public static final ConsumeTimesType ONETIMESADAY = new ConsumeTimesType(2);
    //不受限制
    public static final ConsumeTimesType MANYTIMESADAY = new ConsumeTimesType(3);


    private int code;

    public ConsumeTimesType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ConsumeTimesType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ConsumeTimesType) o).code) return false;

        return true;
    }

    public static ConsumeTimesType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ConsumeTimesType> getAll() {
        return map.values();
    }
}
