package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.DbException;

import com.enjoyf.platform.service.lottery.Ticket;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-12
 * Time: 上午10:55
 * To change this template use File | Settings | File Templates.
 */
public interface TicketAccessor {
    public Ticket insert(Ticket ticket, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<Ticket> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<Ticket> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public Ticket get(QueryExpress queryExpress, Connection conn) throws DbException;
}
