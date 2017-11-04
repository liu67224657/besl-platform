package com.enjoyf.platform.service.stats;


import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class UserChargeType implements Serializable {

    //content, profile, gameres
    private static Map<String, UserChargeType> map = new HashMap<String, UserChargeType>();
    //the content
    public static final UserChargeType CANEL = new UserChargeType("0");//取消

    public static final UserChargeType SUCCESS = new UserChargeType("1");//成功

    public static final UserChargeType FAIL = new UserChargeType("-1");//失败


    private String code;

    public UserChargeType() {
    }

    public UserChargeType(String code) {
        this.code = code.toLowerCase();

        map.put(this.code, this);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static Map<String, UserChargeType> getStatusMap() {
        return map;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof UserChargeType) {
            return code.equalsIgnoreCase(((UserChargeType) obj).getCode());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return code.hashCode();
    }

    public static UserChargeType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<UserChargeType> getAll() {
        return map.values();
    }
}
