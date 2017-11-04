package com.enjoyf.webapps.joyme.weblogic.point;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-8-2 下午2:48
 * Description:
 */
public class PointReduceType {
       private static Map<Integer, PointReduceType> map = new HashMap<Integer, PointReduceType>();

    public static final PointReduceType OUT_LIMIT = new PointReduceType(0);
    public static final PointReduceType NOT_LIMIT = new PointReduceType(1);
    public static final PointReduceType HAS_LIMIT = new PointReduceType(2);

    private int code;

    public PointReduceType(int code) {
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


        if (code != ((PointReduceType) o).code) return false;

        return true;
    }

    public static PointReduceType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<PointReduceType> getAll() {
        return map.values();
    }
}
