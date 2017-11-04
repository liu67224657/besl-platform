package com.enjoyf.platform.service.tools;

import com.google.common.base.Strings;
import org.jfree.util.PublicCloneable;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-10-27
 * Time: 下午6:45
 * Desc: 内容类型：文章：文本，图片，。。。回复：文本，图片
 */
public class ContentType implements Serializable {

    public static Map<String, ContentType> map = new HashMap<String, ContentType>();

    //文章：文字
    public static final ContentType CONTENTTEXT = new ContentType("ct");
     //文章：图片
    public static final ContentType CONTENTIMG = new ContentType("ci");

    //文章：文字
    public static final ContentType REPLYTEXT = new ContentType("rt");
     //文章：图片
    public static final ContentType REPLYIMG = new ContentType("ri");


    private String code;

    public ContentType(String c) {
        this.code = c.toLowerCase();
        map.put(code, this);
    }

    public String getCode() {
        return this.code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "ContentType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ContentType)) {
            return false;
        }
        return code.equalsIgnoreCase(((ContentType) obj).getCode());
    }

    public static ContentType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ContentType> getAll() {
        return map.values();
    }
}
