/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.notice.wiki;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class WikiNoticeType implements Serializable {
    private static Map<Integer, WikiNoticeType> map = new HashMap<Integer, WikiNoticeType>();


    //social client
    public static final WikiNoticeType AT = new WikiNoticeType(1);
    public static final WikiNoticeType REPLY = new WikiNoticeType(2);
    public static final WikiNoticeType AGREE = new WikiNoticeType(3);
    public static final WikiNoticeType FOCUS = new WikiNoticeType(4);
    public static final WikiNoticeType IMPROVE = new WikiNoticeType(5);

    private int code;

    private WikiNoticeType(int c) {
        code = c;

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
        return "WikiNoticeType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof WikiNoticeType)) {
            return false;
        }

        return code==(((WikiNoticeType) obj).getCode());
    }

    public static WikiNoticeType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<WikiNoticeType> getAll() {
        return map.values();
    }
}
