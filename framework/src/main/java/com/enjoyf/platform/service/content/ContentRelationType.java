package com.enjoyf.platform.service.content;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-7
 * Time: 上午10:58
 * To change this template use File | Settings | File Templates.
 */
public class ContentRelationType implements Serializable {
      private static Map<String, ContentRelationType> map = new HashMap<String, ContentRelationType>();

    public static ContentRelationType GAME = new ContentRelationType("game");
    public static ContentRelationType GROUP = new ContentRelationType("group");

    //
    public static ContentRelationType ADMIN_ADJUST_POINT = new ContentRelationType("sapoint");

    private String code;

    private ContentRelationType(String c) {
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
        return "ContentRelationType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ContentRelationType)) {
            return false;
        }

        return code.equalsIgnoreCase(((ContentRelationType) obj).getCode());
    }

    public static ContentRelationType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ContentRelationType> getAll() {
        return map.values();
    }

}
