package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.util.StringUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-2-14
 * Time: 上午11:50
 * To change this template use File | Settings | File Templates.
 */
public class ProfileActiveStatus implements Serializable {
    private static Map<String, ProfileActiveStatus> map = new HashMap<String, ProfileActiveStatus>();
    public static ProfileActiveStatus INIT = new ProfileActiveStatus("init");
    public static ProfileActiveStatus FORBID_POST = new ProfileActiveStatus("forbidpost"); //限制发言
    public static ProfileActiveStatus FORBID_LOGIN = new ProfileActiveStatus("forbidlogin");//限制登陆
    public static ProfileActiveStatus BAN = new ProfileActiveStatus("ban");//封号

    private String code;

    private ProfileActiveStatus(String code) {
        this.code = code.toLowerCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public static ProfileActiveStatus getByCode(String code) {
        if(StringUtil.isEmpty(code)){
            return null;
        }
        return map.get(code.toLowerCase());
    }

    @Override
    public String toString() {
        return "ProfileActiveStatus{" +
                "code='" + code + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return o != null
                && o instanceof ProfileActiveStatus
                && ((ProfileActiveStatus) o).code.equalsIgnoreCase(this.code);

    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }
}
