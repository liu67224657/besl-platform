package com.enjoyf.webapps.joyme.weblogic.user;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举类
 */
public class UserAllowLetterType {
    private static Map<String, UserAllowLetterType> map = new HashMap<String, UserAllowLetterType>();

    public static final UserAllowLetterType ALLOW_LETTER_TYPE_ALL = new UserAllowLetterType("A");
    public static final UserAllowLetterType ALLOW_LETTER_TYPE_NONE = new UserAllowLetterType("N");
    public static final UserAllowLetterType ALLOW_LETTER_TYPE_FOCUS = new UserAllowLetterType("C");

    private String code;

    private UserAllowLetterType(String code) {
        this.code = code.toLowerCase();
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static UserAllowLetterType getByCode(String code) {
        return map.get(code);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof UserAllowLetterType)) {
            return false;
        }

        return code.equalsIgnoreCase(((UserAllowLetterType) obj).getCode());
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
