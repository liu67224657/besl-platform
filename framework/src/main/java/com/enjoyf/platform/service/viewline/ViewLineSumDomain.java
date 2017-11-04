/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description:
 */
public class ViewLineSumDomain implements Serializable {
    //全自动，自动填充+手动审核，无自动。
    private static Map<String, ViewLineSumDomain> map = new HashMap<String, ViewLineSumDomain>();

    //the auto fill types.
    //auto fill and auto confirm
    public static final ViewLineSumDomain CATEGORY = new ViewLineSumDomain("category");

    //auto fill but no confirm
    public static final ViewLineSumDomain LINE = new ViewLineSumDomain("line");

    //
    private String code;

    private ViewLineSumDomain(String c) {
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
        return "ViewLineSumDomain: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        return !((obj == null) || !(obj instanceof ViewLineSumDomain)) && code.equalsIgnoreCase(((ViewLineSumDomain) obj).getCode());

    }

    public static ViewLineSumDomain getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ViewLineSumDomain> getAll() {
        return map.values();
    }
}
