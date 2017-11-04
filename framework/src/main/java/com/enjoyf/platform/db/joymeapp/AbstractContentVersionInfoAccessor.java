package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.AppContentVersionInfo;
import com.enjoyf.platform.service.joymeapp.ContentPackageType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-22
 * Time: 下午5:26
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractContentVersionInfoAccessor extends AbstractSequenceBaseTableAccessor<AppContentVersionInfo> implements ContentVersionAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractContentVersionInfoAccessor.class);

    private static final String KEY_SEQUENCE_NAME = "SEQ_VERSION_CONTENT_ID";
    protected static final String KEY_TABLE_NAME = "app_update_info";


    @Override
    public AppContentVersionInfo insert(AppContentVersionInfo contentVersionInfo, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            contentVersionInfo.setId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            pstmt = conn.prepareStatement(getInsertSql());

            //id, appkey, curr_version, version_url, publish_date, createuserid, createuserip, removestatus,version_info
            pstmt.setLong(1, contentVersionInfo.getId());
            pstmt.setString(2, contentVersionInfo.getAppKey());
            pstmt.setLong(3, contentVersionInfo.getCurrent_version());
            pstmt.setString(4, contentVersionInfo.getVersion_url());
            pstmt.setTimestamp(5, new Timestamp(contentVersionInfo.getPublishDate() == null ? System.currentTimeMillis() : contentVersionInfo.getPublishDate().getTime()));
            pstmt.setString(6, contentVersionInfo.getCreateUserId());
            pstmt.setString(7, contentVersionInfo.getCreateIp());
            pstmt.setString(8, contentVersionInfo.getRemoveStatus().getCode());
            pstmt.setString(9, contentVersionInfo.getVersion_info());

            pstmt.setInt(10, contentVersionInfo.getPackageType());
            pstmt.setBoolean(11, contentVersionInfo.isNecessaryUpdate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert AppContentVersionInfo, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return null;
    }

    @Override
    protected AppContentVersionInfo rsToObject(ResultSet rs) throws SQLException {
        AppContentVersionInfo versionInfo = new AppContentVersionInfo();
        versionInfo.setId(rs.getInt("id"));
        versionInfo.setAppKey(rs.getString("appkey"));
        versionInfo.setCurrent_version(rs.getLong("curr_version"));
        versionInfo.setVersion_url(rs.getString("version_url"));
        versionInfo.setPublishDate(new Date(rs.getTimestamp("publish_date").getTime()));
        versionInfo.setCreateUserId(rs.getString("createuserid"));
        versionInfo.setCreateIp(rs.getString("createuserip"));
        versionInfo.setRemoveStatus(ActStatus.getByCode(rs.getString("removestatus")));
        versionInfo.setVersion_info(rs.getString("version_info"));

        versionInfo.setPackageType(rs.getInt("package_type"));

        versionInfo.setNecessaryUpdate(rs.getBoolean("necessary_update"));
        return versionInfo;
    }

    @Override
    public List<AppContentVersionInfo> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AppContentVersionInfo> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public AppContentVersionInfo get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public long getLastestVersionByAppKey(String appKey, Connection conn) throws DbException {
        long returnLong = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT MAX(curr_version) as last_vsersion FROM app_update_info WHERE appkey= ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            //pushmsgid, appkey, msgicon, msgsubject, shortmessage, options, pushstatus, createuserid,createdate
            pstmt.setString(1, appKey);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getLong("last_vsersion");
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryLastastMsgIdByPlatform, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnLong;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (id, appkey, curr_version, version_url, publish_date, createuserid, createuserip, removestatus,version_info,package_type,necessary_update) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("AppErrorInfo INSERT Script:" + insertSql);
        }

        return insertSql;
    }


}
