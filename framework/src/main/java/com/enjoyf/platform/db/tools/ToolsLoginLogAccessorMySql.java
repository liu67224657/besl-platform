package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.service.tools.ToolsLoginLog;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: taijunli
 * Date: 12-1-18
 * Time: 下午1:04
 * Desc:
 */
class ToolsLoginLogAccessorMySql extends AbstractToolsLoginLogAccessor {
    private static final Logger logger = LoggerFactory.getLogger(ToolsLoginLogAccessorMySql.class);


    @Override
    public List<ToolsLoginLog> queryLoginLogs(ToolsLoginLog entity, Pagination p, Connection conn) throws DbException {
        List<ToolsLoginLog> returnValue = new ArrayList<ToolsLoginLog>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ").append(getTableName()).append(" WHERE 1=1");

        if (entity != null && !Strings.isNullOrEmpty(entity.getUserId())) {
            sql.append(" AND USERID = ?");
        }
        if (entity != null && entity.getStartTime() != null) {
            sql.append(" AND LOGINTIME >= ?");
        }
        if (entity != null && entity.getEndTime() != null) {
            sql.append(" AND LOGINTIME <= ?");
        }


        sql.append(" ORDER BY LOGINTIME DESC LIMIT ?, ?");

        if (logger.isDebugEnabled()) {
            logger.debug("queryLoginLogs script:" + sql);
        }

        try {

            int i = 1;

            p.setTotalRows(viewQueryLoginRowSize(entity, conn));

            pstmt = conn.prepareStatement(sql.toString());

            if (entity != null && !Strings.isNullOrEmpty(entity.getUserId())) {
                pstmt.setString(i++, entity.getUserId());
            }
            if (entity != null && entity.getStartTime() != null) {
                pstmt.setTimestamp(i++, new Timestamp(entity.getStartTime().getTime()));
            }
            if (entity != null && entity.getEndTime() != null) {
                pstmt.setTimestamp(i++, new Timestamp(entity.getEndTime().getTime()));
            }

            pstmt.setInt(i++, p.getStartRowIdx());
            pstmt.setInt(i++, p.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryLoginLogs, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return returnValue;
    }

    private int viewQueryLoginRowSize(ToolsLoginLog entity, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(1) FROM ").append(getTableName()).append(" WHERE 1=1");

        if (entity != null && !Strings.isNullOrEmpty(entity.getUserId())) {
            sql.append(" AND USERID = ?");
        }
        if (entity != null && entity.getStartTime() != null) {
            sql.append(" AND LOGINTIME >= ?");
        }
        if (entity != null && entity.getEndTime() != null) {
            sql.append(" AND LOGINTIME <= ?");
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" viewQueryLoginRowSize script:" + sql);
        }

        try {
            int i = 1;
            pstmt = conn.prepareStatement(sql.toString());

            if (entity != null && !Strings.isNullOrEmpty(entity.getUserId())) {
                pstmt.setString(i++, entity.getUserId());
            }
            if (entity != null && entity.getStartTime() != null) {
                pstmt.setTimestamp(i++, new Timestamp(entity.getStartTime().getTime()));
            }
            if (entity != null && entity.getEndTime() != null) {
                pstmt.setTimestamp(i++, new Timestamp(entity.getEndTime().getTime()));
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On viewQueryLoginRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

}
