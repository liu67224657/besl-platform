package com.enjoyf.platform.service.sync;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-27
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class ShareType implements Serializable {
    private static Map<Integer, ShareType> map = new HashMap<Integer, ShareType>();

    public static final ShareType CONTENT = new ShareType(0);
    public static final ShareType WIKI = new ShareType(1);
    public static final ShareType GIFTMARKET = new ShareType(2);
    public static final ShareType LOTTERY = new ShareType(3);
    public static final ShareType NEW_GAME = new ShareType(4);
    //画报
    public static final ShareType CLIENT = new ShareType(5);
    //社交端
    public static final ShareType SOCIAL_CLIENT = new ShareType(6);

    private int code;


    public ShareType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ShareType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ShareType) o).code) return false;

        return true;
    }

    public static ShareType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ShareType> getAll() {
        return map.values();
    }
}
