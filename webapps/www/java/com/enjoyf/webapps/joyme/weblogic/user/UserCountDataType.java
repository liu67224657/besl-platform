package com.enjoyf.webapps.joyme.weblogic.user;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举类
 */
public class UserCountDataType {
    private static Map<String, UserCountDataType> map = new HashMap<String, UserCountDataType>();

    public static final UserCountDataType COUNT_DATA_FOCUSNUM = new UserCountDataType("focusnum");
    public static final UserCountDataType COUNT_DATA_BLOGNUM = new UserCountDataType("blognum");
    public static final UserCountDataType COUNT_DATA_FOCUSMENUM = new UserCountDataType("focusmenum");
    public static final UserCountDataType COUNT_DATA_REPRINTENUM = new UserCountDataType("reprintednum");
    public static final UserCountDataType COUNT_DATA_LIKENUM = new UserCountDataType("likenum");


    private String code;

    private UserCountDataType(String code) {
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

    public static UserCountDataType getByCode(String code) {
        return map.get(code);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof UserCountDataType)) {
            return false;
        }

        return code.equalsIgnoreCase(((UserCountDataType) obj).getCode());
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
