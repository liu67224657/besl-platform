package com.enjoyf.platform.service.ask.wiki;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2017-3-22 0022.
 */
public class ContentTagLine implements Serializable {

    private static Map<String, ContentTagLine> map = new HashMap<String, ContentTagLine>();

    //推荐位置
    public static final ContentTagLine RECOMMEND = new ContentTagLine("wiki_recommend");


    private String code;

    public ContentTagLine(String code) {
        this.code = code;

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ContentTagLine: code=" + code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ContentTagLine) o).code) return false;

        return true;
    }

    public static ContentTagLine getByCode(String c) {
        return map.get(c);
    }

    public static Collection<ContentTagLine> getAll() {
        return map.values();
    }
}
