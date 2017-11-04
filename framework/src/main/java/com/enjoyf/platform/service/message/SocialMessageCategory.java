package com.enjoyf.platform.service.message;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-27
 * Time: 下午3:43
 * To change this template use File | Settings | File Templates.
 */
public class SocialMessageCategory implements Serializable {
     private static Map<Integer, SocialMessageCategory> map = new HashMap<Integer, SocialMessageCategory>();

    //user-->user
    public static final SocialMessageCategory DEFAULT_MSG = new SocialMessageCategory(0);
    //admin-->user 运营号tools推送
    public static final SocialMessageCategory ADMIN_MSG = new SocialMessageCategory(1);

    private int code;

    public SocialMessageCategory(int c) {
        this.code = c;

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
        return "NoticeType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof SocialMessageCategory)) {
            return false;
        }

        return code == (((SocialMessageCategory) obj).getCode());
    }

    public static SocialMessageCategory getByCode(Integer c) {
        if (c == null) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<SocialMessageCategory> getAll() {
        return map.values();
    }
}
