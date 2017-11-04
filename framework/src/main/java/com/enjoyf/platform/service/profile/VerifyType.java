package com.enjoyf.platform.service.profile;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-11-1
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
public class VerifyType implements Serializable{
    private static Map<String, VerifyType> map = new HashMap<String, VerifyType>();

    public static final VerifyType N_VERIFY = new VerifyType("N");
    /** 个人认证 */
    public static final VerifyType P_VERIFY = new VerifyType("P");
    /** 企业&公司认证 */
    public static final VerifyType C_VERIFY = new VerifyType("C");
    /** 行业人士 */
    public static final VerifyType I_VERIFY = new VerifyType("I");

    private String code;

    private VerifyType(String c) {
        code = c.toLowerCase();
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof VerifyType) {
            return code.equalsIgnoreCase(((VerifyType) obj).getCode());
        } else {
            return false;
        }
    }
    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "VerifyType: code=" + code;
    }

    public static VerifyType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }
        return map.get(c.toLowerCase());
    }

    public static Collection getAll() {
        return map.values();
    }  
}
