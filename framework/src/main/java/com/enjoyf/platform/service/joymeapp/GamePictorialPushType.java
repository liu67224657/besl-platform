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
public class GamePictorialPushType implements Serializable {
    private static Map<Integer, GamePictorialPushType> map = new HashMap<Integer, GamePictorialPushType>();

    public static final GamePictorialPushType DEFAULT = new GamePictorialPushType(0);

    public static final GamePictorialPushType CMS_NATIVE = new GamePictorialPushType(1);

    public static final GamePictorialPushType CMS_LIST = new GamePictorialPushType(2);      //导航栏目列表页

    public static final GamePictorialPushType WIKI_WEBVIEW = new GamePictorialPushType(4);

    public static final GamePictorialPushType DOWNLOAD = new GamePictorialPushType(9);
    //
    private int code;

    private GamePictorialPushType(int c) {
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


        if (code != ((GamePictorialPushType) o).code) return false;

        return true;
    }

    public static GamePictorialPushType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<GamePictorialPushType> getAll() {
        return map.values();
    }
}
