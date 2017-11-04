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
public class QuestionType implements Serializable {
    private static Map<Integer, QuestionType> map = new HashMap<Integer, QuestionType>();

    public static final QuestionType ONEONONE = new QuestionType(1);    //邀请
    public static final QuestionType TIMELIMIT = new QuestionType(2);      //限时

    private int code;

    public QuestionType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "QuestionType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((QuestionType) o).code) return false;

        return true;
    }

    public static QuestionType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<QuestionType> getAll() {
        return map.values();
    }
}
