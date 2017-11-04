/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbTypeException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class SocialAccessorFactory {
    private static Map<DataBaseType, SocialRelationAccessor> socialRelationAccessorMap = new HashMap<DataBaseType, SocialRelationAccessor>();
    private static Map<DataBaseType, RelationCategoryAccessor> relationCategoryAccessorMap = new HashMap<DataBaseType, RelationCategoryAccessor>();
    private static Map<DataBaseType, InviteImportInfoAccessor> inviteAccessorMap = new HashMap<DataBaseType, InviteImportInfoAccessor>();

    public static synchronized SocialRelationAccessor factorySocialRelationAccessor(DataBaseType dataBaseType) throws DbTypeException {
        SocialRelationAccessor accessor = socialRelationAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new SocialRelationAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new SocialRelationAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new SocialRelationAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            socialRelationAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized RelationCategoryAccessor factoryRelationCategoryAccessor(DataBaseType dataBaseType) throws DbTypeException {
        RelationCategoryAccessor accessor = relationCategoryAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new RelationCategoryAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new RelationCategoryAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new RelationCategoryAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            relationCategoryAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized InviteImportInfoAccessor factoryInviteAccessor(DataBaseType dataBaseType) throws DbTypeException {
        InviteImportInfoAccessor accessorImport = inviteAccessorMap.get(dataBaseType);

        if (accessorImport == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessorImport = new InviteImportInfoAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessorImport = new InviteImportInfoAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessorImport = new InviteImportInfoAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            inviteAccessorMap.put(dataBaseType, accessorImport);
        }

        return accessorImport;
    }

}
