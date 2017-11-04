package com.enjoyf.platform.service.profile;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-16
 * Time: 上午10:00
 * To change this template use File | Settings | File Templates.
 */
public class ProfileExperienceType implements Serializable{
    private static Map<String, ProfileExperienceType> map = new HashMap<String, ProfileExperienceType>();

    //the favorite tags
    public static final ProfileExperienceType SCHOOL = new ProfileExperienceType("school");
    public static final ProfileExperienceType COMPANY = new ProfileExperienceType("company");

    private String code;

    public ProfileExperienceType(String c) {
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
        return "ProfileExperienceType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ProfileExperienceType)) {
            return false;
        }

        return code.equalsIgnoreCase(((ProfileExperienceType) obj).getCode());
    }

    public static ProfileExperienceType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ProfileExperienceType> getAll() {
        return map.values();
    }
}
