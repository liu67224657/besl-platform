package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbTypeException;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 下午2:58
 * Desc:
 */
class ToolsAccessorFactory {

    private static Map<DataBaseType, ToolsAuditUserAccessor> accessorUserMap = new HashMap<DataBaseType, ToolsAuditUserAccessor>();
    private static Map<DataBaseType, ToolsAuditContentAccessor> accessorContentMap = new HashMap<DataBaseType, ToolsAuditContentAccessor>();

//    private static Map<DataBaseType, ToolsLogAccessor> accessorLogMap = new HashMap<DataBaseType, ToolsLogAccessor>();

    //privile
    private static Map<DataBaseType, ToolsPrivilegeUserAccessor> accessorPrivileUserMap = new HashMap<DataBaseType, ToolsPrivilegeUserAccessor>();
    private static Map<DataBaseType, ToolsPrivilegeRoleAccessor> accessorPrivileRolesMap = new HashMap<DataBaseType, ToolsPrivilegeRoleAccessor>();
    private static Map<DataBaseType, ToolsPrivilegeResAccessor> accessorPrivileResMap = new HashMap<DataBaseType, ToolsPrivilegeResAccessor>();
    private static Map<DataBaseType, ToolsLogAccessor> accessorPrivileLogMap = new HashMap<DataBaseType, ToolsLogAccessor>();
    private static Map<DataBaseType, ToolsLoginLogAccessor> accessorPrivileLoginLogMap = new HashMap<DataBaseType, ToolsLoginLogAccessor>();

    public static ToolsAuditContentAccessor auditContentFactory(DataBaseType dataBaseType) throws DbTypeException {
        ToolsAuditContentAccessor accessor = null;

        accessor = accessorContentMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ToolsAuditContentAccessorSqlServer();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ToolsAuditContentAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ToolsAuditContentAccessorMySql();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            accessorContentMap.put(dataBaseType, accessor);
        }

        return accessor;
    }


    public static ToolsAuditUserAccessor auditUserFactory(DataBaseType dataBaseType) throws DbTypeException {
        ToolsAuditUserAccessor accessor = null;

        accessor = accessorUserMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ToolsAuditUserAccessorSqlServer();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ToolsAuditUserAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ToolsAuditUserAccessorMySql();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            accessorUserMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

//
//    public static ToolsLogAccessor logFactory(DataBaseType dataBaseType) throws DbTypeException {
//        ToolsLogAccessor accessor = null;
//
//        accessor = accessorLogMap.get(dataBaseType);
//
//        if (accessor == null) {
//            if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
//                accessor = new ToolsLogAccessorSqlServer();
//            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
//                accessor = new ToolsLogAccessorOracle();
//            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
//                accessor = new ToolsLogAccessorMySql();
//            } else {
//                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
//                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
//            }
//
//            accessorLogMap.put(dataBaseType, accessor);
//        }
//
//        return accessor;
//    }

    //privile
    public static ToolsPrivilegeUserAccessor privileUserFactory(DataBaseType dataBaseType) throws DbTypeException {
        ToolsPrivilegeUserAccessor accessor = null;

        accessor = accessorPrivileUserMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ToolsPrivilegeUserAccessorSqlServer();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ToolsPrivilegeUserAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ToolsPrivilegeUserAccessorMySql();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            accessorPrivileUserMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static ToolsPrivilegeRoleAccessor privileRolesFactory(DataBaseType dataBaseType) throws DbTypeException {
        ToolsPrivilegeRoleAccessor accessor = null;

        accessor = accessorPrivileRolesMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ToolsPrivilegeRoleAccessorSqlServer();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ToolsPrivilegeRoleAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ToolsPrivilegeRoleAccessorMySql();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            accessorPrivileRolesMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static ToolsPrivilegeResAccessor privileResFactory(DataBaseType dataBaseType) throws DbTypeException {
        ToolsPrivilegeResAccessor accessor = null;

        accessor = accessorPrivileResMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ToolsPrivilegeResAccessorSqlServer();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ToolsPrivilegeResAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ToolsPrivilegeResAccessorMySql();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            accessorPrivileResMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static ToolsLogAccessor privileLogFactory(DataBaseType dataBaseType) throws DbTypeException {
        ToolsLogAccessor accessor = null;

        accessor = accessorPrivileLogMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ToolsLogAccessorSqlServer();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ToolsLogAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ToolsLogAccessorMySql();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            accessorPrivileLogMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static ToolsLoginLogAccessor privileLoginLogFactory(DataBaseType dataBaseType) throws DbTypeException {
        ToolsLoginLogAccessor accessor = null;

        accessor = accessorPrivileLoginLogMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ToolsLoginLogAccessorSqlServer();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ToolsLoginLogAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ToolsLoginLogAccessorMySql();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            accessorPrivileLoginLogMap.put(dataBaseType, accessor);
        }

        return accessor;
    }
}
