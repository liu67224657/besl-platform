package com.enjoyf.platform.service.timeline;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-14
 * Time: 下午9:10
 * To change this template use File | Settings | File Templates.
 */
public class SocialTimeLineDomain implements Serializable {
    private static Map<String, SocialTimeLineDomain> map = new HashMap<String, SocialTimeLineDomain>();

    //the home page timeline
    public static final SocialTimeLineDomain HOME = new SocialTimeLineDomain("home");

    //the blog timeline
    public static final SocialTimeLineDomain BLOG = new SocialTimeLineDomain("blog");

    //the my miyou
    public static final SocialTimeLineDomain MY_MIYOU = new SocialTimeLineDomain("mymiyou");

    //ugc wiki
    public static final SocialTimeLineDomain MY_WIKISTATUS = new SocialTimeLineDomain("mywikistatus");//我的动态
    public static final SocialTimeLineDomain WIKISTATUS = new SocialTimeLineDomain("wikistatus");//好友动态

    private String code;


    public SocialTimeLineDomain(String c) {
        code = c.toLowerCase();

        map.put(code, this);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof SocialTimeLineDomain)) {
            return false;
        }

        return code.equalsIgnoreCase(((SocialTimeLineDomain) obj).getCode());
    }

    @Override
    public String toString() {
        return "SocialTimeLineDomain: code=" + code;
    }

    public static SocialTimeLineDomain getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<SocialTimeLineDomain> getAll() {
        return map.values();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
