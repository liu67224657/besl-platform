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
public class WanbaItemDomain implements Serializable {
    private static Map<Integer, WanbaItemDomain> map = new HashMap<Integer, WanbaItemDomain>();

    public static final WanbaItemDomain MYANSER = new WanbaItemDomain(1);    //我的回答
    public static final WanbaItemDomain MYQUESTION = new WanbaItemDomain(2);      //我的提问
    public static final WanbaItemDomain ASKME = new WanbaItemDomain(3);      //回答我的

    public static final WanbaItemDomain GAMERECOMMEND = new WanbaItemDomain(4);
    public static final WanbaItemDomain RECOMMEND = new WanbaItemDomain(5);

    public static final WanbaItemDomain TIMELIMIT_ING = new WanbaItemDomain(6);
    public static final WanbaItemDomain TIMELIMIT_ACCEPT = new WanbaItemDomain(9);
    public static final WanbaItemDomain TIMELIMIT_EXPIRE = new WanbaItemDomain(10);

    public static final WanbaItemDomain INVITE_QUESTIONLIST = new WanbaItemDomain(7);//被邀请的列表
    public static final WanbaItemDomain ANSERLIST = new WanbaItemDomain(8);


    private int code;

    public WanbaItemDomain(int code) {
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


        if (code != ((WanbaItemDomain) o).code) return false;

        return true;
    }

    public static WanbaItemDomain getByCode(int c) {
        return map.get(c);
    }

    public static Collection<WanbaItemDomain> getAll() {
        return map.values();
    }
}
