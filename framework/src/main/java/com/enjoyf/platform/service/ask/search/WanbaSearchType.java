package com.enjoyf.platform.service.ask.search;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/10
 * Description:
 */
public class WanbaSearchType implements Serializable {
    private static Map<Integer, WanbaSearchType> map = new HashMap<Integer, WanbaSearchType>();

    public static final WanbaSearchType QUESION = new WanbaSearchType(1);
    public static final WanbaSearchType PROFILE = new WanbaSearchType(2);

    private int code;

    public WanbaSearchType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "QuestionActionType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((WanbaSearchType) o).code) return false;

        return true;
    }

    public static WanbaSearchType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<WanbaSearchType> getAll() {
        return map.values();
    }
}
