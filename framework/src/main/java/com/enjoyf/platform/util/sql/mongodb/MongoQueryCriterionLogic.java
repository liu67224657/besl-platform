/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.sql.mongodb;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-23 下午2:19
 * Description:
 */
public class MongoQueryCriterionLogic implements Serializable {
    //
    public static final MongoQueryCriterionLogic LOGIC_AND = new MongoQueryCriterionLogic("$and");
    public static final MongoQueryCriterionLogic LOGIC_OR = new MongoQueryCriterionLogic("$or");

    //
    private String code = null;

    private MongoQueryCriterionLogic(String code) {
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

        if (obj instanceof MongoQueryCriterionLogic) {
            return code.equals(((MongoQueryCriterionLogic) obj).getCode());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return code.hashCode();
    }
}
