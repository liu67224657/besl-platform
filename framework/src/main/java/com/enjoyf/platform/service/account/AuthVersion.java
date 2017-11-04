package com.enjoyf.platform.service.account;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class AuthVersion implements Serializable {
    private static Map<String, AuthVersion> syncCodeMap = new HashMap<String, AuthVersion>();

    public static final AuthVersion SYNC_AUTH_VERSION_AUTH10 = new AuthVersion("oauth10");
    public static final AuthVersion SYNC_AUTH_VERSION_AUTH20 = new AuthVersion("oauth20");


    private String code;

    public AuthVersion(String code) {
        this.code = code;
        syncCodeMap.put(code, this);
    }

    public static AuthVersion getByCode(String code) {
        if (!syncCodeMap.containsKey(code)) {
            return null;
        }

        return syncCodeMap.get(code);
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof AuthVersion)) {
            return false;
        }

        return code.equalsIgnoreCase(((AuthVersion) obj).getCode());
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
