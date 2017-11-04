/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.props;

import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a mailto="yinpengyi@platform.com">Yin Pengyi</a>
 */
public class I18nServiceType {
    private String code;
    private String name;

    private static Map<String, I18nServiceType> map = new HashMap<String, I18nServiceType>();

    public static final I18nServiceType MAIL_TEMPLATE = new I18nServiceType("mail", "templates of all type of emails");
    public static final I18nServiceType ROOM = new I18nServiceType("room", "messaged of room");

    /////////////////////////////////////////////////////////////
    private I18nServiceType(String id, String name) {
        code = id;
        this.name = name;

        //
        map.put(code, this);
    }


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int hashCode() {
        return code.hashCode();
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof I18nServiceType)) {
            return false;
        }

        return code.equalsIgnoreCase(((I18nServiceType) obj).getCode());
    }

    public static I18nServiceType getByCode(String c) {
        if (StringUtil.isEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<I18nServiceType> getAllI18nServiceTypes() {
        return map.values();
    }
}
