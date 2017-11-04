package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.lottery.Ticket;
import com.enjoyf.platform.service.lottery.TicketAward;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-12
 * Time: 上午11:02
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractTicketAwardAccessor extends AbstractSequenceBaseTableAccessor<TicketAward> implements TicketAwardAccessor {
    private Logger logger = LoggerFactory.getLogger(AbstractTicketAwardAccessor.class);
    private static final String KEY_SEQUENCE_NAME = "SEQ_TICKETAWARD_ID";
    protected static final String KEY_TABLE_NAME = "ticket_award";

    @Override
    public TicketAward insert(TicketAward ticketAward, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            ticketAward.setTicketAwardId(getSeqNo(KEY_SEQUENCE_NAME, conn));
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setLong(1, ticketAward.getTicketAwardId());
            pstmt.setLong(2, ticketAward.getTicketId());
            pstmt.setInt(3, ticketAward.getAwardLevel());
            pstmt.setString(4, ticketAward.getAwardName());
            pstmt.setString(5, ticketAward.getAwardDesc());
            pstmt.setString(6, ticketAward.getAwardPic());
            pstmt.setInt(7, ticketAward.getAwardCount());
            pstmt.setInt(8, ticketAward.getCurrentCount());
            pstmt.setString(9, ticketAward.getCreateUserId());
            pstmt.setString(10, ticketAward.getCreateIp());
            pstmt.setTimestamp(11, new Timestamp(ticketAward.getCreateDate() == null ? System.currentTimeMillis() : ticketAward.getCreateDate().getTime()));
            pstmt.setString(12, ticketAward.getLastModifyUserId());
            pstmt.setString(13, ticketAward.getLastModifyIp());
            if (ticketAward.getLastModifyDate() != null) {
                pstmt.setTimestamp(14, new Timestamp(ticketAward.getLastModifyDate().getTime()));
            } else {
                pstmt.setNull(14, Types.TIMESTAMP);
            }
            pstmt.setString(15, ticketAward.getValidStatus().getCode());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ticket, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }


        return null;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<TicketAward> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<TicketAward> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public TicketAward get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected TicketAward rsToObject(ResultSet rs) throws SQLException {
        TicketAward ticket = new TicketAward();
        ticket.setTicketAwardId(rs.getLong("ticket_award_id"));
        ticket.setTicketId(rs.getLong("ticket_id"));
        ticket.setAwardLevel(rs.getInt("award_level"));
        ticket.setAwardName(rs.getString("award_name"));
        ticket.setAwardDesc(rs.getString("award_desc"));
        ticket.setAwardPic(rs.getString("award_pic"));
        ticket.setAwardCount(rs.getInt("award_count"));
        ticket.setCurrentCount(rs.getInt("current_count"));
        ticket.setCreateUserId(rs.getString("createuserid"));
        ticket.setCreateIp(rs.getString("createip"));
        ticket.setCreateDate(rs.getTimestamp("createdate"));
        ticket.setLastModifyUserId(rs.getString("lastmodifyuserid"));
        ticket.setLastModifyIp(rs.getString("lastmodifyip"));
        ticket.setLastModifyDate(rs.getTimestamp("lastmodifydate"));
        ticket.setValidStatus(ValidStatus.getByCode(rs.getString("validstatus")));
        return ticket;
    }

    private String getInsertSql() {
        String insertSql = "insert into " + KEY_TABLE_NAME + "(ticket_award_id,ticket_id,award_level,award_name,award_desc,award_pic,award_count,current_count,createuserid,createip,createdate,lastmodifyuserid,lastmodifyip,lastmodifydate,validstatus) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AppErrorInfo INSERT Script:" + insertSql);
        }
        return insertSql;
    }

}

