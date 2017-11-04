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
public class RelationType implements Serializable {
    private static Map<String, RelationType> map = new HashMap<String, RelationType>();

    //好友关系。
    public static final RelationType ALL = new RelationType("all");

    //话版关注关系
    public static final RelationType BOARD_FOLLOW = new RelationType("board");

    //关注和粉丝。.
    public static final RelationType FOCUS = new RelationType("focus");

     //关注和粉丝--社交端
    public static final RelationType SFOCUS = new RelationType("sfocus");

    //
    private String code;

    public RelationType(String c) {
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
        if ((obj == null) || !(obj instanceof RelationType)) {
            return false;
        }

        return code.equalsIgnoreCase(((RelationType) obj).getCode());
    }

    public static RelationType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<RelationType> getAll() {
        return map.values();
    }
}
