package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto: ericliu@enjoyfound.com> ericliu</a>
 */
public class HeaderMenu {
    private static Map<String, HeaderMenu> map = new HashMap<String, HeaderMenu>();

    public static final HeaderMenu HEAD_MENU_HOME = new HeaderMenu("home");
    public static final HeaderMenu HEAD_MENU_INDEX = new HeaderMenu("index");
    public static final HeaderMenu HEAD_MENU_DISCOVERY = new HeaderMenu("discovery");
    public static final HeaderMenu HEAD_MENU_GAME = new HeaderMenu("game");
    public static final HeaderMenu HEAD_MENU_HOT = new HeaderMenu("hot");
    public static final HeaderMenu HEADER_MENU_ABOUT = new HeaderMenu("about");
    public static final HeaderMenu HEADER_MENU_MYBLOG = new HeaderMenu("myblog");
    public static final HeaderMenu HEADER_MENU_TALENT = new HeaderMenu("talent");
    public static final HeaderMenu HEADER_MENU_GROUP = new HeaderMenu("group");
    public static final HeaderMenu HEADER_MENU_ACTIVITY = new HeaderMenu("activity");
   public static final HeaderMenu HEADER_MENU_GIFTMARKET = new HeaderMenu("giftmarket");



    private String code;

    public HeaderMenu(String code) {
        this.code = code;
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public static Map<String, HeaderMenu> getMap() {
        return map;
    }

    /**
     * get by code default null
     *
     * @param code
     * @return
     */
    public static HeaderMenu getByCode(String code) {
        return map.containsKey(code) ? map.get(code) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeaderMenu)) return false;

        HeaderMenu obj = (HeaderMenu) o;

        if (!code.equals(obj.getCode())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
