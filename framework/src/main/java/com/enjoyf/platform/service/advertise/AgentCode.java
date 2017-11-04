/**
 * (C) 2009 Fivewh platform platform.com
 */
package com.enjoyf.platform.service.advertise;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class AgentCode implements Serializable {

    private static Map<String, AgentCode> map = new LinkedHashMap<String, AgentCode>();


    public static final AgentCode DIANRU = new AgentCode("dianru");//tt语音
    private String code;

    private AgentCode(String code) {
        this.code = code.toLowerCase();

        map.put(this.code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "DepositChannel code:" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof AgentCode) {
            return code.equalsIgnoreCase(((AgentCode) obj).getCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static AgentCode getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<AgentCode> getAll() {
        return map.values();
    }
}
