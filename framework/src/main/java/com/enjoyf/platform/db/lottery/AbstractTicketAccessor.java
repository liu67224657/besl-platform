package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;

import com.enjoyf.platform.service.lottery.Ticket;
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
public abstract class AbstractTicketAccessor extends AbstractSequenceBaseTableAccessor<Ticket> implements TicketAccessor {
    private Logger logger = LoggerFactory.getLogger(AbstractTicketAccessor.class);
    private static final String KEY_SEQUENCE_NAME = "SEQ_TICKET_ID";
    protected static final String KEY_TABLE_NAME = "ticket";

    @Override
    public Ticket insert(Ticket ticket, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            ticket.setTicketId(getSeqNo(KEY_SEQUENCE_NAME, conn));
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setLong(1, ticket.getTicketId());
            pstmt.setString(2, ticket.getTicketName());
            pstmt.setString(3, ticket.getTicketDesc());
            pstmt.setInt(4, ticket.getBase_rate());
            pstmt.setInt(5, ticket.getCurr_num());
            pstmt.setInt(6, ticket.getAwardLevelCount());
            pstmt.setString(7, ticket.getValidStatus().getCode());
            pstmt.setInt(8, ticket.getWin_type());
            pstmt.setString(9, ticket.getWinCronexp());
            if (ticket.getStart_time() != null) {
                pstmt.setTimestamp(10, new Timestamp(ticket.getStart_time().getTime()));
            } else {
                pstmt.setNull(10, Types.TIMESTAMP);
            }
            if (ticket.getEnd_time() != null) {
                pstmt.setTimestamp(11, new Timestamp(ticket.getEnd_time().getTime()));
            } else {
                pstmt.setNull(11, Types.TIMESTAMP);
            }
            pstmt.setString(12, ticket.getCreateUserid());
            pstmt.setString(13, ticket.getCreateIp());
            pstmt.setTimestamp(14, new Timestamp(ticket.getCreateDate() == null ? System.currentTimeMillis() : ticket.getCreateDate().getTime()));
            pstmt.setString(15, ticket.getLastModifyUserid());
            pstmt.setString(16, ticket.getLastModifyIp());
            if (ticket.getLastModifyDate() != null) {
                pstmt.setTimestamp(17, new Timestamp(ticket.getLastModifyDate().getTime()));
            } else {
                pstmt.setNull(17, Types.TIMESTAMP);
            }

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
    public List<Ticket> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<Ticket> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public Ticket get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected Ticket rsToObject(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setTicketId(rs.getLong("ticket_id"));
        ticket.setTicketName(rs.getString("ticket_name"));
        ticket.setTicketDesc(rs.getString("ticket_desc"));
        ticket.setBase_rate(rs.getInt("base_rate"));
        ticket.setCurr_num(rs.getInt("curr_num"));
        ticket.setAwardLevelCount(rs.getInt("award_level_count"));
        ticket.setValidStatus(ValidStatus.getByCode(rs.getString("validstatus")));
        ticket.setWin_type(rs.getInt("win_type"));
        ticket.setWinCronexp(rs.getString("wincronexp"));
        ticket.setStart_time((rs.getTimestamp("start_time")));
        ticket.setEnd_time(rs.getTimestamp("end_time"));
        ticket.setCreateUserid(rs.getString("createuserid"));
        ticket.setCreateIp(rs.getString("createip"));
        ticket.setCreateDate(rs.getTimestamp("createdate"));
        ticket.setLastModifyUserid(rs.getString("lastmodifyuserid"));
        ticket.setLastModifyIp(rs.getString("lastmodifyip"));
        ticket.setLastModifyDate(rs.getTimestamp("lastmodifydate"));
        return ticket;
    }

    private String getInsertSql() {
        String insertSql = "insert into " + KEY_TABLE_NAME + "(ticket_id,ticket_name,ticket_desc,base_rate,curr_num,award_level_count,validstatus,win_type,wincronexp,start_time,end_time,createuserid,createip,createdate,lastmodifyuserid,lastmodifyip,lastmodifydate) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AppErrorInfo INSERT Script:" + insertSql);
        }
        return insertSql;
    }

}

