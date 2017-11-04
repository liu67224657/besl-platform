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
public class ViewLineLogDomain implements Serializable {
    //全自动，自动填充+手动审核，无自动。
    private static Map<String, ViewLineLogDomain> map = new HashMap<String, ViewLineLogDomain>();

    //the auto fill types.
    //auto fill and auto confirm
    public static final ViewLineLogDomain CATEGORY = new ViewLineLogDomain("category");

    //auto fill but no confirm
    public static final ViewLineLogDomain LINE = new ViewLineLogDomain("line");

    //
    private String code;

    private ViewLineLogDomain(String c) {
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
        return !((obj == null) || !(obj instanceof ViewLineLogDomain)) && code.equalsIgnoreCase(((ViewLineLogDomain) obj).getCode());

    }

    public static ViewLineLogDomain getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ViewLineLogDomain> getAll() {
        return map.values();
    }
}
