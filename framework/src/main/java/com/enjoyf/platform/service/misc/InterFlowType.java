package com.enjoyf.platform.service.misc;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-14
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class InterFlowType implements Serializable {
    private static Map<Integer, InterFlowType> map = new HashMap<Integer, InterFlowType>();

    public static final InterFlowType TIEBA = new InterFlowType(1);
    public static final InterFlowType QQ = new InterFlowType(2);

    private Integer code;

    private InterFlowType(int i) {
        code = i;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return "InterFlowType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof InterFlowType)) {
            return false;
        }

        return code == ((InterFlowType) obj).getCode();
    }

    public static InterFlowType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<InterFlowType> getAll() {
        return map.values();
    }
}
