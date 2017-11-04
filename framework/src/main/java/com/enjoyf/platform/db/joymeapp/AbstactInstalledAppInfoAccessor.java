package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.InstalledAppInfo;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-2
 * Time: 下午6:20
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstactInstalledAppInfoAccessor extends AbstractBaseTableAccessor<InstalledAppInfo> implements InstalledAppInfoAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstactInstalledAppInfoAccessor.class);


    protected static final String KEY_TABLE_NAME = "app_installapp_info";


    @Override
    public InstalledAppInfo insert(InstalledAppInfo entry, Connection conn) throws DbException {

        PreparedStatement pstmt = null;

        try {

            pstmt = conn.prepareStatement(getInsertSql());

            //clientid, clienttoken, appkey, installed_info
            pstmt.setString(1, entry.getClientId());
            pstmt.setString(2, entry.getClientToken());
            pstmt.setString(3, entry.getAppKey());
            pstmt.setString(4, entry.getInstallInfo());
            pstmt.setInt(5, entry.getPlatform());
            pstmt.setTimestamp(6, new Timestamp(entry.getCreateTime()!=null?entry.getCreateTime().getTime():System.currentTimeMillis()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            if(!(e instanceof MySQLIntegrityConstraintViolationException)){
                GAlerter.lab("On insert AppErrorInfo, a SQLException occured." + e);
            }
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (clientid, clienttoken, appkey, installed_info, platform,create_time) VALUES (?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("InstalledAppInfo INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    @Override
    protected InstalledAppInfo rsToObject(ResultSet rs) throws SQLException {
        return null;
    }
}
