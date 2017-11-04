package com.enjoyf.platform.db.message;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.message.PushMessage;
import com.enjoyf.platform.service.message.PushMessageCode;
import com.enjoyf.platform.service.message.PushMessageOptions;
import com.enjoyf.platform.service.message.PushMessageType;
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
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-2
 * Time: 上午10:06
 * To change this template use File | Settings | File Templates.
 */
public class AbstractPushMsgAccessor extends AbstractSequenceBaseTableAccessor<PushMessage> implements PushMsgAccessor {

    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractPushMsgAccessor.class);

    protected static final String KEY_TABLE_NAME = "PUSH_MESSAGE";

    private static final String KEY_SEQUENCE_NAME = "SEQ_PUSH_MESSAGE_ID";

    @Override
    public PushMessage insert(PushMessage pushMessage, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {

            pstmt = conn.prepareStatement(getInsertSql());

            //PUSHMSGTYPE PUSHMSGCODE MSGBODY MSGOPTIONS SENDDATE SENDSTATUS REMOVESTATUS REMOVEDATE

            pushMessage.setPushMsgId(getSeqNo(KEY_SEQUENCE_NAME,conn));


            pstmt.setLong(1, pushMessage.getPushMsgId());

            pstmt.setString(2, pushMessage.getPushMessageType().getCode());

            pstmt.setString(3, pushMessage.getPushMsgCode().getCode());

            pstmt.setString(4, pushMessage.getMsgBody());

            pstmt.setString(5, pushMessage.getMsgOptions().toJson());

            pstmt.setTimestamp(6, pushMessage.getSendDate() != null ? new Timestamp(pushMessage.getSendDate().getTime()) : null);

            pstmt.setString(7, pushMessage.getSendStatus() != null ? pushMessage.getSendStatus().getCode() : null);

            pstmt.setString(8, pushMessage.getRemoveStatus() != null ? pushMessage.getRemoveStatus().getCode() : null);

            pstmt.setTimestamp(9, pushMessage.getRemoveDate() != null ? new Timestamp(pushMessage.getRemoveDate().getTime()) : null);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert PUSH_MESSAGE, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return pushMessage;
    }

    @Override
    public PushMessage get(QueryExpress queryExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<PushMessage> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<PushMessage> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected PushMessage rsToObject(ResultSet rs) throws SQLException {
        PushMessage entry = new PushMessage();

        //PUSHMSGID PUSHMSGTYPE PUSHMSGCODE MSGBODY MSGOPTIONS SENDDATE SENDSTATUS REMOVESTATUS REMOVEDATE

        entry.setPushMsgId(rs.getLong("PUSHMSGID"));

        entry.setPushMessageType(PushMessageType.getByCode(rs.getString("PUSHMSGTYPE")));

        entry.setPushMsgCode(PushMessageCode.getByCode(rs.getString("PUSHMSGCODE")));

        entry.setMsgBody(rs.getString("MSGBODY"));

        entry.setMsgOptions(PushMessageOptions.parse(rs.getString("MSGOPTIONS")));

        entry.setSendDate(rs.getTimestamp("SENDDATE") != null ? new Date(rs.getTimestamp("SENDDATE").getTime()) : null);

        entry.setSendStatus(ActStatus.getByCode(rs.getString("SENDSTATUS")));

        entry.setRemoveStatus(ValidStatus.getByCode(rs.getString("REMOVESTATUS")));

        entry.setRemoveDate(rs.getTimestamp("REMOVEDATE") != null ? new Date(rs.getTimestamp("REMOVEDATE").getTime()) : null);

        return entry;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (PUSHMSGID, PUSHMSGTYPE, PUSHMSGCODE, MSGBODY, MSGOPTIONS, SENDDATE, SENDSTATUS, REMOVESTATUS, REMOVEDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("PUSH_MESSAGE INSERT Script:" + insertSql);
        }

        return insertSql;
    }
}
