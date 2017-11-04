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
public class EmailahthStatus {
    private static Map<String, EmailahthStatus> map = new HashMap<String, EmailahthStatus>();

    public static final EmailahthStatus EMAIL_AUTH_STATUS_NO = new EmailahthStatus("N");
    public static final EmailahthStatus EMAIL_AUTH_STATUS_YES = new EmailahthStatus("Y");

    private String code;

    private EmailahthStatus(String code) {
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

    public static EmailahthStatus getByCode(String code) {
        return map.get(code);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof EmailahthStatus)) {
            return false;
        }

        return code.equalsIgnoreCase(((EmailahthStatus) obj).getCode());
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
