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
public class AppTopMenuCategory implements Serializable {
    //content, profile, gameres
    private static Map<Integer, AppTopMenuCategory> map = new HashMap<Integer, AppTopMenuCategory>();
    //the content
    public static final AppTopMenuCategory DEFAULT = new AppTopMenuCategory(0);   //小端轮播图
    public static final AppTopMenuCategory CLIENT_LINE = new AppTopMenuCategory(1);  //大端轮播图
    public static final AppTopMenuCategory NEWS_CLIENT_LINE = new AppTopMenuCategory(2);//手游画报轮播图

    public static final AppTopMenuCategory WANBA_ASK = new AppTopMenuCategory(3);//玩霸问题轮播图、广告

    public static final AppTopMenuCategory WANBA_ASK_RECOMMEND_INSERT = new AppTopMenuCategory(4);//玩霸推荐热门位置插入广告
    //
    private int code;

    private AppTopMenuCategory(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AppTopMenuCategory: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AppTopMenuCategory) o).code) return false;

        return true;
    }

    public static AppTopMenuCategory getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AppTopMenuCategory> getAll() {
        return map.values();
    }
}
