package com.enjoyf.platform.service.mail.mailtemplate;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Strings;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class MailContextType {
    private static Map<String, MailContextType> map = new HashMap<String, MailContextType>();

    //
    public static MailContextType HTML = new MailContextType("html");
    public static MailContextType TEXT = new MailContextType("text");

    //
    private String code;

    //////////////////////////////////////////////////////////////////////
    private MailContextType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static MailContextType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    //////////////////////////////////////////////////////////////////////
    public int hashCode() {
        return code.hashCode();
    }

    public String toString() {
        return "MailContextType: code=" + code;
    }

    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof MailContextType)) {
            return false;
        }

        return code.equalsIgnoreCase(((MailContextType) obj).getCode());
    }
}
