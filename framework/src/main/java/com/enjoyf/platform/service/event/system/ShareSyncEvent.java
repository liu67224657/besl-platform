package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.usercenter.ProfileFlag;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-26
 * Time: 上午11:48
 * To change this template use File | Settings | File Templates.
 */
public class ShareSyncEvent extends SystemEvent {
    private long shareId;
    private String profileUno;
    //todo usercenter
//    private Set<AccountDomain> accountDomainSet;
    private ProfileFlag profileFlag;

    public ShareSyncEvent() {
        super(SystemEventType.SHARE_EXCHANGE_GIFT);
    }

    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public String getProfileUno() {
        return profileUno;
    }

    public void setProfileUno(String profileUno) {
        this.profileUno = profileUno;
    }

//    public Set<AccountDomain> getAccountDomainSet() {
//        return accountDomainSet;
//    }

//    public void setAccountDomainSet(Set<AccountDomain> accountDomainSet) {
//        this.accountDomainSet = accountDomainSet;
//    }

    public ProfileFlag getProfileFlag() {
        return profileFlag;
    }

    public void setProfileFlag(ProfileFlag profileFlag) {
        this.profileFlag = profileFlag;
    }
}
