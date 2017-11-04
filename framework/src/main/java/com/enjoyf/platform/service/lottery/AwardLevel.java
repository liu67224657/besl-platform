package com.enjoyf.platform.service.lottery;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午1:09
 * To change this template use File | Settings | File Templates.
 */
public class AwardLevel implements Serializable {
    private static Map<Integer, AwardLevel> map = new HashMap<Integer, AwardLevel>();
    //一等奖
    public static final AwardLevel first  = new AwardLevel(1);
    //二等奖
    public static final AwardLevel second  = new AwardLevel(2);
    //三等奖
    public static final AwardLevel third  = new AwardLevel(3);
    //四等奖
    public static final AwardLevel four = new AwardLevel(4);
    //五等奖
    public static final AwardLevel five = new AwardLevel(5);
    //六等奖
    public static final AwardLevel six = new AwardLevel(6);
    //七等奖
    public static final AwardLevel seven = new AwardLevel(7);

    private int code;

    public AwardLevel(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "lottery_award_level: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AwardLevel) o).code) return false;

        return true;
    }

    public static AwardLevel getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AwardLevel> getAll() {
        return map.values();
    }
}
