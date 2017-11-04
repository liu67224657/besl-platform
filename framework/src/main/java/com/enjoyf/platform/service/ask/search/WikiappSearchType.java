package com.enjoyf.platform.service.ask.search;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2017-3-29 0029.
 */
public class WikiappSearchType implements Serializable {
    private static Map<Integer, WikiappSearchType> map = new HashMap<Integer, WikiappSearchType>();

    public static final WikiappSearchType GAME = new WikiappSearchType(1);
    public static final WikiappSearchType ARCHIVE = new WikiappSearchType(2);

    private int code;

    public WikiappSearchType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "WikiappSearchType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((WikiappSearchType) o).code) return false;

        return true;
    }

    public static WikiappSearchType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<WikiappSearchType> getAll() {
        return map.values();
    }
}
