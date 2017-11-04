package com.enjoyf.platform.service.ask.wiki;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2017-3-22 0022.
 */
public class ContentLineType implements Serializable {

    private static Map<Integer, ContentLineType> map = new HashMap<Integer, ContentLineType>();

    //CMS文章
    public static final ContentLineType CONTENTLINE_ARCHIVE = new ContentLineType(1);

    //WIKI
    public static final ContentLineType CONTENTLINE_WIKI = new ContentLineType(2);

    private int code;

    public ContentLineType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ContentLineType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ContentLineType) o).code) return false;

        return true;
    }

    public static ContentLineType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ContentLineType> getAll() {
        return map.values();
    }
}
