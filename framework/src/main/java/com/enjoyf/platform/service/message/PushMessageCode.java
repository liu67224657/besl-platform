package com.enjoyf.platform.service.message;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午6:40
 * To change this template use File | Settings | File Templates.
 */
public class PushMessageCode implements Serializable {
    private static Map<String, PushMessageCode> map = new HashMap<String, PushMessageCode>();

    // 有一篇新文章
    public static final PushMessageCode content = new PushMessageCode("content");
    // news mess
    public static final PushMessageCode news = new PushMessageCode("news");
    //版本更新
    public static final PushMessageCode version = new PushMessageCode("version");

    private String code;

    public PushMessageCode(String c) {
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
        return "PushMessageCode: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof PushMessageCode)) {
            return false;
        }

        return code.equalsIgnoreCase(((PushMessageCode) obj).getCode());
    }

    public static PushMessageCode getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<PushMessageCode> getAll() {
        return map.values();
    }
}
