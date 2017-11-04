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
public class QueryCriterionLogic implements Serializable {
    //
    public static final QueryCriterionLogic LOGIC_AND = new QueryCriterionLogic("AND");
    public static final QueryCriterionLogic LOGIC_OR = new QueryCriterionLogic("OR");

    //
    private String code = null;

    private QueryCriterionLogic(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String toString() {
        return "QueryCriterionLogic: code->" + code;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof QueryCriterionLogic) {
            return code.equals(((QueryCriterionLogic) obj).getCode());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return code.hashCode();
    }
}
