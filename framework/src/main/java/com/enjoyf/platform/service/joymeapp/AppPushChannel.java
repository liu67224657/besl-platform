package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 上午10:01
 * Description:
 */
public class AppPushChannel implements Serializable {

    private static Map<Integer, AppPushChannel> map = new HashMap<Integer, AppPushChannel>();

    //自有的IOS的APNs推送
    public static final AppPushChannel DEFAULT = new AppPushChannel(0);

    //生产环境推送
    public static final AppPushChannel UMENG_PROD  = new AppPushChannel(1);

    //开发环境测试推送
    public static final AppPushChannel UMENG_DEV = new AppPushChannel(2);

    private int code;

    private AppPushChannel(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AppPushChannel: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AppPushChannel) o).code) return false;

        return true;
    }

    public static AppPushChannel getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AppPushChannel> getAll() {
        return map.values();
    }
}
