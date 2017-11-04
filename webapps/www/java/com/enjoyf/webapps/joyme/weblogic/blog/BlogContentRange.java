package com.enjoyf.webapps.joyme.weblogic.blog;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:博客属性修改的类
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class BlogContentRange implements Serializable {
    private static Map<String, BlogContentRange> map = new HashMap<String, BlogContentRange>();

    //ALL 所有人 CHIOCE 允许设置条件的人 默认为ALL
    public static final BlogContentRange BlOG_RANGE_ALL = new BlogContentRange("A");
    public static final BlogContentRange BlOG_RANGE_CHIOCE = new BlogContentRange("C");

    private String code;

    private BlogContentRange(String code) {
        this.code = code;
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static BlogContentRange getByCode(String code) {
        return map.get(code);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BlogContentRange)) {
            return false;
        }

        return code.equalsIgnoreCase(((BlogContentRange) obj).getCode());
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
