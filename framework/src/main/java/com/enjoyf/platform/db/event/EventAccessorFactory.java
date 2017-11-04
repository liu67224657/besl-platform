/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbTypeException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class EventAccessorFactory {
    private static Map<DataBaseType, UserEventAccessor> userEventAccessorMap = new HashMap<DataBaseType, UserEventAccessor>();
    private static Map<DataBaseType, PageViewEventAccessor> pvEventAccessorMap = new HashMap<DataBaseType, PageViewEventAccessor>();
    private static Map<DataBaseType, PageViewLocationAccessor> pvLocationAccessorMap = new HashMap<DataBaseType, PageViewLocationAccessor>();
    private static Map<DataBaseType, PageViewEventStatsAccessor> pvEventStatsAccessorMap = new HashMap<DataBaseType, PageViewEventStatsAccessor>();

    public static synchronized UserEventAccessor factoryUserEventAccessor(DataBaseType dataBaseType) throws DbTypeException {
        UserEventAccessor accessor = userEventAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new UserEventAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new UserEventAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new UserEventAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            userEventAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized PageViewEventAccessor factoryPageViewEventAccessor(DataBaseType dataBaseType) throws DbTypeException {
        PageViewEventAccessor accessor = pvEventAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new PageViewEventAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new PageViewEventAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new PageViewEventAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            pvEventAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized PageViewLocationAccessor factoryPageViewLocationAccessor(DataBaseType dataBaseType) throws DbTypeException {
        PageViewLocationAccessor accessor = pvLocationAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new PageViewLocationAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new PageViewLocationAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new PageViewLocationAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            pvLocationAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized PageViewEventStatsAccessor factoryPageViewEventStatsAccessor(DataBaseType dataBaseType) throws DbTypeException {
        PageViewEventStatsAccessor accessor = pvEventStatsAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new PageViewEventStatsAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new PageViewEventStatsAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new PageViewEventStatsAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            pvEventStatsAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }
}
