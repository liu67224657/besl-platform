package com.enjoyf.platform.db;

@SuppressWarnings("serial")
public class DbTypeException extends DbException {
	
    public static final int DBTYPE_NOT_SUPPORT = BASE_DB_DATATYPE + 1;

    public DbTypeException(int val, String name) {
        super(val, name);
    }
}
