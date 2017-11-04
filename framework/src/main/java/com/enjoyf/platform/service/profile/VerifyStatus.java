package com.enjoyf.platform.service.profile;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class VerifyStatus implements Serializable {

    private static Map<String, VerifyStatus> map = new HashMap<String, VerifyStatus>();

    public static VerifyStatus ACCESS = new VerifyStatus("access");    //通过
    public static VerifyStatus AUDIT = new VerifyStatus("audit");     //审核中
    public static VerifyStatus DISAPPROVED = new VerifyStatus("disapproved");    //未通过
//    public static VerifyStatus REMOVE = new VerifyStatus("remove");   //封停

    private String code;

    public VerifyStatus(String code) {
        this.code = code.toLowerCase();

        map.put(this.code, this);
    }

    public String getCode() {
        return code;
    }


    public static Map<String, VerifyStatus> getStatusMap() {
        return map;
    }

    public String toString() {
        return "ValidStatus code:" + code;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof VerifyStatus) {
            return code.equalsIgnoreCase(((VerifyStatus) obj).getCode());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return code.hashCode();
    }

    public static VerifyStatus getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<VerifyStatus> getAll() {
        return map.values();
    }
}
