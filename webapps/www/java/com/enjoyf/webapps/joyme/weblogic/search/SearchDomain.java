/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.joyme.weblogic.search;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@enjoyfound.com">Eric Liu</a>
 * Create time: 11-8-17 下午1:23
 * Description:搜索的domain根据domain调用不同的搜索策略
 */
public class SearchDomain implements Serializable {
    private static Map<String, SearchDomain> map = new HashMap<String, SearchDomain>();

    //全部
    public static final SearchDomain RECENT = new SearchDomain("recent");
    public static final SearchDomain HOT = new SearchDomain("hot");
    public static final SearchDomain FOCUS = new SearchDomain("focus");
    public static final SearchDomain GUIDE = new SearchDomain("guide");
    public static final SearchDomain HELP = new SearchDomain("help");
    public static final SearchDomain JOKE = new SearchDomain("joke");

    //
    private String code;

    public SearchDomain(String c) {
        code = c.toLowerCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "SearchDomain: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof SearchDomain)) {
            return false;
        }

        return code.equalsIgnoreCase(((SearchDomain) obj).getCode());
    }

    public static SearchDomain getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<SearchDomain> getAll() {
        return map.values();
    }
}
