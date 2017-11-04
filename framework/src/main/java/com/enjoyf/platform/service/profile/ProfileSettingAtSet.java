package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-10-19
 * Time: 下午6:32
 * Desc:
 */
public class ProfileSettingAtSet implements Serializable {

    private Set<ProfileSettingAt> ats = new LinkedHashSet<ProfileSettingAt>();

    public ProfileSettingAtSet() {
    }

    public ProfileSettingAtSet(Set ats) {
        this.ats = ats;
    }

    public ProfileSettingAtSet(Collection ats) {
        this.ats.addAll(ats);
    }

    public ProfileSettingAtSet(String jsonStr) {
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                ats = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<ProfileSettingAt>>() {
                });
            } catch (IOException e) {
                //
                GAlerter.lab("ProfileSettingAtSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public Set<ProfileSettingAt> getAts() {
        return ats;
    }

    public void setAts(Set<ProfileSettingAt> ats) {
        this.ats = ats;
    }

    public void add(ProfileSettingAt at) {
        ats.add(at);
    }

    public void add(Set<ProfileSettingAt> ats) {
        this.ats.addAll(ats);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(ats);
    }

    public static ProfileSettingAtSet parse(String jsonStr) {
        ProfileSettingAtSet returnValue = new ProfileSettingAtSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<ProfileSettingAt> profileSettingAt = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<Set<ProfileSettingAt>>() {
                });

                returnValue.add(profileSettingAt);
            } catch (IOException e) {
                GAlerter.lab("ProfileSettingAtSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
