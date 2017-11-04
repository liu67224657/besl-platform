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
public class AuthAppType implements Serializable {
    //
    private static Map<String, AuthAppType> map = new HashMap<String, AuthAppType>();

    /**
     * internal client
     */
    public static final AuthAppType INTERNAL_CLIENT = new AuthAppType("internal_client");

    /**
     * out client
     */
    public static final AuthAppType OUT_CLIENT = new AuthAppType("out_client");

    /**
     * internal web
     */
    public static final AuthAppType INTERNAL_WEB = new AuthAppType("internal_web");

    /**
     * out web.
     */
    public static final AuthAppType OUT_WEB = new AuthAppType("out_web");

    public static final AuthAppType GAME = new AuthAppType("game");

    public static final AuthAppType VIDEO_SDK = new AuthAppType("video_sdk");


    private String code;

    private AuthAppType(String c) {
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

        if (obj instanceof AuthAppType) {
            return code.equalsIgnoreCase(((AuthAppType) obj).getCode());
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
        return "AuthAppType: code=" + code;
    }

    public static AuthAppType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }
        return map.get(c.toLowerCase());
    }

    public static Collection getAll() {
        return map.values();
    }

}
