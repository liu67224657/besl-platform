package com.enjoyf.platform.service.stats;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class StatDomainPrefix implements Serializable {

    private static Map<String, StatDomainPrefix> map = new HashMap<String, StatDomainPrefix>();

    public static final String KEY_DOMAIN_SEPARATOR = "|";

    //the content stats domains.
    public final static StatDomainPrefix DOMAIN_PREFIX_CONTENT = new StatDomainPrefix("cnt", "default");

    //the account default stats domains.
    public final static StatDomainPrefix DOMAIN_PREFIX_ACCOUNT = new StatDomainPrefix("acc", "default");

    //user event stats domains.
    public final static StatDomainPrefix DOMAIN_PREFIX_USER_EVENT = new StatDomainPrefix("uev", "default");

    //the account default stats domains.
    public final static StatDomainPrefix DOMAIN_PREFIX_OPS = new StatDomainPrefix("ops", "default");

    //advertise stats domains
    public final static StatDomainPrefix DOMAIN_PREFIX_ADVERTISE = new StatDomainPrefix("adv", "default");

    ////
    private String code;
    private String partitionCode;

    ///////////////////////////////////////////////////////////////////////////
    public StatDomainPrefix(String c, String pc) {
        this.code = c.toLowerCase();
        this.partitionCode = pc.toUpperCase();

        map.put(this.code, this);
    }

    public String getCode() {
        return code;
    }

    public String getPartitionCode() {
        return partitionCode;
    }

    ///////////////////////////////////////////////////////////////////////////
    public int hashCode() {
        return code.hashCode();
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof StatDomainPrefix)) {
            return false;
        }

        return code.equalsIgnoreCase(((StatDomainPrefix) obj).getCode());
    }

    public static StatDomainPrefix getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<StatDomainPrefix> getAll() {
        return map.values();
    }
}
