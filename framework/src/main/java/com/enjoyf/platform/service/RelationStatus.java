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
public class RelationStatus implements Serializable {
    private static Map<Integer, RelationStatus> map = new HashMap<Integer, RelationStatus>();

    public static final RelationStatus UNFOCUS = new RelationStatus(0);    //未关注
    public static final RelationStatus FOCUS = new RelationStatus(1);      //已关注
    public static final RelationStatus EACH_FOCUS = new RelationStatus(2);  //互相关注

    private int code;

    public RelationStatus(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "RelationStatus: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((RelationStatus) o).code) return false;

        return true;
    }

    public static RelationStatus getByCode(int c) {
        return map.get(c);
    }

    public static Collection<RelationStatus> getAll() {
        return map.values();
    }
}
