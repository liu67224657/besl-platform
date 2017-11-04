package com.enjoyf.platform.service.message;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午4:51
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class PushMessageType implements Serializable {
    private static Map<String, PushMessageType> map = new HashMap<String, PushMessageType>();

    // user
    public static final PushMessageType USER = new PushMessageType("user");

    // device
    public static final PushMessageType DEVICE = new PushMessageType("device");


    private String code;

    public PushMessageType(String c) {
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
        return "PushMessageType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof PushMessageType)) {
            return false;
        }

        return code.equalsIgnoreCase(((PushMessageType) obj).getCode());
    }

    public static PushMessageType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<PushMessageType> getAll() {
        return map.values();
    }
}
