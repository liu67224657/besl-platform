package com.enjoyf.platform.service.mail.mailtemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Strings;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class MailType {
	
    private static Map<String, MailType> map = new HashMap<String, MailType>();

    public static MailType ALERTER = new MailType("alerter");
    public static MailType PASSPORT_REGISTER = new MailType("passport.register");
    public static MailType PASSPORT_RECOVERPWD = new MailType("passport.recoverpwd");

    private String code;


    private MailType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static MailType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<MailType> getAllMailTypes() {
        return map.values();
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "MailType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof MailType)) {
            return false;
        }

        return code.equalsIgnoreCase(((MailType) obj).getCode());
    }
}
