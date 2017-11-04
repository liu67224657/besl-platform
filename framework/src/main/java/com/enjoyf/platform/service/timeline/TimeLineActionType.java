package com.enjoyf.platform.service.timeline;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by pengxu on 2017/1/4.
 */
public class TimeLineActionType implements Serializable {
    private static Map<String, TimeLineActionType> map = new LinkedHashMap<String, TimeLineActionType>();


    public static final TimeLineActionType ADD_WIKI = new TimeLineActionType("add_wiki", " 添加了wiki");
    public static final TimeLineActionType EDIT_WIKI = new TimeLineActionType("edit_wiki", "编辑了wiki");
    public static final TimeLineActionType FOCUS_WIKI = new TimeLineActionType("focus_wiki", " 关注了wiki");
    public static final TimeLineActionType ADD_PAGE = new TimeLineActionType("add_page", "添加了页面");
    public static final TimeLineActionType EDIT_PAGE = new TimeLineActionType("edit_page", "编辑了页面");
    public static final TimeLineActionType FOCUS_USER = new TimeLineActionType("focus_user", "关注了用户");
    public static final TimeLineActionType FAVIRATE_PAGE = new TimeLineActionType("favirate_page", "收藏了页面");

    //
    private String code;
    private String value;


    public TimeLineActionType(String appkey, String value) {
        this.code = appkey;
        this.value = value;
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }


    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "TimeLineActionType: code=" + code + " value=" + value;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof TimeLineActionType)) {
            return false;
        }

        return code.equalsIgnoreCase(((TimeLineActionType) obj).getCode());
    }

    public static TimeLineActionType getByCode(String c) {
        return map.get(c) == null ? null : map.get(c);
    }

    public static Collection<TimeLineActionType> getAll() {
        return map.values();
    }
}
