package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Author: zhaoxin
 * Date: 11-11-2
 * Time: 上午11:39
 * Desc:
 */
class AbstractToolsLogAccessor implements ToolsLogAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractToolsLogAccessor.class);
    private static final String KEY_SEQUENCE_NAME = "SEQ_TOOLS_LOG_ID";
    private static final String KEY_TABLE_NAME_PREFIX = "TOOLS_LOG_";
    private static final String KEY_TABLE_NAME = "TOOLS_LOGIN_LOG";

    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }


    protected String getTableName(Date d) throws DbException {
        String KEY_TABLE_SUFFIX_FMT = "yyyyMM";
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableDateSuffix(d, KEY_TABLE_SUFFIX_FMT);
    }

    @Override
    public ToolsLog getLog(long id, Date date, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ToolsLog entity = null;

        String sql = "SELECT * FROM " + getTableName(date) + " WHERE LID = ?";

        if (logger.isDebugEnabled()) {
            logger.debug(" ToolsLogAccessorMySql  getLog  Script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                entity = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On getLog , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return entity;
    }


    @Override
    public ToolsLog insertLog(ToolsLog entity, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = getInsertSql(entity.getOpTime());

        try {
            entity.setLid(getSeqNo(conn));

            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, (int) getSeqNo(conn));
            pstmt.setString(2, entity.getOperType() != null ? entity.getOperType().getModule() : null);
            pstmt.setString(3, entity.getOperType() != null ? entity.getOperType().getOper() : null);
            pstmt.setString(4, entity.getOpUserId());
            pstmt.setTimestamp(5, entity.getOpTime() != null ? new java.sql.Timestamp(entity.getOpTime().getTime()) : new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setString(6, entity.getOpIp());
            pstmt.setString(7, entity.getSrcId());
            pstmt.setString(8, entity.getOpBefore());
            pstmt.setString(9, entity.getOpAfter());
            pstmt.setString(10, entity.getDescription());

            pstmt.execute();
        } catch (SQLException e) {
            GAlerter.lab("On mothod insertLog add a log, a SQLException occured.", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return entity;
    }



    @Override
    public List<ToolsLog> queryLogs(ToolsLog entity, Pagination p, Connection conn) throws DbException {
        return Collections.emptyList();
    }


    private String getInsertSql(Date date) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(date) + " (LID, BTYPE, STYPE, OPUSERID, OPTIME, OPIP, SRCID, OPBEFORE, OPAFTER, DESCRIPTION) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("AbstractToolsLogAccessor INSERT Script:" + insertSql);
        }

        return insertSql;
    }


    //LID, BTYPE, STYPE, OPUSERID, OPTIME, OPIP, SRCID, OPBEFORE, OPAFTER, DESCRIPTION
    protected ToolsLog rsToObject(ResultSet rs) throws SQLException {
        ToolsLog entity = new ToolsLog();

        entity.setLid(rs.getInt("LID"));
        entity.setOperType(LogOperType.getByCode(rs.getString("BTYPE") + "." + rs.getString("STYPE")));
        entity.setOpUserId(rs.getString("OPUSERID"));
        entity.setOpTime(rs.getDate("OPTIME"));
        entity.setOpIp(rs.getString("OPIP"));
        entity.setSrcId(rs.getString("SRCID"));
        entity.setOpBefore(rs.getString("OPBEFORE"));
        entity.setOpAfter(rs.getString("OPAFTER"));
        entity.setDescription(rs.getString("DESCRIPTION"));

        return entity;
    }
}
