/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db;

/**
 * @author Garrison
 */
public class DbConnException extends DbException {
	
    public static final int CONN_GET_FAILED = DbException.BASE_DB_CONN + 1;
    public static final int CONN_POOL_GET_FAILED = DbException.BASE_DB_CONN + 11;

    public DbConnException(int val, String name) {
        super(val, name);
    }
}
