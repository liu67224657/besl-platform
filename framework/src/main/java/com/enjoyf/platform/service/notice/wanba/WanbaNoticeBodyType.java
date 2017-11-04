package com.enjoyf.platform.service.notice.wanba;

import com.enjoyf.platform.service.notice.AbstractUserNoticeBody;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/23
 */
public class WanbaNoticeBodyType implements Serializable {
    private static Map<Integer, WanbaNoticeBodyType> map = new HashMap<Integer, WanbaNoticeBodyType>();

    public static WanbaNoticeBodyType QUESTION_NEWANSWER = new WanbaNoticeBodyType(1);
    public static WanbaNoticeBodyType QUESTION_ACCEPTANSWER = new WanbaNoticeBodyType(2);
    public static WanbaNoticeBodyType QUESTION_EXPIRE = new WanbaNoticeBodyType(3);
    public static WanbaNoticeBodyType QUESTION_FOLLOWQUESIONACCEPT = new WanbaNoticeBodyType(4);
    public static WanbaNoticeBodyType QUESTION_INVITE_ANSWERQUESTION = new WanbaNoticeBodyType(5);
    public static WanbaNoticeBodyType QUESTION_ONEONONE_ASK = new WanbaNoticeBodyType(6);
    public static WanbaNoticeBodyType QUESTION_VERIFY = new WanbaNoticeBodyType(7);


    private int code;

    public WanbaNoticeBodyType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "WanbaNoticeBodyType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((WanbaNoticeBodyType) o).code) return false;

        return true;
    }

    public static WanbaNoticeBodyType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<WanbaNoticeBodyType> getAll() {
        return map.values();
    }
}
