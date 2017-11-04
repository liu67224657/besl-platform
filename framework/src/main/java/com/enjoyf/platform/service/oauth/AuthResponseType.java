package com.enjoyf.platform.service.oauth;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Yin Pengyi
 * Date: 11-11-07
 * Time: 13:24
 * Desc:
 */
public class AuthResponseType implements Serializable {
    //
    private static Map<String, AuthResponseType> map = new HashMap<String, AuthResponseType>();

    /**
     * token
     */
    public static final AuthResponseType TOKEN = new AuthResponseType("token");

    /**
     * code
     */
    public static final AuthResponseType CODE = new AuthResponseType("code");

    //
    private String code;

    //
    private AuthResponseType(String c) {
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

        if (obj instanceof AuthResponseType) {
            return code.equalsIgnoreCase(((AuthResponseType) obj).getCode());
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
        return "AuthResponseType: code=" + code;
    }

    public static AuthResponseType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection getAll() {
        return map.values();
    }

}
