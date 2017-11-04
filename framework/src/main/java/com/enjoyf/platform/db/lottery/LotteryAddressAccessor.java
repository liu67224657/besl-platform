package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.lottery.LotteryAddress;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by ericliu on 16/3/7.
 */
public interface LotteryAddressAccessor {

    public LotteryAddress insert(LotteryAddress userAccount, Connection conn) throws DbException;

    public LotteryAddress get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<LotteryAddress> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<LotteryAddress> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
