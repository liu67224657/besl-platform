package com.enjoyf.platform.service.tools;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The editor stats item type object.
 */
public class EditorStatsItemSubType implements Serializable {
    //
    private static Map<String, EditorStatsItemSubType> map = new LinkedHashMap<String, EditorStatsItemSubType>();

    //the initialize status
    public static EditorStatsItemSubType PINGCE = new EditorStatsItemSubType("pingce");
    public static EditorStatsItemSubType ZHUANTI = new EditorStatsItemSubType("zhuanti");
    public static EditorStatsItemSubType GONGLUE = new EditorStatsItemSubType("gonglue");

    private String code;

    private EditorStatsItemSubType(String code) {
        this.code = code.toLowerCase();
        map.put(this.code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "EditorStatsItemType, code:" + code;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof EditorStatsItemSubType && code.equals(((EditorStatsItemSubType) obj).getCode());
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static EditorStatsItemSubType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<EditorStatsItemSubType> getAll() {
        return map.values();
    }
}
