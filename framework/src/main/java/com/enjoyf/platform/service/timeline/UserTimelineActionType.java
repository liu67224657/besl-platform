/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.timeline;

import com.enjoyf.platform.util.reflect.ReflectUtil;
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
public class UserTimelineActionType implements Serializable {
    private static Map<String, UserTimelineActionType> map = new HashMap<String, UserTimelineActionType>();

    //增加wiki
    public static final UserTimelineActionType ADD_WIKI = new UserTimelineActionType("add_wiki");

    //编辑wiki
    public static final UserTimelineActionType EDIT_WIKI = new UserTimelineActionType("edit_wiki");

    //关注wiki
    public static final UserTimelineActionType FOCUS_WIKI = new UserTimelineActionType("focus_wiki");

    //增加页面
    public static final UserTimelineActionType ADD_PAGE = new UserTimelineActionType("add_page");

    //编辑页面
    public static final UserTimelineActionType EDIT_PAGE = new UserTimelineActionType("edit_page");

    //关注用户
    public static final UserTimelineActionType FOCUS_USER = new UserTimelineActionType("focus_user");

    //收藏页面
    public static final UserTimelineActionType FAVIRATE_PAGE = new UserTimelineActionType("favirate_page");

    //
    private String code;

    public UserTimelineActionType(String c) {
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
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof UserTimelineActionType)) {
            return false;
        }

        return code.equalsIgnoreCase(((UserTimelineActionType) obj).getCode());
    }

    public static UserTimelineActionType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<UserTimelineActionType> getAll() {
        return map.values();
    }
}
