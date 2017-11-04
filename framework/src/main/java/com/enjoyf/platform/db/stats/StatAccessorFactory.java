/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.stats;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbTypeException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
class StatAccessorFactory {
    private static Map<DataBaseType, StatItemAccessor> accessorMap = new HashMap<DataBaseType, StatItemAccessor>();

    public static synchronized StatItemAccessor statFactory(DataBaseType dataBaseType) throws DbTypeException {
        StatItemAccessor itemAccessor = accessorMap.get(dataBaseType);

        if (itemAccessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                itemAccessor = new StatItemAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                itemAccessor = new StatItemAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                itemAccessor = new StatItemAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT, "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            accessorMap.put(dataBaseType, itemAccessor);
        }

        return itemAccessor;
    }
}
