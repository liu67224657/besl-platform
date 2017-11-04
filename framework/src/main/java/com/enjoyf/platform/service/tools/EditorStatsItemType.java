package com.enjoyf.platform.service.tools;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The editor stats item type object.
 */
public class EditorStatsItemType implements Serializable {
    //
    private static Map<String, EditorStatsItemType> map = new LinkedHashMap<String, EditorStatsItemType>();

    //the initialize status
    public static EditorStatsItemType ARTICLE = new EditorStatsItemType("article");
    public static EditorStatsItemType GAME = new EditorStatsItemType("game");

    private String code;

    private EditorStatsItemType(String code) {
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
        return obj != null && obj instanceof EditorStatsItemType && code.equals(((EditorStatsItemType) obj).getCode());
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static EditorStatsItemType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<EditorStatsItemType> getAll() {
        return map.values();
    }
}
