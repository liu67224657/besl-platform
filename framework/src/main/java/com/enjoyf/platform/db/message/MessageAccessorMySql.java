package com.enjoyf.platform.db.message;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class MessageAccessorMySql extends AbstractMessageAccessor {

    @Override
    public List<Long> queryTopicIds(String ownUno, MessageType type, Pagination page, Connection conn) throws DbException {
        List<Long> returnValue = new ArrayList<Long>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            page.setTotalRows(queryRowSize(ownUno, type, conn));

            pstmt = conn.prepareStatement("SELECT TOPICID, MAX(SENDDATE) FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND MSGTYPE = ? AND REMOVESTATUS = ? GROUP BY TOPICID ORDER BY SENDDATE DESC LIMIT ?, ?");

            pstmt.setString(1, ownUno);
            pstmt.setString(2, type.getCode());
            pstmt.setString(3, ActStatus.UNACT.getCode());

            pstmt.setInt(4, page.getStartRowIdx());
            pstmt.setInt(5, page.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rs.getLong("TOPICID"));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryTopicIds, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<Message> query(String ownUno, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(getTableName(ownUno), queryExpress, page, conn);
    }

    @Override
    public List<Message> query(String ownUno, QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return query(getTableName(ownUno), queryExpress, range, conn);
    }

    private int queryRowSize(String ownUno, MessageType type, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(DISTINCT(TOPICID)) FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND MSGTYPE = ? AND REMOVESTATUS = ?";

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setString(2, type.getCode());
            pstmt.setString(3, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On MessageAccessorMySql queryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

    public List<String> querySenderUnos(String ownUno, MessageType type, Pagination page, Connection conn) throws DbException {
        List<String> returnValue = new ArrayList<String>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT DISTINCT(SENDERUNO) AS SUNO, MAX(SENDDATE) AS DA FROM (SELECT SENDERUNO,SENDDATE FROM " + getTableName(ownUno) +
                " WHERE OWNUNO = ? AND RECIEVERUNO = OWNUNO AND MSGTYPE = ? AND REMOVESTATUS = ? UNION SELECT RECIEVERUNO,SENDDATE FROM " +
                getTableName(ownUno) + " WHERE OWNUNO = ? AND SENDERUNO = OWNUNO AND MSGTYPE = ? AND REMOVESTATUS = ?) AS B GROUP BY SUNO ORDER BY DA DESC LIMIT ?, ?";

        try {
            page.setTotalRows(querySenderRowSize(ownUno, type, conn));

            pstmt = conn.prepareStatement(sql);


            pstmt.setString(1, ownUno);
            pstmt.setString(2, type.getCode());
            pstmt.setString(3, ActStatus.UNACT.getCode());

            pstmt.setString(4, ownUno);
            pstmt.setString(5, type.getCode());
            pstmt.setString(6, ActStatus.UNACT.getCode());

            pstmt.setInt(7, page.getStartRowIdx());
            pstmt.setInt(8, page.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rs.getString("SUNO"));
            }
        } catch (SQLException e) {
            GAlerter.lab("On querySenderUnos, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    private int querySenderRowSize(String ownUno, MessageType type, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(DISTINCT(SENDERUNO)) FROM (SELECT SENDERUNO FROM " + getTableName(ownUno) +
                " WHERE OWNUNO = ? AND RECIEVERUNO = OWNUNO AND MSGTYPE = ? AND REMOVESTATUS = ? UNION SELECT RECIEVERUNO FROM " +
                getTableName(ownUno) + " WHERE OWNUNO = ? AND SENDERUNO = OWNUNO AND MSGTYPE = ? AND REMOVESTATUS = ?) AS B";

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setString(2, type.getCode());
            pstmt.setString(3, ActStatus.UNACT.getCode());

            pstmt.setString(4, ownUno);
            pstmt.setString(5, type.getCode());
            pstmt.setString(6, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On MessageAccessorMySql querySenderRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

    @Override
    public List<Message> query(String ownUno, String senderUno, MessageType type, Pagination page, Connection conn) throws DbException {
        List<Message> returnValue = new ArrayList<Message>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = null;
        if (ownUno.equalsIgnoreCase(senderUno)) {
            sql = "SELECT * FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND SENDERUNO = ? AND RECIEVERUNO = ? AND MSGTYPE = ? AND REMOVESTATUS = ? ORDER BY SENDDATE DESC LIMIT ?, ?";
        } else if (!StringUtil.isEmpty(senderUno)) {
            sql = "SELECT * FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND (SENDERUNO = ? OR RECIEVERUNO = ?) AND MSGTYPE = ? AND REMOVESTATUS = ? ORDER BY SENDDATE DESC LIMIT ?, ?";
        } else {
            sql = "SELECT * FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND MSGTYPE = ? AND REMOVESTATUS = ? ORDER BY SENDDATE DESC LIMIT ?, ?";
        }

        try {
            page.setTotalRows(queryRowSize(ownUno, senderUno, type, conn));

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            if (!StringUtil.isEmpty(senderUno)) {
                pstmt.setString(2, senderUno);
                pstmt.setString(3, senderUno);
                pstmt.setString(4, type.getCode());

                pstmt.setString(5, ActStatus.UNACT.getCode());
                pstmt.setInt(6, page.getStartRowIdx());
                pstmt.setInt(7, page.getPageSize());
            } else {
                pstmt.setString(2, type.getCode());
                pstmt.setString(3, ActStatus.UNACT.getCode());
                pstmt.setInt(4, page.getStartRowIdx());
                pstmt.setInt(5, page.getPageSize());
            }


            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On select messages, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    private int queryRowSize(String ownUno, String senderUno, MessageType type, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = null;
        if (ownUno.equalsIgnoreCase(senderUno)) {
            sql = "SELECT COUNT(1) FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND SENDERUNO = ? AND RECIEVERUNO = ? AND MSGTYPE = ? AND REMOVESTATUS = ?";
        } else if (!StringUtil.isEmpty(senderUno)) {
            sql = "SELECT COUNT(1) FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND (SENDERUNO = ? OR RECIEVERUNO = ?) AND MSGTYPE = ? AND REMOVESTATUS = ?";
        } else {
            sql = "SELECT * FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND MSGTYPE = ? AND REMOVESTATUS = ?";
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            if (!StringUtil.isEmpty(senderUno)) {
                pstmt.setString(2, senderUno);
                pstmt.setString(3, senderUno);
                pstmt.setString(4, type.getCode());
                pstmt.setString(5, ActStatus.UNACT.getCode());
            } else {
                pstmt.setString(2, type.getCode());
                pstmt.setString(3, ActStatus.UNACT.getCode());
            }

            rs = pstmt.executeQuery();
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On MessageAccessorMySql queryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

    @Override
    public List<Message> query(String ownUno, Long topicId, Pagination page, Connection conn) throws DbException {
        List<Message> returnValue = new ArrayList<Message>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            page.setTotalRows(queryRowSize(ownUno, topicId, conn));

            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND TOPICID = ? AND REMOVESTATUS = ? ORDER BY SENDDATE DESC LIMIT ?, ?");

            pstmt.setString(1, ownUno);
            pstmt.setLong(2, topicId);
            pstmt.setString(3, ActStatus.UNACT.getCode());
            pstmt.setInt(4, page.getStartRowIdx());
            pstmt.setInt(5, page.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On select messages, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    private int queryRowSize(String ownUno, Long topicId, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(1) FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND TOPICID = ? AND REMOVESTATUS = ?";

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setLong(2, topicId);
            pstmt.setString(3, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On MessageAccessorMySql queryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

    @Override
    public Message getLastest(String ownUno, String senderUno, MessageType type, Connection conn) throws DbException {
        Message returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (ownUno.equalsIgnoreCase(senderUno)) {
                pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND SENDERUNO = ? AND RECIEVERUNO = ? AND MSGTYPE = ? AND REMOVESTATUS = ? ORDER BY SENDDATE DESC LIMIT 1");
            } else {
                pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND (SENDERUNO = ? OR RECIEVERUNO = ?) AND MSGTYPE = ? AND REMOVESTATUS = ? ORDER BY SENDDATE DESC LIMIT 1");
            }
            pstmt.setString(1, ownUno);
            pstmt.setString(2, senderUno);
            pstmt.setString(3, senderUno);
            pstmt.setString(4, type.getCode());
            pstmt.setString(5, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On getLastest, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public Message getLastest(String ownUno, Long topicId, Connection conn) throws DbException {
        Message returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(ownUno) + " WHERE TOPICID = ? AND OWNUNO = ? AND REMOVESTATUS = ? ORDER BY SENDDATE DESC LIMIT 1");

            pstmt.setLong(1, topicId);
            pstmt.setString(2, ownUno);
            pstmt.setString(3, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On getLastest, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }
}
