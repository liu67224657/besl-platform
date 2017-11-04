/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util;

import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-23 上午10:49
 * Description:
 */
public class PageRows<T> implements Serializable {
    private Pagination page;
    private List<T> rows = new ArrayList<T>();
    public PageRows() {
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
