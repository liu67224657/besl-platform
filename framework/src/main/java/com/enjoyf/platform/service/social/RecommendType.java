/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.social;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@enjoyfound.stuff.com">Liu hao</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class RecommendType implements Serializable {
    private static Map<String, RecommendType> map = new HashMap<String, RecommendType>();

    //关注和粉丝。.
    public static final RecommendType FOLLOW_EACHOTHER = new RecommendType("fe");

    //
    private String code;

    public RecommendType(String c) {
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
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof RecommendType)) {
            return false;
        }

        return code.equalsIgnoreCase(((RecommendType) obj).getCode());
    }

    public static RecommendType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<RecommendType> getAll() {
        return map.values();
    }
}
