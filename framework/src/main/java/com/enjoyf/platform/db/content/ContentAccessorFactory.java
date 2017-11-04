/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbTypeException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class ContentAccessorFactory {
    private static Map<DataBaseType, ContentAccessor> contentAccessorMap = new HashMap<DataBaseType, ContentAccessor>();
    private static Map<DataBaseType, ResourceFileAccessor> resourceFileAccessorMap = new HashMap<DataBaseType, ResourceFileAccessor>();
    private static Map<DataBaseType, WallContentAccessor> wallContentAccessorMap = new HashMap<DataBaseType, WallContentAccessor>();
    private static Map<DataBaseType, ContentInteractionAccessor> interactionAccessorMap = new HashMap<DataBaseType, ContentInteractionAccessor>();
    private static Map<DataBaseType, ContentInteractionStatAccessor> interactionStatAccessorMap = new HashMap<DataBaseType, ContentInteractionStatAccessor>();

    public static synchronized ContentAccessor factoryContentAccessor(DataBaseType dataBaseType) throws DbTypeException {
        ContentAccessor accessor = contentAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ContentAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ContentAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ContentAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            contentAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized ResourceFileAccessor factoryResourceFileAccessor(DataBaseType dataBaseType) throws DbTypeException {
        ResourceFileAccessor accessor = resourceFileAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ResourceFileAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ResourceFileAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ResourceFileAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            resourceFileAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized ContentInteractionAccessor factoryInteractionAccessorAccessor(DataBaseType dataBaseType) throws DbTypeException {
        ContentInteractionAccessor accessor = interactionAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ContentInteractionAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ContentInteractionAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ContentInteractionAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            interactionAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized ContentInteractionStatAccessor factoryInteractionStatAccessorAccessor(DataBaseType dataBaseType) throws DbTypeException {
        ContentInteractionStatAccessor accessor = interactionStatAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ContentInteractionStatAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ContentInteractionStatAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ContentInteractionStatAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            interactionStatAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized WallContentAccessor factoryWallContentAccessor(DataBaseType dataBaseType) throws DbTypeException {
        WallContentAccessor accessor = wallContentAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new WallContentAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new WallContentAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new WallContentAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            wallContentAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

}
