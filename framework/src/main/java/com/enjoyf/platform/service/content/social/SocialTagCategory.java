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
public class SocialTagCategory implements Serializable {

    private static Map<Integer, SocialTagCategory> map = new HashMap<Integer, SocialTagCategory>();

    //编辑 后台 添加
    public static final SocialTagCategory ADJUST = new SocialTagCategory(1);
    //用户 自定义
    public static final SocialTagCategory USER_DEFINED = new SocialTagCategory(2);

    private int code;

    public SocialTagCategory(int code) {
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


        if (code != ((SocialTagCategory) o).code) return false;

        return true;
    }

    public static SocialTagCategory getByCode(int c) {
        return map.get(c);
    }

    public static Collection<SocialTagCategory> getAll() {
        return map.values();
    }
}
