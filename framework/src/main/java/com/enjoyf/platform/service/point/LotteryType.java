package com.enjoyf.platform.service.point;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午1:09
 * To change this template use File | Settings | File Templates.
 */
public class LotteryType implements Serializable {
    private static Map<Integer, LotteryType> map = new HashMap<Integer, LotteryType>();
    public static final LotteryType HEAD = new LotteryType(1); //头像框
    public static final LotteryType BG = new LotteryType(2);   //背景框
    public static final LotteryType COMMENT = new LotteryType(3);  //评论框
    public static final LotteryType CHAT = new LotteryType(4); //聊天气泡

    private int code;

    public LotteryType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "GoodsType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((LotteryType) o).code) return false;

        return true;
    }

    public static LotteryType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<LotteryType> getAll() {
        return map.values();
    }
}
