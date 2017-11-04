/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.userprops;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbTypeException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class UserPropsAccessorFactory {
    private static Map<DataBaseType, UserPropsAccessor> accessorMap = new HashMap<DataBaseType, UserPropsAccessor>();

    public static UserPropsAccessor factory(DataBaseType dataBaseType) throws DbTypeException {
        UserPropsAccessor accessor = null;

        accessor = accessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new UserPropsAccessorSqlServer();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new AbstractUserPropsAccessor();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new UserPropsAccessorMySql();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            accessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }
}
