package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.tools.ToolsLog;
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
class ToolsLogAccessorMySql extends AbstractToolsLogAccessor {
    private static final Logger logger = LoggerFactory.getLogger(ToolsLogAccessorMySql.class);


    @Override
    public List<ToolsLog> queryLogs(ToolsLog entity, Pagination p, Connection conn) throws DbException {
        List<ToolsLog> returnValue = new ArrayList<ToolsLog>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ").append(getTableName(entity.getOpTime())).append(" WHERE 1=1");


        if (entity != null && !Strings.isNullOrEmpty(entity.getOpUserId())) {
            sql.append(" AND OPUSERID = ?");
        }
        if (entity != null && entity.getStartTime() != null) {
            sql.append(" AND OPTIME >= ?");
        }
        if (entity != null && entity.getEndTime() != null) {
            sql.append(" AND OPTIME <= ?");
        }
        if (entity != null && !Strings.isNullOrEmpty(entity.getOpAfter())) {
            sql.append(" AND OPAFTER like ?");
        }
        sql.append(" ORDER BY OPTIME DESC LIMIT ?, ?");

        if (logger.isDebugEnabled()) {
            logger.debug("queryLogs script:" + sql);
        }

        try {

            int i = 1;
            p.setTotalRows(viewQueryRowSize(entity, conn));
            pstmt = conn.prepareStatement(sql.toString());

            if (entity != null && !Strings.isNullOrEmpty(entity.getOpUserId())) {
                pstmt.setString(i++, entity.getOpUserId());
            }
            if (entity != null && entity.getStartTime() != null) {
                pstmt.setTimestamp(i++, new Timestamp(entity.getStartTime().getTime()));
            }
            if (entity != null && entity.getEndTime() != null) {
                pstmt.setTimestamp(i++, new Timestamp(entity.getEndTime().getTime()));
            }
            if (entity != null && !Strings.isNullOrEmpty(entity.getOpAfter())) {
                pstmt.setString(i++, "%" + entity.getOpAfter() + "%");
            }

            pstmt.setInt(i++, p.getStartRowIdx());
            pstmt.setInt(i++, p.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On query privilege user list, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return returnValue;
    }

    private int viewQueryRowSize(ToolsLog entity, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(1) FROM ").append(getTableName(entity.getOpTime())).append(" WHERE 1=1");


        if (entity != null && !Strings.isNullOrEmpty(entity.getOpUserId())) {
            sql.append(" AND OPUSERID = ?");
        }
        if (entity != null && entity.getStartTime() != null) {
            sql.append(" AND OPTIME >= ?");
        }
        if (entity != null && entity.getEndTime() != null) {
            sql.append(" AND OPTIME <= ?");
        }
        if (entity != null && !Strings.isNullOrEmpty(entity.getOpAfter())) {
            sql.append(" AND OPAFTER like ?");
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" viewQueryRowSize script:" + sql);
        }

        try {
            int i = 1;
            pstmt = conn.prepareStatement(sql.toString());


            if (entity != null && !Strings.isNullOrEmpty(entity.getOpUserId())) {
                pstmt.setString(i++, entity.getOpUserId());
            }
            if (entity != null && entity.getStartTime() != null) {
                pstmt.setTimestamp(i++, new Timestamp(entity.getStartTime().getTime()));
            }
            if (entity != null && entity.getEndTime() != null) {
                pstmt.setTimestamp(i++, new Timestamp(entity.getEndTime().getTime()));
            }
            if (entity != null && !Strings.isNullOrEmpty(entity.getOpAfter())) {
                pstmt.setString(i++, "%" + entity.getOpAfter() + "%");
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On  queryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

}
