package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppStatInstallLog;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-7-25 上午10:59
 * Description:
 */
public abstract class AbstractAppStatInstallLogAccessor extends AbstractBaseTableAccessor<AppStatInstallLog> implements AppStatInstallLogAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAppStatInstallLogAccessor.class);

    private static final String KEY_TABLE_NAME = "app_stat_install_log_";
    private static final int TABLE_NUM = 10;


    @Override
    public AppStatInstallLog insert(AppStatInstallLog appStatInstallLog, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            //app_id, platform, dest_app_id, dest_app_name, dest_app_install_count,
            // app_log_sequence_id,create_date,create_time,display_order
            pstmt = conn.prepareStatement(getInsertSql(appStatInstallLog.getAppSeqId()), pstmt.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, appStatInstallLog.getAppId());
            pstmt.setInt(2, appStatInstallLog.getAppPlatform().getCode());
            pstmt.setLong(3, appStatInstallLog.getDestAppId());
            pstmt.setString(4, appStatInstallLog.getDestAppName());
            pstmt.setInt(5, appStatInstallLog.getDestInstallCount());
            pstmt.setLong(6, appStatInstallLog.getAppSeqId());
            pstmt.setDate(7, new Date(appStatInstallLog.getCreateTime() == null ? System.currentTimeMillis() : appStatInstallLog.getCreateTime().getTime()));
            pstmt.setTimestamp(8, new Timestamp(appStatInstallLog.getCreateTime() == null ? System.currentTimeMillis() : appStatInstallLog.getCreateTime().getTime()));
            pstmt.setInt(9, appStatInstallLog.getDisplayorder());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                appStatInstallLog.setAppInstallLogId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert appinfo, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeConnection(conn);
        }
        return appStatInstallLog;
    }


    @Override
    public List<AppStatInstallLog> query(long seqId, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(getTableName(seqId), queryExpress, conn);
    }

    @Override
    public int update(long seqId, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(getTableName(seqId), updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(long seqId, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(getTableName(seqId), queryExpress, conn);
    }

    @Override
    public AppStatInstallLog get(long seqId, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(getTableName(seqId), queryExpress, conn);
    }

    @Override
    protected AppStatInstallLog rsToObject(ResultSet rs) throws SQLException {
        AppStatInstallLog returnObj = new AppStatInstallLog();
        returnObj.setAppInstallLogId(rs.getLong("app_install_log_id"));
        returnObj.setAppId(rs.getLong("app_id"));
        returnObj.setAppPlatform(AppPlatform.getByCode(rs.getInt("platform")));
        returnObj.setDestAppId(rs.getLong("dest_app_id"));
        returnObj.setDestAppName(rs.getString("dest_app_name"));
        returnObj.setDestInstallCount(rs.getInt("dest_app_install_count"));
        returnObj.setAppSeqId(rs.getLong("app_log_sequence_id"));
        returnObj.setCreateDate(rs.getDate("create_date"));
        returnObj.setCreateTime(rs.getTimestamp("create_time"));
        returnObj.setDisplayorder(rs.getInt("display_order"));
        return returnObj;
    }


    protected String getTableName(long seqId) {
        return KEY_TABLE_NAME + TableUtil.getTableNumSuffix(seqId, TABLE_NUM);
    }

    private String getInsertSql(long seqId) {
        String insertSql = "INSERT INTO " + getTableName(seqId) + " (app_id, platform, dest_app_id, dest_app_name, dest_app_install_count, app_log_sequence_id,create_date,create_time,display_order) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";

        if (logger.isDebugEnabled()) {
            logger.debug("AppStatInstallLog INSERT Script:" + insertSql);
        }

        return insertSql;
    }

}
