package com.enjoyf.platform.service.misc;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-14
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class IndexCacheType implements Serializable {
    private static Map<Integer, IndexCacheType> map = new HashMap<Integer, IndexCacheType>();

    public static final IndexCacheType PC = new IndexCacheType(1);
    public static final IndexCacheType MOBILE = new IndexCacheType(2);

    private Integer code;

    private IndexCacheType(int i) {
        code = i;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return "GamePropValueType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof IndexCacheType)) {
            return false;
        }

        return code == ((IndexCacheType) obj).getCode();
    }

    public static IndexCacheType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<IndexCacheType> getAll() {
        return map.values();
    }
}
