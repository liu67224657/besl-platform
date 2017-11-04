package com.enjoyf.platform.service.search.solr;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-27
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class SolrCore implements Serializable {
    private static Map<String, SolrCore> map = new HashMap<String, SolrCore>();

    //用户
	public static final SolrCore USERS = new SolrCore("users");
    //wiki页面
    public static final SolrCore PAGES = new SolrCore("pages");

    private String core;

    public SolrCore(String c) {
        core = c.toLowerCase();
        map.put(core, this);
    }

    public String getCore() {
        return core;
    }

    @Override
    public String toString() {
        return "SolrCore: core=" + core;
    }

    @Override
    public int hashCode() {
        return core.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return core.equalsIgnoreCase(((SolrCore) o).getCore());
    }

    public static SolrCore getByCore(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }
        return map.get(c.toLowerCase());
    }

}
