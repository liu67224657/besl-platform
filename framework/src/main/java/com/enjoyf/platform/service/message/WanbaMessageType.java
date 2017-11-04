package com.enjoyf.platform.service.message;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2015/4/2.
 */
public class WanbaMessageType implements Serializable {
    private static Map<Integer, WanbaMessageType> map = new HashMap<Integer, WanbaMessageType>();

    public static final WanbaMessageType TOPIC = new WanbaMessageType(1, "topic", "话题");

    public static final WanbaMessageType MIYOU = new WanbaMessageType(2, "miyou", "迷友");

    public static final WanbaMessageType HOT = new WanbaMessageType(3, "hot", "热门");

    public static final WanbaMessageType GIFT = new WanbaMessageType(4, "gift", "礼包");

    public static final WanbaMessageType TASK = new WanbaMessageType(5, "task", "任务");

    public static final WanbaMessageType SHAKE = new WanbaMessageType(6, "shake", "摇一摇");

    public static final WanbaMessageType MY = new WanbaMessageType(7, "my", "我的");

    public static final WanbaMessageType MYCARD = new WanbaMessageType(8, "mycard", "我的帖子");

    public static final WanbaMessageType MYGIFTMARKET = new WanbaMessageType(9, "mygiftmarket", "我的商城");


    private int code;
    private String name;
    private String chname;

    public WanbaMessageType(int c, String name, String chname) {
        this.code = c;
        this.name = name;
        this.chname = chname;
        map.put(code, this);
    }

    public String getChname() {
        return chname;
    }

    public void setChname(String chname) {
        this.chname = chname;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return "WanbaMessageType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof WanbaMessageType)) {
            return false;
        }

        return code == (((WanbaMessageType) obj).getCode());
    }

    public static WanbaMessageType getByCode(Integer c) {
        if (c == null) {
            return null;
        }

        return map.get(c);
    }


    public static Collection<WanbaMessageType> getAll() {
        return map.values();
    }
}
