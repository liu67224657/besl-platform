package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.UserLogin;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/10/23
 * Description:
 */
public interface UserLoginAccessor {

    public UserLogin insert(UserLogin lotteryAward, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public UserLogin get(QueryExpress queryExpess, Connection conn) throws DbException;

    public List<UserLogin> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<UserLogin> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

}
