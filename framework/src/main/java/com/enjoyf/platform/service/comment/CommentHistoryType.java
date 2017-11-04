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
public class CommentHistoryType implements Serializable {
    private static Map<Integer, CommentHistoryType> map = new HashMap<Integer, CommentHistoryType>();

    public static final CommentHistoryType DEFAULT = new CommentHistoryType(0);//默认
    public static final CommentHistoryType AGREE = new CommentHistoryType(1);    //点赞
    public static final CommentHistoryType SCORE = new CommentHistoryType(2);      //评分
    public static final CommentHistoryType REPLY = new CommentHistoryType(3);        //评论
    public static final CommentHistoryType VOTE = new CommentHistoryType(4);        //投票
    private int code;

    public CommentHistoryType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "CommentHistoryType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((CommentHistoryType) o).code) return false;

        return true;
    }

    public static CommentHistoryType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<CommentHistoryType> getAll() {
        return map.values();
    }
}
