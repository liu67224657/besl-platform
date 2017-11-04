/**
 * (C) 2010 Fivewh platform platform.com
 */
package com.enjoyf.platform.service.stats;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public abstract class AbstractStatDomain implements StatDomain {
    protected static Map<String, StatDomain> map = new HashMap<String, StatDomain>();

    private StatDomainPrefix prefix;
    private String code;
    private boolean multiSection = false;

    protected AbstractStatDomain(String c, boolean multi) {
        code = c.toLowerCase();
        multiSection = multi;

        prefix = StatDomainPrefix.getByCode(c.substring(0, c.indexOf(StatDomainPrefix.KEY_DOMAIN_SEPARATOR)));

        if (prefix == null) {
            throw new RuntimeException("The stat domin is incorrect, so can't find the stat domian prefix.");
        }

        //
        map.put(code, this);
    }

    public StatDomainPrefix getDomainPrefix() {
        return prefix;
    }

    public String getCode() {
        return code;
    }

    public boolean isMultiSection() {
        return multiSection;
    }

    ////////////////////////////////////////////////////////////////
    public int hashCode() {
        return code.hashCode();
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof StatDomain)) {
            return false;
        }

        return code.equalsIgnoreCase(((StatDomain) obj).getCode());
    }
}
