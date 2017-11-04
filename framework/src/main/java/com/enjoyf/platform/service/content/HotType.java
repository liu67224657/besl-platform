package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.StringUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:热点文章类型
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class HotType implements Serializable{
     private static Map<String, HotType> map = new HashMap<String, HotType>();

    //the original type.
    public static final HotType HOT = new HotType("hot");


    private String code;

    private HotType(String c) {
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
        return "ContentPublishType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof HotType)) {
            return false;
        }

        return code.equalsIgnoreCase(((HotType) obj).getCode());
    }

    public static HotType getByCode(String c) {
        if (StringUtil.isEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<HotType> getAll() {
        return map.values();
    }
}
