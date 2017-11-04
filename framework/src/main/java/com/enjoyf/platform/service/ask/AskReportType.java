package com.enjoyf.platform.service.ask;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2016/12/7 0007.
 */
public class AskReportType implements Serializable {

    private static Map<Integer, AskReportType> map = new HashMap<Integer, AskReportType>();

    public static final AskReportType CONTENT = new AskReportType(1);

    private int code;

    public AskReportType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AskReportType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AskReportType) o).code) return false;

        return true;
    }

    public static AskReportType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AskReportType> getAll() {
        return map.values();
    }
}
