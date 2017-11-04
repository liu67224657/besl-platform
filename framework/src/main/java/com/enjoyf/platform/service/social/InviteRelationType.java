/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.social;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class InviteRelationType implements Serializable {
    private static Map<String, InviteRelationType> map = new HashMap<String, InviteRelationType>();

    //全部
    public static final InviteRelationType REGISTER = new InviteRelationType("reg");

    //关注和粉丝。.
    public static final InviteRelationType UNREGISTER = new InviteRelationType("unreg");

    //好友关系。
    public static final InviteRelationType SOCIAL = new InviteRelationType("social");

    //
    private String code;

    public InviteRelationType(String c) {
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
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof InviteRelationType)) {
            return false;
        }

        return code.equalsIgnoreCase(((InviteRelationType) obj).getCode());
    }

    public static InviteRelationType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<InviteRelationType> getAll() {
        return map.values();
    }
}
