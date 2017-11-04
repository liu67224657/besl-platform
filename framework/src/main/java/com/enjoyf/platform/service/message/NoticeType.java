/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.message;

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
public class NoticeType implements Serializable {
    private static Map<String, NoticeType> map = new HashMap<String, NoticeType>();

    //new blog article
    public static final NoticeType NEW_CONTENT = new NoticeType("nc");

    //new fans added
    public static final NoticeType NEW_FANS = new NoticeType("nf");

     //new fans added
    public static final NoticeType SOCIAL_NEW_FANS = new NoticeType("snf");

    //new private message
    public static final NoticeType NEW_MESSAGE = new NoticeType("nm");

    //new system bullentin
    public static final NoticeType NEW_BULLETIN  = new NoticeType("nb");

    //new blog reply to me
    public static final NoticeType NEW_REPLY = new NoticeType("nr");

    //new at me
    public static final NoticeType NEW_AT = new NoticeType("na");

    //email address auth
    public static final NoticeType MAIL_AUTH = new NoticeType("ma");

    //complete profile info
    public static final NoticeType COMPLETE_PROFILE = new NoticeType("cp");

    //billing coin value
    public static final NoticeType BILLING_COIN = new NoticeType("bc");

    //social client
    public static final NoticeType SOCIAL_CLIENT = new NoticeType("sc");

    private String code;

    public NoticeType(String c) {
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
        return "NoticeType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof NoticeType)) {
            return false;
        }

        return code.equalsIgnoreCase(((NoticeType) obj).getCode());
    }

    public static NoticeType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<NoticeType> getAll() {
        return map.values();
    }
}
