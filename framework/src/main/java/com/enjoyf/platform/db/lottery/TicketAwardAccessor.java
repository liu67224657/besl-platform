package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.lottery.Ticket;
import com.enjoyf.platform.service.lottery.TicketAward;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-12
 * Time: 上午10:55
 * To change this template use File | Settings | File Templates.
 */
public interface TicketAwardAccessor {
    public TicketAward insert(TicketAward ticket, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<TicketAward> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<TicketAward> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public TicketAward get(QueryExpress queryExpress, Connection conn) throws DbException;
}
