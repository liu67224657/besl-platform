/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description: 分类角度
 */
public class ViewCategoryAspect implements Serializable {
    //
    private static Map<String, ViewCategoryAspect> map = new HashMap<String, ViewCategoryAspect>();
    private static Map<ViewItemType, Set<ViewCategoryAspect>> typeMap = new HashMap<ViewItemType, Set<ViewCategoryAspect>>();

    //the content aspects
    public static final ViewCategoryAspect CONTENT_GAME = new ViewCategoryAspect(ViewItemType.CONTENT, "game");
    public static final ViewCategoryAspect CONTENT_RANDOM = new ViewCategoryAspect(ViewItemType.CONTENT, "random");
    public static final ViewCategoryAspect CONTENT_DAILY = new ViewCategoryAspect(ViewItemType.CONTENT, "daily");
    public static final ViewCategoryAspect CONTENT_CHINAJOY = new ViewCategoryAspect(ViewItemType.CONTENT, "chinajoy");

    //the board aspect
    public static final ViewCategoryAspect CONTENT_BOARD = new ViewCategoryAspect(ViewItemType.CONTENT, "board");

    public static final ViewCategoryAspect CUSTOM_INDEX = new ViewCategoryAspect(ViewItemType.CUSTOM, "index");

    public static final ViewCategoryAspect CUSTOM_BAIKE = new ViewCategoryAspect(ViewItemType.CUSTOM, "baike");

    public static final ViewCategoryAspect CUSTOM_GAME_THUMB = new ViewCategoryAspect(ViewItemType.CUSTOM, "gamecover");

    public static final ViewCategoryAspect CONTENT_MAGAZINE = new ViewCategoryAspect(ViewItemType.CONTENT, "magazine");

    public static final ViewCategoryAspect GAME_RELATION= new ViewCategoryAspect(ViewItemType.CUSTOM, "gamerelation");

    public static final ViewCategoryAspect CUSTOM_GIFTINDEX= new ViewCategoryAspect(ViewItemType.CUSTOM, "giftindex");

    //
    private ViewItemType itemType;
    private String code;

    private ViewCategoryAspect(ViewItemType type, String suffix) {
        itemType = type;
        code = type.getCode() + "." + suffix.toLowerCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public ViewItemType getItemType() {
        return itemType;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "ViewCategoryAspect: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ViewCategoryAspect)) {
            return false;
        }

        return code.equalsIgnoreCase(((ViewCategoryAspect) obj).getCode());
    }

    public static ViewCategoryAspect getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ViewCategoryAspect> getAll() {
        return map.values();
    }
}
