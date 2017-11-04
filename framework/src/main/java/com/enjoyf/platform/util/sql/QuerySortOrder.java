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
public class QuerySortOrder implements Serializable {
    //
    public static final QuerySortOrder ASC = new QuerySortOrder("ASC");
    public static final QuerySortOrder DESC = new QuerySortOrder("DESC");

    //
    private String code = null;

    private QuerySortOrder(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String toString() {
        return "QuerySortOrder: code->" + code;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof QuerySortOrder) {
            return code.equals(((QuerySortOrder) obj).getCode());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return code.hashCode();
    }
}
