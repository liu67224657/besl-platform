package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.UserLotteryLog;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

public interface UserLotteryLogAccessor {

    public UserLotteryLog insert(UserLotteryLog userLotteryLog, Connection conn) throws DbException;

    public UserLotteryLog get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<UserLotteryLog> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<UserLotteryLog> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
