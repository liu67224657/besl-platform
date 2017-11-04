/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.misc;

import com.google.common.base.Strings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class OperationNoticeType {
    private static Map<String, OperationNoticeType> map = new HashMap<String, OperationNoticeType>();

    //the blog messages timeline
    public static final OperationNoticeType BLOG = new OperationNoticeType("blog");

    //the notice message timeline
    public static final OperationNoticeType NOTICE = new OperationNoticeType("notice");

    private String code;

    public OperationNoticeType(String c) {
        code = c.toLowerCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "OperationNoticeType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof OperationNoticeType)) {
            return false;
        }

        return code.equalsIgnoreCase(((OperationNoticeType) obj).getCode());
    }

    public static OperationNoticeType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<OperationNoticeType> getAll() {
        return map.values();
    }
}
