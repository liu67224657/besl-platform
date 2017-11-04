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
public class ActivityObjectType implements Serializable {
    private static Map<Integer, ActivityObjectType> map = new HashMap<Integer, ActivityObjectType>();

    //
    public static final ActivityObjectType WIKI_ARTICLE = new ActivityObjectType(1);
    public static final ActivityObjectType WIKI_BBS = new ActivityObjectType(2);

    //
    private int code;

    public ActivityObjectType(Integer c) {
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
        if ((obj == null) || !(obj instanceof ActivityObjectType)) {
            return false;
        }

        return code == (((ActivityObjectType) obj).getCode());
    }

    public static ActivityObjectType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ActivityObjectType> getAll() {
        return map.values();
    }
}
