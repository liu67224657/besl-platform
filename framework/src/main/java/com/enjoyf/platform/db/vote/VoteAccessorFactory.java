/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbTypeException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-20
 * Time: 上午16:24
 * To change this template use File | Settings | File Templates.
 */
class VoteAccessorFactory {

    private static Map<DataBaseType, VoteSubjectAccessor> voteSubjectAccessorMap = new HashMap<DataBaseType, VoteSubjectAccessor>();

    private static Map<DataBaseType, VoteOptionAccessor> voteOptionAccessorMap = new HashMap<DataBaseType, VoteOptionAccessor>();

    private static Map<DataBaseType, VoteSubjectRecordAccessor> voteSubjectRecordAccessorMap = new HashMap<DataBaseType, VoteSubjectRecordAccessor>();

    private static Map<DataBaseType, VoteUserRecordAccessor> voteUserRecordAccessorMap = new HashMap<DataBaseType, VoteUserRecordAccessor>();

    public static synchronized VoteSubjectAccessor factoryVoteSubjectAccessor(DataBaseType dataBaseType) throws DbTypeException {
        VoteSubjectAccessor subjectAccessor = voteSubjectAccessorMap.get(dataBaseType);

        if (subjectAccessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                subjectAccessor = new VoteSubjectAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                subjectAccessor = new VoteSubjectAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                subjectAccessor = new VoteSubjectAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            voteSubjectAccessorMap.put(dataBaseType, subjectAccessor);
        }

        return subjectAccessor;
    }

    public static synchronized VoteOptionAccessor factoryVoteOptionAccessor(DataBaseType dataBaseType) throws DbTypeException {
        VoteOptionAccessor optionAccessor = voteOptionAccessorMap.get(dataBaseType);

        if (optionAccessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                optionAccessor = new VoteOptionAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                optionAccessor = new VoteOptionAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                optionAccessor = new VoteOptionAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            voteOptionAccessorMap.put(dataBaseType, optionAccessor);
        }

        return optionAccessor;
    }

    public static synchronized VoteSubjectRecordAccessor factoryVoteSubjectRecordAccessor(DataBaseType dataBaseType) throws DbTypeException {
        VoteSubjectRecordAccessor recordSubjectAccessor = voteSubjectRecordAccessorMap.get(dataBaseType);

        if (recordSubjectAccessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                recordSubjectAccessor = new VoteSubjectRecordAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                recordSubjectAccessor = new VoteSubjectRecordAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                recordSubjectAccessor = new VoteSubjectRecordAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            voteSubjectRecordAccessorMap.put(dataBaseType, recordSubjectAccessor);
        }

        return recordSubjectAccessor;
    }
    
    public static synchronized VoteUserRecordAccessor factoryVoteUserRecordAccessor(DataBaseType dataBaseType) throws DbTypeException {
        VoteUserRecordAccessor recordUserAccessor = voteUserRecordAccessorMap.get(dataBaseType);

        if (recordUserAccessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                recordUserAccessor = new VoteUserRecordAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                recordUserAccessor = new VoteUserRecordAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                recordUserAccessor = new VoteUserRecordAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            voteUserRecordAccessorMap.put(dataBaseType, recordUserAccessor);
        }

        return recordUserAccessor;
    }
}
