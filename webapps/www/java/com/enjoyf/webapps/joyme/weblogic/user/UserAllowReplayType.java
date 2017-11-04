package com.enjoyf.webapps.joyme.weblogic.user;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举类
 */
public class UserAllowReplayType {
    private static Map<String, UserAllowReplayType> map = new HashMap<String, UserAllowReplayType>();

    public static final UserAllowReplayType ALLOW_REPLAY_TYPE_ALL = new UserAllowReplayType("A");
    public static final UserAllowReplayType ALLOW_REPLAY_TYPE_NONE = new UserAllowReplayType("N");
    public static final UserAllowReplayType ALLOW_REPLAY_TYPE_FOCUS = new UserAllowReplayType("C");

    private String code;

    private UserAllowReplayType(String code) {
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

    public static UserAllowReplayType getByCode(String code) {
        return map.get(code);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof UserAllowReplayType)) {
            return false;
        }

        return code.equalsIgnoreCase(((UserAllowReplayType) obj).getCode());
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
