/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.social;

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
public class InviteDomain implements Serializable {
    private static Map<String, InviteDomain> map = new HashMap<String, InviteDomain>();

    //好友关系。
    public static final InviteDomain GAME = new InviteDomain("game");

    //话版关注关系
    public static final InviteDomain DEFAULT = new InviteDomain("def");

    //
    private String code;

    public InviteDomain(String c) {
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
        return "RelationType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof InviteDomain)) {
            return false;
        }

        return code.equalsIgnoreCase(((InviteDomain) obj).getCode());
    }

    public static InviteDomain getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<InviteDomain> getAll() {
        return map.values();
    }
}
