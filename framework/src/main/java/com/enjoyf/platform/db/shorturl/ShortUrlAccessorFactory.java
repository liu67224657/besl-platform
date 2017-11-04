/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.shorturl;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbTypeException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class ShortUrlAccessorFactory {
    private static Map<DataBaseType, ShortUrlAccessor> socialRelationAccessorMap = new HashMap<DataBaseType, ShortUrlAccessor>();

    public static synchronized ShortUrlAccessor factoryShortUrlAccessor(DataBaseType dataBaseType) throws DbTypeException {
        ShortUrlAccessor accessor = socialRelationAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ShortUrlAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ShortUrlAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ShortUrlAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            socialRelationAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

}
