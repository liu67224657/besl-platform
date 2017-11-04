package com.enjoyf.platform.service.advertise.app;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-10
 * Time: 下午1:55
 * To change this template use File | Settings | File Templates.
 */
public class AppAdvertiseRedirectType implements Serializable {

    //
    private static Map<Integer, AppAdvertiseRedirectType> map = new HashMap<Integer, AppAdvertiseRedirectType>();

     public static final AppAdvertiseRedirectType WEBVIEW = new AppAdvertiseRedirectType(0);

     public static final AppAdvertiseRedirectType APPSTORE = new AppAdvertiseRedirectType(1);



    //
    private int code;

    public AppAdvertiseRedirectType(Integer c) {
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
        return "AppAdvertisePublishType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof AppAdvertiseRedirectType)) {
            return false;
        }

        return code==((AppAdvertiseRedirectType) obj).getCode();
    }

    public static AppAdvertiseRedirectType getByCode(Integer c) {
        if (c==null) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<AppAdvertiseRedirectType> getAll() {
        return map.values();
    }
}
