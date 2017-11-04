/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.sql.mongodb;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-23 下午2:19
 * Description:
 */
public class MongoOperators implements Serializable {
    //
    //
    public static final MongoOperators EQ = new MongoOperators("$eq");

    public static final MongoOperators IN = new MongoOperators("$in");
    public static final MongoOperators NOT_IN = new MongoOperators("$nin");
    public static final MongoOperators NE = new MongoOperators("$ne");

    public static final MongoOperators GT = new MongoOperators("$gt");
    public static final MongoOperators LT = new MongoOperators("$lt");

    public static final MongoOperators GEQ = new MongoOperators("$gte");
    public static final MongoOperators LEQ = new MongoOperators("$lte");

    public static final MongoOperators LIKE = new MongoOperators("like");

    //
    private String code = null;

    private MongoOperators(String code) {
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

        if (obj instanceof MongoOperators) {
            return code.equals(((MongoOperators) obj).getCode());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return code.hashCode();
    }
}
