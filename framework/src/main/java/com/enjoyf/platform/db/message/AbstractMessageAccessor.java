/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.message;


import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class AbstractMessageAccessor extends AbstractSequenceBaseTableAccessor<Message> implements MessageAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "MESSAGE_";
    private static final String KEY_SEQUENCE_NAME = "SEQ_MESSAGE_ID";
    private static final int TABLE_NUM = 100;

    @Override
    public Message insert(Message message, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            message.setMsgId(getSeqNo(KEY_SEQUENCE_NAME,conn));

            pstmt = conn.prepareStatement(getInsertSql(message.getOwnUno()));

            //MSGID, MSGTYPE, TOPICID, OWNUNO, SENDERUNO, RECIEVERUNO, BODY, SENDDATE, SENDIP, READSTATUS, READDATE, REMOVESTATUS, REMOVEDATE
            pstmt.setLong(1, message.getMsgId());
            pstmt.setString(2, message.getMsgType().getCode());

            pstmt.setLong(3, message.getTopicId() != null ? message.getTopicId() : message.getMsgId());

            pstmt.setString(4, message.getOwnUno());
            pstmt.setString(5, message.getSenderUno() == null ? "" : message.getSenderUno());
            pstmt.setString(6, message.getRecieverUno());

            pstmt.setString(7, message.getBody());
            pstmt.setTimestamp(8, message.getSendDate() != null ? new Timestamp(message.getSendDate().getTime()) : null);
            pstmt.setString(9, message.getSendIp());

            pstmt.setString(10, message.getReadStatus().getCode());
            pstmt.setTimestamp(11, message.getReadDate() != null ? new Timestamp(message.getReadDate().getTime()) : null);

            pstmt.setString(12, message.getRemoveStatus().getCode());
            pstmt.setTimestamp(13, message.getRemoveDate() != null ? new Timestamp(message.getRemoveDate().getTime()) : null);

            //
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert Message, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return message;
    }

    @Override
    public Message getMessage(String ownUno, QueryExpress queryExpress, Connection conn) throws DbException {
        return get(getTableName(ownUno), queryExpress, conn);
    }

    @Override
    public List<Message> query(String ownUno, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Message> query(String ownUno, QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public int updateMessage(String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(getTableName(ownUno), updateExpress, queryExpress, conn);
    }

    @Override
    public List<Long> queryTopicIds(String ownUno, MessageType type, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public List<String> querySenderUnos(String ownUno, MessageType type, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public List<Message> query(String ownUno, String senderUno, MessageType type, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public List<Message> query(String ownUno, Long topicId, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public Message getLastest(String ownUno, String senderUno, MessageType type, Connection conn) throws DbException {
        return null;
    }

    @Override
    public Message getLastest(String ownUno, Long topicId, Connection conn) throws DbException {
        return null;
    }

    @Override
    public int getTopicSize(String ownUno, String senderUno, MessageType type, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = null;
        if (ownUno.equalsIgnoreCase(senderUno)) {
            sql = "SELECT COUNT(1) FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND SENDERUNO = ? AND RECIEVERUNO = ? AND MSGTYPE = ? AND REMOVESTATUS = ?";
        } else {
            sql = "SELECT COUNT(1) FROM " + getTableName(ownUno) + " WHERE OWNUNO = ? AND (SENDERUNO = ? OR RECIEVERUNO = ?) AND MSGTYPE = ? AND REMOVESTATUS = ?";
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setString(2, senderUno);
            pstmt.setString(3, senderUno);
            pstmt.setString(4, type.getCode());
            pstmt.setString(5, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On MessageAccessorMySql getTopicSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

    @Override
    public boolean remove(String ownUno, Long msgId, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("UPDATE " + getTableName(ownUno) + " SET REMOVESTATUS = ?, REMOVEDATE = ? WHERE MSGID = ? AND OWNUNO = ?");

            pstmt.setString(1, ActStatus.ACTED.getCode());
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

            pstmt.setLong(3, msgId);
            pstmt.setString(4, ownUno);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On update message removed, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean removeTopicMessages(String ownUno, Long topicId, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("UPDATE " + getTableName(ownUno) + " SET REMOVESTATUS = ?, REMOVEDATE = ? WHERE TOPICID = ? AND OWNUNO = ?");

            pstmt.setString(1, ActStatus.ACTED.getCode());
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

            pstmt.setLong(3, topicId);
            pstmt.setString(4, ownUno);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On removeTopicMessages, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean removeSenderMessages(String ownUno, String senderUno, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("UPDATE " + getTableName(ownUno) + " SET REMOVESTATUS = ?, REMOVEDATE = ? WHERE OWNUNO = ?  AND ((SENDERUNO = ? AND RECIEVERUNO = ?) OR (SENDERUNO = ? AND RECIEVERUNO = ?))");

            pstmt.setString(1, ActStatus.ACTED.getCode());
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

            pstmt.setString(3, ownUno);

            pstmt.setString(4, ownUno);
            pstmt.setString(5, senderUno);

            pstmt.setString(6, senderUno);
            pstmt.setString(7, ownUno);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On removeSenderMessages, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return true;
    }

    ///private methods.
    private String getInsertSql(String uno) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(uno)
                + " (MSGID, MSGTYPE, TOPICID, OWNUNO, SENDERUNO, RECIEVERUNO, MSGBODY, SENDDATE, SENDIP, READSTATUS, READDATE, REMOVESTATUS, REMOVEDATE)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("Message INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    protected String getTableName(String uno) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }

    protected Message rsToObject(ResultSet rs) throws SQLException {
        Message entry = new Message();

        //MSGID, MSGTYPE, TOPICID, OWNUNO, SENDERUNO, RECIEVERUNO, BODY, SENDDATE, SENDIP, READSTATUS, READDATE, REMOVESTATUS, REMOVEDATE
        entry.setMsgId(rs.getLong("MSGID"));

        entry.setMsgType(MessageType.getByCode(rs.getString("MSGTYPE")));

        entry.setTopicId(rs.getLong("TOPICID"));
        entry.setOwnUno(rs.getString("OWNUNO"));

        entry.setSenderUno(rs.getString("SENDERUNO"));
        entry.setRecieverUno(rs.getString("RECIEVERUNO"));

        entry.setBody(rs.getString("MSGBODY"));

        entry.setSendDate(rs.getTimestamp("SENDDATE") != null ? new Date(rs.getTimestamp("SENDDATE").getTime()) : null);
        entry.setSendIp(rs.getString("SENDIP"));

        entry.setReadStatus(ActStatus.getByCode(rs.getString("READSTATUS")));
        entry.setReadDate(rs.getTimestamp("READDATE") != null ? new Date(rs.getTimestamp("READDATE").getTime()) : null);

        entry.setReadStatus(ActStatus.getByCode(rs.getString("REMOVESTATUS")));
        entry.setRemoveDate(rs.getTimestamp("REMOVEDATE") != null ? new Date(rs.getTimestamp("REMOVEDATE").getTime()) : null);

        return entry;
    }
}
