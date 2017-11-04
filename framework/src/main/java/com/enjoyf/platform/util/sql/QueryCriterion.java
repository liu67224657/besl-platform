/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.sql;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-23 下午2:19
 * Description:
 */
public class QueryCriterion implements Serializable {
    //
    private ObjectField field;

    //
    private QueryCriterionRelation relation = QueryCriterionRelation.EQ;
    private QueryCriterionRelation relation2;

    //
    private Object value;
    private Object value2;

    public QueryCriterion(ObjectField field, QueryCriterionRelation relation, Object value) {
        this.field = field;
        this.relation = relation;
        this.value = value;
    }

    public QueryCriterion(ObjectField field, QueryCriterionRelation relation, QueryCriterionRelation relation2, Object value, Object value2) {
        this.field = field;
        this.relation = relation;
        this.relation2 = relation2;
        this.value = value;
        this.value2 = value2;
    }

    public ObjectField getField() {
        return field;
    }

    public QueryCriterionRelation getRelation() {
        return relation;
    }

    public Object getValue() {
        return value;
    }

    public QueryCriterionRelation getRelation2() {
        return relation2;
    }

    public Object getValue2() {
        return value2;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
