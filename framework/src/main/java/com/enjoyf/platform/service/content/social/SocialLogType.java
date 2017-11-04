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
public class SocialLogType implements Serializable {

    private static Map<Integer, SocialLogType> map = new HashMap<Integer, SocialLogType>();

    //水印
    public static final SocialLogType SOCIAL_WATERMARK = new SocialLogType(1);
    //背景音乐
    public static final SocialLogType SOCIAL_BGAUDIO = new SocialLogType(2);
    //活动
    public static final SocialLogType SOCIAL_ACTIVITY = new SocialLogType(3);
    //分享
    public static final SocialLogType SOCIAL_CONTENT = new SocialLogType(4);

    private int code;

    public SocialLogType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "SocialLogType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((SocialLogType) o).code) return false;

        return true;
    }

    public static SocialLogType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<SocialLogType> getAll() {
        return map.values();
    }
}
