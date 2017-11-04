package com.enjoyf.webapps.joyme.weblogic.comment;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-19
 * Time: 下午5:33
 * To change this template use File | Settings | File Templates.
 */
public class AllowCommentStatus {
    private static Map<Integer, AllowCommentStatus> map = new HashMap<Integer, AllowCommentStatus>();
    //不允许
    public static final AllowCommentStatus NO_ALLOW = new AllowCommentStatus(0);
    //允许
    public static final AllowCommentStatus ALLOW = new AllowCommentStatus(1);
    //封号
    public static final AllowCommentStatus FORBID_UNO = new AllowCommentStatus(-1);
    //封号
    public static final AllowCommentStatus FORBID_IP = new AllowCommentStatus(-2);

    //同一个对象一天之内20次评分
    public static final AllowCommentStatus TWENTY_TIMES_A_DAY_ONE_COMMENT = new AllowCommentStatus(-3);

    //一个用户一分钟内不能发送同样的评论内容
    public static final AllowCommentStatus NOT_POST_SAME_TEXT_INTERVAL = new AllowCommentStatus(-4);


    //一个用户15秒内不能发送评论内容
    public static final AllowCommentStatus NOT_POST_SAME_TEXT_FIFTEEN_INTERVAL = new AllowCommentStatus(-5);

    private int code;

    public AllowCommentStatus(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AllowCommentStatus) o).code) return false;

        return true;
    }

    public static AllowCommentStatus getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AllowCommentStatus> getAll() {
        return map.values();
    }
}
