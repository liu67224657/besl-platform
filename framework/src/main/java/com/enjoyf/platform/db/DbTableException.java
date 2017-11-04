/**
 * (C) 2010 Fivewh platform platform.com
 */
package com.enjoyf.platform.db;

public class DbTableException extends DbException {
	
    public static final int PARTITION_TABLE_NAME_ERROR = DbException.BASE_DB_TABLE + 1;

    protected DbTableException(int val, String name) {
        super(val, name);
    }
}
