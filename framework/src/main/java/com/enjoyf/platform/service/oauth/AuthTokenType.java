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
public class AuthTokenType implements Serializable {
    //
    private static Map<String, AuthTokenType> map = new HashMap<String, AuthTokenType>();

    /**
     * auth code
     */
    public static final AuthTokenType AUTH = new AuthTokenType("auth");

    /**
     * access token
     */
    public static final AuthTokenType ACCESS = new AuthTokenType("access");

    /**
     * refresh token.
     */
    public static final AuthTokenType REFRESH = new AuthTokenType("refresh");


    private String code;

    private AuthTokenType(String c) {
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

        if (obj instanceof AuthTokenType) {
            return code.equalsIgnoreCase(((AuthTokenType) obj).getCode());
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
        return "AuthTokenType: code=" + code;
    }

    public static AuthTokenType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }
        return map.get(c.toLowerCase());
    }

    public static Collection getAll() {
        return map.values();
    }

}
