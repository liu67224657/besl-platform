package com.enjoyf.platform.service.point;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-8-20 上午9:55
 * Description:
 */
public class UserExchangeDomain implements Serializable {
    private static Map<String, UserExchangeDomain> map = new HashMap<String, UserExchangeDomain>();

    //
    public static final UserExchangeDomain CLIENT = new UserExchangeDomain("client"); //客户端
    public static final UserExchangeDomain PC = new UserExchangeDomain("pc"); //PC
    public static final UserExchangeDomain WEIXIN = new UserExchangeDomain("weixin");
    public static final UserExchangeDomain DOWNLOADN = new UserExchangeDomain("download");
    public static final UserExchangeDomain SHARE= new UserExchangeDomain("share");



    //
    private String code;

    public UserExchangeDomain(String c) {
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
        return "UserExchangeType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof UserExchangeDomain)) {
            return false;
        }

        return code.equalsIgnoreCase(((UserExchangeDomain) obj).getCode());
    }

    public static UserExchangeDomain getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<UserExchangeDomain> getAll() {
        return map.values();
    }
}
