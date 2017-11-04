package com.enjoyf.platform.service.message;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-27
 * Time: 下午3:43
 * To change this template use File | Settings | File Templates.
 */
public class UserRelationType implements Serializable {
    private static Map<Integer, UserRelationType> map = new HashMap<Integer, UserRelationType>();
    //打开应用（纯文本等）不做跳转
    public static final UserRelationType DEFAULT = new UserRelationType(0);

    public static final UserRelationType FOCUS = new UserRelationType(1);

    public static final UserRelationType UNFOCUS = new UserRelationType(2);

    //消息列表
    public static final UserRelationType WANBA_MESSAGE_LIST = new UserRelationType(9);


    private int code;

    public UserRelationType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return "NoticeType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof UserRelationType)) {
            return false;
        }

        return code == (((UserRelationType) obj).getCode());
    }

    public static UserRelationType getByCode(Integer c) {
        if (c == null) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<UserRelationType> getAll() {
        return map.values();
    }
}
