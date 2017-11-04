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
public class AuthGrantType implements Serializable {
    //
    private static Map<String, AuthGrantType> map = new HashMap<String, AuthGrantType>();

    /**
     * password
     */
    public static final AuthGrantType PASSWORD = new AuthGrantType("password");

    /**
     * authorization code
     */
    public static final AuthGrantType AUTHORIZATION_CODE = new AuthGrantType("authorization_code");

    /**
     * refresh token
     */
    public static final AuthGrantType REFRESH_TOKEN = new AuthGrantType("refresh_token");

    //
    private String code;

    //
    private AuthGrantType(String c) {
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

        if (obj instanceof AuthGrantType) {
            return code.equalsIgnoreCase(((AuthGrantType) obj).getCode());
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
        return "AuthGrantType: code=" + code;
    }

    public static AuthGrantType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection getAll() {
        return map.values();
    }

}
