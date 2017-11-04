/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.profile;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-11 下午5:41
 * Description:
 */
public class ProfileOnlineStatus implements Serializable {
    private static Map<String, ProfileOnlineStatus> map = new HashMap<String, ProfileOnlineStatus>();

    //online
    public static final ProfileOnlineStatus ONLINE = new ProfileOnlineStatus("online");

    //offline
    public static final ProfileOnlineStatus OFFLINE = new ProfileOnlineStatus("offline");

    //hidden
    public static final ProfileOnlineStatus HIDDEN = new ProfileOnlineStatus("hidden");

    //unknown
    public static final ProfileOnlineStatus UNKNOWN = new ProfileOnlineStatus("unknown");

    private String code;

    public ProfileOnlineStatus(String c) {
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
        return "ProfileOnlineStatus: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ProfileOnlineStatus)) {
            return false;
        }

        return code.equalsIgnoreCase(((ProfileOnlineStatus) obj).getCode());
    }

    public static ProfileOnlineStatus getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ProfileOnlineStatus> getAll() {
        return map.values();
    }
}
