/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.viewline.autofillrule.ViewLineAutoFillContentRule;
import com.enjoyf.platform.service.viewline.autofillrule.ViewLineAutoFillGameRule;
import com.enjoyf.platform.service.viewline.autofillrule.ViewLineAutoFillProfileRule;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description: 元素类型
 */
public class ViewItemType implements Serializable {
    //content, profile, gameres
    private static Map<String, ViewItemType> map = new HashMap<String, ViewItemType>();

    //the content
    public static final ViewItemType CONTENT = new ViewItemType("content");
    public static final ViewItemType PROFILE = new ViewItemType("profile");
    public static final ViewItemType GAME = new ViewItemType("game");
    public static final ViewItemType GROUP = new ViewItemType("group");
    public static final ViewItemType WIKI = new ViewItemType("wiki");
    //自定义类型
    public static final ViewItemType CUSTOM = new ViewItemType("custom");

    public static final ViewItemType ACTIVITY = new ViewItemType("activity");

    //
    private String code;

    private ViewItemType(String c) {
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
        return "ViewItemType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ViewItemType)) {
            return false;
        }

        return code.equalsIgnoreCase(((ViewItemType) obj).getCode());
    }

    public ViewLineAutoFillRule parse(String jsonStr) {
        ViewLineAutoFillRule returnValue = null;

        //
        if (!Strings.isNullOrEmpty(jsonStr)) {
            TypeReference typeReference = null;

            //
            if (this.equals(ViewItemType.CONTENT)) {
                typeReference = new TypeReference<ViewLineAutoFillContentRule>() {
                };
            } else if (this.equals(ViewItemType.PROFILE)) {
                typeReference = new TypeReference<ViewLineAutoFillProfileRule>() {
                };
            } else if (this.equals(ViewItemType.GAME) || this.equals(ViewItemType.GROUP)) {
                typeReference = new TypeReference<ViewLineAutoFillGameRule>() {
                };
            } else if (this.equals(ViewItemType.CUSTOM)) {
                typeReference = new TypeReference(){
                };
            }

            if (typeReference != null) {
                //
                try {
                    returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, typeReference);
                } catch (IOException e) {
                    GAlerter.lab("ViewLineAutoFillRule parse error, jsonStr:" + jsonStr, e);
                }
            }
        }

        return returnValue;
    }

    public static ViewItemType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ViewItemType> getAll() {
        return map.values();
    }
}
