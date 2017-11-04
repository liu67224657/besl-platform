package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.*;
import com.enjoyf.platform.service.joymeapp.AppErrorInfo;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-22
 * Time: 下午5:26
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractErrorInfoAccessor extends AbstractBaseTableAccessor<AppErrorInfo> implements ErrorInfoAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractErrorInfoAccessor.class);

    protected static final String KEY_TABLE_NAME = "app_errorinfo_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";

    @Override
    public AppErrorInfo insert(AppErrorInfo entry, Connection conn) throws DbException {

        PreparedStatement pstmt = null;

        try {

            pstmt = conn.prepareStatement(getInsertSql(entry.getErrorDate()));

            //clientid, appkey, platform, appversion, errorinfo, errordate, device, screen,osversion,ip,access_token,token_secr,reportdate
            pstmt.setString(1, entry.getClientId());
            pstmt.setString(2, entry.getAppKey());
            pstmt.setInt(3, entry.getPlatform());
            pstmt.setString(4, entry.getAppVersion());
            pstmt.setString(5, entry.getErrorInfo());
            pstmt.setTimestamp(6, new Timestamp(entry.getErrorDate().getTime()));
            pstmt.setString(7, entry.getDevice());
            pstmt.setString(8, entry.getScreen());
            pstmt.setString(9, entry.getOsVersion());
            pstmt.setString(10, entry.getIp());
            pstmt.setString(11, entry.getAccess_token());
            pstmt.setString(12, entry.getToken_scr());
            pstmt.setTimestamp(13,new Timestamp(entry.getReportDate()==null?System.currentTimeMillis():entry.getReportDate().getTime()));
            pstmt.setString(14,entry.getChannelId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert AppErrorInfo, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    private String getInsertSql(Date createDate) {
        String insertSql = "INSERT INTO " + getTableName(createDate) + " (clientid, appkey, platform, appversion, errorinfo, errordate, device, screen,osversion,ip,access_token,token_secr,reportdate,channelid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("AppErrorInfo INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    private String getTableName(Date d) {
        return KEY_TABLE_NAME + TableUtil.getTableDateSuffix(d, KEY_TABLE_SUFFIX_FMT);
    }

    @Override
    protected AppErrorInfo rsToObject(ResultSet rs) throws SQLException {
        return null;
    }
}
