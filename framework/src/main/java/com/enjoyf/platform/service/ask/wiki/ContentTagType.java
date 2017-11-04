package com.enjoyf.platform.service.ask.wiki;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2017-3-22 0022.
 */
public class ContentTagType implements Serializable {

    private static Map<Integer, ContentTagType> map = new HashMap<Integer, ContentTagType>();

    //CMS文章
    public static final ContentTagType ARCHIVE = new ContentTagType(1);

    //wap页面
    public static final ContentTagType WAP = new ContentTagType(2);

    private int code;

    public ContentTagType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ContentTagType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ContentTagType) o).code) return false;

        return true;
    }

    public static ContentTagType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ContentTagType> getAll() {
        return map.values();
    }
}
