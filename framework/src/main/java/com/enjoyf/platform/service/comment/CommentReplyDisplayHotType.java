/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@fivewh.com">Eric Liu</a>
 * Create time: 11-8-17 下午5:21
 * Description:  外部文章表
 */
public class CommentReplyDisplayHotType implements Serializable {
    private static Map<Integer, CommentReplyDisplayHotType> map = new HashMap<Integer, CommentReplyDisplayHotType>();

    //是否可以出现在热门
    public static final CommentReplyDisplayHotType ALLOW = new CommentReplyDisplayHotType(0); //可以
    public static final CommentReplyDisplayHotType NO_ALLOW = new CommentReplyDisplayHotType(1);  //不可以

    private int code;

    private CommentReplyDisplayHotType(int c) {
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
        if ((obj == null) || !(obj instanceof CommentReplyDisplayHotType)) {
            return false;
        }

        return code == ((CommentReplyDisplayHotType) obj).getCode();
    }

    public static CommentReplyDisplayHotType getByCode(Integer c) {
        if (c == null) {
            return CommentReplyDisplayHotType.ALLOW;
        }

        return map.get(c) == null ? CommentReplyDisplayHotType.ALLOW : map.get(c);
    }

    public static Collection<CommentReplyDisplayHotType> getAll() {
        return map.values();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
