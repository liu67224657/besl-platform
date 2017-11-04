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
public class SignPointType implements Serializable {
    private static Map<String, SignPointType> map = new HashMap<String, SignPointType>();

    /*

    * */
    public static final SignPointType ONE = new SignPointType("1", "10");  //
    public static final SignPointType TWO = new SignPointType("2", "15");
    public static final SignPointType THREE = new SignPointType("3", "20");
    public static final SignPointType FOUR = new SignPointType("4", "25");
    public static final SignPointType FIVE = new SignPointType("5", "30");
    public static final SignPointType SIX = new SignPointType("6", "35");
    public static final SignPointType SEVEN = new SignPointType("7", "40");

     public static final SignPointType OTHER = new SignPointType("8", "45");

    //
    private String code;
    private String value;

    public SignPointType(String appkey, String value) {
        this.code = appkey;
        this.value = value;
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "PointKeyType: code=" + code + " value=" + value;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof SignPointType)) {
            return false;
        }

        return code.equalsIgnoreCase(((SignPointType) obj).getCode());
    }

    public static SignPointType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<SignPointType> getAll() {
        return map.values();
    }
}
