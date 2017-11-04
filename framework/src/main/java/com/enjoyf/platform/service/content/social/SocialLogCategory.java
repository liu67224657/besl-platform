package com.enjoyf.platform.service.content.social;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-15 下午1:29
 * Description:
 */
public class SocialLogCategory implements Serializable {

    private static Map<Integer, SocialLogCategory> map = new HashMap<Integer, SocialLogCategory>();

    //使用
    public static final SocialLogCategory USE = new SocialLogCategory(1);
    //评论
    public static final SocialLogCategory REPLY = new SocialLogCategory(2);
    //点赞
    public static final SocialLogCategory AGREE = new SocialLogCategory(3);
    //礼物
    public static final SocialLogCategory GIFT = new SocialLogCategory(4);
    //分享
    public static final SocialLogCategory SHARE = new SocialLogCategory(5);
    //活动下 文章的总数
    public static final SocialLogCategory TOTALS = new SocialLogCategory(6);

    private int code;

    public SocialLogCategory(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "SocialLogCategory: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((SocialLogCategory) o).code) return false;

        return true;
    }

    public static SocialLogCategory getByCode(int c) {
        return map.get(c);
    }

    public static Collection<SocialLogCategory> getAll() {
        return map.values();
    }
}
