/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.sql;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-23 下午2:20
 * Description:
 */
public class QuerySort implements Serializable {
    //
    private ObjectField field;
    private QuerySortOrder order;

    public QuerySort(ObjectField field, QuerySortOrder order) {
        this.field = field;
        this.order = order;
    }

    public ObjectField getField() {
        return field;
    }

    public QuerySortOrder getOrder() {
        return order;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public static QuerySort add(ObjectField field, QuerySortOrder order) {
        return new QuerySort(field, order);
    }

    public static QuerySort add(ObjectField field) {
        return new QuerySort(field, QuerySortOrder.ASC);
    }
}
