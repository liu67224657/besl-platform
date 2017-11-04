/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.message;


import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.message.Notice;
import com.enjoyf.platform.service.message.NoticeType;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class AbstractNoticeAccessor implements NoticeAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractNoticeAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "NOTICE_";
    private static final String KEY_SEQUENCE_NAME = "SEQ_NOTICE_ID";
    private static final int TABLE_NUM = 100;


    @Override
    public Notice insert(Notice notice, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            notice.setNoticeId(getSeqNo(conn));

            pstmt = conn.prepareStatement(getInsertSql(notice.getOwnUno()));

            //NOTICEID, OWNUNO, NOTICETYPE, NOTICEDESCRIPTION, NOTICECOUNT, VALIDSTATUS, NOTICEDATE, READDATE
            pstmt.setLong(1, notice.getNoticeId());

            pstmt.setString(2, notice.getOwnUno());
            pstmt.setString(3, notice.getNoticeType().getCode());
            pstmt.setString(4, notice.getDescription());
            pstmt.setInt(5, notice.getCount());
            pstmt.setString(6, notice.getValidStatus().getCode());

            pstmt.setTimestamp(7, notice.getNoticeDate() != null ? new Timestamp(notice.getNoticeDate().getTime()) : null);
            pstmt.setTimestamp(8, notice.getReadDate() != null ? new Timestamp(notice.getReadDate().getTime()) : null);

            //
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert Notice, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return notice;
    }

    @Override
    public boolean update(Notice notice, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("UPDATE " + getTableName(notice.getOwnUno()) + " SET NOTICEDESCRIPTION = ?, NOTICECOUNT = NOTICECOUNT + ?, NOTICEDATE = ? WHERE OWNUNO = ? AND NOTICETYPE = ?");

            pstmt.setString(1, notice.getDescription());
            pstmt.setLong(2, notice.getCount());
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            pstmt.setString(4, notice.getOwnUno());
            pstmt.setString(5, notice.getNoticeType().getCode());

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On update notice info, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public List<Notice> query(String ownUno, Connection conn) throws DbException {
        List<Notice> returnValue = new ArrayList<Notice>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND VALIDSTATUS = ? ");

            pstmt.setString(1, ownUno);
            pstmt.setString(2, ValidStatus.VALID.getCode());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On select notice, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public Notice get(String ownUno, NoticeType type, Connection conn) throws DbException {
        Notice returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND NOTICETYPE = ?");

            pstmt.setString(1, ownUno);
            pstmt.setString(2, type.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On select notice, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public boolean reset(String ownUno, NoticeType type, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            if (type.equals(NoticeType.BILLING_COIN)) {
                pstmt = conn.prepareStatement("UPDATE " + getTableName(ownUno) + " SET NOTICECOUNT = 0, READDATE = ?, VALIDSTATUS = '"+ ValidStatus.INVALID.getCode() +"' WHERE OWNUNO = ? AND NOTICETYPE = ?");
            } else {
                pstmt = conn.prepareStatement("UPDATE " + getTableName(ownUno) + " SET NOTICECOUNT = 0, READDATE = ? WHERE OWNUNO = ? AND NOTICETYPE = ?");
            }


            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(2, ownUno);
            pstmt.setString(3, type.getCode());

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On update notice info, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean reset(String ownUno, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("UPDATE " + getTableName(ownUno) + " SET NOTICECOUNT = 0, READDATE = ? WHERE OWNUNO = ?");

            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(2, ownUno);

            //why it's >= 0, when no notice.
            return pstmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            GAlerter.lab("On update notice info, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean resetExcludeNC(String ownUno, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("UPDATE " + getTableName(ownUno) + " SET NOTICECOUNT = 0, READDATE = ? WHERE OWNUNO = ? AND NOTICETYPE != 'nc'");

            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(2, ownUno);

            //why it's >= 0, when no notice.
            return pstmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            GAlerter.lab("On update notice info, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean updateField(String ownUno, NoticeType type, Map<ObjectField, Object> keyValues, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getUpdateScript(ownUno, keyValues));

            int index = ObjectFieldUtil.setStmtParams(pstmt, 1, keyValues);
            pstmt.setString(index++, ownUno);
            pstmt.setString(index++, type.getCode());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            GAlerter.lab("On update, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }

    ///private methods.
    private String getInsertSql(String uno) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(uno)
                + " (NOTICEID, OWNUNO, NOTICETYPE, NOTICEDESCRIPTION, NOTICECOUNT, VALIDSTATUS, NOTICEDATE, READDATE)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("Notice INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    protected String getTableName(String uno) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }

    protected Notice rsToObject(ResultSet rs) throws SQLException {
        Notice entry = new Notice();

        //NOTICEID, OWNUNO, NOTICETYPE, NOTICEDESCRIPTION, NOTICECOUNT, VALIDSTATUS, NOTICEDATE, READDATE
        entry.setNoticeId(rs.getLong("NOTICEID"));

        entry.setOwnUno(rs.getString("OWNUNO"));
        entry.setNoticeType(NoticeType.getByCode(rs.getString("NOTICETYPE")));

        entry.setDescription(rs.getString("NOTICEDESCRIPTION"));
        entry.setCount(rs.getInt("NOTICECOUNT"));
        entry.setValidStatus(rs.getString("VALIDSTATUS") != null ? ValidStatus.getByCode(rs.getString("VALIDSTATUS")) : ValidStatus.VALID);

        entry.setNoticeDate(rs.getTimestamp("NOTICEDATE") != null ? new Date(rs.getTimestamp("NOTICEDATE").getTime()) : null);
        entry.setReadDate(rs.getTimestamp("READDATE") != null ? new Date(rs.getTimestamp("READDATE").getTime()) : null);

        return entry;
    }

    protected String getUpdateScript(String uno, Map<ObjectField, Object> keyValues) throws DbException {
        StringBuffer returnValue = new StringBuffer();

        returnValue.append("UPDATE ").append(getTableName(uno));

        if (keyValues != null && keyValues.size() > 0) {
            returnValue.append(" SET ").append(ObjectFieldUtil.generateMapSetClause(keyValues));
        }

        returnValue.append(" WHERE OWNUNO = ? AND NOTICETYPE = ?");

        if (logger.isDebugEnabled()) {
            logger.debug("Notice update sql:" + returnValue.toString());
        }

        return returnValue.toString();
    }

}
