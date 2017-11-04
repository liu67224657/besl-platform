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
public class AnimePushType implements Serializable {
    //content, profile, gameres
    private static Map<Integer, AnimePushType> map = new HashMap<Integer, AnimePushType>();
    //content, profile, gameres

//    0	打开应用，不做跳转（native-list）
//    1	CMS文章单页（native-article）
//    4	WEBVIEW页面（WIKI首页文章页等）/（web-）
//    9 下载

    //the content
    public static final AnimePushType DEFAULT = new AnimePushType(0);

    public static final AnimePushType CMS_NATIVE = new AnimePushType(1);

    public static final AnimePushType WIKI_WEBVIEW = new AnimePushType(4);

    public static final AnimePushType DOWNLOAD = new AnimePushType(9);
    //
    private int code;

    private AnimePushType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AppRedirectType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AnimePushType) o).code) return false;

        return true;
    }

    public static AnimePushType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AnimePushType> getAll() {
        return map.values();
    }
}
