/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.account;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class AccountDomain implements Serializable {
    private static Map<String, AccountDomain> map = new HashMap<String, AccountDomain>();

    //default account domain..
    public static final AccountDomain DEFAULT = new AccountDomain("def");
    public static final AccountDomain CLIENT = new AccountDomain("client");    //account_client

    public static final AccountDomain SYNC_SINA_WEIBO = new AccountDomain("sinaweibo");
    public static final AccountDomain SYNC_QQ_WEIBO = new AccountDomain("qweibo");
    public static final AccountDomain SYNC_RENREN = new AccountDomain("renren");
    public static final AccountDomain SYNC_QPLUS = new AccountDomain("qplus");
    public static final AccountDomain SYNC_QQ = new AccountDomain("qq");
    public static final AccountDomain SYNC_WANYYI_WEIBO = new AccountDomain("wyweibo");

    public static final AccountDomain ACCOUNT_MOBILE = new AccountDomain("mobile");
    public static final AccountDomain ACCOUNT_WEIXIN = new AccountDomain("weixin");

    //
    private String code;

    public AccountDomain(String c) {
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
        return "AccountDomain: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof AccountDomain)) {
            return false;
        }

        return code.equalsIgnoreCase(((AccountDomain) obj).getCode());
    }

    public static AccountDomain getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<AccountDomain> getAll() {
        return map.values();
    }
}
