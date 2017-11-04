/**
 * (C) 2009 Fivewh platform platform.com
 */
package com.enjoyf.platform.service.stats;

import java.io.Serializable;
import java.util.Map;

import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class PageStatSectionItems implements Serializable {
    // the page
    private Pagination page;
    private Map<StatSection, StatItem> statItems;

    /////////////////////////////////////////////////////////
    public PageStatSectionItems() {
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }

    public Map<StatSection, StatItem> getStatItems() {
        return statItems;
    }

    public void setStatItems(Map<StatSection, StatItem> statItems) {
        this.statItems = statItems;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}

