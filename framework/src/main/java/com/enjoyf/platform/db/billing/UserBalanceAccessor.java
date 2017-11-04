package com.enjoyf.platform.db.billing;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.billing.UserBalance;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/9/8
 * Description:
 */
public interface UserBalanceAccessor {

    public UserBalance insert(UserBalance userBalance, Connection conn) throws DbException;

    public UserBalance get(QueryExpress queryExpress, Connection conn) throws DbException;

    public UserBalance getForUpdate(String balance, Connection conn) throws DbException;

    public List<UserBalance> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<UserBalance> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

}
