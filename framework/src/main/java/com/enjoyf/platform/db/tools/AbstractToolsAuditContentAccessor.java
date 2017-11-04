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
abstract class AbstractToolsAuditContentAccessor implements ToolsAuditContentAccessor {

    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractToolsAuditContentAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "AUDIT_CONTENT_";
    private static final String KEY_SEQUENCE_NAME = "SEQ_TOOLS_AUDIT_CONTENT_ID";
    private static final int TABLE_NUM = 100;


    @Override
    public AuditContent insertAuditContent(AuditContent entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(entry.getUno()));
            entry.setAuditId(getSeqNo(conn));

            //AUDITID,UNO,AUDITCONTENTID,CONTENTTYPE,AUDITUSERID,AUDITIP,AUDITSTATUS,AUDITDATE,AUDITRESULT
            pstmt.setString(1, entry.getAuditIp());
            pstmt.setString(2, entry.getUno());
            pstmt.setString(3, entry.getAuditContentId());
            pstmt.setString(4, entry.getContentType().getCode());
            pstmt.setString(5, entry.getAuditUserId());
            pstmt.setString(6, entry.getAuditIp());
            pstmt.setInt(7, entry.getAuditStatus().getValue());
            pstmt.setTimestamp(8, entry.getAuditDate() != null ? new Timestamp(entry.getAuditDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(9, entry.getAuditResultSet().toJsonStr());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert audit_content, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }


    @Override
    public boolean updateAuditContent(Long auditId, String uno, Map<ObjectField, Object> map, Connection conn) throws DbException {
       PreparedStatement pstmt = null;

        String sql = "UPDATE " + getTableName(uno) + " SET " + ObjectFieldUtil.generateMapSetClause(map) + " WHERE AUDITID = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("AuditContent update script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            int index = ObjectFieldUtil.setStmtParams(pstmt, 1, map);
            pstmt.setLong(index, auditId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            GAlerter.lab("On updateAuditContent a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public AuditContent findAuditContentById(Long aid,String cid,String uno, Connection conn) throws DbException {
        AuditContent returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(cid) + " WHERE AUDITID=?";
        if (logger.isDebugEnabled()) {
            logger.debug("AuditContent findAuditContentById script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, aid);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On  AuditContent findAuditContentById, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public AuditContent findAuditContentByCIdCT(String cid, ContentType contentType,String uno, Connection conn) throws DbException {
        AuditContent returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(cid) + " WHERE AUDITCONTENTID=? AND CONTENTTYPE=?";
        if (logger.isDebugEnabled()) {
            logger.debug("AuditContent findAuditContentById script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, cid);
            pstmt.setString(2, contentType.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On  AuditContent findAuditContentByCIdCT, a SQLException occured.", e);
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

    protected String getTableName(String cid) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(cid.hashCode(), TABLE_NUM);
    }


    ///private methods.
    private String getInsertSql(String cid) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(cid)
                + " (AUDITID,UNO,AUDITCONTENTID,CONTENTTYPE,AUDITUSERID,AUDITIP,AUDITSTATUS,AUDITDATE,AUDITRESULT)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("AuditContentInsertSql INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    protected AuditContent rsToObject(ResultSet rs) throws SQLException {
        AuditContent entity = new AuditContent();
        entity.setAuditId(rs.getLong("AUDITID"));
        entity.setUno(rs.getString("UNO"));
        entity.setAuditContentId(rs.getString("AUDITCONTENTID"));
        entity.setContentType(ContentType.getByCode(rs.getString("CONTENTTYPE")));
        entity.setAuditUserId(rs.getString("AUDITUSERID"));
        entity.setAuditIp(rs.getString("AUDITIP"));
        entity.setAuditStatus(AuditStatus.getByValue(rs.getInt("AUDITSTATUS")));
        entity.setAuditDate(rs.getDate("AUDITDATE"));
        entity.setAuditResultSet(AuditResultSet.parse(rs.getString("AUDITRESULT")));
        return entity;
    }

}
