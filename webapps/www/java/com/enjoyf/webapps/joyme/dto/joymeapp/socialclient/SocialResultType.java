package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-17
 * Time: 下午5:27
 * To change this template use File | Settings | File Templates.
 */
public class SocialResultType {

    private static Map<Integer, SocialResultType> map = new HashMap<Integer, SocialResultType>();
    //主要的结果集（如文章、活动等）
    public static final SocialResultType CONTENT_RESULT = new SocialResultType(1);
    //插入的广告
    public static final SocialResultType AD_RESULT = new SocialResultType(2);

    private int code;

    public SocialResultType(int c) {
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
        return "SocialResultType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;


        if (code != ((SocialResultType) obj).code) return false;

        return true;
    }

    public static SocialResultType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<SocialResultType> getAll() {
        return map.values();
    }
}
