package com.enjoyf.platform.service.ask;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/10
 * Description:
 */
public class AskUserActionType implements Serializable {
    private static Map<Integer, AskUserActionType> map = new HashMap<Integer, AskUserActionType>();

    public static final AskUserActionType AGREEMENT = new AskUserActionType(1);    //点赞
    public static final AskUserActionType FOLLOW = new AskUserActionType(2);    //关注

    public static final AskUserActionType FAVORITE = new AskUserActionType(3);    //收藏
    public static final AskUserActionType INIVITED = new AskUserActionType(4);    //邀请

    private int code;

    public AskUserActionType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "QuestionActionType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AskUserActionType) o).code) return false;

        return true;
    }

    public static AskUserActionType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AskUserActionType> getAll() {
        return map.values();
    }
}
