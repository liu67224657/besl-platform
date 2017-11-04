package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.util.StringUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-2-14
 * Time: 下午5:01
 * To change this template use File | Settings | File Templates.
 */
public class ProfileDomain implements Serializable {
    private static Map<String, ProfileDomain> map = new HashMap<String, ProfileDomain>();

    //all the normal profiles.
    public static ProfileDomain DEFAULT = new ProfileDomain("def");

    //the ef profiles. 公司内部，兼职 编辑
    public static ProfileDomain EF_STAFF = new ProfileDomain("ef.staff");
    public static ProfileDomain EF_INTERN = new ProfileDomain("ef.intern");
    public static ProfileDomain EF_EDITOR = new ProfileDomain("ef.editor");

    //推广相关 水军
    public static ProfileDomain PROMOTE_WT = new ProfileDomain("promote.wt");


    private String code;

    private ProfileDomain(String code) {
        this.code = code.toLowerCase();
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public static ProfileDomain getByCode(String c) {
        if (StringUtil.isEmpty(c)) {
            return null;
        }
        return map.get(c.toLowerCase());
    }

    public static Collection<ProfileDomain> getAll() {
        return map.values();
    }

    @Override
    public boolean equals(Object o) {
        return o != null
                && o instanceof ProfileDomain
                && ((ProfileDomain) o).code.equalsIgnoreCase(this.code);
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ProfileDomain{" +
                "code='" + code + '\'' +
                '}';
    }
}
