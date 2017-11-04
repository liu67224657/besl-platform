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
 * Time: 下午2:01
 * Description: 投票类型
 */
public class VoteOptionType implements Serializable {
    private static Map<String, VoteOptionType> map = new HashMap<String, VoteOptionType>();

    // 选译
    public static final VoteOptionType SELECT = new VoteOptionType("select");

    // 自定义
    public static final VoteOptionType CUSTOM = new VoteOptionType("custom");

    private String code;

    public VoteOptionType(String c) {
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
        return "VoteOptionType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof VoteOptionType)) {
            return false;
        }

        return code.equalsIgnoreCase(((VoteOptionType) obj).getCode());
    }

    public static VoteOptionType getByCode(String c) {
        if (StringUtil.isEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<VoteOptionType> getAll() {
        return map.values();
    }
}
