package com.enjoyf.platform.service.point;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-1-6
 * Time: 下午6:12
 * To change this template use File | Settings | File Templates.
 */
public class ConsumeType implements Serializable {
    private static Map<String, ConsumeType> map = new HashMap<String, ConsumeType>();

    public static final ConsumeType POINT = new ConsumeType("point");
    public static final ConsumeType FREE = new ConsumeType("free");


    private String code;

    public ConsumeType(String c) {
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
        return "ConsumeType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ConsumeType)) {
            return false;
        }

        return code.equalsIgnoreCase(((ConsumeType) obj).getCode());
    }

    public static ConsumeType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ConsumeType> getAll() {
        return map.values();
    }
}
