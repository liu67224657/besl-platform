package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.UserExchangeLog;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-7
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public interface UserExchangeLogAccessor {

    public UserExchangeLog insert(UserExchangeLog exchangeLog, Connection conn) throws DbException;

    public List<UserExchangeLog> queryByUser(String profileId, Date from, Date to, Pagination pagination,String appkey, Connection conn) throws DbException;

    public List<UserExchangeLog> queryByUserGoodsIdConsumeTime(String uno, long goodsId, Date consumeTime, Connection conn) throws DbException;

    public List<UserExchangeLog> queryByUno(String uno, Pagination pagination, Connection conn) throws DbException;

    public List<UserExchangeLog> queryByUserGoodsId(String uno, long goodsId, Connection conn) throws DbException;

    public List<UserExchangeLog> queryByQueryExpress(QueryExpress queryExprss, Connection conn) throws DbException;

    public List<UserExchangeLog> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int queryByDate(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public UserExchangeLog get(QueryExpress queryExpress, Connection conn) throws DbException;
}
