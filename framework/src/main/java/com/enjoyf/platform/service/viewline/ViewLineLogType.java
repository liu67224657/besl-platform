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
public class ViewLineLogType implements Serializable {
    //全自动，自动填充+手动审核，无自动。
    private static Map<String, ViewLineLogType> map = new HashMap<String, ViewLineLogType>();

    //the auto fill types.
    public static final ViewLineLogType BAIKE_HISTORY = new ViewLineLogType("baike.history");


    //
    private String code;

    private ViewLineLogType(String c) {
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
        return !((obj == null) || !(obj instanceof ViewLineLogType)) && code.equalsIgnoreCase(((ViewLineLogType) obj).getCode());

    }

    public static ViewLineLogType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ViewLineLogType> getAll() {
        return map.values();
    }
}
