package com.enjoyf.platform.service.point;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午1:51
 * To change this template use File | Settings | File Templates.
 */
public class UserPointDomain implements Serializable{
    private static Map<Integer, UserPointDomain> map = new HashMap<Integer, UserPointDomain>();

    //
    public static final UserPointDomain ACCOUNT = new UserPointDomain(0); //uno级别
    public static final UserPointDomain PROFILE = new UserPointDomain(1); //profile级别


    //
    private Integer code;

    public UserPointDomain(Integer c) {
        code = c;

        map.put(code, this);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "UserPointDomain: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof UserPointDomain)) {
            return false;
        }

        return code.intValue()==((UserPointDomain) obj).getCode().intValue();
    }

    public static UserPointDomain getByCode(Integer c) {
        if (c==null) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<UserPointDomain> getAll() {
        return map.values();
    }
}
