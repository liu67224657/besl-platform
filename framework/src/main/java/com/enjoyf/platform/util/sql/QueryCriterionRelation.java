/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.sql;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-23 下午2:19
 * Description:
 */
public class QueryCriterionRelation implements Serializable {
    //
    public static final QueryCriterionRelation IS_NULL = new QueryCriterionRelation("IS NULL");
    public static final QueryCriterionRelation IS_NOT_NULL = new QueryCriterionRelation("IS NOT NULL");

    //
    public static final QueryCriterionRelation IN = new QueryCriterionRelation("IN");
    public static final QueryCriterionRelation NOT_IN = new QueryCriterionRelation("NOT IN");

    //
    public static final QueryCriterionRelation BETWEEN = new QueryCriterionRelation("BETWEEN");


    //
    public static final QueryCriterionRelation EQ = new QueryCriterionRelation("=");
    public static final QueryCriterionRelation NE = new QueryCriterionRelation("<>");

    public static final QueryCriterionRelation GT = new QueryCriterionRelation(">");
    public static final QueryCriterionRelation LT = new QueryCriterionRelation("<");

    public static final QueryCriterionRelation GEQ = new QueryCriterionRelation(">=");
    public static final QueryCriterionRelation LEQ = new QueryCriterionRelation("<=");

    public static final QueryCriterionRelation LIKE = new QueryCriterionRelation("LIKE");

    //
    public static final QueryCriterionRelation BITEWISE_AND = new QueryCriterionRelation("&");
    public static final QueryCriterionRelation BITEWISE_OR = new QueryCriterionRelation("|");


    //
    private String code = null;

    private QueryCriterionRelation(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String toString() {
        return "QueryCriterionRelation: code->" + code;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof QueryCriterionRelation) {
            return code.equals(((QueryCriterionRelation) obj).getCode());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return code.hashCode();
    }
}
