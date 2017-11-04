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
public class UserExchangeType implements Serializable {
    private static Map<String, UserExchangeType> map = new HashMap<String, UserExchangeType>();

    //
    public static final UserExchangeType GET_CODE = new UserExchangeType("gcode"); //领取
    public static final UserExchangeType TAO_CODE = new UserExchangeType("tcode"); //淘码
    public static final UserExchangeType DOWNLOAD= new UserExchangeType("download"); //下载
    public static final UserExchangeType SHARE= new UserExchangeType("share"); //分享



    //
    private String code;

    public UserExchangeType(String c) {
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
        if ((obj == null) || !(obj instanceof UserExchangeType)) {
            return false;
        }

        return code.equalsIgnoreCase(((UserExchangeType) obj).getCode());
    }

    public static UserExchangeType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<UserExchangeType> getAll() {
        return map.values();
    }
}
