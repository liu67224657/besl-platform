package com.enjoyf.platform.service.content.social;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-15 下午1:29
 * Description:
 */
public class SocialReportType implements Serializable {

    private static Map<Integer, SocialReportType> map = new HashMap<Integer, SocialReportType>();

    public static final SocialReportType CONTENT = new SocialReportType(1);
    public static final SocialReportType REPLY = new SocialReportType(2);

    private int code;

    public SocialReportType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "SocialReportType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((SocialReportType) o).code) return false;

        return true;
    }

    public static SocialReportType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<SocialReportType> getAll() {
        return map.values();
    }
}
