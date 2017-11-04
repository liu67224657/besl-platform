package com.enjoyf.platform.service.ask;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/10
 * Description:
 */
public class CollectType implements Serializable {
    private static Map<Integer, CollectType> map = new HashMap<Integer, CollectType>();

    public static final CollectType CMS = new CollectType(1);    //文章
    public static final CollectType WIKI = new CollectType(2);      //WIKI

    private int code;

    public CollectType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "QuestionType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((CollectType) o).code) return false;

        return true;
    }

    public static CollectType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<CollectType> getAll() {
        return map.values();
    }
}
