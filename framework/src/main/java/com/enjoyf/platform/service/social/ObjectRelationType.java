package com.enjoyf.platform.service.social;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/12
 * Description:
 */
public class ObjectRelationType implements Serializable {
    private static Map<Integer, ObjectRelationType> map = new HashMap<Integer, ObjectRelationType>();

    public static final ObjectRelationType GAME = new ObjectRelationType(0);
    public static final ObjectRelationType TOPIC = new ObjectRelationType(1);
    public static final ObjectRelationType WIKI = new ObjectRelationType(3);
    public static final ObjectRelationType COLLECT_WIKI_PAGE = new ObjectRelationType(5);

    //for user relation
    public static final ObjectRelationType PROFILE = new ObjectRelationType(2);
    public static final ObjectRelationType WIKI_PROFILE = new ObjectRelationType(4);

    private int code;

    public ObjectRelationType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ObjectRelationType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ObjectRelationType) o).code) return false;

        return true;
    }

    public static ObjectRelationType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ObjectRelationType> getAll() {
        return map.values();
    }
}
