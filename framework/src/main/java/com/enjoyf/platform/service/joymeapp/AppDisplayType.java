package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 上午10:01
 * Description:
 */
public class AppDisplayType implements Serializable {

    private static Map<Integer, AppDisplayType> map = new HashMap<Integer, AppDisplayType>();

    //顶部大图
    public static final AppDisplayType TYPE1 = new AppDisplayType(1);

    //图文混排
    public static final AppDisplayType TYPE2 = new AppDisplayType(2);

    //专栏图片
    public static final AppDisplayType TYPE3 = new AppDisplayType(3);

    //广告位
    public static final AppDisplayType TYPE4 = new AppDisplayType(4);
    //
    private int code;

    private AppDisplayType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AppDisplayType) o).code) return false;

        return true;
    }

    public static AppDisplayType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AppDisplayType> getAll() {
        return map.values();
    }
}
