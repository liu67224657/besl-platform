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
 * Time: 上午10:12
 * Description: 投票域
 */
public class VoteDomain implements Serializable {
    private static Map<String, VoteDomain> map = new HashMap<String, VoteDomain>();

    public static final VoteDomain DEFAULT = new VoteDomain("def");
    //the focus tags
    public static final VoteDomain CONTENT = new VoteDomain("content");
    public static final VoteDomain USER = new VoteDomain("user");
    public static final VoteDomain GAME = new VoteDomain("game");

    private String code;

    public VoteDomain(String c) {
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
        return "VoteDomain: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof VoteDomain)) {
            return false;
        }

        return code.equalsIgnoreCase(((VoteDomain) obj).getCode());
    }

    public static VoteDomain getByCode(String c) {
        if (StringUtil.isEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<VoteDomain> getAll() {
        return map.values();
    }
}
