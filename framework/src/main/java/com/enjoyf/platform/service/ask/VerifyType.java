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
public class VerifyType implements Serializable {
    private static Map<Integer, VerifyType> map = new HashMap<Integer, VerifyType>();

    public static final VerifyType COMMON = new VerifyType(0);    //默认
    public static final VerifyType FAMOUS = new VerifyType(1);      //达人

    private int code;

    public VerifyType(int code) {
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


        if (code != ((VerifyType) o).code) return false;

        return true;
    }

    public static VerifyType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<VerifyType> getAll() {
        return map.values();
    }
}
