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
public class SocialTagType implements Serializable {

    private static Map<Integer, SocialTagType> map = new HashMap<Integer, SocialTagType>();

    //兴趣
    public static final SocialTagType INTEREST = new SocialTagType(1);
    //位置
    public static final SocialTagType LOCATION = new SocialTagType(2);
    //活动
    public static final SocialTagType ACTIVITY = new SocialTagType(3);

    private int code;

    public SocialTagType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "SocialTagType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((SocialTagType) o).code) return false;

        return true;
    }

    public static SocialTagType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<SocialTagType> getAll() {
        return map.values();
    }
}
