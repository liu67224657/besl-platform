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
public class MessageType implements Serializable {
    private static Map<String, MessageType> map = new HashMap<String, MessageType>();

    //private message
    public static final MessageType PRIVATE = new MessageType("private");

    //operation message
    public static final MessageType OPERATION = new MessageType("operation");

    // client push message
    public static final MessageType CLIENTPUSH = new MessageType("clientpush");

     //private message
    public static final MessageType SOCIAL_PRIVATE = new MessageType("sprivate");

    private String code;

    public MessageType(String c) {
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
        if ((obj == null) || !(obj instanceof MessageType)) {
            return false;
        }

        return code.equalsIgnoreCase(((MessageType) obj).getCode());
    }

    public static MessageType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<MessageType> getAll() {
        return map.values();
    }
}
