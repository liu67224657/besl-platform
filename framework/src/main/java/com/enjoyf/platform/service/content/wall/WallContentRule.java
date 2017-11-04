package com.enjoyf.platform.service.content.wall;

import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.util.StringUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-3-13
 * Time: 下午2:12
 * To change this template use File | Settings | File Templates.
 */
public class WallContentRule implements Serializable{
    private static Map<String, WallContentRule> map = new HashMap<String, WallContentRule>();

    //has subject and content
    public static final WallContentRule SC = new WallContentRule("sc");
    //has subject or content
    public static final WallContentRule SORC = new WallContentRule("sorc");
    //not has subject and content
    public static final WallContentRule NONE = new WallContentRule("none");

    private String code;

    public WallContentRule(String c) {
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
        return "WallContentRule: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof WallBlockType)) {
            return false;
        }

        return code.equalsIgnoreCase(((WallBlockType) obj).getCode());
    }


    public static WallContentRule getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static WallContentRule getContentRule(Content content){
        if(!StringUtil.isEmpty(content.getSubject()) && !StringUtil.isEmpty(content.getContent())){
            return WallContentRule.SC;
        }

        if(!StringUtil.isEmpty(content.getSubject()) || !StringUtil.isEmpty(content.getContent())){
            return WallContentRule.SORC;
        }

        return WallContentRule.NONE;
    }

    public static Collection<WallContentRule> getAll() {
        return map.values();
    }
}
