package com.enjoyf.webapps.joyme.weblogic.user;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto: ericliu@enjoyfound.com> ericliu</a>
 */
public class DiscuzModifyField {
    private static Map<String, DiscuzModifyField> map = new HashMap<String, DiscuzModifyField>();

    //screenname,password,domain,headicon
    public static final DiscuzModifyField FIELD_DOMAIN = new DiscuzModifyField("domain");
    public static final DiscuzModifyField FIELD_SCREENNAME = new DiscuzModifyField("screenname");
    public static final DiscuzModifyField FIELD_PASSWORD = new DiscuzModifyField("password");
     public static final DiscuzModifyField FIELD_HEADICON = new DiscuzModifyField("headicon");
    
    private String code;

    public DiscuzModifyField(String code) {
        this.code = code;
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public static Map<String, DiscuzModifyField> getMap() {
        return map;
    }

    /**
     * get by code default null
     *
     * @param code
     * @return
     */
    public static DiscuzModifyField getByCode(String code) {
        return map.containsKey(code) ? map.get(code) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscuzModifyField)) return false;

        DiscuzModifyField obj = (DiscuzModifyField) o;

        if (!code.equals(obj.getCode())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
