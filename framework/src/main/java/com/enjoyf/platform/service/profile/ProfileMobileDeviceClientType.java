package com.enjoyf.platform.service.profile;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-2
 * Time: 下午12:00
 * To change this template use File | Settings | File Templates.
 */
public class ProfileMobileDeviceClientType implements Serializable {
    private static Map<String, ProfileMobileDeviceClientType> map = new HashMap<String, ProfileMobileDeviceClientType>();

    public static final ProfileMobileDeviceClientType IOS = new ProfileMobileDeviceClientType("ios");
    public static final ProfileMobileDeviceClientType ANDROID = new ProfileMobileDeviceClientType("android");

    private String code;

    public ProfileMobileDeviceClientType(String c) {
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
        return "ProfileMobileDeviceClientType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ProfileMobileDeviceClientType)) {
            return false;
        }

        return code.equalsIgnoreCase(((ProfileMobileDeviceClientType) obj).getCode());
    }

    public static ProfileMobileDeviceClientType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ProfileMobileDeviceClientType> getAll() {
        return map.values();
    }

}
