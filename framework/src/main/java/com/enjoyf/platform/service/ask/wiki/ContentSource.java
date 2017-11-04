package com.enjoyf.platform.service.ask.wiki;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2017-3-22 0022.
 */
public class ContentSource implements Serializable {

    private static Map<Integer, ContentSource> map = new HashMap<Integer, ContentSource>();

    //CMS文章
    public static final ContentSource CMS_ARCHIVE = new ContentSource(1);

    //WIKI
    public static final ContentSource WIKI = new ContentSource(2);

    private int code;

    public ContentSource(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ContentSource: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ContentSource) o).code) return false;

        return true;
    }

    public static ContentSource getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ContentSource> getAll() {
        return map.values();
    }
}
