/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.sql;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-23 下午2:19
 * Description:
 */
public class QueryCriterions implements Serializable {
    //the logic
    private QueryCriterionLogic logic = QueryCriterionLogic.LOGIC_AND;

    //the criterions
    private List<QueryCriterions> criterionsList = new ArrayList<QueryCriterions>();
    private QueryCriterion criterion;

    //
    public QueryCriterions() {
    }

    private QueryCriterions(QueryCriterion criterion) {
        this.criterion = criterion;
    }

    private QueryCriterions(QueryCriterionLogic logic, QueryCriterion criterion) {
        this.logic = logic;
        this.criterion = criterion;
    }

    private QueryCriterions(QueryCriterionLogic logic, QueryCriterions[] criterionses) {
        this.logic = logic;
        Collections.addAll(this.criterionsList, criterionses);
    }

    private QueryCriterions(QueryCriterions[] criterionses) {
        Collections.addAll(this.criterionsList, criterionses);
    }

    public boolean isMultiCriterion() {
        return criterion == null && criterionsList.size() > 0;
    }

    public boolean hasCriterion() {
        return criterion != null || criterionsList.size() > 0;
    }

    //
    public QueryCriterionLogic getLogic() {
        return logic;
    }

    public void setLogic(QueryCriterionLogic logic) {
        this.logic = logic;
    }

    public List<QueryCriterions> getCriterionsList() {
        return criterionsList;
    }

    public QueryCriterion getCriterion() {
        return criterion;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }


    //OR logic operation
    public static QueryCriterions or(QueryCriterions left, QueryCriterions right) {
        QueryCriterions returnValue = new QueryCriterions();

        returnValue.setLogic(QueryCriterionLogic.LOGIC_OR);

        //
        returnValue.getCriterionsList().add(left);
        returnValue.getCriterionsList().add(right);

        //
        return returnValue;
    }

    //OR logic operation
    public static QueryCriterions or(QueryCriterions[] criterionses) {
        QueryCriterions returnValue = new QueryCriterions();

        returnValue.setLogic(QueryCriterionLogic.LOGIC_OR);
        for (QueryCriterions criterions : criterionses) {
            returnValue.getCriterionsList().add(criterions);
        }

        return returnValue;
    }

    //AND logic operation
    public static QueryCriterions and(QueryCriterions left, QueryCriterions right) {
        QueryCriterions returnValue = new QueryCriterions();

        returnValue.setLogic(QueryCriterionLogic.LOGIC_AND);

        //
        returnValue.getCriterionsList().add(left);
        returnValue.getCriterionsList().add(right);

        //
        return returnValue;
    }

    //AND logic operation
    public static QueryCriterions and(QueryCriterions[] criterionses) {
        QueryCriterions returnValue = new QueryCriterions();

        returnValue.setLogic(QueryCriterionLogic.LOGIC_AND);
        for (QueryCriterions criterions : criterionses) {
            returnValue.getCriterionsList().add(criterions);
        }

        return returnValue;
    }


    /////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////
    //field = ?
    public static QueryCriterions eq(ObjectField field, Object value) {
        return new QueryCriterions(new QueryCriterion(field, QueryCriterionRelation.EQ, value));
    }

    //field >= ?
    public static QueryCriterions geq(ObjectField field, Object value) {
        return new QueryCriterions(new QueryCriterion(field, QueryCriterionRelation.GEQ, value));
    }

    //field <= ?
    public static QueryCriterions leq(ObjectField field, Object value) {
        return new QueryCriterions(new QueryCriterion(field, QueryCriterionRelation.LEQ, value));
    }

    //field > ?
    public static QueryCriterions gt(ObjectField field, Object value) {
        return new QueryCriterions(new QueryCriterion(field, QueryCriterionRelation.GT, value));
    }

    //field < ?
    public static QueryCriterions lt(ObjectField field, Object value) {
        return new QueryCriterions(new QueryCriterion(field, QueryCriterionRelation.LT, value));
    }

    //field <> ?
    public static QueryCriterions ne(ObjectField field, Object value) {
        return new QueryCriterions(new QueryCriterion(field, QueryCriterionRelation.NE, value));
    }

    //field is not null
    public static QueryCriterions isNotNull(ObjectField field) {
        return new QueryCriterions(new QueryCriterion(field, QueryCriterionRelation.IS_NOT_NULL, null));
    }

    //field is null
    public static QueryCriterions isNull(ObjectField field) {
        return new QueryCriterions(new QueryCriterion(field, QueryCriterionRelation.IS_NULL, null));
    }

    //field in (?, ?, ?, ...)
    public static QueryCriterions in(ObjectField field, Object[] value) {
        return new QueryCriterions(new QueryCriterion(field, QueryCriterionRelation.IN, value));
    }

    //field not in (?, ?, ?, ...)
    public static QueryCriterions notIn(ObjectField field, Object[] value) {
        return new QueryCriterions(new QueryCriterion(field, QueryCriterionRelation.NOT_IN, value));
    }

    //field between ? AND ?
    public static QueryCriterions between(ObjectField field, Object left, Object right) {
        Object[] value = new Object[]{left, right};

        return new QueryCriterions(new QueryCriterion(field, QueryCriterionRelation.BETWEEN, value));
    }

    //field like ?
    public static QueryCriterions like(ObjectField field, Object value) {
        return new QueryCriterions(new QueryCriterion(field, QueryCriterionRelation.LIKE, value));
    }

    //OR bitwise operation: field | ? [=, >, < ...] ?
    public static QueryCriterions bitwiseOr(ObjectField field, QueryCriterionRelation relation2, int value, int value2) {
        return new QueryCriterions(new QueryCriterion(field, QueryCriterionRelation.BITEWISE_OR, relation2, value, value2));
    }

    //AND bitwise operation: field & ? [=, >, < ...] ?
    public static QueryCriterions bitwiseAnd(ObjectField field, QueryCriterionRelation relation2, int value, int value2) {
        return new QueryCriterions(new QueryCriterion(field, QueryCriterionRelation.BITEWISE_AND, relation2, value, value2));
    }
}
