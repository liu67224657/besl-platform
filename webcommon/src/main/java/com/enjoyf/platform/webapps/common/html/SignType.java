package com.enjoyf.platform.webapps.common.html;

import com.enjoyf.platform.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class SignType {
    private static Map<String, SignType> map = new HashMap<String, SignType>();

    //SILENT
    public static final SignType SILENT = new SignType("silent");

    //REQUIRED
    public static final SignType REQUIRED = new SignType("required");

    //NONE
    public static final SignType NONE = new SignType("none");

    private String code;

    private SignType(String c) {
        code = c.toLowerCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public int hashCode() {
        return code.hashCode();
    }

    public String toString() {
        return "SignType: code=" + code;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SignType)) {
            return false;
        }

        return code.equalsIgnoreCase(((SignType) obj).getCode());
    }

    public static SignType getByCode(String c) {
        if (StringUtil.isEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }
}
