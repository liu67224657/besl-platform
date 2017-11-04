package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.InterFlowAccount;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-28
 * Time: 上午11:29
 * To change this template use File | Settings | File Templates.
 */
public interface InterFlowAccountAccessor {

    public InterFlowAccount insert(InterFlowAccount interFlowAccount, Connection conn) throws DbException;

    public List<InterFlowAccount> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<InterFlowAccount> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public InterFlowAccount get(QueryExpress queryExpress, Connection conn) throws DbException;
}
