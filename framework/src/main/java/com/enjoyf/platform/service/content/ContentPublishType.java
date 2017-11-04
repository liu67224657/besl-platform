/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

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
public class ContentPublishType  implements Serializable {
    private static Map<String, ContentPublishType> map = new HashMap<String, ContentPublishType>();

    //the original type.
    public static final ContentPublishType ALL = new ContentPublishType("all");

    //the original type.
    public static final ContentPublishType ORIGINAL = new ContentPublishType("org");

    //the forward type
    public static final ContentPublishType FORWARD = new ContentPublishType("fwd");

    private String code;

    private ContentPublishType(String c) {
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
        return "ContentPublishType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ContentPublishType)) {
            return false;
        }

        return code.equalsIgnoreCase(((ContentPublishType) obj).getCode());
    }

    public static ContentPublishType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ContentPublishType> getAll() {
        return map.values();
    }
}
