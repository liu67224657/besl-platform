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
public class CommentReplyCustomerStatus implements Serializable {
    private static Map<Integer, CommentReplyCustomerStatus> map = new HashMap<Integer, CommentReplyCustomerStatus>();


    public static final CommentReplyCustomerStatus NOT = new CommentReplyCustomerStatus(0); //此评论无客服的回复
    public static final CommentReplyCustomerStatus ALREADY = new CommentReplyCustomerStatus(1);  //此评论有客服的回复
    public static final CommentReplyCustomerStatus ALREADY_CS = new CommentReplyCustomerStatus(2);  //此评论本身即是客服的回复
    private int code;

    private CommentReplyCustomerStatus(int c) {
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
        if ((obj == null) || !(obj instanceof CommentReplyCustomerStatus)) {
            return false;
        }

        return code == ((CommentReplyCustomerStatus) obj).getCode();
    }

    public static CommentReplyCustomerStatus getByCode(Integer c) {
        if (c == null) {
            return CommentReplyCustomerStatus.NOT;
        }

        return map.get(c) == null ? CommentReplyCustomerStatus.NOT : map.get(c);
    }

    public static Collection<CommentReplyCustomerStatus> getAll() {
        return map.values();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
