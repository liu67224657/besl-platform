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
public class QuestionStatus implements Serializable {
    private static Map<Integer, QuestionStatus> map = new HashMap<Integer, QuestionStatus>();

    public static final QuestionStatus INIT = new QuestionStatus(0);    //进行中
    public static final QuestionStatus ACCEPT = new QuestionStatus(1);      //已采纳
    public static final QuestionStatus TIMEOUT = new QuestionStatus(2);      //已过期


    private int code;

    public QuestionStatus(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "QuestionStatus: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((QuestionStatus) o).code) return false;

        return true;
    }

    public static QuestionStatus getByCode(int c) {
        return map.get(c);
    }

    public static Collection<QuestionStatus> getAll() {
        return map.values();
    }
}
