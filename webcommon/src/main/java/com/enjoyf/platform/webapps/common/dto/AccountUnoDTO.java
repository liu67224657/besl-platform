package com.enjoyf.platform.webapps.common.dto;

import com.enjoyf.platform.service.account.AccountDomain;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class AccountUnoDTO {
    private String accountUno;
    private AccountDomain accountDomain;

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
}
