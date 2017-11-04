package com.enjoyf.platform.service.comment;

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
public class CommentVideoType implements Serializable {
    private static Map<Integer, CommentVideoType> map = new HashMap<Integer, CommentVideoType>();

    public static final CommentVideoType DEFAULT = new CommentVideoType(0);//默认
    public static final CommentVideoType BACKGROUND = new CommentVideoType(1);    //后台上传
    private int code;

    public CommentVideoType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public CommentVideoType(){

    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "CommentVideoType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((CommentVideoType) o).code) return false;

        return true;
    }

    public static CommentVideoType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<CommentVideoType> getAll() {
        return map.values();
    }
}
