/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.timeline;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbTypeException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class TimeLineAccessorFactory {

    private static Map<DataBaseType, TimeLineItemAccessor> timeLineItemAccessorMap = new HashMap<DataBaseType, TimeLineItemAccessor>();

    private static Map<DataBaseType, TimeLineDetailAccessor> itemDetailAccessorMap = new HashMap<DataBaseType, TimeLineDetailAccessor>();

    public static synchronized TimeLineItemAccessor factoryTimeLineItemAccessor(DataBaseType dataBaseType) throws DbTypeException {
        TimeLineItemAccessor accessor = timeLineItemAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new TimeLineItemAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new TimeLineItemAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new TimeLineItemAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            timeLineItemAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized TimeLineDetailAccessor factoryItemDetailAccessor(DataBaseType dataBaseType) throws DbTypeException {
        TimeLineDetailAccessor accessor = itemDetailAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new TimeLineDetailAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new TimeLineDetailAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new TimeLineDetailAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            itemDetailAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }
}
