package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-15
 * Time: 下午4:39
 * To change this template use File | Settings | File Templates.
 */
public class SocialContentPlatformDomain implements Serializable {

    private static Map<Integer, SocialContentPlatformDomain> map = new HashMap<Integer, SocialContentPlatformDomain>();

     public static final SocialContentPlatformDomain DEFAULT = new SocialContentPlatformDomain(-1);

    //the original type
    public static final SocialContentPlatformDomain IOS = new SocialContentPlatformDomain(0);

    public static final SocialContentPlatformDomain ANDROID = new SocialContentPlatformDomain(1);
    
    private int code;

    private SocialContentPlatformDomain(int c) {
        code = c;

        map.put(code, this);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof SocialContentPlatformDomain)) {
            return false;
        }

        return code == ((SocialContentPlatformDomain) obj).getCode();
    }

    public static SocialContentPlatformDomain getByCode(Integer c) {
        if (c == null) {
            return SocialContentPlatformDomain.DEFAULT;
        }

        return map.get(c) == null ? SocialContentPlatformDomain.DEFAULT : map.get(c);
    }

    public static Collection<SocialContentPlatformDomain> getAll() {
        return map.values();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
