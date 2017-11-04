package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SyncSet implements Serializable{
    private Set<AccountDomain> syncProviderSet=new HashSet<AccountDomain>();

     public SyncSet() {
    }

    public SyncSet(Set<AccountDomain> set) {
        this.syncProviderSet = set;
    }

    public SyncSet(Collection<AccountDomain> syncProviders) {
        this.syncProviderSet.addAll(syncProviders);
    }

    public void add(Set<AccountDomain> syncProviderSet) {
        this.syncProviderSet.addAll(syncProviderSet);
    }

    public void add(AccountDomain syncProvider) {
        this.syncProviderSet.add(syncProvider);
    }

    public Set<AccountDomain> getSyncProviderSet() {
        return syncProviderSet;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
