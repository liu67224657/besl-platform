package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-6-11
 * Time: 下午5:56
 * To change this template use File | Settings | File Templates.
 */
public class PushListType implements Serializable {
    private static Map<Integer, PushListType> map = new HashMap<Integer, PushListType>();

    public static final PushListType PUSH_MESSAGE = new PushListType(0); //推送消息

    public static final PushListType SYSTEM_MESSAGE = new PushListType(1);  //系统通知

    public static final PushListType ANIME_HISTORYDAY = new PushListType(2);//大动漫历史上的今天

    public static final PushListType PUSH_GAME_MESSAGE = new PushListType(3);  //关注游戏--礼包上架 推送

    public static final PushListType PUSH_GIFT_RESERVE_MESSAGE = new PushListType(4); //预定礼包--礼包上架  推送

    public static final PushListType WANB_ASK_SYSTEM_MESSAGE = new PushListType(5); //玩霸问答 系统通知

    private int code;

    private PushListType(int c) {
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
        return "PushListType: code=" + code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((PushListType) o).code) return false;

        return true;
    }

    public static PushListType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<PushListType> getAll() {
        return map.values();
    }
}
