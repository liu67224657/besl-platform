package com.enjoyf.webapps.joyme.dto.profile;

import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.profile.Profile;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-8-27 上午11:41
 * Description:
 */
public class UserDTO {
    private String accountUno;
    private AccountDomain accountDomain;
    private Profile profile;

    public String getAccountUno() {
        return accountUno;
    }

    public void setAccountUno(String accountUno) {
        this.accountUno = accountUno;
    }

    public AccountDomain getAccountDomain() {
        return accountDomain;
    }

    public void setAccountDomain(AccountDomain accountDomain) {
        this.accountDomain = accountDomain;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
