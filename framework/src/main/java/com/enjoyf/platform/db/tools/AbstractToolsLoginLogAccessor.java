package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.tools.ToolsLoginLog;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Collections;
import java.util.List;

/**
 * Author: zhaoxin
 * Date: 11-11-2
 * Time: 上午11:39
 * Desc:
 */
class AbstractToolsLoginLogAccessor implements ToolsLoginLogAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractToolsLoginLogAccessor.class);
    private static final String KEY_SEQUENCE_NAME = "SEQ_TOOLS_LOG_ID";
    private static final String KEY_TABLE_NAME = "TOOLS_LOGIN_LOG";

    @Override
    public ToolsLoginLog getLoginLog(long id, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ToolsLoginLog entity = null;

        String sql = "SELECT * FROM " + getTableName() + " WHERE LID = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("  getLoginLog  Script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                entity = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On getLoginLog , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return entity;
    }

    @Override
    public ToolsLoginLog insertLoginLog(ToolsLoginLog entity, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = getInsertSql();

        try {
            entity.setLid(getSeqNo(conn));

            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, getSeqNo(conn));
            pstmt.setString(2, entity.getUserId());
            pstmt.setString(3, entity.getPassWord());
            pstmt.setString(4, entity.getSuccess().getCode());
            pstmt.setTimestamp(5, entity.getLoginTime() != null ? new Timestamp(entity.getLoginTime().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(6, entity.getAccIp());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On mothod insertLoginLog add a loginLog, a SQLException occured.", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return entity;
    }

    @Override
    public List<ToolsLoginLog> queryLoginLogs(ToolsLoginLog entity, Pagination p, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    protected String getTableName() throws DbException {
        return KEY_TABLE_NAME;
    }

    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }

    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + getTableName() + " (LID, USERID, PASSWORD, ISSUCCESS, LOGINTIME, ACCIP) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug(" getLoginInsertSql Script:" + insertSql);
        }

        return insertSql;
    }

    //LID, USERID, PASSWORD, ISSUCCESS, LOGINTIME, ACCIP
    protected ToolsLoginLog rsToObject(ResultSet rs) throws SQLException {
        ToolsLoginLog entity = new ToolsLoginLog();

        entity.setLid(rs.getInt("LID"));
        entity.setUserId(rs.getString("USERID"));
        entity.setPassWord(rs.getString("PASSWORD"));
        entity.setSuccess(ActStatus.getByCode(rs.getString("ISSUCCESS")));
        entity.setLoginTime(rs.getDate("LOGINTIME"));
        entity.setAccIp(rs.getString("ACCIP"));

        return entity;
    }
}
