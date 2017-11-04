package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.tools.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 下午3:00
 * Desc:
 */
abstract class AbstractToolsAuditUserAccessor implements ToolsAuditUserAccessor {

    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractToolsAuditUserAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "AUDIT_USER_";
    private static final String KEY_SEQUENCE_NAME = "SEQ_TOOLS_AUDIT_USER_ID";
    private static final int TABLE_NUM = 100;

    @Override
    public AuditUser insertAuditUser(AuditUser entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(entry.getUno()));
            entry.setAuditId(getSeqNo(conn));

            //AUDITID,UNO,AUDITSTATUS,AUDITDATE,AUDITUSERID,AUDITIP
            pstmt.setString(1, entry.getAuditIp());
            pstmt.setString(2, entry.getUno());
            pstmt.setInt(3, entry.getAuditStatus().getValue());
            pstmt.setTimestamp(4, entry.getAuditDate() != null ? new Timestamp(entry.getAuditDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(5, entry.getAuditUserId());
            pstmt.setString(6, entry.getAuditIp());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert audit_user, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }


    @Override
    public boolean updateAuditUser(Long auditId, String uno, Map<ObjectField, Object> map, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getTableName(uno) + " SET " + ObjectFieldUtil.generateMapSetClause(map) + " WHERE AUDITID = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("Content update script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            int index = ObjectFieldUtil.setStmtParams(pstmt, 1, map);
            pstmt.setLong(index, auditId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            GAlerter.lab("On updateAuditUser a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }


    @Override
    public AuditUser findAuditUserByUno(String uno, Connection conn) throws DbException {
        AuditUser returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(uno) + " WHERE UNO=?";
        if (logger.isDebugEnabled()) {
            logger.debug("AuditUser findAuditUserByUno script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, uno);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On  AuditUser findAuditUserByUno, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public AuditUser findAuditUserByUnoAid(String uno, Long aid, Connection conn) throws DbException {
        AuditUser returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(uno) + " WHERE UNO=? AND AUDITID=?";
        if (logger.isDebugEnabled()) {
            logger.debug("AuditUser findAuditUserByUnoAid script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, uno);
             pstmt.setLong(2, aid);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On  AuditUser findAuditUserByUno, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }

    protected String getTableName(String uno) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }


    ///private methods.
    private String getInsertSql(String uno) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(uno)
                + " (AUDITID,UNO,AUDITSTATUS,AUDITDATE,AUDITUSERID,AUDITIP)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("AuditUserInsertSql INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    protected AuditUser rsToObject(ResultSet rs) throws SQLException {
        AuditUser entity = new AuditUser();
        entity.setAuditId(rs.getLong("AUDITID"));
        entity.setUno(rs.getString("UNO"));
        entity.setAuditStatus(AuditStatus.getByValue(rs.getInt("AUDITSTATUS")));
        entity.setAuditDate(rs.getDate("AUDITDATE"));
        entity.setAuditUserId(rs.getString("AUDITUSERID"));
        entity.setAuditIp(rs.getString("AUDITIP"));
        return entity;
    }
}
