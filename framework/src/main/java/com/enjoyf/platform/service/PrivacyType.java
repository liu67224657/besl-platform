package com.enjoyf.platform.service;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:yinpy@platform.com>Yin Pengyi</a>
 */

@SuppressWarnings("serial")
public class PrivacyType implements Serializable {
	
    private static Map<String, PrivacyType> map = new HashMap<String, PrivacyType>();

    //open for all
    public static final PrivacyType PUBLIC = new PrivacyType("public");

    //only for group
    public static final PrivacyType FRIENDS = new PrivacyType("group");

    //only for self
    public static final PrivacyType PRIVATE = new PrivacyType("private");

    private String code;

    /////////////////////////////////////////////////////////////
    private PrivacyType(String c) {
        code = c.toLowerCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public static PrivacyType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "PrivilegeType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof PrivacyType)) {
            return false;
        }

        return code.equalsIgnoreCase(((PrivacyType) obj).getCode());
    }
}