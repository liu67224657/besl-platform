package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.util.StringUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-19
 * Time: 下午1:45
 * Description: 投票结果可见性
 */
public class VoteVisible implements Serializable {
    private static Map<String, VoteVisible> map = new HashMap<String, VoteVisible>();

    //
    public static final VoteVisible ALL = new VoteVisible("all");
    public static final VoteVisible VOTED = new VoteVisible("voted");

    private String code;

    public VoteVisible(String c) {
        code = c.toLowerCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "VoteVisible: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof VoteVisible)) {
            return false;
        }

        return code.equalsIgnoreCase(((VoteVisible) obj).getCode());
    }

    public static VoteVisible getByCode(String c) {
        if (StringUtil.isEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<VoteVisible> getAll() {
        return map.values();
    }
}
