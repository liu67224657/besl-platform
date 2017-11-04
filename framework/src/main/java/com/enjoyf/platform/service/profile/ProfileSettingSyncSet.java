package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.account.AccountDomain;
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
public class ProfileSettingSyncSet implements Serializable {

    private Set<AccountDomain> syncs = new LinkedHashSet<AccountDomain>();

    public ProfileSettingSyncSet() {
    }

    public ProfileSettingSyncSet(Set syncs) {
        this.syncs = syncs;
    }

    public ProfileSettingSyncSet(Collection syncs) {
        this.syncs.addAll(syncs);
    }

    public ProfileSettingSyncSet(String jsonStr) {
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                syncs = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<AccountDomain>>() {
                });
            } catch (IOException e) {
                //
                GAlerter.lab("ProfileSettingSyncSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public Set<AccountDomain> getSyncs() {
        return syncs;
    }

    public void setSyncs(Set<AccountDomain> syncs) {
        this.syncs = syncs;
    }

    public void add(AccountDomain sync) {
        syncs.add(sync);
    }

    public void add(Set<AccountDomain> syncs) {
        this.syncs.addAll(syncs);
    }

    public void remove(AccountDomain sync) {
        syncs.remove(sync);
    }

    public boolean hasProvider(AccountDomain syncProvider) {
        return syncs.contains(syncProvider);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(syncs);
    }

    public static ProfileSettingSyncSet parse(String jsonStr) {
        ProfileSettingSyncSet returnValue = new ProfileSettingSyncSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<AccountDomain> profileSettingSync = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<Set<AccountDomain>>() {
                });

                returnValue.add(profileSettingSync);
            } catch (IOException e) {
                GAlerter.lab("ProfileSettingSyncSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
