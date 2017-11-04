package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-15 下午1:29
 * Description:
 */
public class SocialProfileRecommendType implements Serializable {

    private static Map<Integer, SocialProfileRecommendType> map = new HashMap<Integer, SocialProfileRecommendType>();

    //开屏推荐
    public static final SocialProfileRecommendType DEFAULT = new SocialProfileRecommendType(0);
    //咔哒推荐
    public static final SocialProfileRecommendType FAMOUS = new SocialProfileRecommendType(1);
    //最新加入
    public static final SocialProfileRecommendType NEWEST = new SocialProfileRecommendType(2);

    private int code;

    public SocialProfileRecommendType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "SocialActivityType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((SocialProfileRecommendType) o).code) return false;

        return true;
    }

    public static SocialProfileRecommendType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<SocialProfileRecommendType> getAll() {
        return map.values();
    }
}
