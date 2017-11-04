package com.enjoyf.platform.service.joymeapp.gameclient;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli
 * Date: 2014/12/31
 * Time: 17:02
 */
public class TagDisplyType implements Serializable {

    private static Map<Integer, TagDisplyType> map = new HashMap<Integer, TagDisplyType>();

    //the content
    public static final TagDisplyType SMALL_PIC = new TagDisplyType(1);//缩列图
    public static final TagDisplyType BIG_PIC = new TagDisplyType(2);//通栏
    public static final TagDisplyType ADVERTISING = new TagDisplyType(3);//广告
    public static final TagDisplyType BIG_PIC_NO_TITLE = new TagDisplyType(4);//通栏无标题
    public static final TagDisplyType BIG_PIC_TITLE = new TagDisplyType(5);//通栏有标题
    public static final TagDisplyType MIYOU = new TagDisplyType(6);//迷友帖子

    private int code;

    private TagDisplyType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AppPlatform: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((TagDisplyType) o).code) return false;

        return true;
    }

    public static TagDisplyType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<TagDisplyType> getAll() {
        return map.values();
    }
}
