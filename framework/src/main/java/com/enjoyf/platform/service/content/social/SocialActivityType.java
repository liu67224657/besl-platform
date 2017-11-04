package com.enjoyf.platform.service.content.social;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-15 下午1:29
 * Description:
 */
public class SocialActivityType implements Serializable {

    private static Map<Integer, SocialActivityType> map = new HashMap<Integer, SocialActivityType>();

    //文章
    public static final SocialActivityType CONTENT = new SocialActivityType(1);

    //人
    public static final SocialActivityType PROFILE = new SocialActivityType(2);

    private int code;

    public SocialActivityType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "SocialActivityType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((SocialActivityType) o).code) return false;

        return true;
    }

    public static SocialActivityType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<SocialActivityType> getAll() {
        return map.values();
    }
}
