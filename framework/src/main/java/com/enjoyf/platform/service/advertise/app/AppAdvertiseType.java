package com.enjoyf.platform.service.advertise.app;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli
 * Date: 2014/12/25
 * Time: 11:51
 */
public class AppAdvertiseType implements Serializable {

    //
    private static Map<Integer, AppAdvertiseType> map = new HashMap<Integer, AppAdvertiseType>();

    public static final AppAdvertiseType DEFAULT = new AppAdvertiseType(0);

    public static final AppAdvertiseType GAMECLIENT = new AppAdvertiseType(1);//新手游画报


    //
    private int code;

    public AppAdvertiseType(Integer c) {
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
        return "AppAdvertiseType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof AppAdvertiseType)) {
            return false;
        }

        return code == ((AppAdvertiseType) obj).getCode();
    }

    public static AppAdvertiseType getByCode(Integer c) {
        if (c == null) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<AppAdvertiseType> getAll() {
        return map.values();
    }
}