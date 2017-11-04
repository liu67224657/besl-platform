/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;

import java.sql.SQLException;


/**
 * A class to define db exceptions.
 */

@SuppressWarnings("serial")
public class DbException extends ServiceException {

    public static final int BASE_DB_DATASOURCE = ServiceException.BASE_DB + 100;
    public static final int BASE_DB_LOCK = ServiceException.BASE_DB + 200;
    public static final int BASE_DB_CONN = ServiceException.BASE_DB + 300;
    public static final int BASE_DB_DATATYPE = ServiceException.BASE_DB + 400;
    public static final int BASE_DB_TABLE = ServiceException.BASE_DB + 500;

    public static final int DB_GENERIC = ServiceException.BASE_DB + 1;
    public static final int SQL_GENERIC = ServiceException.BASE_DB + 2;
    public static final int TABLE_SEQUENCE_ERROR = ServiceException.BASE_DB + 3;
    public static final int TABLE_NO_COLUMN = ServiceException.BASE_DB + 4;
    public static final int DUPLICATED_PK = ServiceException.BASE_DB + 5;

    public static final int ENTITY_PARAM_IS_NOT_EXISTS = ServiceException.BASE_DB + 6;

    public static final int BASE_DB_MONGODB_UNKONWHOST = BASE_DB_DATASOURCE + 1;

    private SQLException sqlException = null;
    private boolean shouldAlert = true;

    public DbException(SQLException cause) {
        super(getDbExceptionValue(cause), cause.getMessage(), null, cause);
        sqlException = cause;
    }

    public DbException(int val, String name) {
        super(val, name);
    }

    public DbException(int val, Throwable t) {
        super(val, t);
    }

    public DbException(int val, String name, boolean shouldAlert) {
        super(val, name);
        this.shouldAlert = shouldAlert;
    }

    public SQLException getSQLException() {
        return sqlException;
    }

    public boolean shouldAlert() {
        return shouldAlert;
    }

    public static int getDbExceptionValue(SQLException ex) {
        String sqlState = ex.getSQLState();
        if (StringUtil.isEmpty(sqlState)) {
            return DbException.SQL_GENERIC;
        }

        if ("23000".equals(sqlState)) {
            return DbException.DUPLICATED_PK;
        } else {
            return DbException.SQL_GENERIC;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());

        return sb.toString();
    }
}
