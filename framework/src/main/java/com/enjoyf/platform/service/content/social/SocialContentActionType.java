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
public class SocialContentActionType implements Serializable {

    private static Map<Integer, SocialContentActionType> map = new HashMap<Integer, SocialContentActionType>();

    public static final SocialContentActionType AGREE = new SocialContentActionType(1);

    private int code;

    public SocialContentActionType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "GoodsType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((SocialContentActionType) o).code) return false;

        return true;
    }

    public static SocialContentActionType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<SocialContentActionType> getAll() {
        return map.values();
    }
}
