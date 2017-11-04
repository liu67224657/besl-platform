/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbTypeException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class ProfileAccessorFactory {
    private static Map<DataBaseType, ProfileDetailAccessor> profileAccessorMap = new HashMap<DataBaseType, ProfileDetailAccessor>();
    private static Map<DataBaseType, ProfileSumAccessor> profileSumAccessorMap = new HashMap<DataBaseType, ProfileSumAccessor>();

    private static Map<DataBaseType, ProfileBlogAccessor> profileBlogAccessorMap = new HashMap<DataBaseType, ProfileBlogAccessor>();
    private static Map<DataBaseType, ProfileSettingAccessor> profileSettingAccessorMap = new HashMap<DataBaseType, ProfileSettingAccessor>();

    private static Map<DataBaseType, ProfileExperienceAccessor> profileExperienceAccessorMap = new HashMap<DataBaseType, ProfileExperienceAccessor>();

    public static synchronized ProfileDetailAccessor factoryProfileAccessor(DataBaseType dataBaseType) throws DbTypeException {
        ProfileDetailAccessor detailAccessor = profileAccessorMap.get(dataBaseType);

        if (detailAccessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                detailAccessor = new ProfileDetailAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                detailAccessor = new ProfileDetailAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                detailAccessor = new ProfileDetailAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            profileAccessorMap.put(dataBaseType, detailAccessor);
        }

        return detailAccessor;
    }

    public static synchronized ProfileSumAccessor factoryProfileSumAccessor(DataBaseType dataBaseType) throws DbTypeException {
        ProfileSumAccessor accessor = profileSumAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ProfileSumAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ProfileSumAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ProfileSumAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            profileSumAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }


    public static synchronized ProfileBlogAccessor factoryProfileBlogAccessor(DataBaseType dataBaseType) throws DbTypeException {
        ProfileBlogAccessor accessor = profileBlogAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ProfileBlogAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ProfileBlogAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ProfileBlogAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            profileBlogAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized ProfileSettingAccessor factoryProfileSettingAccessor(DataBaseType dataBaseType) throws DbTypeException {
        ProfileSettingAccessor accessor = profileSettingAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ProfileSettingAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ProfileSettingAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ProfileSettingAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            profileSettingAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized ProfileExperienceAccessor factoryProfilePersonInfoAccessor(DataBaseType dataBaseType) throws DbTypeException {
        ProfileExperienceAccessor accessor = profileExperienceAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new ProfileExperienceAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new ProfileExperienceAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new ProfileExperienceAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            profileExperienceAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

}
