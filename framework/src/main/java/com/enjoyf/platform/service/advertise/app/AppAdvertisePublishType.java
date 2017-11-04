package com.enjoyf.platform.service.advertise.app;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-6-7
 * Time: 下午4:19
 * To change this template use File | Settings | File Templates.
 */
public class AppAdvertisePublishType implements Serializable {
    //
    private static Map<Integer, AppAdvertisePublishType> map = new HashMap<Integer, AppAdvertisePublishType>();
    //开屏
    public static final AppAdvertisePublishType LOADING = new AppAdvertisePublishType(0);
    //弹窗
    public static final AppAdvertisePublishType POP = new AppAdvertisePublishType(1);
    //咔哒文章列表
    public static final AppAdvertisePublishType ARTICLE_LIS = new AppAdvertisePublishType(2);
    //咔哒广场
    public static final AppAdvertisePublishType ACTIVITY_LIS = new AppAdvertisePublishType(3);
    //小端 通版 首页
    public static final AppAdvertisePublishType MINI_GENERAL_BOTTOM = new AppAdvertisePublishType(4);
    //小游戏 暂停
    public static final AppAdvertisePublishType SMALL_GAME_PAUSE = new AppAdvertisePublishType(5);
    //小游戏 过关
    public static final AppAdvertisePublishType SMALL_GAME_PASS = new AppAdvertisePublishType(6);
    //小游戏 game over
    public static final AppAdvertisePublishType SMALL_GAME_OVER = new AppAdvertisePublishType(7);

    //玩霸 标签广告
    public static final AppAdvertisePublishType GAME_CLIENT = new AppAdvertisePublishType(8);

    //迷系列 首页弹窗
    public static final AppAdvertisePublishType MI_INDEX_POP = new AppAdvertisePublishType(9);
    //迷系列 搜索页
    public static final AppAdvertisePublishType MI_SEARCH = new AppAdvertisePublishType(10);
    //迷系列 详情页
    public static final AppAdvertisePublishType MI_DETAIL = new AppAdvertisePublishType(11);
    //迷系列 文字广告
    public static final AppAdvertisePublishType MI_TEXT = new AppAdvertisePublishType(12);

    private int code;

    public AppAdvertisePublishType(Integer c) {
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
        if ((obj == null) || !(obj instanceof AppAdvertisePublishType)) {
            return false;
        }

        return code == ((AppAdvertisePublishType) obj).getCode();
    }

    public static AppAdvertisePublishType getByCode(Integer c) {
        if (c == null) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<AppAdvertisePublishType> getAll() {
        return map.values();
    }
}
