/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.usercenter.activityuser;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class ActivityActionType implements Serializable {
    private static Map<Integer, ActivityActionType> map = new HashMap<Integer, ActivityActionType>();

    //
    public static final ActivityActionType MODIFY_WIKI = new ActivityActionType(1);
    public static final ActivityActionType COMMENT = new ActivityActionType(2);

    //
    private int code;

    public ActivityActionType(Integer c) {
        code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ActivityActionType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ActivityActionType)) {
            return false;
        }

        return code == (((ActivityActionType) obj).getCode());
    }

    public static ActivityActionType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ActivityActionType> getAll() {
        return map.values();
    }
}
