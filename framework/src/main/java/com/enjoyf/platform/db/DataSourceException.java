/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db;

/**
 * @author Garrison
 */
@SuppressWarnings("serial")
public class DataSourceException extends DbException {
	
    public static final int DS_NOTFOUND = DbException.BASE_DB_DATASOURCE + 1;
    public static final int DS_MISS_CONFIGURE = DbException.BASE_DB_DATASOURCE + 2;
    public static final int DS_CREATE_ERROR = DbException.BASE_DB_DATASOURCE + 3;

    public DataSourceException(int val) {
        this(val, null);
    }
    
    public DataSourceException(int val, String name) {
    	super(val, name);
    }
}
