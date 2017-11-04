/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.oauth;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbTypeException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class OAuthAccessorFactory {
    //
    private static Map<DataBaseType, AuthAppAccessor> authAppAccessorMap = new HashMap<DataBaseType, AuthAppAccessor>();
    private static Map<DataBaseType, AuthTokenAccessor> authTokenAccessorMap = new HashMap<DataBaseType, AuthTokenAccessor>();

    public static synchronized AuthAppAccessor factoryAuthAppAccessor(DataBaseType dataBaseType) throws DbTypeException {
        AuthAppAccessor accessor = authAppAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new AuthAppAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new AuthAppAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new AuthAppAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            authAppAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized AuthTokenAccessor factoryAuthTokenAccessor(DataBaseType dataBaseType) throws DbTypeException {
        AuthTokenAccessor accessor = authTokenAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new AuthTokenAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new AuthTokenAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new AuthTokenAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            authTokenAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }
}
