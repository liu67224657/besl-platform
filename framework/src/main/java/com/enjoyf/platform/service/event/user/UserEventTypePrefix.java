/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.user;

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
public class UserEventTypePrefix implements Serializable{
    public static final String KEY_DOMAIN_SEPARATOR = "|";

    private static Map<String, UserEventTypePrefix> map = new HashMap<String, UserEventTypePrefix>();

    //the event type domain
    public static final UserEventTypePrefix ACCOUNT = new UserEventTypePrefix("a", "account");
    public static final UserEventTypePrefix OPERATION = new UserEventTypePrefix("o", "operation");
    public static final UserEventTypePrefix CONTENT = new UserEventTypePrefix("c", "content");
    public static final UserEventTypePrefix SOCIAL = new UserEventTypePrefix("s", "social");

    private String code;
    private String prefix;

    public UserEventTypePrefix(String c, String p) {
        code = c.toLowerCase();
        prefix = p.toUpperCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "UserEventTypePrefix: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof UserEventTypePrefix)) {
            return false;
        }

        return code.equalsIgnoreCase(((UserEventTypePrefix) obj).getCode());
    }

    public static UserEventTypePrefix getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<UserEventTypePrefix> getAll() {
        return map.values();
    }
}
