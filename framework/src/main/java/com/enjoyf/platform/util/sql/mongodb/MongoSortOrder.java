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
public class MongoSortOrder implements Serializable {
    //
    public static final MongoSortOrder ASC = new MongoSortOrder(1);
    public static final MongoSortOrder DESC = new MongoSortOrder(-1);

    //
    private int code = -1;

    private MongoSortOrder(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String toString() {
        return "QuerySortOrder: code->" + code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MongoSortOrder that = (MongoSortOrder) o;

        if (code != that.code) return false;

        return true;
    }

    public int hashCode() {
        return code;
    }
}
