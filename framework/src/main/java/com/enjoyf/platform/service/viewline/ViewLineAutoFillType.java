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
 * Description: line fill 类型，fill工作只是包括添加内容和审核。
 * 任何line都可以排序，任何line都可以进行元素删除。
 */
public class ViewLineAutoFillType implements Serializable {
    //全自动，自动填充+手动审核，无自动。
    private static Map<String, ViewLineAutoFillType> map = new HashMap<String, ViewLineAutoFillType>();

    //the auto fill types.
    //auto fill and auto confirm
    public static final ViewLineAutoFillType AUTO_FULL = new ViewLineAutoFillType("auto.full");

    //auto fill but no confirm
    public static final ViewLineAutoFillType AUTO_NO_CONFIRM = new ViewLineAutoFillType("auto.no.confirm");

    //not auto fill and confirm
    public static final ViewLineAutoFillType AUTO_NONE = new ViewLineAutoFillType("auto.none");

    //
    private String code;

    private ViewLineAutoFillType(String c) {
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
        return "ViewLineAutoFillType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        return !((obj == null) || !(obj instanceof ViewLineAutoFillType)) && code.equalsIgnoreCase(((ViewLineAutoFillType) obj).getCode());

    }

    public static ViewLineAutoFillType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ViewLineAutoFillType> getAll() {
        return map.values();
    }
}
