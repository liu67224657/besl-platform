package com.enjoyf.platform.service.ask.wiki;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2017-3-22 0022.
 */
public class ContentLineOwn implements Serializable {

    private static Map<Integer, ContentLineOwn> map = new HashMap<Integer, ContentLineOwn>();

    //CMS所有文章
    public static final ContentLineOwn ALL_ARCHIVE = new ContentLineOwn(1);

    //游戏下所有文章
    public static final ContentLineOwn GAME_ALL_ARCHIVE = new ContentLineOwn(2);

    //今天发的所有文章
    public static final ContentLineOwn TODAY_ALL_ARCHIVE = new ContentLineOwn(3);

    private int code;

    public ContentLineOwn(int code) {
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


        if (code != ((ContentLineOwn) o).code) return false;

        return true;
    }

    public static ContentLineOwn getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ContentLineOwn> getAll() {
        return map.values();
    }
}
