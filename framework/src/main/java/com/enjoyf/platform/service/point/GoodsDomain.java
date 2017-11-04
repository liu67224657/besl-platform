package com.enjoyf.platform.service.point;

import com.google.common.base.Strings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-8-20 上午9:55
 * Description:
 */
public class GoodsDomain {
    private static Map<String, GoodsDomain> map = new HashMap<String, GoodsDomain>();

    //
    public static final GoodsDomain MARKET = new GoodsDomain("market");
    public static final GoodsDomain EXCHANGE = new GoodsDomain("exchange");


    //
    private String code;

    public GoodsDomain(String c) {
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
        return "GoodsDomain: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof GoodsDomain)) {
            return false;
        }

        return code.equalsIgnoreCase(((GoodsDomain) obj).getCode());
    }

    public static GoodsDomain getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<GoodsDomain> getAll() {
        return map.values();
    }
}
