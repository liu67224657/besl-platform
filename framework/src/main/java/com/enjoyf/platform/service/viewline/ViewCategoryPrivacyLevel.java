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
 * Description: category privacy type。
 */
public class ViewCategoryPrivacyLevel implements Serializable {
    //
    private static Map<String, ViewCategoryPrivacyLevel> map = new HashMap<String, ViewCategoryPrivacyLevel>();

    //分类
    public static final ViewCategoryPrivacyLevel ONE = new ViewCategoryPrivacyLevel("one");

    //置顶
    public static final ViewCategoryPrivacyLevel TWO = new ViewCategoryPrivacyLevel("two");

    //
    private String code;

    private ViewCategoryPrivacyLevel(String c) {
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
        return "ViewLineType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ViewCategoryPrivacyLevel)) {
            return false;
        }

        return code.equalsIgnoreCase(((ViewCategoryPrivacyLevel) obj).getCode());
    }

    public static ViewCategoryPrivacyLevel getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ViewCategoryPrivacyLevel> getAll() {
        return map.values();
    }
}
