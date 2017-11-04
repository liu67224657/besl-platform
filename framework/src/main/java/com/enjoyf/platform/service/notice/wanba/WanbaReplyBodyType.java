package com.enjoyf.platform.service.notice.wanba;

import com.enjoyf.platform.service.notice.AbstractUserNoticeBody;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/23
 */
public class WanbaReplyBodyType implements Serializable {
    private static Map<Integer, WanbaReplyBodyType> map = new HashMap<Integer, WanbaReplyBodyType>();

    public static WanbaReplyBodyType REPLY_ANSWER = new WanbaReplyBodyType(1); //回复答案
    public static WanbaReplyBodyType REPLY_REPLEY = new WanbaReplyBodyType(2);//回复评论

    private int code;

    public WanbaReplyBodyType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "WanbaNoticeBodyType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((WanbaReplyBodyType) o).code) return false;

        return true;
    }

    public static WanbaReplyBodyType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<WanbaReplyBodyType> getAll() {
        return map.values();
    }
}
