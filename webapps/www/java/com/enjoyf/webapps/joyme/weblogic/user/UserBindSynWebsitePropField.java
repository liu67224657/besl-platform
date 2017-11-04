package com.enjoyf.webapps.joyme.weblogic.user;
/**
 * <p/>
 * Description:用户详细信息属性修改的类
 * </p>
 * @author yongmingxu
 */

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserBindSynWebsitePropField implements Serializable {
    private static Map<String, UserBindSynWebsitePropField> map = new HashMap<String, UserBindSynWebsitePropField>();
    public static final UserBindSynWebsitePropField FIELD_SYNWEBSITENAME = new UserBindSynWebsitePropField("synwebsitename");
    public static final UserBindSynWebsitePropField FIELD_LOGINID = new UserBindSynWebsitePropField("loginid");
    public static final UserBindSynWebsitePropField FIELD_LOGINPWD = new UserBindSynWebsitePropField("loginpwd");
    public static final UserBindSynWebsitePropField FIELD_ISUSED = new UserBindSynWebsitePropField("isused");
    public static final UserBindSynWebsitePropField FIELD_TOKENSEC = new UserBindSynWebsitePropField("tokensec");
    public static final UserBindSynWebsitePropField FIELD_TOKENKEY = new UserBindSynWebsitePropField("tokenkey");
    public static final UserBindSynWebsitePropField FIELD_BINDINGDATE = new UserBindSynWebsitePropField("bindingdate");
    public static final UserBindSynWebsitePropField FIELD_LASTMODFIYDATE = new UserBindSynWebsitePropField("lastmodfiydate");
    public static final UserBindSynWebsitePropField FIELD_ISSYNPHRASE = new UserBindSynWebsitePropField("issynphrase");
    public static final UserBindSynWebsitePropField FIELD_ISSYNPHOTO = new UserBindSynWebsitePropField("issynphoto");
    public static final UserBindSynWebsitePropField FIELD_ISSYNTITLE = new UserBindSynWebsitePropField("issyntitle");
    public static final UserBindSynWebsitePropField FIELD_ISSYNMUSIC = new UserBindSynWebsitePropField("issynmusic");
    public static final UserBindSynWebsitePropField FIELD_ISSYNVEDIO = new UserBindSynWebsitePropField("issynvedio");


    private String code;

    private UserBindSynWebsitePropField(String code) {
        this.code = code;
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static UserBindSynWebsitePropField getByCode(String code) {
        return map.get(code);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof UserBindSynWebsitePropField)) {
            return false;
        }

        return code.equalsIgnoreCase(((UserBindSynWebsitePropField) obj).getCode());
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
