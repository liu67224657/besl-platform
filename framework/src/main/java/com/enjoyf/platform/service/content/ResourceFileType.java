/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class ResourceFileType implements Serializable {
    private static Map<String, ResourceFileType> map = new HashMap<String, ResourceFileType>();

    //the default image type
    public static final ResourceFileType IMAGE = new ResourceFileType("image");

    //the haedicon image
    public static final ResourceFileType HEADICON = new ResourceFileType("headicon");

    //the app image
    public static final ResourceFileType APP = new ResourceFileType("app");

    //the game resource images
    public static final ResourceFileType GAME = new ResourceFileType("game");

    //the Original resource images
    public static final ResourceFileType ORIGINAL = new ResourceFileType("original");

    //the resource audio
    public static final ResourceFileType AUDIO = new ResourceFileType("audio");

    //the Original resource audio
    public static final ResourceFileType SOCIAL_APP_PIC = new ResourceFileType("socialapppic");

    //the Original resource audio
    public static final ResourceFileType WANBA = new ResourceFileType("wanba");
    public static final ResourceFileType VIDEO = new ResourceFileType("video");


    private String code;

    private ResourceFileType(String c) {
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
        return "ResourceFileType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ResourceFileType)) {
            return false;
        }

        return code.equalsIgnoreCase(((ResourceFileType) obj).getCode());
    }

    public static ResourceFileType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ResourceFileType> getAll() {
        return map.values();
    }
}
