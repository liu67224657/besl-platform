package com.enjoyf.webapps.joyme.weblogic.point;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-13
 * Time: 下午4:32
 * To change this template use File | Settings | File Templates.
 */
public class PointResultMsg {
        private static Map<Integer, PointResultMsg> map = new HashMap<Integer, PointResultMsg>();

    public static final PointResultMsg SUCCESS = new PointResultMsg(1);
    public static final PointResultMsg FAILED = new PointResultMsg(0);
    public static final PointResultMsg OUT_OF_POINT_LIMIT = new PointResultMsg(-1);
    public static final PointResultMsg OUT_OF_POINT_TIMES = new PointResultMsg(-2);

    private int code;

    public PointResultMsg(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((PointResultMsg) o).code) return false;

        return true;
    }

    public static PointResultMsg getByCode(int c) {
        return map.get(c);
    }

    public static Collection<PointResultMsg> getAll() {
        return map.values();
    }
}
