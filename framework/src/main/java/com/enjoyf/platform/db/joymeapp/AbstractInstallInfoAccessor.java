package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.*;
import com.enjoyf.platform.service.joymeapp.AppInstallInfo;
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
public abstract class AbstractInstallInfoAccessor extends AbstractBaseTableAccessor<AppInstallInfo> implements InstallInfoAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractInstallInfoAccessor.class);

    protected static final String KEY_TABLE_NAME = "app_install_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";

    @Override
    public AppInstallInfo insert(AppInstallInfo entry, Connection conn) throws DbException {

        PreparedStatement pstmt = null;

        try {

            pstmt = conn.prepareStatement(getInsertSql(entry.getInstallDate()));

            //clientid, appkey, platform, appversion, channelid, installtype, device, screen, osversion,ip,installdate,access_token,token_secr,reportdate
            pstmt.setString(1, entry.getClientId());
            pstmt.setString(2, entry.getAppKey());
            pstmt.setInt(3, entry.getPlatform());
            pstmt.setString(4, entry.getAppVersion());
            pstmt.setString(5, entry.getChannelid());
            pstmt.setInt(6, entry.getInstallType());
            pstmt.setString(7, entry.getDevice());
            pstmt.setString(8, entry.getScreen());
            pstmt.setString(9, entry.getOsVersion());
            pstmt.setString(10, entry.getIp());
            pstmt.setTimestamp(11, new Timestamp(entry.getInstallDate().getTime()));
            pstmt.setString(12, entry.getAccess_token());
            pstmt.setString(13, entry.getToken_scr());
            pstmt.setTimestamp(14,new Timestamp(entry.getReportDate()==null?System.currentTimeMillis():entry.getReportDate().getTime()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert AppInstallInfo, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    private String getInsertSql(Date createDate) {
        String insertSql = "INSERT INTO " + getTableName(createDate) + " (clientid, appkey, platform, appversion, channelid, installtype, device, screen, osversion,ip,installdate,access_token,token_secr,reportdate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("AppInstallInfo INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    private String getTableName(Date d) {
        return KEY_TABLE_NAME + TableUtil.getTableDateSuffix(d, KEY_TABLE_SUFFIX_FMT);
    }

    @Override
    protected AppInstallInfo rsToObject(ResultSet rs) throws SQLException {
        return null;
    }
}
