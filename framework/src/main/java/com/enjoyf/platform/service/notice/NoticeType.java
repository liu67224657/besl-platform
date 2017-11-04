package com.enjoyf.platform.service.notice;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/10
 * Description:
 */
public class NoticeType implements Serializable {
    private static Map<String, NoticeType> map = new HashMap<String, NoticeType>();

    public static NoticeType ANSWER = new NoticeType("answer", 1);   //回答
    public static NoticeType REPLY = new NoticeType("reply", 1);    //评论
    public static NoticeType AT = new NoticeType("at", 1);            //AT
    public static NoticeType AGREE = new NoticeType("agree", 1);   //点赞
    public static NoticeType FOLLOW = new NoticeType("follow", 1); //关注
    public static NoticeType SYSTEM = new NoticeType("sys", 1);

    private String code;//
    private int dtype;//1数字 2小红点 3文字

    public NoticeType(String code, int dtype) {
        this.code = code;
        this.dtype = dtype;

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public int getDtype() {
        return dtype;
    }

    @Override
    public String toString() {
        return "UserNoticeType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((NoticeType) o).code) return false;

        return true;
    }

    public static NoticeType getByCode(String c) {
        return map.get(c);
    }

    public static Collection<NoticeType> getAll() {
        return map.values();
    }

}
