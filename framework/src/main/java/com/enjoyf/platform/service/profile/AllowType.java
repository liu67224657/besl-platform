package com.enjoyf.platform.service.profile;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-8-27
 * Time: 下午6:52
 * Desc:  是否允许他人(投稿，私信，回信)\n‘N’表示不允许他人(投稿，私信，回信)；\n‘A’表示允许所有人(投稿，私信，回信)；\n‘C’表示设定条件的人允许(投稿，私信，回信)
 */
public class AllowType implements Serializable {

    private static Map<String, AllowType> map = new HashMap<String, AllowType>();

    /**‘N’表示不允许他人(投稿，私信，回信)*/
    public static final AllowType NO_ALLOW = new AllowType("N");
    /**‘A’表示允许所有人发送(投稿，私信，回信)*/
    public static final AllowType A_ALLOW = new AllowType("A");
    /**‘C’表示设定条件的人允许发送(投稿，私信，回信)*/
    public static final AllowType C_ALLOW = new AllowType("C");


    private String code;

    private AllowType(String c) {
        code = c.toLowerCase();
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof AllowType) {
            return code.equalsIgnoreCase(((AllowType) obj).getCode());
        } else {
            return false;
        }
    }
    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "AllowType: code=" + code;
    }

    public static AllowType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }
        return map.get(c.toLowerCase());
    }

    public static Collection getAll() {
        return map.values();
    }

}
