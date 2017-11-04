package com.enjoyf.platform.db.billing;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.billing.DepositLog;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-1-6
 * Time: 上午9:57
 * To change this template use File | Settings | File Templates.
 */
interface DepositLogAccessor {

    public DepositLog insert(DepositLog entry, Connection conn) throws DbException;

    public DepositLog get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<DepositLog> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<DepositLog> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public String queryBySql(String sql,Connection conn)throws DbException;
}
