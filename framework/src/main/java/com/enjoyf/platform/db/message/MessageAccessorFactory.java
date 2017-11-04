/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.message;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbTypeException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class MessageAccessorFactory {
    private static Map<DataBaseType, MessageAccessor> messageAccessorMap = new HashMap<DataBaseType, MessageAccessor>();
    private static Map<DataBaseType, NoticeAccessor> noticeAccessorMap = new HashMap<DataBaseType, NoticeAccessor>();
    private static Map<DataBaseType, PushMsgAccessor> pushMsgAccessorMap = new HashMap<DataBaseType, PushMsgAccessor>();

    public static synchronized MessageAccessor factoryMessageAccessor(DataBaseType dataBaseType) throws DbTypeException {
        MessageAccessor accessor = messageAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new MessageAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new MessageAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new MessageAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            messageAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized NoticeAccessor factoryNoticeAccessor(DataBaseType dataBaseType) throws DbTypeException {
        NoticeAccessor accessor = noticeAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new NoticeAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new NoticeAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new NoticeAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            noticeAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

    public static synchronized PushMsgAccessor factoryPushMsgAccessor(DataBaseType dataBaseType) throws DbTypeException {
        PushMsgAccessor accessor = pushMsgAccessorMap.get(dataBaseType);

        if (accessor == null) {
            if (dataBaseType.equals(DataBaseType.DB_TYPE_ORACLE)) {
                accessor = new PushMsgAccessorOracle();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_MYSQL)) {
                accessor = new PushMsgAccessorMySql();
            } else if (dataBaseType.equals(DataBaseType.DB_TYPE_SQLSERVER)) {
                accessor = new PushMsgAccessorSqlServer();
            } else {
                throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT,
                        "The type of database, " + dataBaseType.getName() + ", is not supported.");
            }

            pushMsgAccessorMap.put(dataBaseType, accessor);
        }

        return accessor;
    }

}
